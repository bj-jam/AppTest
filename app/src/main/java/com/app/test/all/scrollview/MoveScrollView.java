package com.app.test.all.scrollview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.test.R;

import java.util.ArrayList;

public class MoveScrollView extends FrameLayout {

    private IdeaViewPager viewPager;
    private IdeaScrollView ideaScrollView;
    private TextView text;
    private LinearLayout header;
    private RadioGroup radioGroup;
    private LinearLayout headerParent;
    private ImageView icon;
    private View layer;
    private float currentPercentage = 0;
    private RadioGroup.OnCheckedChangeListener radioGroupListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                radioButton.setTextColor(radioButton.isChecked() ? getRadioCheckedAlphaColor(currentPercentage) : getRadioAlphaColor(currentPercentage));
                if (radioButton.isChecked() && isNeedScrollTo) {
                    ideaScrollView.setPosition(i);
                }
            }
        }
    };
    private boolean isNeedScrollTo = true;

    public MoveScrollView(@NonNull Context context) {
        super(context);
        initView();
    }

    public MoveScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MoveScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public MoveScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    protected void initView() {
        View.inflate(getContext(), R.layout.view_scroll_view, this);
//        StatusBarCompat.translucentStatusBar(this);
        header = (LinearLayout) findViewById(R.id.header);
        headerParent = (LinearLayout) findViewById(R.id.headerParent);
        icon = (ImageView) findViewById(R.id.icon);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        ideaScrollView = (IdeaScrollView) findViewById(R.id.ideaScrollView);
        viewPager = (IdeaViewPager) findViewById(R.id.viewPager);
        layer = findViewById(R.id.layer);

        Rect rectangle = new Rect();
        ((Activity) getContext()).getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);
        ideaScrollView.setViewPager(viewPager, getMeasureHeight(headerParent) - rectangle.top);
        icon.setImageAlpha(0);
        radioGroup.setAlpha(0);
        radioGroup.check(radioGroup.getChildAt(0).getId());

        View one = findViewById(R.id.one);
        View two = findViewById(R.id.two);
        View four = findViewById(R.id.four);
        View three = findViewById(R.id.three);
        ArrayList<Integer> araryDistance = new ArrayList<>();

        araryDistance.add(0);
        araryDistance.add(getMeasureHeight(one) - getMeasureHeight(headerParent));
        araryDistance.add(getMeasureHeight(one) + getMeasureHeight(two) - getMeasureHeight(headerParent));
        araryDistance.add(getMeasureHeight(one) + getMeasureHeight(two) + getMeasureHeight(three) - getMeasureHeight(headerParent));

        ideaScrollView.setArrayDistance(araryDistance);

        ideaScrollView.setOnScrollChangedColorListener(new IdeaScrollView.OnScrollChangedColorListener() {
            @Override
            public void onChanged(float percentage) {

                int color = getAlphaColor(percentage > 0.9f ? 1.0f : percentage);
                header.setBackgroundDrawable(new ColorDrawable(color));
                radioGroup.setBackgroundDrawable(new ColorDrawable(color));
                icon.setImageAlpha((int) ((percentage > 0.9f ? 1.0f : percentage) * 255));
                radioGroup.setAlpha((percentage > 0.9f ? 1.0f : percentage) * 255);

                setRadioButtonTextColor(percentage);

            }

            @Override
            public void onChangedFirstColor(float percentage) {

            }

            @Override
            public void onChangedSecondColor(float percentage) {

            }
        });

        ideaScrollView.setOnSelectedIndicateChangedListener(new IdeaScrollView.OnSelectedIndicateChangedListener() {
            @Override
            public void onSelectedChanged(int position) {
                isNeedScrollTo = false;
                radioGroup.check(radioGroup.getChildAt(position).getId());
                isNeedScrollTo = true;
            }
        });

        radioGroup.setOnCheckedChangeListener(radioGroupListener);
    }

    public void setRadioButtonTextColor(float percentage) {
        if (Math.abs(percentage - currentPercentage) >= 0.1f) {
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                radioButton.setTextColor(radioButton.isChecked() ? getRadioCheckedAlphaColor(percentage) : getRadioAlphaColor(percentage));
            }
            this.currentPercentage = percentage;
        }
    }

    public int getMeasureHeight(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        return view.getMeasuredHeight();
    }

    public int getAlphaColor(float f) {
        return Color.argb((int) (f * 255), 0x09, 0xc1, 0xf4);
    }

    public int getLayerAlphaColor(float f) {
        return Color.argb((int) (f * 255), 0x09, 0xc1, 0xf4);
    }

    public int getRadioCheckedAlphaColor(float f) {
        return Color.argb((int) (f * 255), 0x44, 0x44, 0x44);
    }

    public int getRadioAlphaColor(float f) {
        return Color.argb((int) (f * 255), 0xFF, 0xFF, 0xFF);
    }
}
