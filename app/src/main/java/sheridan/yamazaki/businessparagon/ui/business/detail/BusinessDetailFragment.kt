package sheridan.yamazaki.businessparagon.ui.business.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import sheridan.yamazaki.businessparagon.databinding.BusinessDetailFragmentBinding

@AndroidEntryPoint
class BusinessDetailFragment: Fragment() {

    private lateinit var binding: BusinessDetailFragmentBinding
    private val viewModel: BusinessDetailViewModel by viewModels()
   // private val safeArgs: BusinessDetailFragmentArgs by navArgs()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
       // val navController = findNavController()
        //viewModel.loadData(safeArgs.businessId)

        val id = requireArguments().getString("id")

        if (id != null) {
            viewModel.loadData(id)
        }

        binding = BusinessDetailFragmentBinding.inflate(inflater, container, false)
        viewModel.business.observe(viewLifecycleOwner) { business ->
            binding.business = business
        }
        return binding.root
    }
}