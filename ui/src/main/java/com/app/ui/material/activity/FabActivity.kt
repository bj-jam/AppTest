package com.app.ui.material.activity

import android.os.Bundle
import android.view.View
import com.app.ui.R
import com.app.ui.material.BaseActivity
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar

class FabActivity : BaseActivity(), View.OnClickListener {
    var mColl: CollapsingToolbarLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fab)
        mColl = findViewById(R.id.coll)
        findViewById<View>(R.id.fab2).setOnClickListener(this)
        findViewById<View>(R.id.fab1).setOnClickListener(this)
        mColl?.title = "阳春白雪"
    }

    override fun onClick(view: View) {
        val id = view.id
        if (id == R.id.fab1) {
            Snackbar.make(view, "SnackBar", Snackbar.LENGTH_LONG).show()
        }
    }
}