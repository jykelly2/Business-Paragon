package sheridan.yamazaki.businessparagon.ui.business.checkout

import sheridan.yamazaki.businessparagon.model.Product
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import sheridan.yamazaki.businessparagon.R
import sheridan.yamazaki.businessparagon.databinding.CheckoutListItemBinding
import sheridan.yamazaki.businessparagon.model.Business
import java.text.SimpleDateFormat
import java.util.*

class CheckoutListAdapter(
        val products: ArrayList<Product>,
        val payment: Boolean,
        private val onClick: (Int) -> Unit
): ListAdapter<Product, CheckoutListAdapter.ViewHolder>(ProductDiffCallback())  {

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CheckoutListItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(products[position], position)
    }

    inner class ViewHolder constructor(
            private val binding: CheckoutListItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(product: Product, position: Int) {
            binding.product = product
            if (product.picture != ""){
                Glide.with(binding.root)
                        .load(product.picture)
                        .into(binding.productPicture)
            }else{
                binding.productPicture.setImageResource(R.drawable.ic_launcher_background)
            }

            binding.totalItemPrice.text = "C$" + String.format("%.2f", (product.unitPrice?.times(product.quantity!!)))
            binding.productPrice.text ="Unit Price: C$"+ String.format("%.2f", product.unitPrice)
            if (!payment) binding.deleteButton.setOnClickListener { onClick(position) }
            else binding.deleteButton.visibility = View.INVISIBLE
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