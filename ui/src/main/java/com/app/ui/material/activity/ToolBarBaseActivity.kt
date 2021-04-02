package com.app.ui.material.activity

import android.graphics.Color
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.app.ui.R
import com.app.ui.material.BaseActivity

/**
 * ToolBar的基础使用
 */
class ToolBarBaseActivity : BaseActivity() {
    var mToolbar: Toolbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_tool_bar_base)
        mToolbar = findViewById(R.id.toolbar)
        mToolbar?.inflateMenu(R.menu.toolbar_menu) //设置右上角的填充菜单
        mToolbar?.setNavigationIcon(R.mipmap.ic_drawer_home) //设置导航栏图标
        mToolbar?.setLogo(R.mipmap.ic_launcher) //设置app的logo
        mToolbar?.title = "主标题" //设置主标题
        mToolbar?.setTitleTextColor(Color.MAGENTA)
        mToolbar?.subtitle = "子标题" //设置子标题
        mToolbar?.setSubtitleTextColor(Color.WHITE)

        //设置菜单的点击事件
        mToolbar?.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_search -> {
                    Toast.makeText(this@ToolBarBaseActivity, R.string.menu_search, Toast.LENGTH_SHORT).show()
                }
                R.id.action_notification -> {
                    Toast.makeText(this@ToolBarBaseActivity, R.string.menu_notifications, Toast.LENGTH_SHORT).show()
                }
                R.id.action_item01 -> {
                    Toast.makeText(this@ToolBarBaseActivity, R.string.item01, Toast.LENGTH_SHORT).show()
                }
                R.id.action_item02 -> {
                    Toast.makeText(this@ToolBarBaseActivity, R.string.item02, Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
    }
}