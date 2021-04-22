package sheridan.yamazaki.businessparagon.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import java.io.Serializable
import java.util.*

@IgnoreExtraProperties
data class User (var username: String? = null,
                 var email: String? = null,
                 var password: String? = null,
                 var phoneNumber: String? = null,
                 var address: String? = null,
                 var cardType: String? = null,
                 var cardNumber: Int? = null,
                 var cvv: Int? = null,
                 var expiryDate: Date? = null,
                 @DocumentId var id: String? = null
                 )
