package com.app.test.game.api;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.Calendar;

public abstract class NoFastClickListener implements BaseQuickAdapter.OnItemClickListener {
    private int hashCode;

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (hashCode != view.hashCode()) {
            lastClickTime = currentTime;
            onNoDoubleClick(adapter, view, position);
            hashCode = view.hashCode();
            return;
        }
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(adapter, view, position);
        }
    }

    private static final int MIN_CLICK_DELAY_TIME = 900;
    private long lastClickTime = 0;

    public abstract void onNoDoubleClick(BaseQuickAdapter adapter, View view, int position);
}
