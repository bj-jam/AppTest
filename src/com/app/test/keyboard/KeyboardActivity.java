package com.app.test.keyboard;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;

import com.app.test.R;

/**
 *
 */

public class KeyboardActivity extends Activity {
    private ScrollView scrollView;
    private View mRootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);
        mRootView = findViewById(R.id.mRootView);
        mRootView.addOnLayoutChangeListener(mListener);
        scrollView = (ScrollView) findViewById(R.id.mScrollView);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyBoard();
                return false;
            }
        });
    }


    public View.OnLayoutChangeListener mListener = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            Log.i("jam", (oldBottom - bottom) + "\t" + oldBottom);
            if (oldBottom != 0 && (oldBottom - bottom) > 150) {
                srcoll();
            }
        }
    };

    public void srcoll() {
        int scrollY = scrollView.getHeight();
//        if (scrollY != 0) {
        scrollView.smoothScrollTo(0, 99999);
//        }
    }

    private InputMethodManager manager;

    private void hideSoftKeyBoard() {
        if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
            if (manager == null) {
                manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            }
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
