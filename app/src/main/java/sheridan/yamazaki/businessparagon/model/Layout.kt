package sheridan.yamazaki.businessparagon.model

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties


@IgnoreExtraProperties
data class Layout(var name: String? = null,
                  var layout: String? = null,
                  var textColour: String? = null,
                  var backgroundColour: String? = null,
                  @DocumentId var id: String? = null)


