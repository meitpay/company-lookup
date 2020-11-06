import react.*
import react.dom.*
import kotlinx.coroutines.*
import kotlinx.css.*
import styled.*

private val scope = MainScope()

val App = functionalComponent<RProps> { _ ->
    headerH1("List")

    val (orgList, setOrgList) = useState(emptyList<Organization>())
    useEffect(dependencies = listOf()) {
       scope.launch {
           setOrgList(fetchAll())
           println(orgList)
       }
    }

    li {
        for (o in orgList) {
            styledUl {
                css {
                    color = Color.whiteSmoke
                }
                key = o.organizationNumber
                +"""
                    NAME = ${o.name}
                    ORG NR = ${o.organizationNumber}
                    ACTIVTY CODE = ${o.activityCode.activityCode}
                    HOMEPAGE = ${o.homePage}
                    NUM EMPLOYEES = ${o.employees}
                    COMMUNE = ${o.postalAddress.commune}
                """.trimIndent()
            }
        }
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
