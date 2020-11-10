import api.generateFile
import api.fetchAll
import api.uploadFile
import handler.InputComponent
import kotlinext.js.jsObject
import react.*
import react.dom.*
import kotlinx.coroutines.*
import kotlinx.css.*
import kotlinx.html.*
import styled.*

private val scope = MainScope()

val App = functionalComponent<RProps> { _ ->
    headerH1("Simple App")

    val (organizationList, setOrganizationList) = useState(emptyList<Organization>())
    useEffect(dependencies = listOf()) {
        scope.launch {
            setOrganizationList(fetchAll())
        }
    }
    child(
        InputComponent,
        props = jsObject {
            onSubmit = { file ->
                scope.launch {
                    uploadFile(file)
                    setOrganizationList(fetchAll())
                }
            }
            downloadFile = {
                run {
                    scope.launch {
                        api.downloadFile()
                    }
                }
            }
            generateFile = {
                run {
                    scope.launch {
                        generateFile(organizationList)
                    }
                }
            }
        }
    )

    if (organizationList.isNotEmpty()) {
        dataGrid(organizationList)
    }
}

private fun RBuilder.dataGrid(organizationList: List<Organization>) {
    div {
        styledTable {
            css {
                border = "2px solid black"
                backgroundColor = Color.darkGrey
                color = Color.whiteSmoke
                borderCollapse = BorderCollapse.collapse
            }

            thead {
                styledTr {
                    css {
                        border = "2px solid black"
                        borderCollapse = BorderCollapse.collapse
                        backgroundColor = Color.dimGrey
                    }
                    tableHeader("ID")
                    tableHeader("Name")
                    tableHeader("Activity Code")
                    tableHeader("Home Page")
                    tableHeader("Number of Employees")
                    tableHeader("Commune")
                }
            }

            tbody {
                for (organization in organizationList) {
                    styledTr {
                        css {
                            border = "2px solid black"
                            borderCollapse = BorderCollapse.collapse
                        }

                        tableData(organization.organizationNumber)
                        tableData(organization.name)
                        tableData(organization.activityCode.activityCode)
                        tableData(organization.homePage)
                        tableData(organization.employees.toString())
                        tableData(organization.postalAddress.commune)

                    }
                }
            }
        }
    }
}

private fun RDOMBuilder<TR>.tableHeader(title: String) {
    styledTh {
        css {
            color = Color.black
            border = BorderStyle.inherit.toString()
            borderCollapse = BorderCollapse.collapse
            textAlign = TextAlign.start
            padding = "10px"
        }
        +title
    }
}

private fun RDOMBuilder<TR>.tableData(data: String) {
    styledTd {
        css {
            color = Color.black
            border = BorderStyle.inherit.toString()
            borderCollapse = BorderCollapse.collapse
            textAlign = TextAlign.start
            padding = "10px"
        }
        +data
    }
}

private fun RBuilder.headerH1(name: String) {
    styledH1 {
        css {
            color = Color.whiteSmoke
            display = Display.flex
            justifyContent = JustifyContent.center
        }
        +name
    }
}
