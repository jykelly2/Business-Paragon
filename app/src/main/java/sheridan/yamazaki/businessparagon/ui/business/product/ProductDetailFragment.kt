package sheridan.yamazaki.businessparagon.ui.business.product

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import sheridan.yamazaki.businessparagon.R
import sheridan.yamazaki.businessparagon.databinding.ProductDetailFragmentBinding
import sheridan.yamazaki.businessparagon.model.ShoppingCart
import sheridan.yamazaki.businessparagon.ui.business.checkout.CheckoutFragment
import sheridan.yamazaki.businessparagon.ui.business.detail.BusinessDetailFragment


@AndroidEntryPoint
class ProductDetailFragment: Fragment() {
    private lateinit var binding: ProductDetailFragmentBinding
    private val viewModel: ProductDetailViewModel by viewModels()
    private var businessId: String = ""
    private var cartBusinessId: String = ""
    private var businessName: String = ""
    private var currentUserId: String = ""
    private var productId : String = ""
    private var cartQuantity: Int = 0
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        businessId = requireArguments().getString("businessId").toString()
        productId = requireArguments().getString("productId").toString()
        businessName = requireArguments().getString("businessName").toString()
        auth = Firebase.auth
        currentUserId = auth.currentUser.uid

        if (businessId.isNotEmpty() && productId.isNotEmpty()) {
            viewModel.returnProduct(businessId, productId)
            viewModel.returnLayout(businessId, "productdetail")
        }

        if (currentUserId.isNotEmpty()){
            viewModel.returnShoppingCartSize(currentUserId)
        }

        binding = ProductDetailFragmentBinding.inflate(inflater, container, false)

        binding.addToCartButton.setOnClickListener { addToCart() }

        viewModel.product.observe(viewLifecycleOwner) { product ->
            binding.product = product
            if (product.picture != ""){
                Glide.with(binding.root)
                        .load(product.picture)
                        .into(binding.productPicture)
            }else{
                binding.productPicture.setImageResource(R.drawable.ic_launcher_background)
            }
            binding.productPrice.text = "C$" + String.format("%.2f", product.unitPrice)
        }

        viewModel.cartSize.observe(viewLifecycleOwner) { cartSize ->
            cartQuantity = cartSize
            if (cartQuantity > 0) viewModel.returnCartBusinessId(currentUserId)
            activity?.invalidateOptionsMenu()
        }

        viewModel.cartBusinessId.observe(viewLifecycleOwner) { businessId ->
            cartBusinessId = businessId
        }

