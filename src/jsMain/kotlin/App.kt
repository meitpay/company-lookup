import react.*
import react.dom.*
import kotlinx.coroutines.*
import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
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
    fileForm()
    dataGrid(organizationList)
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

private fun RBuilder.fileForm() {
    form {
        styledInput {
            styledInput(Color.lightGreen)
            attrs {
                type = InputType.file
                name = "upload"
                id = "my-upload-btn"
                onClickFunction = {
                    console.log("upload button clicked")
                }
            }
        }

        styledInput {
            styledInput(Color.lightBlue)
            css {
                float = kotlinx.css.Float.right
            }
            attrs {
                type = InputType.submit
                name = "download"
                id = "download-btn"
                onClickFunction = {
                    console.log("download button clicked")
                }
            }
        }
    }
}

private fun StyledDOMBuilder<INPUT>.styledInput(inputColor: Color) {
    css {
        backgroundColor = inputColor
        border = "none"
        color = Color.white
        padding = "15px 32px"
        textAlign = TextAlign.center
        textDecoration = TextDecoration.none
        display = Display.inlineBlock;
        fontSize = LinearDimension("16px")
        margin = "4px 2px"
        cursor = Cursor.pointer
    }
}

private fun RDOMBuilder<TR>.tableHeader(title: String) {
    styledTh {
        css {
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