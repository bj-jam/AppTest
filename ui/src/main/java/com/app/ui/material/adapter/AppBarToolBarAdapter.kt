package com.app.ui.material.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ui.R

/**
 */
class AppBarToolBarAdapter : RecyclerView.Adapter<AppBarToolBarAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app_bar_tool_bar, null)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.text.text = "" + position
    }

    override fun getItemCount(): Int {
        return 30
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var text: TextView = itemView.findViewById<View>(R.id.text) as TextView
    }
}