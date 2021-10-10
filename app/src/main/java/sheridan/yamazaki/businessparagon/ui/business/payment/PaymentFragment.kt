package sheridan.yamazaki.businessparagon.ui.business.payment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import sheridan.yamazaki.businessparagon.R
import sheridan.yamazaki.businessparagon.databinding.PaymentFragmentBinding
import sheridan.yamazaki.businessparagon.model.Product
import sheridan.yamazaki.businessparagon.model.User
import sheridan.yamazaki.businessparagon.ui.business.checkout.CheckoutFragment
import sheridan.yamazaki.businessparagon.ui.business.checkout.CheckoutListAdapter
import sheridan.yamazaki.businessparagon.ui.business.list.BusinessListFragment


@AndroidEntryPoint
class PaymentFragment: Fragment() {
    private val firebaseAnalytics = Firebase.analytics
    private lateinit var binding: PaymentFragmentBinding
    private val viewModel: PaymentViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private var businessId: String = ""
    private var currentUserId: String = ""
    private val displayList = ArrayList<Product>()

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        auth = Firebase.auth
        currentUserId = auth.currentUser.uid
        businessId = requireArguments().getString("id").toString()
        val userEmail = requireArguments().getString("email").toString()

        if (businessId.isNotEmpty()) {
            viewModel.returnLayout(businessId, "payment")
        }

        if (currentUserId.isNotEmpty()){
            viewModel.returnShoppingCart(currentUserId)
            viewModel.loadUserData(currentUserId)
        }

        binding = PaymentFragmentBinding.inflate(inflater, container, false)

        binding.email.text = userEmail
        binding.orderButton.setOnClickListener {
            placeOrder()
            logAnalyticsEvent()
        }
        binding.emailChangeText.setOnClickListener { returnToCheckoutView() }

        val adapter = CheckoutListAdapter(displayList, true, onClick = {})

