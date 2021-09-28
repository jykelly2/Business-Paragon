package sheridan.yamazaki.businessparagon.ui.business.payment

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import sheridan.yamazaki.businessparagon.R
import sheridan.yamazaki.businessparagon.databinding.CheckoutFragmentBinding
import sheridan.yamazaki.businessparagon.databinding.PaymentFragmentBinding
import sheridan.yamazaki.businessparagon.model.Business
import sheridan.yamazaki.businessparagon.model.Product
import sheridan.yamazaki.businessparagon.model.User
import sheridan.yamazaki.businessparagon.ui.authentication.SignUpFragment
import sheridan.yamazaki.businessparagon.ui.business.checkout.CheckoutFragment
import sheridan.yamazaki.businessparagon.ui.business.checkout.CheckoutListAdapter
import sheridan.yamazaki.businessparagon.ui.business.list.BusinessListFragment
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class PaymentFragment: Fragment() {
    private lateinit var binding: PaymentFragmentBinding
    private val viewModel: PaymentViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private var businessId: String = ""
    private var currentUserId: String = ""
    private val displayList = ArrayList<Product>()

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
        binding.orderButton.setOnClickListener { placeOrder() }
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
            binding.layout = layout
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
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fl_wrapper, fragment)
                commit()
            }
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
        return User(user.username, user.email, user.password, phone, address, user.cardType, user.cardNumber,user.cvv,
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