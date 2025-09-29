package ddwu.com.mobile.retrofittest.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ddwu.com.mobile.retrofittest.data.DailyBoxOfficeMovie
import ddwu.com.mobile.retrofittest.databinding.ListItemBinding

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.ItemHolder>(){

    var items: List<DailyBoxOfficeMovie>? = null

    override fun getItemCount(): Int = items?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.itemBinding.tvItem.text = (items?.get(position) as DailyBoxOfficeMovie).toString()
    }

    class ItemHolder(val itemBinding: ListItemBinding) : RecyclerView.ViewHolder(itemBinding.root)

}