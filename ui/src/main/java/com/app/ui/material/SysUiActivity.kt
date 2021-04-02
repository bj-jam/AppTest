package com.app.ui.material

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.ui.R
import com.app.ui.material.activity.*
import com.app.ui.material.adapter.ItemAdapter
import com.app.ui.material.bean.ItemBean

class SysUiActivity : BaseActivity(), ItemAdapter.OnItemClickListener {
    var recyclerView: RecyclerView? = null
    private var mAdapter: ItemAdapter? = null
    private var itemDates: MutableList<ItemBean>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_main)
        recyclerView = findViewById(R.id.recycler_view)
        initData()
        mAdapter = ItemAdapter(this, itemDates)
        mAdapter?.setListener(this)
        recyclerView?.layoutManager = GridLayoutManager(this, 2)
        recyclerView?.adapter = mAdapter
    }

    private fun initData() {
        itemDates = mutableListOf();
        itemDates?.add(ItemBean(getString(R.string.appbar_study), AppbarLayoutActivity::class.java))
        itemDates?.add(ItemBean(getString(R.string.fab_study), FabActivity::class.java))
        itemDates?.add(ItemBean(getString(R.string.ali_home), AliHomeActivity::class.java))
        itemDates?.add(ItemBean(getString(R.string.copy_airbnb_home), AirbnbHomeActivity::class.java))
        itemDates?.add(ItemBean(getString(R.string.tab_coordinator_layout), Instance3Activity::class.java))
        itemDates?.add(ItemBean(getString(R.string.with_snackbar), SnackBarActivity::class.java))
        itemDates?.add(ItemBean(getString(R.string.with_appbar_layout), AppBarToolBarActivity::class.java))
        itemDates?.add(ItemBean(getString(R.string.display_icon), CustomTabViewActivity::class.java))
        itemDates?.add(ItemBean(getString(R.string.custom_tab_layout), CustomTabViewActivity::class.java))
        itemDates?.add(ItemBean(getString(R.string.main_toolbar), ToolBarBaseActivity::class.java))
        itemDates?.add(ItemBean(getString(R.string.toolbar_1), ToolbarFirstActivity::class.java))
        itemDates?.add(ItemBean(getString(R.string.toolbar_3), ToolbarSecondActivity::class.java))
        itemDates?.add(ItemBean(getString(R.string.recyclerview_click_delete), DragRecyclerViewActivity::class.java))
        itemDates?.add(ItemBean(getString(R.string.rv_add_remove_item), ItemOperationActivity::class.java))
    }

    override fun onItemClick(position: Int) {
        startActivity(Intent(this, itemDates?.get(position)?.aClass))
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return super.onTouchEvent(event)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return super.dispatchTouchEvent(ev)
    }
}