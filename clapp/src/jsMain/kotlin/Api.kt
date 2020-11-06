import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.browser.window
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer

val endpoint = window.location.origin // only needed until https://github.com/ktorio/ktor/issues/1695 is resolved
val jsonClient = HttpClient {
    install(JsonFeature) { serializer = KotlinxSerializer() }
}

suspend fun fetchAll(): List<Organization> {
    return jsonClient.get(endpoint + Organization.path)
}
