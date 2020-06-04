package com.app.test.scrollview;

import android.app.Activity;
import android.os.Bundle;

import com.app.test.R;

public class ScrollViewActivity extends Activity {

    private static final String TAG = "ScrollViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_view);
    }
}
