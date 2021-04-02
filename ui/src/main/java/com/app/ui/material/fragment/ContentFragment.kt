package com.app.ui.material.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.app.ui.R

class ContentFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_content, container, false)
        val tv = view.findViewById<View>(R.id.menu_title) as TextView
        val title = arguments!![KEY_TITLE] as String
        if (!TextUtils.isEmpty(title)) {
            tv.text = title
        }
        return view
    }

    companion object {
        private const val KEY_TITLE = "key_title"

        @JvmStatic
        fun newInstance(title: String?): ContentFragment {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            val fragment = ContentFragment()
            fragment.arguments = args
            return fragment
        }
    }
}