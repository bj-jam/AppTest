package com.app.test.all

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.test.R
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author lcx
 * Created at 2021/3/10
 * Describe:
 */
public class UiEntryActivity : Activity() {

    private lateinit var adapter: UiEntryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        listView.layoutManager = GridLayoutManager(this, 3)
        listView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        listView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL))
        adapter = UiEntryAdapter()
        listView.adapter = adapter
    }
}