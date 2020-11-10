import service.BregProvider
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import service.FileIO
import java.io.File

fun main() {
    embeddedServer(
        Netty,
        port = 8080,
        watchPaths = listOf("CommonMain", "JsMain", "JvmMain"),
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    install(CORS) {
        method(HttpMethod.Get)
        method(HttpMethod.Post)
        method(HttpMethod.Delete)
        anyHost()
    }

    install(StatusPages) {
        this.exception<Throwable> { e ->
            call.respondText(e.localizedMessage, ContentType.Text.Plain)
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
                val file = FileIO()
                val ids = file.readFile()
                val breg = BregProvider()
                call.respond(breg.organizationList(ids))
            }
        }

        route(FileCompanion.downloadPath) {
            get {
                try {
                    val fileIO = FileIO()
                    val file = fileIO.getFile("tmp/output.xlsx")
                    call.response.header("Content-Disposition", "attachment; filename=\"${file.name}\"")
                    call.respondFile(file)
                    file.delete()
                } catch (e: Exception) {
                    println(e)
                }
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        route(FileCompanion.generatePath) {
            post {
                val file = FileIO()
                try {
                    file.generateFile(call.receive())
                    call.respond(HttpStatusCode.OK)
                } catch (e: Exception) {
                    println(e)
                }
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        route(FileCompanion.uploadPath) {
            post {
                try {
                    val multipart = call.receiveMultipart()
                    multipart.forEachPart { part ->
                        if (part is PartData.FileItem) {
                            val name = part.originalFileName!!
                            val fileIO = FileIO()
                            if (fileIO.fileExists("tmp/{$name}")) {
                                val file = File("tmp{$name}")
                                file.delete()
                            }
                            val file = File("tmp/$name")

                            part.streamProvider().use { its ->
                                file.outputStream().buffered().use {
                                    its.copyTo(it)
                                }
                            }
                        }
                        part.dispose()
                    }
                    call.respond(HttpStatusCode.OK)
                } catch (e: Exception) {
                    println(e)
                }
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}

