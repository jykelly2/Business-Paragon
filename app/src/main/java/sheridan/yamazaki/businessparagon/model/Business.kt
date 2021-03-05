package sheridan.yamazaki.businessparagon.model

import java.io.Serializable
import java.util.*

data class Business (var name: String? = null,
                     var category: String? = null,
                     var email: String? = null,
                     var phoneNumber: String? = null,
                     var address: String? = null,
                     var logo: String? = null,
                     var id: Long? = null)
