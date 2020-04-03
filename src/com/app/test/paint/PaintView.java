package com.app.test.paint;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.app.test.R;

/**
 * @author lcx
 * Created at 2020.4.3
 * Describe:
 */
public class PaintView extends FrameLayout {

    public PaintView(Context context) {
        super(context);
        initView();
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PaintView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.layout_paint_normal, this);
    }
}
