package sheridan.yamazaki.businessparagon.ui.business.detail


import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.product_list_item.view.*
import sheridan.yamazaki.businessparagon.R
import sheridan.yamazaki.businessparagon.databinding.BusinessDetailFragmentBinding
import sheridan.yamazaki.businessparagon.model.Business
import sheridan.yamazaki.businessparagon.model.Product
import sheridan.yamazaki.businessparagon.ui.business.checkout.CheckoutFragment
import sheridan.yamazaki.businessparagon.ui.business.list.BusinessListFragment
import sheridan.yamazaki.businessparagon.ui.business.product.ProductDetailFragment
import java.util.*


@AndroidEntryPoint
class BusinessDetailFragment: Fragment() {

    private lateinit var binding: BusinessDetailFragmentBinding
    private val viewModel: BusinessDetailViewModel by viewModels()

    private var businessId: String = ""
    //private var businessName: String = ""
    private var cartBusinessId: String = ""
    private var currentUserId: String = ""
    private var cartQuantity: Int = 0
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = Firebase.auth
        currentUserId = auth.currentUser.uid

        businessId = requireArguments().getString("id").toString()
        if (businessId.isNotEmpty()) {
            viewModel.loadData(businessId)
            viewModel.returnLayout(businessId, "detail")
        }

        if (currentUserId.isNotEmpty()){
            viewModel.returnShoppingCartSize(currentUserId)
        }
        binding = BusinessDetailFragmentBinding.inflate(inflater, container, false)

        val adapter = ProductListAdapter(onClick ={
            startProductDetailFragment(it.id!!)
        })
         binding.recyclerProducts.adapter = adapter

        viewModel.products.observe(viewLifecycleOwner) { product ->
            adapter.submitList(product)
        }

        viewModel.business.observe(viewLifecycleOwner) { business ->
            binding.business = business
          //  business.name?.let {   businessName = it }
            //activity?.invalidateOptionsMenu()
            if (business.logo != ""){
                Glide.with(binding.root)
                        .load(business.logo)
                        .into(binding.businessLogo)
            }else{
                binding.businessLogo.setImageResource(R.drawable.ic_launcher_background)
            }
        }

        viewModel.cartSize.observe(viewLifecycleOwner) { cartSize ->
            cartQuantity = cartSize
            if (cartSize > 0) viewModel.returnCartBusinessId(currentUserId)
            activity?.invalidateOptionsMenu()
        }

        viewModel.cartBusinessId.observe(viewLifecycleOwner) { businessId ->
            cartBusinessId = businessId
        }

        viewModel.layout.observe(viewLifecycleOwner) { layout ->
            binding.recyclerProducts.setBackgroundColor(Color.parseColor(layout.backgroundColour))
            for (product in binding.recyclerProducts){
                product.product_card.setCardBackgroundColor(Color.parseColor(layout.itemBackgroundColor))
                product.product_name.setTextColor(Color.parseColor(layout.productNameColor))
                product.product_price.setTextColor(Color.parseColor(layout.productPriceColor))
                product.product_card.layoutParams.height = layout.itemHeight?.toInt() ?: 100
                product.product_card.layoutParams.width = layout.itemWidth?.toInt() ?: 100
                layout.titleTextSize?.toFloat()?.let { product.product_name.textSize = it }
                product.product_name.typeface = Typeface.create(layout.productNameFont, Typeface.BOLD);
            }
            //binding.recyclerProducts.product_card.setCardBackgroundColor(Color.parseColor(layout.itemBackgroundColor))
        }

        binding.executePendingBindings()
        return binding.root
    }
    private fun startProductDetailFragment(productId: String){
        Log.d("id67",productId)
        val fragment = ProductDetailFragment()
        val bundle = Bundle()
        bundle.putString("businessName", binding.business?.name)
        bundle.putString("businessId", businessId)
        bundle.putString("productId", productId)
        fragment.arguments = bundle

        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        val cartMenuItem = menu!!.findItem(R.id.cartFragment)
        val actionView = cartMenuItem.actionView;

        val cartBadgeTextView = actionView.findViewById<TextView>(R.id.cart_badge_text_view)
        cartBadgeTextView.text = cartQuantity.toString();
        cartBadgeTextView.visibility = if (cartQuantity == 0){ View.INVISIBLE }else View.VISIBLE
        actionView.setOnClickListener {
            onOptionsItemSelected(cartMenuItem)
        }

//        val menuItem = menu!!.findItem(R.id.menu_search)
//        if (menuItem != null){
//            val searchView = menuItem.actionView as SearchView
//
//            searchView.queryHint = "Search for business.."
//            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//                override fun onQueryTextSubmit(query: String): Boolean {
//                    return true
//                }
//
//                override fun onQueryTextChange(newText: String?): Boolean {
//                    if (newText!!.isNotEmpty()) {
//                        displayList.clear()
//                        val search = newText.toLowerCase(Locale.getDefault())
//
//                        items.forEach {
//                            if (it.name?.toLowerCase(Locale.getDefault())?.kotlinTextContains(search)!!) {
//                                displayList.add(it)
//                            }
//                            /* displayList.forEach{
//                                 Log.d("herere", it.name!!)
//                             }*/
//                            binding.recyclerBusinesses.adapter?.notifyDataSetChanged()
//                        }
//
//                    } else {
//                        displayList.clear()
//                        displayList.addAll(items)
//                        binding.recyclerBusinesses.adapter?.notifyDataSetChanged()
//                    }
//                    return true
//                }
//            })
//        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = (requireActivity() as AppCompatActivity)
        toolbar.supportActionBar?.show()
        toolbar.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
        toolbar.title = "Business Paragon" //if (businessName.isNotEmpty()) businessName else "Business Paragon" ;
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
                bundle.putString("id", cartBusinessId)
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
        }else{
            val fragment = BusinessListFragment()

            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fl_wrapper, fragment)
                commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}