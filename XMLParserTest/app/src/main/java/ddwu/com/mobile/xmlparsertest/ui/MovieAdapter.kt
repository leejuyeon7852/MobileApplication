package ddwu.com.mobile.movieparsertest.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ddwu.com.mobile.xmlparsertest.data.DailyBoxOffice
import ddwu.com.mobile.xmlparsertest.databinding.ListItemBinding

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.ItemHolder>(){

    var items: List<DailyBoxOffice>? = null

    override fun getItemCount(): Int = items?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.itemBinding.tvItem.text = (items?.get(position) as DailyBoxOffice).toString()
    }

    class ItemHolder(val itemBinding: ListItemBinding) : RecyclerView.ViewHolder(itemBinding.root)

}