package sheridan.yamazaki.businessparagon.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class BusinessAnalytic(
        var id: String? = null,
        var category: String? = null,
        var city: String? = null,
        var name:String? = null,
        var analyticType:String? = null,
        @DocumentId var documentId: String? = null)