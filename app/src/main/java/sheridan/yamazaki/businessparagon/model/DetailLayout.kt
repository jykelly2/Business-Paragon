package sheridan.yamazaki.businessparagon.model

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties


@IgnoreExtraProperties
data class DetailLayout(
        var name: String? = null,
        // var layout: String? = null,
        var normalTextColor: String? = null,
        var normalTextStyle: String? = null,
        var normalTextFont: String? = null,
        var titleTextColor: String? = null,
        var titleTextStyle: String? = null,
        var titleTextFont: String? = null,
        var subtitleTextColor: String? = null,
        var subtitleTextStyle: String? = null,
        var subtitleTextFont: String? = null,
        var backgroundColor: String? = null,
        var alignment: String? = null,
        var productImageSize: String? = null,
        var foregroundColor: String? = null,
        @DocumentId var id: String? = null)
//                  var contactInfoAlignment: String? = null,
//                  var itemBackgroundColor: String? = null,
//                  var itemHeight: String? = null,
//                  var itemPopUpOnHover: String? = null,
//                  var itemWidth: String? = null,
//                  var itemsAlignment: String? = null,
//                  var pageHeight: String? = null,
//                  var pageWidth: String? = null,
//                  var productImageAlignment: String? = null,
//                  var productNameFont: String? = null,
//                        var productNameColor: String? = null,
//                  var productPriceColor: String? = null,
//                  var productPriceFont: String? = null,
//                  var searchBarSize: String? = null,
//                  var shoppingCartIconSize: String? = null,
//                  var titleTextSize: String? = null,


