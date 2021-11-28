package sheridan.yamazaki.businessparagon.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class ProductAnalytic(
        var id: String? = null,
        var business: String? = null,
        var productName: String? = null,
        var analyticType:String? = null,
        @DocumentId var documentId: String? = null)