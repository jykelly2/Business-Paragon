package sheridan.yamazaki.businessparagon.ui.business.detail

import sheridan.yamazaki.businessparagon.model.Product
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import sheridan.yamazaki.businessparagon.R
import sheridan.yamazaki.businessparagon.databinding.ProductListItemBinding
import java.text.SimpleDateFormat
import java.util.*

class ProductListAdapter(
): ListAdapter<Product, ProductListAdapter.ViewHolder>(ProductDiffCallback())  {

    companion object{
        private val FORMAT = SimpleDateFormat("MM/dd/yyyy", Locale.US)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ProductListItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder constructor(
            private val binding: ProductListItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(product: Product) {
            binding.product = product
            if (product.picture != ""){
                Glide.with(binding.root)
                        .load(product.picture)
                        .into(binding.productPicture)
            }else{
                binding.productPicture.setImageResource(R.drawable.ic_launcher_background)
            }

            binding.productPrice.text = "$" + String.format("%.2f", product.unitPrice)
            binding.executePendingBindings()
        }

    }

    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}