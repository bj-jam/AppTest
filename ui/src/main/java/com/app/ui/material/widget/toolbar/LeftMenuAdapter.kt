package com.app.ui.material.widget.toolbar

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.app.ui.R

/**
 *
 */
class LeftMenuAdapter(private val mContext: Context, objects: Array<MenuItem?>?) : ArrayAdapter<MenuItem?>(mContext, -1, objects) {
    private val mInflater: LayoutInflater = LayoutInflater.from(mContext)
    private var mSelected = 0
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val convertView = view ?: mInflater.inflate(R.layout.item_left_menu, parent, false)
        val icon = convertView.findViewById<View>(R.id.item_icon) as ImageView
        val title = convertView.findViewById<View>(R.id.item_title) as TextView
        val item = getItem(position)
        title.text = item?.text
        icon.setImageResource(item?.icon ?: 0)
        convertView?.setBackgroundColor(Color.TRANSPARENT)
        if (position == mSelected) {
            icon.setImageResource(getItem(position)!!.iconSelected)
            convertView?.setBackgroundColor(mContext.resources.getColor(R.color.state_menu_item_selected))
        }
        return convertView
    }

    fun setSelected(position: Int) {
        mSelected = position
        notifyDataSetChanged()
    }

}