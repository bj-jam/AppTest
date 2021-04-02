package com.app.ui.material.activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.ui.R
import com.app.ui.material.BaseActivity
import com.app.ui.material.anim.CustomItemAnimator
import com.app.ui.material.adapter.ItemOperationAdapter
import java.util.*

class ItemOperationActivity : BaseActivity() {
    var rvGoodsList: RecyclerView? = null
    private val list: MutableList<String> = ArrayList()
    private var mOperationAdapter: ItemOperationAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_remove_item)
        rvGoodsList = findViewById(R.id.rv_goods_list)
        findViewById<View>(R.id.ll_add_goods).setOnClickListener { mOperationAdapter?.addData(list.size) }
        list.add("电话0")
        list.add("电话1")
        mOperationAdapter = ItemOperationAdapter(this, list)
        rvGoodsList?.layoutManager = LinearLayoutManager(this)
        rvGoodsList?.adapter = mOperationAdapter

        val itemAnimator = CustomItemAnimator()
        itemAnimator.addDuration = 500
        itemAnimator.removeDuration = 500
        rvGoodsList?.itemAnimator = itemAnimator
    }
}