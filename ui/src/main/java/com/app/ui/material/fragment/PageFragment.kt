package com.app.ui.material.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.app.ui.R

/**
 *
 */
class PageFragment : Fragment() {
    private var mPage = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPage = arguments!!.getInt(ARGS_PAGE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_page, container, false)
        val textView = view.findViewById<View>(R.id.textView) as TextView
        textView.text = "第" + mPage + "页"
        return view
    }

    companion object {
        const val ARGS_PAGE = "args_page"

        @JvmStatic
        fun newInstance(page: Int): PageFragment {
            val args = Bundle()
            args.putInt(ARGS_PAGE, page)
            val fragment = PageFragment()
            fragment.arguments = args
            return fragment
        }
    }
}