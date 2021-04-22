package sheridan.yamazaki.businessparagon.ui.business.list

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
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
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.business_list_fragment.*
import sheridan.yamazaki.businessparagon.R
import sheridan.yamazaki.businessparagon.databinding.BusinessListFragmentBinding
import sheridan.yamazaki.businessparagon.model.Business
import sheridan.yamazaki.businessparagon.ui.business.detail.BusinessDetailFragment
import sheridan.yamazaki.businessparagon.ui.business.detail.BusinessDetailViewModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.text.contains as kotlinTextContains

@AndroidEntryPoint
class BusinessListFragment : Fragment() {

    private val firebaseAnalytics = Firebase.analytics
    private lateinit var binding: BusinessListFragmentBinding
    private val viewModel: BusinessListViewModel by viewModels()

    var items = ArrayList<Business>()
    val displayList = ArrayList<Business>()
    override fun onCreateView(
           inflater: LayoutInflater, container: ViewGroup?,
           savedInstanceState: Bundle?
   ): View? {
        binding = BusinessListFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        //binding.viewModel = viewModel

        val adapter = BusinessListAdapter(displayList, onClick = {
            logAnalyticsEvent(it)
            startBusinessDetailFragment(it.id!!)
        })

        binding.recyclerBusinesses.adapter = adapter

       viewModel.businesses.observe(viewLifecycleOwner){
           adapter?.submitList(it)
           adapter.businesses.removeAll(it)
           adapter.businesses.addAll(it)
           items.removeAll(it)
           items.addAll(it)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        val menuItem = menu!!.findItem(R.id.menu_search)
        if (menuItem != null){
            val searchView = menuItem.actionView as SearchView

            searchView.queryHint = "Search for business.."
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
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
                           /* displayList.forEach{
                                Log.d("herere", it.name!!)
                            }*/
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
        return super.onOptionsItemSelected(item)
    }

}


