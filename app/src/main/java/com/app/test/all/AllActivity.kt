package com.app.test.all

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.app.test.R
import com.app.test.all.scrollview.MoveScrollView
import com.app.test.all.view.LoginView
import com.app.test.all.view.TextInputLayout
import com.app.test.path.ViewAdapter
import kotlinx.android.synthetic.main.activity_all.*

/**
 *
 * @author lcx
 * Created at 2020.8.21
 * Describe:
 */
class AllActivity : AppCompatActivity() {


    var adapter: ViewAdapter? = null
    val dataList: ArrayList<View> by lazy { ArrayList<View>() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all)
        initDate()
    }

    private fun initDate() {
        dataList.clear()
        dataList.add(TextInputLayout(this))
        dataList.add(LoginView(this))
        dataList.add(MoveScrollView(this))
        adapter = ViewAdapter(dataList);

        mViewPager?.adapter = adapter;
    }
}