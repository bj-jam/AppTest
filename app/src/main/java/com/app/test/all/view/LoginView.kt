package com.app.test.all.view

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v4.app.FragmentActivity
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.app.test.R
import com.app.test.all.model.LoginModel
import com.app.test.databinding.ViewDataBindLayoutBinding

/**
 *
 * @author lcx
 * Created at 2020.9.18
 * Describe:
 */
class LoginView constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : FrameLayout(context, attrs, defStyle) {
    lateinit var binding: ViewDataBindLayoutBinding
    lateinit var viewModel: LoginModel

    companion object {

    }

    init {
        val factory = LayoutInflater.from(context)
        viewModel = LoginModel(context as FragmentActivity)
        binding = DataBindingUtil.inflate(factory, R.layout.view_data_bind_layout, this, true)
        binding.viewModel = viewModel
    }


}