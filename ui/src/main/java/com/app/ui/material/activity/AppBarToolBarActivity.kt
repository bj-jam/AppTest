package com.app.ui.material.activity

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.ui.R
import com.app.ui.material.BaseActivity
import com.app.ui.material.adapter.AppBarToolBarAdapter

class AppBarToolBarActivity : BaseActivity() {
    var mRecycler: RecyclerView? = null
    var mToolbar: Toolbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aapbar_toolbar)
        mRecycler = findViewById(R.id.recycler)
        mToolbar = findViewById(R.id.toolbar)
        mToolbar?.title = "Material Design"
        setSupportActionBar(mToolbar)
        val manager = LinearLayoutManager(this)
        val adapter = AppBarToolBarAdapter()
        mRecycler?.layoutManager = manager
        mRecycler?.adapter = adapter
    }
}