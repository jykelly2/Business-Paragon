package sheridan.yamazaki.businessparagon.ui.business.checkout

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
import sheridan.yamazaki.businessparagon.model.Business
import sheridan.yamazaki.businessparagon.model.Product
import sheridan.yamazaki.businessparagon.ui.authentication.SignUpFragment
import sheridan.yamazaki.businessparagon.ui.business.list.BusinessListFragment
import sheridan.yamazaki.businessparagon.ui.business.payment.PaymentFragment


@AndroidEntryPoint
class CheckoutFragment: Fragment() {
    private lateinit var binding: CheckoutFragmentBinding
    private val viewModel: CheckoutViewModel by viewModels()
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
        Log.d("emailchi", requireArguments().getString("email").toString())
        val userEmail = if (requireArguments().getString("email") != null) {
            requireArguments().getString("email").toString()
        }else auth.currentUser.email.toString()

        if (businessId.isNotEmpty()) {
            viewModel.returnLayout(businessId, "detail")
        }

        if (currentUserId.isNotEmpty()){
            viewModel.returnShoppingCart(currentUserId)
        }

        binding = CheckoutFragmentBinding.inflate(inflater, container, false)

        binding.email.setText(userEmail)
        binding.paymentButton.setOnClickListener { proceedToPayment() }

        val text = "Looking for more? Continue Shopping"
        val spannableString = SpannableString(text)
        val signUpFragment = SignUpFragment()

        val continueShoppingClickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                returnToExploreView()
            }
        }

        spannableString.setSpan(continueShoppingClickableSpan, 18, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.continueShopping.setText(spannableString, TextView.BufferType.SPANNABLE)
        binding.continueShopping.movementMethod = LinkMovementMethod.getInstance()

        val adapter = CheckoutListAdapter(displayList, false, onClick = {
             Log.d("producti", displayList[it].productName.toString())
            displayList[it].shoppingCartId?.let { it1 -> viewModel.removeFromUserShoppingCart(currentUserId, it1) }
            updateList(it)
        })

        binding.recyclerCheckout.adapter = adapter

        viewModel.products.observe(viewLifecycleOwner) { products ->
            Log.d("checkouti", products.size.toString())
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

    private fun updateList(position: Int){
        val newSubtotal = binding.subtotal.text.toString().drop(2).toDouble() - (displayList[position].unitPrice?.times(displayList[position].quantity!!)!!)
        updatePrice(newSubtotal)
        displayList.removeAt(position)
        binding.recyclerCheckout.adapter?.notifyDataSetChanged()

    }

    private fun updatePrice(subtotal: Double){
        binding.subtotal.text = "C$" + String.format("%.2f", subtotal)
        val HST = subtotal * 0.13
        val total = subtotal + HST
        binding.HST.text = "C$" + String.format("%.2f", HST)
        binding.total.text = "C$" + String.format("%.2f", total)
    }

    private fun proceedToPayment() {
        if(binding.email.text.toString().isEmpty()){
            Toast.makeText(
                    activity,
                    "Please fill out email for order confirmation!",
                    Toast.LENGTH_SHORT
            ).show()
        }else if (displayList.size <= 0){
            Toast.makeText(
                    activity,
                    "Please add products to the cart first!",
                    Toast.LENGTH_SHORT
            ).show()
        }
        else{
            val fragment = PaymentFragment()
            val bundle = Bundle()
            bundle.putString("id", businessId)
            bundle.putString("email",binding.email.text.toString())
            fragment.arguments = bundle
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fl_wrapper, fragment)
                commit()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = (requireActivity() as AppCompatActivity)
        toolbar.supportActionBar?.show()
        toolbar.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
        toolbar.title = "Checkout" ;
    }

    override fun onDestroyView() {
        val toolbar = (requireActivity() as AppCompatActivity)
        toolbar.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setHasOptionsMenu(false)
        toolbar.title = "Business Paragon"
        super.onDestroyView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        returnToExploreView()
        return true
    }

    private fun returnToExploreView(){
        val fragment = BusinessListFragment()
        val bundle = Bundle()
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
    }
}