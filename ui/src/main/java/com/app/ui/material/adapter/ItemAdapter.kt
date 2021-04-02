package com.app.ui.material.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ui.R
import com.app.ui.material.bean.ItemBean

class ItemAdapter(private val mContext: Context, private val data: MutableList<ItemBean>?) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    private var mListener: OnItemClickListener? = null
    fun setListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvRv.text = data?.get(position)?.name ?: "22"
        holder.tvRv.setOnClickListener {
            mListener?.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvRv: TextView

        init {
            tvRv = view.findViewById(R.id.tv_rv)
        }
    }
}