package api

import FileCompanion
import FileCompanion.Companion.isValid
import Organization
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.fetch.RequestInit
import org.w3c.files.File
import org.w3c.xhr.FormData

val endpoint = window.location.origin // only needed until https://github.com/ktorio/ktor/issues/1695 is resolved
val jsonClient = HttpClient {
    install(JsonFeature) { serializer = KotlinxSerializer() }
}

suspend fun fetchAll(): List<Organization> {
    return jsonClient.get(endpoint + Organization.path)
}

suspend fun generateFile(organizationList: List<Organization>) {
    jsonClient.post<Unit>(endpoint + FileCompanion.generatePath) {
        contentType(ContentType.Application.Json)
        body = organizationList
    }
}

fun downloadFile() {
    window.open(endpoint + FileCompanion.downloadPath)
}

suspend fun uploadFile(file: File) {
    if (isValid(file.type)) {
        val form = FormData()
        form.append("file", file, "input.xlsx")
        window.fetch(endpoint + FileCompanion.uploadPath, RequestInit().apply {
            method = "POST"
            headers = ContentType.MultiPart.FormData
            body = form
        }).await()
    }
}
