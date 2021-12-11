package sheridan.yamazaki.businessparagon.ui.business.analytic

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import sheridan.yamazaki.businessparagon.R
import sheridan.yamazaki.businessparagon.databinding.AnalyticFragmentBinding
import sheridan.yamazaki.businessparagon.ui.business.detail.BusinessDetailFragment
import sheridan.yamazaki.businessparagon.ui.business.product.ProductDetailFragment
import sheridan.yamazaki.businessparagon.ui.business.product.ProductDetailViewModel

@AndroidEntryPoint
class AnalyticFragment : Fragment(){

    private lateinit var binding: AnalyticFragmentBinding
    private var topBusinessId: String = ""
    private var topProductBusinessId: String = ""
    private var topProductId: String = ""

    private val viewModel: ProductDetailViewModel by viewModels()
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = AnalyticFragmentBinding.inflate(inflater, container, false)

        //top product and business click function
        binding.topProductCard.setOnClickListener{
            startProductDetailFragment(topProductBusinessId, topProductId)
        }

        binding.topBusinessCard.setOnClickListener {
            startBusinessDetailFragment(topBusinessId)
        }

        //get top product and business from the view model
        viewModel.returnProductAnalytic()
        viewModel.returnBusinessAnalytic()


        // get top product and business and bind it to the view
        viewModel.productAnalytic.observe(viewLifecycleOwner) { productAnalytics ->
            productAnalytics.business?.let { productAnalytics.id?.let { it1 -> viewModel.returnProduct(it, it1) } }

            topProductId = productAnalytics.id.toString()
            topProductBusinessId = productAnalytics.business.toString()
        }

        viewModel.businessAnalytic.observe(viewLifecycleOwner) { businessAnalytics ->
            businessAnalytics.id?.let { viewModel.loadData(it) }
            topBusinessId = businessAnalytics.id.toString()
        }

        viewModel.product.observe(viewLifecycleOwner) { product ->
            binding.topProduct = product
            if (product.picture != ""){
                Glide.with(binding.root)
                        .load(product.picture)
                        .into(binding.topProductPictures)
            }else{
                binding.topProductPictures.setImageResource(R.drawable.ic_launcher_background)
            }
            binding.topProductPrice.text = "C$" + String.format("%.2f", product.unitPrice)
        }

        viewModel.business.observe(viewLifecycleOwner) { business ->
            binding.topBusiness = business
            if (business.logo != ""){
                Glide.with(binding.root)
                        .load(business.logo)
                        .into(binding.topBusinessPictures)
            }else{
                binding.topBusinessPictures.setImageResource(R.drawable.ic_launcher_background)
            }
        }

        return binding.root
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

    private fun startProductDetailFragment(businessId:String, productId: String){
        val fragment = ProductDetailFragment()
        val bundle = Bundle()
        bundle.putString("businessName", "")
        bundle.putString("businessId", businessId)
        bundle.putString("productId", productId)
        fragment.arguments = bundle

        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
    }
}