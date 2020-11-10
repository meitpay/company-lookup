package service

import Organization
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.*

const val baseUrl = "https://data.brreg.no/enhetsregisteret/api/enheter/"

class BregProvider {
    var client = OkHttpClient()

    fun organizationList(list: MutableList<Int>): MutableList<Organization> {
        val organization = Organization

        organization.clear()
        list.forEach {
            organization.add(fetchOrganizationData(it))
        }
        return organization.get()
    }

    private fun fetchOrganizationData(orgNumber: Int): Organization {
        val request = Request.Builder().url(baseUrl + orgNumber).build()
        val response = client.newCall(request).execute()
        val format = Json {
            ignoreUnknownKeys = true
        }

        if (response.code == 200) {
            return format.decodeFromString(response.body!!.string())
        } else {
            // TODO: 09.11.2020 log the error
        }
        response.close()
        return Organization()
    }
}
