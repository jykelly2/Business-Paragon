package sheridan.yamazaki.businessparagon.model

import android.renderscript.Float2
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Product(var productName: String? = null,
                   var unitPrice: Double? = null,
                   var description: String? = null,
                   var picture : String? = null,
                   var quantity: Int? = null,
                   var shoppingCartId: String? = null,
                   @DocumentId var id: String? = null)