package ddwu.com.mobile.miniproject2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ddwu.com.mobile.miniproject2.data.MyLoc
import ddwu.com.mobile.miniproject2.databinding.LocsItemBinding


class LocationAdapter() : RecyclerView.Adapter<LocationAdapter.LocViewHolder>() {

    var locations: List<MyLoc> = emptyList()

    override fun getItemCount() = locations?.size ?: 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocViewHolder {
        val locsBinding = LocsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocViewHolder(locsBinding)
    }

    override fun onBindViewHolder(
        holder: LocViewHolder,
        position: Int
    ) {
        holder.locsBinding.tvLocation.text = locations.get(position)?.locTitle
        holder.locsBinding.clItem.setOnClickListener{   // 항목 클릭 이벤트 처리
            clickListener?.onItemClick(it, position)
        }
        holder.locsBinding.clItem.setOnLongClickListener {
            longClickListener?.onLongClick(it, position)
            true
        }
    }

    class LocViewHolder(val locsBinding: LocsItemBinding) : RecyclerView.ViewHolder(locsBinding.root)


    /*adapter 항목 클릭 이벤트 처리를 위한 인터페이스 및 필요함수 정의*/
    interface OnLocClickListener {
        fun onItemClick(view: View, position: Int)
    }
    var clickListener: OnLocClickListener? = null
    fun setOnLocClickListener(listener: OnLocClickListener) {
        this.clickListener = listener
    }

    fun setList(newList: List<MyLoc>) {
        locations = newList
        notifyDataSetChanged()
    }

    //롱클릭 -> 삭제
    interface OnLocLongClickListener {
        fun onLongClick(view: View, position: Int)
    }
    var longClickListener: OnLocLongClickListener? = null
    fun setOnLocLongClickListener(listener: OnLocLongClickListener) {
        longClickListener = listener
    }

}

