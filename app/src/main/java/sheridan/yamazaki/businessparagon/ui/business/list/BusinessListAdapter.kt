package sheridan.yamazaki.businessparagon.ui.business.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import sheridan.yamazaki.businessparagon.R
import sheridan.yamazaki.businessparagon.databinding.BusinessListItemBinding
import sheridan.yamazaki.businessparagon.model.Business


class BusinessListAdapter(
    private val onClick: (Business) -> Unit
): ListAdapter<Business, BusinessListAdapter.ViewHolder>(BusinessDiffCallback())  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = BusinessListItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder constructor(
        private val binding: BusinessListItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(business: Business) {
            // Load image
            binding.business = business
            if (business.logo != ""){
                Glide.with(binding.root)
                        .load(business.logo)
                        .into(binding.businessLogo)
            }else{
                binding.businessLogo.setImageResource(R.drawable.ic_launcher_background)
            }
            binding.root.setOnClickListener { onClick(business) }
            binding.executePendingBindings()
        }
    }

    class BusinessDiffCallback : DiffUtil.ItemCallback<Business>() {
        override fun areItemsTheSame(oldItem: Business, newItem: Business): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Business, newItem: Business): Boolean {
            return oldItem == newItem
        }
    }
}
