package service

import Organization
import com.github.nwillc.poink.workbook
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import java.io.File
import java.io.FileNotFoundException
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

class FileIO {
    private val inputPath = "tmp/input.xlsx"
    private val outputPath = "tmp/output.xlsx"

    fun fileExists(path: String): Boolean {
        val file = File(path)
        return file.exists()
    }

    fun getFile(path: String): File {
        if (fileExists(path)) {
            return File(path)
        } else {
            throw FileNotFoundException("The selected file don't exist!")
        }
    }

    fun readFile(): MutableList<Int> {
        val orgNumbers = mutableListOf<Int>()

        if (fileExists(inputPath)) {
            workbook(inputPath) {
                sheet(0) {
                    for (x in 0..lastRowNum) {
                        val currentValue = getRow(x).getCell(0)
                        try {
                            orgNumbers.add(currentValue.numericCellValue.toInt())
                        } catch (e: IllegalStateException) {
                            println(e)
                        }
                    }
                }
            }
        }
        return orgNumbers
    }

    fun generateFile(organization: List<Organization>) {
        if (organization.isEmpty()) {
            throw IllegalArgumentException("Empty list of organizations")
        }
        workbook {
            val headerStyle = cellStyle("Header") {
                fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
                fillPattern = FillPatternType.SOLID_FOREGROUND
            }

            sheet {
                val headerList = listOf(
                        "Organisasjonsnummer",
                        "Selskapsnavn",
                        "Kommune",
                        "Hjemmeside",
                        "Næringskode",
                        "Antall Ansatte"
                )
                row(headerList, headerStyle)

                for ((i, _) in headerList.withIndex()) {
                    autoSizeColumn(i)
                }
                organization.forEach {
                    row(listOf(
                        it.organizationNumber,
                        it.name,
                        it.postalAddress.commune,
                        it.homePage,
                        it.activityCode.activityCode,
                        it.employees
                    ))
                }
            }
        }.write(outputPath)
    }
}
