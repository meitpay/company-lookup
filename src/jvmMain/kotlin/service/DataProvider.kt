package service

import Organization
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.*

const val baseUrl = "https://data.brreg.no/enhetsregisteret/api/enheter/"

class DataProvider {
    var client = OkHttpClient()

    fun fetchOrganizationData(orgNumber: Int): Organization {
        val request = Request.Builder().url(baseUrl + orgNumber).build()
        val response = client.newCall(request).execute()

        val format = Json {
            ignoreUnknownKeys = true
        }
        return format.decodeFromString(response.body!!.string())
    }
}
