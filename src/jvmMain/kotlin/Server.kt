import com.auth0.jwk.UrlJwkProvider
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.sentry.Sentry
import io.sentry.SentryOptions
import service.BregProvider
import service.FileIO
import java.io.File

fun main(args: Array<String>) {
    Sentry.init { options: SentryOptions ->
        options.dsn = ConfigFactory.load().getString("sentry.url")
    }

    embeddedServer(
        Netty,
        commandLineEnvironment(args)
    ).start(wait = true)
}

fun Application.module() {
    install(Authentication) {
        jwt {
            realm = "Ktor auth0"
            skipWhen { ConfigFactory.load().getString("ktor.deployment.environment") == "dev" }
            verifier(UrlJwkProvider(ConfigFactory.load().getString("auth0.issuer")))
            validate { credential ->
                val payload = credential.payload
                if (payload.audience.contains(ConfigFactory.load().getString("auth0.audience"))) {
                    JWTPrincipal(payload)
                } else {
                    null
                }
            }
        }
    }

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
        authenticate {
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
//                        file.delete()
                    } catch (e: Exception) {
                        Sentry.captureException(e)
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
                        Sentry.captureException(e)
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
                        Sentry.captureException(e)
                    }
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
    }
}
