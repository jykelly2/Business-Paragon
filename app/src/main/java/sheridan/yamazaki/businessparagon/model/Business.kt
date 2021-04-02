package sheridan.yamazaki.businessparagon.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Business (var name: String? = null,
                     var category: String? = null,
                     var email: String? = null,
                     var phoneNumber: String? = null,
                     var streetAddress: String? = null,
                     var city: String? = null,
                     var province: String? = null,
                     var postalCode: String? = null,
                     var logo: String? = null,
                     @DocumentId var id: String? = null)