        viewModel.layout.observe(viewLifecycleOwner) { layout ->
            //binding.layout = layout
            //product.product_card.setCardBackgroundColor(Color.parseColor(layout.itemBackgroundColor))
            view?.setBackgroundColor(Color.parseColor(layout.backgroundColor))
            val normalFontStyle = layout.normalTextStyle?.let { getFontStyleEnum(it) }
            val titleFontStyle = layout.titleTextStyle?.let { getFontStyleEnum(it) }
            val subtitleFontStyle = layout.subtitleTextStyle?.let { getFontStyleEnum(it) }
            val alignment = layout.alignment?.let { getAlignmentEnum(it) }

            if (alignment != null) {
                binding.productName.gravity = alignment
                binding.productPrice.gravity = alignment
                binding.quantityTitle.gravity = alignment
                binding.description.gravity = alignment
                binding.productDescription.gravity = alignment
            }


            binding.productName.setTextColor(Color.parseColor(layout.titleTextColor))
            binding.productPrice.setTextColor(Color.parseColor(layout.subtitleTextColor))
            binding.description.setTextColor(Color.parseColor(layout.subtitleTextColor))
            binding.quantityTitle.setTextColor(Color.parseColor(layout.subtitleTextColor))
            binding.productDescription.setTextColor(Color.parseColor(layout.normalTextColor))
            binding.productName.typeface = titleFontStyle?.let { Typeface.create(layout.titleTextFont, it) };
            binding.productPrice.typeface = subtitleFontStyle?.let { Typeface.create(layout.subtitleTextFont, it) };
            binding.quantityTitle.typeface = subtitleFontStyle?.let { Typeface.create(layout.subtitleTextFont, it) };
            binding.description.typeface = subtitleFontStyle?.let { Typeface.create(layout.subtitleTextFont, it) };
            binding.productDescription.typeface = normalFontStyle?.let { Typeface.create(layout.normalTextFont, it) };
            binding.addToCartButton.setBackgroundColor(Color.parseColor((layout.foregroundColor)))
            //product.product_card.layoutParams.height = layout.itemHeight?.toInt() ?: 100
            //product.product_card.layoutParams.width = layout.itemWidth?.toInt() ?: 100
            //layout.titleTextSize?.toFloat()?.let { binding.productName.textSize = it }
            //layout.titleTextSize?.toFloat()?.let { binding.productName.textSize = it }
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

    private fun getAlignmentEnum(alignment: String): Int {
        var alignmentStyle = 0
        alignmentStyle = when (alignment){
            "right" -> Gravity.RIGHT
            "left" -> Gravity.LEFT
            else -> Gravity.CENTER
        }
        return alignmentStyle
    }

    private fun addToCart() {
        Log.d("carbusiid", "$businessId, $cartBusinessId")
        if (businessId.trim().toString() != cartBusinessId.trim().toString() && cartBusinessId != ""){
            Toast.makeText(
                    activity,
                    "You can only add from one business! Please clear the cart if you want to add product from another business!",
                    Toast.LENGTH_SHORT
            ).show()
        }else if(binding.quantity.text.toString().isEmpty() || binding.quantity.text.toString() == "0"){
            Toast.makeText(
                    activity,
                    "Quantity should be more than 0!",
                    Toast.LENGTH_SHORT
            ).show()
        }
        else{
            val shoppingCart = ShoppingCart(businessId, productId, binding.quantity.text.toString().toInt(), false)
            viewModel.addToUserShoppingCart(currentUserId, shoppingCart)
            Toast.makeText(
                    activity,
                    "Added to Shopping Cart!",
                    Toast.LENGTH_SHORT
            ).show()
            cartQuantity += 1
            activity?.invalidateOptionsMenu()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = (requireActivity() as AppCompatActivity)
        toolbar.supportActionBar?.show()
        toolbar.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
        toolbar.title = if (businessName.isNotEmpty()) businessName else "Business Paragon" ;
    }

    override fun onDestroyView() {
        val toolbar = (requireActivity() as AppCompatActivity)
        toolbar.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setHasOptionsMenu(false)
        toolbar.title = "Business Paragon"
        super.onDestroyView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.toString() == "Cart"){
            if (cartQuantity > 0) {
                val fragment = CheckoutFragment()
                val bundle = Bundle()
                bundle.putString("id", businessId)
                fragment.arguments = bundle

                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.fl_wrapper, fragment)
                    commit()
                }
            }else{
                Toast.makeText(
                        activity,
                        "Add products to shopping cart first!",
                        Toast.LENGTH_SHORT
                ).show()
            }
        }else {
            val fragment = BusinessDetailFragment()
            val bundle = Bundle()
            bundle.putString("id", businessId)
            fragment.arguments = bundle

            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fl_wrapper, fragment)
                commit()
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.cart_menu, menu)
        val menuItem = menu!!.findItem(R.id.cartFragment)
        val actionView = menuItem.actionView;

        val cartBadgeTextView = actionView.findViewById<TextView>(R.id.cart_badge_text_view)
        cartBadgeTextView.text = cartQuantity.toString();
        cartBadgeTextView.visibility = if (cartQuantity == 0){ View.INVISIBLE }else View.VISIBLE
        actionView.setOnClickListener {
            onOptionsItemSelected(menuItem)
        }
    }
}