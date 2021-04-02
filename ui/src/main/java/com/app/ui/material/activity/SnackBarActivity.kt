package com.app.ui.material.activity

import android.os.Bundle
import com.app.ui.R
import com.app.ui.material.BaseActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class SnackBarActivity : BaseActivity() {
    var mFab: FloatingActionButton? = null
    private var mSnackBar: Snackbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_combine_snackbar)
        mFab = findViewById(R.id.fab)
        mFab?.setOnClickListener { v ->
            if (mSnackBar == null) {
                mSnackBar = Snackbar.make(v, "快来往这边看，哈哈哈", Snackbar.LENGTH_LONG).setAction("cancel") { }
            }
            if (mSnackBar?.isShown == true) mSnackBar?.dismiss() else {
                mSnackBar?.show()
            }
        }
    }
}