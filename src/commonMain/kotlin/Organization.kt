import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

const val ERROR_MISSING_FIELD = "ERROR MISSING DATA"
const val ERROR_MISSING_FIELD_INT = Int.MIN_VALUE

@Serializable
data class PostalAddress(@SerialName("kommune") val commune: String)

@Serializable
data class ActivityCode(@SerialName("kode") val activityCode: String)

@Serializable
data class Organization(
    @SerialName("organisasjonsnummer") val organizationNumber: String = ERROR_MISSING_FIELD,
    @SerialName("navn") val name: String = ERROR_MISSING_FIELD,
    @SerialName("hjemmeside") val homePage: String = ERROR_MISSING_FIELD,
    @SerialName("postadresse") val postalAddress: PostalAddress = PostalAddress(ERROR_MISSING_FIELD),
    @SerialName("naeringskode1") val activityCode: ActivityCode = ActivityCode(ERROR_MISSING_FIELD),
    @SerialName("antallAnsatte") val employees: Int = ERROR_MISSING_FIELD_INT
) {

    companion object {
        const val path = "/api/organization"
        private val organization = mutableListOf<Organization>()

        fun add(organization: Organization) {
            this.organization.add(organization)
        }

        fun get(): MutableList<Organization> {
            return organization.filter { it.organizationNumber != ERROR_MISSING_FIELD }.toMutableList()
        }

        fun clear() {
            return organization.clear()
        }
    }
}
