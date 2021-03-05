package sheridan.yamazaki.businessparagon.ui.list
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.*
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import sheridan.yamazaki.businessparagon.databinding.BusinessListFragmentBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

import sheridan.yamazaki.businessparagon.R

@AndroidEntryPoint
class BusinessListFragment : Fragment() {

    private lateinit var binding: BusinessListFragmentBinding
    private val viewModel: BusinessListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BusinessListFragmentBinding.inflate(inflater, container, false)

        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.recyclerBusinesses.addItemDecoration(divider)

        val adapter = BusinessListAdapter(onClick = {
            Log.d("clicked", "clicked")
        })

        Log.d("viewmodelll", viewModel.businesses.value?.get(0)?.name.toString())
        binding.recyclerBusinesses.adapter = adapter
      //  binding.lifecycleOwner = viewLifecycleOwner
        //binding.viewModel = viewModel

       viewModel.businesses.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }

        return binding.root
    }
    private fun notImplemented() {
        Snackbar.make(
                binding.root,
                getString(R.string.not_implemented),
                Snackbar.LENGTH_LONG
        ).show()
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