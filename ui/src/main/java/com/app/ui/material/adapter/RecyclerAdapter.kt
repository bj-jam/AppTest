package com.app.ui.material.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ui.R
import com.app.ui.material.adapter.RecyclerAdapter.MainViewHolder

class RecyclerAdapter(private val mContext: Context, private val data: List<String>?) : RecyclerView.Adapter<MainViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_main, parent, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.tvNum.text = data!![position]
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvNum: TextView = view.findViewById(R.id.tv_num)

    }
}