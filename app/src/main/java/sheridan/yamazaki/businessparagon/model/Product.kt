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
                   var unitsInStock: Int? = null,
                   var unitWeight: Int? = null,
                   var availableColors: String? = null,
                   var availableSize: String? = null,
                   var categoryId: String? = null,
                   var color: String? = null,
                   var currentOrder: Int? = null,
                   var discount: Int? = null,
                   var discountAvailable: Boolean? = null,
                   var note: String? = null,
                   var productAvailable: Boolean? = null,
                   var quantityPerUnit: Int? = null,
                   var reorderLevel: String? = null,
                   var size: String? = null,
                   var unitsOnOrder: Int? = null,
                   @DocumentId var id: String? = null)