        binding.recyclerPayment.adapter = adapter

        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.user = user
        }

        viewModel.products.observe(viewLifecycleOwner) { products ->
            adapter.submitList(products)
            adapter.products.addAll(products)
            var subtotal = 0.00
            for (item in products){
                subtotal += item.unitPrice?.times(item.quantity!!)!!
            }
            updatePrice(subtotal)
        }

        viewModel.layout.observe(viewLifecycleOwner) { layout ->
            //binding.layout = layout
            view?.setBackgroundColor(Color.parseColor(layout.backgroundColor))
            val normalFontStyle = layout.normalTextStyle?.let { getFontStyleEnum(it) }
            val titleFontStyle = layout.titleTextStyle?.let { getFontStyleEnum(it) }
            val subtitleFontStyle = layout.subtitleTextStyle?.let { getFontStyleEnum(it) }
            val alignment = layout.alignment?.let { getAlignmentEnum(it) }

            if (alignment != null) {
                binding.shoppingCartTitle.gravity = alignment
                binding.checkoutTitle.gravity = alignment
              //  binding.checkImage.foregroundGravity = alignment
                binding.exclamationImage.foregroundGravity = alignment
                binding.paymentTitle.gravity = alignment
                binding.countryText.gravity = alignment
                binding.country.gravity = alignment
                binding.fullNameText.gravity = alignment
                binding.fullName.gravity = alignment
                binding.phoneText.gravity = alignment
                binding.phone.gravity = alignment
                binding.addressText.gravity = alignment
                binding.address.gravity = alignment
                binding.cityText.gravity = alignment
                binding.city.gravity = alignment
                binding.provinceText.gravity = alignment
                binding.province.gravity = alignment
                binding.postalCodeText.gravity = alignment
                binding.postalCode.gravity = alignment
            }

//            val layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
//            if (alignment != null) {
//                layoutParams.leftMargin = 50
//                layoutParams.width = 130
//                layoutParams.height = 130
// //               layoutParams.startToEnd = alignment
//                layoutParams.startToEnd = view?.id?.plus(100) ?: 50
//               layoutParams.topToBottom = binding.checkoutTitle.id
//            }
////            binding.checkImage.layoutParams.width = 100
////            binding.checkImage.layoutParams.height = 100
//            binding.checkImage.layoutParams = layoutParams


            binding.shoppingCartTitle.setTextColor(Color.parseColor(layout.titleTextColor))
            binding.subtotalTitle.setTextColor(Color.parseColor(layout.subtitleTextColor))
            binding.HSTTitle.setTextColor(Color.parseColor(layout.subtitleTextColor))
            binding.totalTitle.setTextColor(Color.parseColor(layout.subtitleTextColor))
            binding.subtotal.setTextColor(Color.parseColor(layout.subtitleTextColor))
            binding.HST.setTextColor(Color.parseColor(layout.subtitleTextColor))
            binding.total.setTextColor(Color.parseColor(layout.subtitleTextColor))
            binding.checkoutTitle.setTextColor(Color.parseColor(layout.titleTextColor))
            binding.emailText.setTextColor(Color.parseColor(layout.subtitleTextColor))
            binding.email.setTextColor(Color.parseColor(layout.normalTextColor))
            binding.emailChangeText.setTextColor(Color.parseColor(layout.normalTextColor))
            binding.paymentTitle.setTextColor(Color.parseColor(layout.titleTextColor))
            binding.billingText.setTextColor(Color.parseColor(layout.subtitleTextColor))
            binding.billingInfoText.setTextColor(Color.parseColor(layout.normalTextColor))
            binding.countryText.setTextColor(Color.parseColor(layout.subtitleTextColor))
            binding.country.setTextColor(Color.parseColor(layout.normalTextColor))
            binding.fullNameText.setTextColor(Color.parseColor(layout.subtitleTextColor))
            binding.fullName.setTextColor(Color.parseColor(layout.normalTextColor))
            binding.phoneText.setTextColor(Color.parseColor(layout.subtitleTextColor))
            binding.phone.setTextColor(Color.parseColor(layout.normalTextColor))
            binding.addressText.setTextColor(Color.parseColor(layout.subtitleTextColor))
            binding.address.setTextColor(Color.parseColor(layout.normalTextColor))
            binding.cityText.setTextColor(Color.parseColor(layout.subtitleTextColor))
            binding.city.setTextColor(Color.parseColor(layout.normalTextColor))
            binding.provinceText.setTextColor(Color.parseColor(layout.subtitleTextColor))
            binding.province.setTextColor(Color.parseColor(layout.normalTextColor))
            binding.postalCodeText.setTextColor(Color.parseColor(layout.subtitleTextColor))
            binding.postalCode.setTextColor(Color.parseColor(layout.normalTextColor))

            binding.shoppingCartTitle.typeface = titleFontStyle?.let { Typeface.create(layout.titleTextFont, it) };
            binding.subtotalTitle.typeface = subtitleFontStyle?.let { Typeface.create(layout.subtitleTextFont, it) };
            binding.HSTTitle.typeface = subtitleFontStyle?.let { Typeface.create(layout.subtitleTextFont, it) };
            binding.totalTitle.typeface = subtitleFontStyle?.let { Typeface.create(layout.subtitleTextFont, it) };
            binding.subtotal.typeface = subtitleFontStyle?.let { Typeface.create(layout.subtitleTextFont, it) };
            binding.HST.typeface = subtitleFontStyle?.let { Typeface.create(layout.subtitleTextFont, it) };
            binding.total.typeface = subtitleFontStyle?.let { Typeface.create(layout.subtitleTextFont, it) };
            binding.checkoutTitle.typeface = titleFontStyle?.let { Typeface.create(layout.titleTextFont, it) };
            binding.emailText.typeface = subtitleFontStyle?.let { Typeface.create(layout.subtitleTextFont, it) };
            binding.email.typeface = normalFontStyle?.let { Typeface.create(layout.normalTextFont, it) };
            binding.emailChangeText.typeface = normalFontStyle?.let { Typeface.create(layout.normalTextFont, it) };
            binding.paymentTitle.typeface = titleFontStyle?.let { Typeface.create(layout.titleTextFont, it) };
            binding.billingText.typeface = subtitleFontStyle?.let { Typeface.create(layout.subtitleTextFont, it) };
            binding.billingInfoText.typeface = normalFontStyle?.let { Typeface.create(layout.normalTextFont, it) };
            binding.countryText.typeface = subtitleFontStyle?.let { Typeface.create(layout.subtitleTextFont, it) };
            binding.country.typeface = normalFontStyle?.let { Typeface.create(layout.normalTextFont, it) };
            binding.fullNameText.typeface = subtitleFontStyle?.let { Typeface.create(layout.subtitleTextFont, it) };
            binding.fullName.typeface = normalFontStyle?.let { Typeface.create(layout.normalTextFont, it) };
            binding.phoneText.typeface = subtitleFontStyle?.let { Typeface.create(layout.subtitleTextFont, it) };
            binding.phone.typeface = normalFontStyle?.let { Typeface.create(layout.normalTextFont, it) };
            binding.addressText.typeface = subtitleFontStyle?.let { Typeface.create(layout.subtitleTextFont, it) };
            binding.address.typeface = normalFontStyle?.let { Typeface.create(layout.normalTextFont, it) };
            binding.cityText.typeface = subtitleFontStyle?.let { Typeface.create(layout.subtitleTextFont, it) };
            binding.city.typeface = normalFontStyle?.let { Typeface.create(layout.normalTextFont, it) };
            binding.provinceText.typeface = subtitleFontStyle?.let { Typeface.create(layout.subtitleTextFont, it) };
            binding.province.typeface = normalFontStyle?.let { Typeface.create(layout.normalTextFont, it) };
            binding.postalCodeText.typeface = subtitleFontStyle?.let { Typeface.create(layout.subtitleTextFont, it) };
            binding.postalCode.typeface = normalFontStyle?.let { Typeface.create(layout.normalTextFont, it) };
            binding.orderButton.typeface = subtitleFontStyle?.let { Typeface.create(layout.subtitleTextFont, it) };
           // binding.layout = layout
            //product.product_card.setCardBackgroundColor(Color.parseColor(layout.itemBackgroundColor))
            // binding.productName.setTextColor(Color.parseColor(layout.productNameColor))
            //binding.productPrice.setTextColor(Color.parseColor(layout.productPriceColor))
            //product.product_card.layoutParams.height = layout.itemHeight?.toInt() ?: 100
            //product.product_card.layoutParams.width = layout.itemWidth?.toInt() ?: 100
            //layout.titleTextSize?.toFloat()?.let { binding.productName.textSize = it }
            //layout.titleTextSize?.toFloat()?.let { binding.productName.textSize = it }
            // binding.productName.typeface = Typeface.create(layout.productNameFont, Typeface.BOLD);
            //binding.productPrice.typeface = Typeface.create(layout.productPriceFont, Typeface.BOLD);
        }
        binding.executePendingBindings()
        return binding.root
    }

    private fun getFontStyleEnum(textStyle: String): Int {
        var fontStyle = 0
        fontStyle = when (textStyle){
            "normal" -> Typeface.NORMAL
            "bold" -> Typeface.BOLD
            else -> Typeface.ITALIC
        }
        return fontStyle
    }

    private fun logAnalyticsEvent(){
        val productNames = ArrayList<String>()
        val productIds = ArrayList<String>()
        var price = 0.00
        for (item in displayList){
            item.productName?.let { productNames.add(it)}
            item.id?.let { productIds.add(it) }
            price += item.unitPrice!!
        }
        firebaseAnalytics.logEvent("purchased_products"){
            param("ids", productIds.toString())
            param("business", businessId)
            param("price", price.toString())
            param("products", productNames.toString())
        }
    }

    private fun getAlignmentEnum(alignment: String): Int {
        var alignmentStyle = 0
        alignmentStyle = when (alignment){
            "right" -> Gravity.RIGHT
            "left" -> Gravity.LEFT
            else -> Gravity.CENTER
        }
        return alignmentStyle
    }

    private fun updatePrice(subtotal: Double){
        binding.subtotal.text = "C$" + String.format("%.2f", subtotal)
        val HST = subtotal * 0.13
        val total = subtotal + HST
        binding.HST.text = "C$" + String.format("%.2f", HST)
        binding.total.text = "C$" + String.format("%.2f", total)
    }

    private fun placeOrder() {
        if(!validateInput()){
            Toast.makeText(
                    activity,
                    "Please fill out all required order confirmation!",
                    Toast.LENGTH_SHORT
            ).show()
        }
        else{
            val user = createUserObj()
            viewModel.updateUser(user)
            val shoppingCarts = ArrayList<String>()
            for (item in displayList){
                item.shoppingCartId?.let { shoppingCarts.add(it) }
            }
            viewModel.updateUserShoppingCart(currentUserId, shoppingCarts)
            Toast.makeText(
                    activity,
                    "Order is placed!",
                    Toast.LENGTH_SHORT
            ).show()
            val fragment = BusinessListFragment()
            Handler().postDelayed(Runnable { activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fl_wrapper, fragment)
                commit()
            } }, 2000)
