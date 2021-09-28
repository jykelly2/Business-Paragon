package sheridan.yamazaki.businessparagon.model

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties


@IgnoreExtraProperties
data class CheckoutLayout(var name: String? = null,
                         var layout: String? = null,
                         var checkoutSectionHeight: String? = null,
                         var checkoutSectionWidth: String? = null,
                         var backgroundColour: String? = null,
                         var contactInfoAlignment: String? = null,
                         var itemBackgroundColor: String? = null,
                         var itemHeight: String? = null,
                         var itemPopUpOnHover: String? = null,
                         var itemWidth: String? = null,
                         var itemsAlignment: String? = null,
                         var pageHeight: String? = null,
                         var pageWidth: String? = null,
                         var paymentInfoSectionHeight: String? = null,
                         var paymentInfoSectionWidth: String? = null,
                         var productImageAlignment: String? = null,
                         var productNameColor: String? = null,
                         var productPriceFont: String? = null,
                         var productNameFont: String? = null,
                         var productPriceColor: String? = null,
                         var searchBarSize: String? = null,
                         var shoppingCartIconSize: String? = null,
                         var shoppingCartISectionHeight: String? = null,
                         var shoppingCartSectionWidth: String? = null,
                         var titleTextSize: String? = null,
                         var totalPricingTextFont: String? = null,
                         var totalPricingTextSize: String? = null,
                         @DocumentId var id: String? = null)


