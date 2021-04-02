package com.app.ui.material.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ui.R
import com.app.ui.material.adapter.ItemOperationAdapter.AddRemoveItemViewHolder

/**
 * 添加或者删除item适配器
 */
class ItemOperationAdapter(private val mContext: Context, private val data: MutableList<String>?) : RecyclerView.Adapter<AddRemoveItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddRemoveItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_add_remove, parent, false)
        return AddRemoveItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddRemoveItemViewHolder, position: Int) {
        holder.tvGoods.text = data?.get(position)
        holder.tvDelete.setOnClickListener { removeData(position) }
    }

    private fun removeData(position: Int) {
        data?.removeAt(position)
        //删除动画
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount) //必须添加这一行，否则删除会有问题
    }

    fun addData(position: Int) {
        data?.add(position, "我是商品" + data.size)
        //添加动画
        notifyItemInserted(position)
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    class AddRemoveItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvGoods: TextView = view.findViewById(R.id.tv_goods)
        var tvDelete: TextView = view.findViewById(R.id.tv_delete)
//        init {
//            tvGoods = view.findViewById(R.id.tv_goods)
//            tvDelete = view.findViewById(R.id.tv_delete)
//        }
    }
}