//            activity?.supportFragmentManager?.beginTransaction()?.apply {
//                replace(R.id.fl_wrapper, fragment)
//                commit()
//            }
        }
    }

    private fun createUserObj(): User {
        val country : String = binding.country.text.toString().trim()
        val fullName : String = binding.fullName.text.toString().trim()
        val phone: String = binding.phone.text.toString().trim()
        val address: String = binding.address.text.toString().trim()
        val city: String = binding.city.text.toString().trim()
        val province: String = binding.province.text.toString().trim()
        val postalCode: String = binding.postalCode.text.toString().trim()
        val id = auth.currentUser.uid

        val user = binding.user!!
        return User(user.username, user.email, user.password, phone, address, user.cardType, user.cardNumber, user.cvv,
                user.expiryDate, country, fullName, city, province, postalCode, id)
    }

    private fun validateInput(): Boolean{
        val email : String = binding.email.text.toString().trim()
        val country : String = binding.country.text.toString().trim()
        val fullName : String = binding.fullName.text.toString().trim()
       // val phone: String = binding.phone.text.toString().trim()
        val address: String = binding.address.text.toString().trim()
        val city: String = binding.city.text.toString().trim()
        val province: String = binding.province.text.toString().trim()
        val postalCode: String = binding.postalCode.text.toString().trim()
        return  email.isNotEmpty() && country.isNotEmpty() && fullName.isNotEmpty()
                && address.isNotEmpty() && province.isNotEmpty()
                && city.isNotEmpty() && postalCode.isNotEmpty()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = (requireActivity() as AppCompatActivity)
        toolbar.supportActionBar?.show()
        toolbar.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
        toolbar.title = "Payment"
    }

    override fun onDestroyView() {
        val toolbar = (requireActivity() as AppCompatActivity)
        toolbar.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setHasOptionsMenu(false)
        toolbar.title = "Business Paragon"
        super.onDestroyView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        returnToCheckoutView()
        return true
    }

    private fun returnToCheckoutView(){
        val fragment = CheckoutFragment()
        val bundle = Bundle()
        bundle.putString("id", businessId)
        bundle.putString("email", binding.email.text as String?)
        fragment.arguments = bundle
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
    }
}