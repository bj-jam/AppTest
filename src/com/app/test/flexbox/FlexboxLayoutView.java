package com.app.test.flexbox;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.app.test.R;

/**
 * @author lcx
 * Created at 2020.4.1
 * Describe:
 */
public class FlexboxLayoutView extends FrameLayout {
    public FlexboxLayoutView(Context context) {
        super(context);
        initView();
    }

    public FlexboxLayoutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public FlexboxLayoutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    void initView() {
        View.inflate(getContext(), R.layout.view_flexbox, this);
    }
}
