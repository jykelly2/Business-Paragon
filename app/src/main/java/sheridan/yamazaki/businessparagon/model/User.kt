package sheridan.yamazaki.businessparagon.model

import java.io.Serializable
import java.util.*

data class User (var username: String,
                 var email: String,
                 var password: String,
                 var phoneNumber: String,
                 var address: String,
                 var cardType: String,
                 var cardNumber: Int,
                 var cvv: Int,
                 var expiryDate: Date
                 )

