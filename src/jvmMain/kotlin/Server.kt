import service.DataProvider
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

val organizationNumbers: List<Int> = listOf(991125175, 913519302, 986498192)

fun main() {
    embeddedServer(Netty, 9090) {
        install(ContentNegotiation) {
            json()
        }

        install(StatusPages) {
            this.exception<Throwable> {
                e -> call.respondText(e.localizedMessage, ContentType.Text.Plain)
                throw e
            }
        }

        routing {
            get("/") {
                call.respondText(
                    this::class.java.classLoader.getResource("index.html")!!.readText(),
                    ContentType.Text.Html
                )
            }
            static("/") {
                resources("")
            }

            route(Organization.path) {
                get {
                    call.respond(organizationList(organizationNumbers))
                }
            }
        }
    }.start(wait = true)
}

fun organizationList(list: List<Int>): MutableList<Organization> {
    val provider = DataProvider()
    val organization = Organization

    organization.clear()
    list.forEach {
        organization.add(provider.fetchOrganizationData(it))
    }
    return organization.get()
}
