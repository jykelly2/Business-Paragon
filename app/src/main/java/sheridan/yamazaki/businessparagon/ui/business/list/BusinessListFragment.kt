package sheridan.yamazaki.businessparagon.ui.business.list

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import sheridan.yamazaki.businessparagon.R
import sheridan.yamazaki.businessparagon.databinding.BusinessListFragmentBinding
import sheridan.yamazaki.businessparagon.model.Business
import sheridan.yamazaki.businessparagon.ui.business.detail.BusinessDetailFragment
import sheridan.yamazaki.businessparagon.ui.business.detail.BusinessDetailViewModel

@AndroidEntryPoint
class BusinessListFragment : Fragment() {

    private val firebaseAnalytics = Firebase.analytics
    private lateinit var binding: BusinessListFragmentBinding
    private val viewModel: BusinessListViewModel by viewModels()
    private val detailViewModel: BusinessDetailViewModel by viewModels()
    lateinit var navController: NavController

   // private val adapter: BusinessListAdapter?=null

   // var items = List<Business>()
    //val displayList = ArrayList<Business>()
    override fun onCreateView(
           inflater: LayoutInflater, container: ViewGroup?,
           savedInstanceState: Bundle?
   ): View? {
        binding = BusinessListFragmentBinding.inflate(inflater, container, false)


        val adapter = BusinessListAdapter(onClick = {
            //val action = BusinessListFragmentDirections.actionBusinessListToDetail(it.id!!)
            // navController.navigate(action)
            // detailViewModel.loadData(it.id!!)
            logAnalyticsEvent(it)
            startBusinessDetailFragment(it.id!!)
            Log.d("clicked", (viewModel.businesses.value?.size ?: 0).toString())
        })

        binding.recyclerBusinesses.adapter = adapter
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

       /*viewModel.businesses.observe(viewLifecycleOwner){
           adapter?.submitList(it)
        }*/
        return binding.root
    }

    private fun logAnalyticsEvent(business: Business){
        firebaseAnalytics.logEvent("selectedBusiness"){
            param("name", business.name.toString())
            param("id", business.id.toString())
            param("category", business.category.toString())
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

    private fun notImplemented() {
        Snackbar.make(
                binding.root,
                getString(R.string.not_implemented),
                Snackbar.LENGTH_LONG
        ).show()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
       // navController = Navigation.findNavController(view)
    }

    private fun startSearch(search: String){
        val filteredItems = ArrayList<Business>()
        for (i in 0 until (viewModel.businesses.value?.size ?: 0)){

        }
    }
}

/*import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import sheridan.yamazaki.businessparagon.databinding.FragmentExploreBinding
import sheridan.yamazaki.businessparagon.databinding.FragmentLoginBinding

class ExploreFragment : Fragment() {
    private lateinit var binding: FragmentExploreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }
}*/