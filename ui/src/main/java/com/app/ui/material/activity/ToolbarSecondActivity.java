package com.app.ui.material.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.app.ui.R;
import com.app.ui.material.BaseActivity;


public class ToolbarSecondActivity extends BaseActivity {
    Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar_instance3);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        //与drawerLayout的结合使用
        //显示左侧菜单图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
//        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(android.view.MenuItem item) {
//                int itemId = item.getItemId();
//                if (itemId == R.id.ab_search) {
//                    Toast.makeText(ToolbarInstance3Activity.this, "1", Toast.LENGTH_SHORT).show();
//                } else if (itemId == R.id.action_shares) {
//                    Toast.makeText(ToolbarInstance3Activity.this, "2", Toast.LENGTH_SHORT).show();
//                } else if (itemId == R.id.action_settings) {
//                    Toast.makeText(ToolbarInstance3Activity.this, "3", Toast.LENGTH_SHORT).show();
//                }
//                return true;
//            }
//        });
    }

    /**
     * 也可以用onOptionsItemSelected实现菜单item的点击事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.ab_search) {
            Toast.makeText(ToolbarSecondActivity.this, "1", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.action_shares) {
            Toast.makeText(ToolbarSecondActivity.this, "2", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.action_settings) {
            Toast.makeText(ToolbarSecondActivity.this, "3", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.instance3_menu, menu);
        return true;
    }
}
