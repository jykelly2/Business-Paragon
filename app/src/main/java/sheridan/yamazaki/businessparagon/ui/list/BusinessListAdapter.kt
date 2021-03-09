package sheridan.yamazaki.businessparagon.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import sheridan.yamazaki.businessparagon.R
import sheridan.yamazaki.businessparagon.databinding.BusinessListItemBinding
import sheridan.yamazaki.businessparagon.model.Business
//import com.bumptech.glide.Glide

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
            //val resources = binding.root.resources

            // Load image
       //     Glide.with(binding.businessLogo.context)
            //        .load(business.logo)
            //        .into(binding.businessLogo)

            binding.business = business
            if (business.logo != ""){
                Glide.with(binding.root)
                        .load(business.logo)
                        .into(binding.businessLogo)
            }else{
                binding.businessLogo.setImageResource(R.drawable.ic_launcher_background)
            }

          //  binding.businessName.text = business.name
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
/*class BusinessListAdapter (): ListAdapter<Business, BusinessListAdapter.ViewHolder>(BusinessDiffCallback()){

    class ViewHolder private constructor(
        private val binding: BusinessListItemBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(count: Int, Business: Business){
            binding.count.text = binding.root.context.getString(R.string.count, count)
            binding.business = Business
           /* val media = "http://tetervak.dev.fast.sheridanc.on.ca/Examples/jQuery/Businesss3/images/Businesss/"+Business.pictureSM
            if (media !== null) {
                Glide.with(binding.root)
                    .load(media)
                    .into(binding.BusinessImage)
            } else {
                binding.BusinessImage.setImageResource(R.drawable.ic_launcher_background)
            }*/
            binding.businessLogo.setImageResource(R.drawable.ic_launcher_background)
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = BusinessListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position + 1, getItem(position))
        val business = getItem(position)

        holder.itemView.setOnClickListener(){ v -> showDetails(v, business.id) }
    }

    private fun showDetails(v: View, id: Long) {
        Log.d("details", id.toString())
      //  val action = BusinessListFragmentDirections.actionInputToOutput(id)
        //Navigation.findNavController(v).navigate(action)
    }

    class BusinessDiffCallback : DiffUtil.ItemCallback<Business>() {
        override fun areItemsTheSame(oldItem: Business, newItem: Business): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Business, newItem: Business): Boolean {
            return oldItem == newItem
        }
    }
}*/