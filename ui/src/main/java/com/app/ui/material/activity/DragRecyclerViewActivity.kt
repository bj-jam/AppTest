package com.app.ui.material.activity

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.app.ui.R
import com.app.ui.material.BaseActivity
import com.app.ui.material.bean.TagBean
import com.app.ui.material.adapter.DragRecyclerViewAdapter
import com.app.ui.material.listener.ItemDragHelperCallback
import java.util.*

/**
 * RecyclerView拖拽排序，点击删除
 */
class DragRecyclerViewActivity : BaseActivity() {
    var mRecyclerView: RecyclerView? = null
    private val topItems: MutableList<TagBean> = ArrayList()
    private val bottomItems: MutableList<TagBean> = ArrayList()
    private var adapter: DragRecyclerViewAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classify)
        mRecyclerView = findViewById(R.id.recy)
        initData()
        init()
    }

    private fun init() {
        val manager = GridLayoutManager(this, 4)
        mRecyclerView?.layoutManager = manager
        val callback = ItemDragHelperCallback()
        val helper = ItemTouchHelper(callback)
        //将ItemTouchHelper和RecyclerView建立关联
        helper.attachToRecyclerView(mRecyclerView)
        adapter = DragRecyclerViewAdapter(this, helper, topItems, bottomItems, 1)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = adapter?.getItemViewType(position)
                //从效果可知，当是两个头布局的时候要占据一行
                return if (viewType == DragRecyclerViewAdapter.TYPE_MY || viewType == DragRecyclerViewAdapter.TYPE_OTHER) 1 else 4
            }
        }
        mRecyclerView?.adapter = adapter
        adapter?.setOnMyChannelItemClickListener { _, _ -> Toast.makeText(this@DragRecyclerViewActivity, "要跳转了", Toast.LENGTH_SHORT).show() }
        adapter?.setmCompleteListener { Toast.makeText(this@DragRecyclerViewActivity, "完成了", Toast.LENGTH_SHORT).show() }
    }

    private fun initData() {
        topItems.add(TagBean("11", 1))
        topItems.add(TagBean("22", 2))
        topItems.add(TagBean("33", 3))
        topItems.add(TagBean("44", 4))
        topItems.add(TagBean("55", 5))
        topItems.add(TagBean("66", 6))
        topItems.add(TagBean("77", 7))
        topItems.add(TagBean("88", 8))
        topItems.add(TagBean("99", 9))
        topItems.add(TagBean("111", 10))
        topItems.add(TagBean("222", 2))
        topItems.add(TagBean("333", 3))
        topItems.add(TagBean("444", 4))
        topItems.add(TagBean("555", 5))
        topItems.add(TagBean("666", 6))
        topItems.add(TagBean("777", 7))
        topItems.add(TagBean("888", 8))
        topItems.add(TagBean("999", 9))
        topItems.add(TagBean("1111", 10))
        bottomItems.add(TagBean("小说", 11))
        bottomItems.add(TagBean("时尚", 12))
        bottomItems.add(TagBean("历史", 13))
        bottomItems.add(TagBean("育儿", 14))
        bottomItems.add(TagBean("搞笑", 15))
        bottomItems.add(TagBean("数码", 16))
        bottomItems.add(TagBean("养生", 17))
        bottomItems.add(TagBean("电影", 18))
        bottomItems.add(TagBean("手机", 19))
        bottomItems.add(TagBean("旅游", 20))
        bottomItems.add(TagBean("时尚", 21))
        bottomItems.add(TagBean("历史", 22))
        bottomItems.add(TagBean("育儿", 23))
        bottomItems.add(TagBean("搞笑", 24))
        bottomItems.add(TagBean("数码", 25))
        bottomItems.add(TagBean("养生", 26))
        bottomItems.add(TagBean("电影", 27))
        bottomItems.add(TagBean("手机", 28))
        bottomItems.add(TagBean("旅游", 29))
    }
}