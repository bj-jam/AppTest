package com.app.test.circle;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;

import com.bumptech.glide.Glide;
import com.app.test.R;
import com.app.test.utils.DisplayUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 圆形布局
 */
public class CirclePicWidget extends ConstraintLayout {
    protected CircleImageView circleImageView;
    protected CircleImageView circleImageView1;
    protected CircleImageView circleImageView2;
    protected CircleImageView circleImageView3;
    protected CircleImageView circleImageView4;

    private LayoutParams layoutParams;
    private LayoutParams layoutParams1;
    private LayoutParams layoutParams2;
    private LayoutParams layoutParams3;
    private LayoutParams layoutParams4;
    private int initSize;
    private int initAngle;
    private float firstAngle;
    int width = DisplayUtil.dip2px(getContext(), 40);
    int radius;


    public CirclePicWidget(@NonNull Context context) {
        super(context);
        initView();
    }

    public CirclePicWidget(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CirclePicWidget(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
        View.inflate(getContext(), R.layout.view_circel_pic, this);
        circleImageView = findViewById(R.id.civ1);
        circleImageView1 = findViewById(R.id.civ2);
        circleImageView2 = findViewById(R.id.civ3);
        circleImageView3 = findViewById(R.id.civ4);
        circleImageView4 = findViewById(R.id.civ5);
        layoutParams = (LayoutParams) circleImageView.getLayoutParams();
        layoutParams1 = (LayoutParams) circleImageView1.getLayoutParams();
        layoutParams2 = (LayoutParams) circleImageView2.getLayoutParams();
        layoutParams3 = (LayoutParams) circleImageView3.getLayoutParams();
        layoutParams4 = (LayoutParams) circleImageView4.getLayoutParams();
    }

    /**
     * 设置数据
     *
     * @param picList
     */
    public void setPicList(List<String> picList) {
        setViewHide();
        handleCount(picList == null ? 0 : picList.size());
        if (picList != null && picList.size() > 0) {
            for (int i = 0; i < picList.size(); i++) {
                CircleImageView view = null;
                LayoutParams params = null;
                if (i == 0) {
                    view = circleImageView;
                    params = layoutParams;
                } else if (i == 1) {
                    view = circleImageView1;
                    params = layoutParams1;
                } else if (i == 2) {
                    view = circleImageView2;
                    params = layoutParams2;
                } else if (i == 3) {
                    view = circleImageView3;
                    params = layoutParams3;
                } else if (i == 4) {
                    view = circleImageView4;
                    params = layoutParams4;
                }
                view.setVisibility(VISIBLE);
                final String url = picList.get(i);
                params.width = initSize;
                params.height = initSize;
                params.circleRadius = radius;
                params.circleAngle = firstAngle + i * initAngle;
                view.setLayoutParams(params);
                Glide.with(getContext()).load(url).asBitmap().placeholder(R.drawable.icon_project_pic).centerCrop().into(view);
            }
        } else {
            CircleImageView view = circleImageView;
            LayoutParams params = layoutParams;
            view.setVisibility(VISIBLE);
            params.width = initSize;
            params.height = initSize;
            params.circleRadius = radius;
            params.circleAngle = firstAngle;
            view.setLayoutParams(params);
            Glide.with(getContext()).load(R.drawable.icon_project_pic)
                    .asBitmap()
                    .placeholder(R.drawable.icon_project_pic)
                    .centerCrop().into(view);
        }
        postInvalidate();
    }


    /**
     * 处理数据
     *
     * @param count
     */
    private void handleCount(int count) {
        if (count == 0 || count == 1) {
            initSize = DisplayUtil.dip2px(getContext(), 40);
            initAngle = 0;
            firstAngle = 0;
        } else if (count == 2) {
            initSize = DisplayUtil.dip2px(getContext(), 22);
            initAngle = 180;
            firstAngle = 90;
        } else if (count == 3) {
            initSize = DisplayUtil.dip2px(getContext(), 22);
            initAngle = 120;
            firstAngle = 120;
        } else if (count == 4) {
            initSize = DisplayUtil.dip2px(getContext(), 20);
            initAngle = 90;
            firstAngle = 45;
        } else {
            initSize = DisplayUtil.dip2px(getContext(), 18);
            initAngle = 72;
            firstAngle = 72;
        }
        if (count == 4)
            radius = width / 2 - initSize / 2 + 5;
        else
            radius = width / 2 - initSize / 2;

    }

    private void setViewHide() {
        if (circleImageView != null)
            circleImageView.setVisibility(GONE);
        if (circleImageView1 != null)
            circleImageView1.setVisibility(GONE);
        if (circleImageView2 != null)
            circleImageView2.setVisibility(GONE);
        if (circleImageView3 != null)
            circleImageView3.setVisibility(GONE);
        if (circleImageView4 != null)
            circleImageView4.setVisibility(GONE);
    }


}
