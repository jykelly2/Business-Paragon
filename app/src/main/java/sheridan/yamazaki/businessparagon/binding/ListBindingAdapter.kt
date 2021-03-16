package sheridan.yamazaki.businessparagon.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter

@BindingAdapter("listData")
fun <T>bindRecyclerView(recyclerView: RecyclerView, list: List<T>?) {
    @Suppress("UNCHECKED_CAST")
    val adapter = recyclerView.adapter as ListAdapter<T, *>
    adapter.submitList(list)
}