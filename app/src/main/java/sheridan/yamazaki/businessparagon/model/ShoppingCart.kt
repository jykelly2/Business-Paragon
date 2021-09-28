package sheridan.yamazaki.businessparagon.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import java.io.Serializable
import java.util.*

@IgnoreExtraProperties
data class ShoppingCart (
        var businessId: String? = null,
        var productId: String? = null,
        var quantity: Int? = null,
        var processed: Boolean? = null,
        @DocumentId var id: String? = null
)