import api.getOrganizationList
import handler.InputComponent
import kotlinext.js.jsObject
import react.*
import react.dom.*
import kotlinx.coroutines.*
import kotlinx.css.*
import kotlinx.html.TR
import styled.*

private val scope = MainScope()
const val name = "Simple App"

val App = functionalComponent<RProps> { _ ->
    val (organizationList, setOrganizationList) = useState(emptyList<Organization>())
    useEffect(dependencies = listOf()) {
        scope.launch {
            setOrganizationList(getOrganizationList())
        }
    }

    styledH1 {
        css {
            display = Display.flex
            color = Color.orange
            justifyContent = JustifyContent.center
        }
        +name
    }

    child(
        InputComponent,
        props = jsObject {
            onUploadFile = { file ->
                scope.launch {
                    api.uploadFile(file)
                    setOrganizationList(getOrganizationList())
                }
            }
            onGenerateFile = {
                scope.launch {
                    api.generateFile(organizationList)
                }
            }
            onDownloadFile = {
                scope.launch {
                    api.downloadFile()
                }
            }
        }
    )

    div {
        styledTable {
            css {
                borderCollapse = BorderCollapse.collapse
                width = LinearDimension("100%")
                color = Color.whiteSmoke
            }
            thead {
                tr {
                    tableHeadCSS("Organisasjonsnummer")
                    tableHeadCSS("Navn")
                    tableHeadCSS("Aktivitetskode")
                    tableHeadCSS("Hjemmeside")
                    tableHeadCSS("Antall ansatte")
                    tableHeadCSS("Postnummer")
                }
            }

            tbody {
                organizationList.forEach {
                    styledTr {
                        css {
                            nthChild("odd") {
                                backgroundColor = Color.darkGrey
                            }
                        }
                        tableDataCSS(it.organizationNumber)
                        tableDataCSS(it.name)
                        tableDataCSS(it.activityCode.activityCode)
                        tableDataCSS(it.homePage)
                        tableDataCSS(it.employees.toString())
                        tableDataCSS(it.postalAddress.commune)
                    }
                }
            }
        }
    }
}

private fun StyledDOMBuilder<TR>.tableDataCSS(it: String) {
    styledTd {
        css {
            textAlign = TextAlign.start
            border = "2px solid #ddd"
            paddingTop = LinearDimension("12px")
            paddingBottom = LinearDimension("12px")
        }
        +it
    }
}

private fun RDOMBuilder<TR>.tableHeadCSS(it: String) {
    styledTh {
        css {
            textAlign = TextAlign.start
            border = "2px solid #ddd"
            paddingTop = LinearDimension("12px")
            paddingBottom = LinearDimension("12px")
        }
        +it
    }
}
