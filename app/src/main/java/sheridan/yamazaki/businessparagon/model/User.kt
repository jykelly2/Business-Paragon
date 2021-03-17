package sheridan.yamazaki.businessparagon.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import java.io.Serializable
import java.util.*

@IgnoreExtraProperties
data class User (var username: String,
                 var email: String,
                 var password: String,
                 var phoneNumber: String,
                 var address: String,
                 var cardType: String,
                 var cardNumber: Int,
                 var cvv: Int,
                 var expiryDate: Date,
                 @DocumentId var id: String? = null
                 )
