package sheridan.yamazaki.businessparagon.ui.business.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.navigation.NavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.business_list_fragment.*
import sheridan.yamazaki.businessparagon.R
import sheridan.yamazaki.businessparagon.databinding.BusinessListFragmentBinding
import sheridan.yamazaki.businessparagon.model.Business
import sheridan.yamazaki.businessparagon.model.User
import sheridan.yamazaki.businessparagon.ui.business.checkout.CheckoutFragment
import sheridan.yamazaki.businessparagon.ui.business.detail.BusinessDetailFragment
import sheridan.yamazaki.businessparagon.ui.business.detail.BusinessDetailViewModel
import sheridan.yamazaki.businessparagon.ui.chatbot.ChatbotActivity
import java.util.*
import kotlin.collections.ArrayList
import kotlin.text.contains as kotlinTextContains

@AndroidEntryPoint
class BusinessListFragment : Fragment() {

    private val firebaseAnalytics = Firebase.analytics
    private lateinit var binding: BusinessListFragmentBinding
    private val viewModel: BusinessListViewModel by viewModels()
    private var cartBusinessId: String = ""
    private var cartQuantity: Int = 0
    private var currentUserId: String = ""
    private lateinit var auth: FirebaseAuth
    private var currentUser: User? = null

    var items = ArrayList<Business>()
    val displayList = ArrayList<Business>()
    override fun onCreateView(
           inflater: LayoutInflater, container: ViewGroup?,
           savedInstanceState: Bundle?
   ): View? {
        binding = BusinessListFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        auth = Firebase.auth
        currentUserId = auth.currentUser.uid

        //get user's shopping cart and businesses from the view model
        if (currentUserId.isNotEmpty()){
            viewModel.loadUserData(currentUserId)
            viewModel.returnShoppingCartSize(currentUserId)
        }

        //set business list adapter and onclick on selected business
        val adapter = BusinessListAdapter(displayList, onClick = {
            logAnalyticsEvent(it)
            startBusinessDetailFragment(it.id!!)
        })

        binding.btnChatbot.setOnClickListener{
            startChatbotActivity()
        }

        binding.recyclerBusinesses.adapter = adapter

        viewModel.user.observe(viewLifecycleOwner) { user ->
            currentUser = user
        }

        //bind business lists to the view (list adapter)
       viewModel.businesses.observe(viewLifecycleOwner){
           val preferenceList = ArrayList<Business>()
           for (business in it){
               if (business.city?.toLowerCase()  == currentUser?.city?.toLowerCase()){
                   preferenceList.add(business)
               }
           }

           adapter?.submitList(preferenceList)
           adapter.businesses.removeAll(preferenceList)
           adapter.businesses.addAll(preferenceList)

           items.removeAll(preferenceList)
           items.addAll(preferenceList)
        }

        viewModel.cartSize.observe(viewLifecycleOwner) { cartSize ->
            cartQuantity = cartSize
            if (cartSize > 0) viewModel.returnCartBusinessId(currentUserId)
            activity?.invalidateOptionsMenu()
        }

        viewModel.cartBusinessId.observe(viewLifecycleOwner) { businessId ->
            cartBusinessId = businessId
        }

        displayList.addAll(items)
        return binding.root
    }

    private fun logAnalyticsEvent(business: Business){
        firebaseAnalytics.logEvent("selectedBusiness"){
            param("name", business.name.toString())
            param("id", business.id.toString())
            param("category", business.category.toString())
            param("city", business.city.toString())
        }
    }

    private fun logSearchAnalyticsEvent(business: Business){
        firebaseAnalytics.logEvent("searched_business"){
            param("name", business.name.toString())
            param("id", business.id.toString())
            param("city", business.city.toString())
            param("category", business.category.toString())
        }
    }

    private fun startChatbotActivity(){
        requireActivity().run {
            startActivity(Intent(this, ChatbotActivity::class.java))
            finish()
        }
    }

    private fun startBusinessDetailFragment(id: String){
        val fragment = BusinessDetailFragment()
        val bundle = Bundle()
        bundle.putString("id", id)
        fragment.arguments = bundle

        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    //have the search filter out the business list depending on the search text
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

        val menuItem = menu!!.findItem(R.id.menu_search)
        if (menuItem != null){
            val searchView = menuItem.actionView as SearchView

            searchView.queryHint = "Search for business.."
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    if (displayList.size == 1) {
                        logSearchAnalyticsEvent(displayList[0])
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText!!.isNotEmpty()) {
                        displayList.clear()
                        val search = newText.toLowerCase(Locale.getDefault())

                        items.forEach {
                            if (it.name?.toLowerCase(Locale.getDefault())?.kotlinTextContains(search)!!) {
                                displayList.add(it)
                            }
                            binding.recyclerBusinesses.adapter?.notifyDataSetChanged()
                        }

                    } else {
                        displayList.clear()
                        displayList.addAll(items)
                        binding.recyclerBusinesses.adapter?.notifyDataSetChanged()
                    }
                    return true
                }

            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
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
        }
        return super.onOptionsItemSelected(item)
    }

}


