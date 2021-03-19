package com.app.ui.zhibo

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.app.ui.R
import com.app.ui.zhibo.utils.ViewHolderUtil

/**
 * 评论列表适配器
 */
class MessageAdapter(private val mContext: Context?, private var data: List<String>?) : BaseAdapter() {
    override fun getCount(): Int {
        return if (data == null) 0 else data!!.size
    }

    override fun getItem(position: Int): String {
        return data!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun notifyAdapter(data: List<String>?) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View = convertView ?: View.inflate(mContext, R.layout.item_messageadapter, null)
        // 评论
        val tv_msg = ViewHolderUtil.getView<TextView>(view, R.id.tv_msg)
        tv_msg.text = data!![position]
        return view
    }
}