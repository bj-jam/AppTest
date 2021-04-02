package com.app.ui.material.activity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;

import com.app.ui.R;
import com.app.ui.material.BaseActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;


public class AirbnbHomeActivity extends BaseActivity {

    ImageView imageview;
    ImageView ivArrow;
    LinearLayout llTitle;
    Toolbar toolbar;
    LinearLayout llContent;
    TabLayout tablayout;
    CollapsingToolbarLayout toolbarLayout;
    AppBarLayout appBar;

    CollapsingToolbarLayoutState mLayoutState = CollapsingToolbarLayoutState.EXPANDED;

    private final String[] TITLE = {"1", "2", "3", "4"};
    private AnimationSet mShowSet;
    private AnimationSet mHideSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_airbnb_app_home_nav);
        imageview = findViewById(R.id.imageview);
        ivArrow = findViewById(R.id.iv_arrow);
        llTitle = findViewById(R.id.ll_title);
        toolbar = findViewById(R.id.toolbar);
        llContent = findViewById(R.id.ll_content);
        tablayout = findViewById(R.id.tablayout);
        toolbarLayout = findViewById(R.id.toolbar_layout);
        appBar = findViewById(R.id.app_bar);
        initTablayout();
        initData();
    }

    private void initData() {
        collapsingListener();
        showAnim();
        hideAnim();
    }

    private void hideAnim() {
        mHideSet = new AnimationSet(true);
        mHideSet.setInterpolator(new LinearInterpolator());
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        TranslateAnimation hiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f);
        mHideSet.addAnimation(alphaAnimation);
        mHideSet.addAnimation(hiddenAction);
        mHideSet.setDuration(300);
    }

    private void showAnim() {
        mShowSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5F);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        TranslateAnimation showAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowSet.addAnimation(alphaAnimation);
        mShowSet.addAnimation(scaleAnimation);
        mShowSet.setDuration(500);
    }

    private void collapsingListener() {
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    if (mLayoutState != CollapsingToolbarLayoutState.EXPANDED) {
                        mLayoutState = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                        llTitle.setVisibility(View.GONE);
                        ivArrow.setVisibility(View.VISIBLE);
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (mLayoutState != CollapsingToolbarLayoutState.COLLAPSED) {
                        llContent.startAnimation(mHideSet);
                        llContent.setVisibility(View.GONE);
                        llTitle.startAnimation(mShowSet);
                        llTitle.setVisibility(View.VISIBLE);
                        ivArrow.setVisibility(View.GONE);
                        mLayoutState = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                    }
                } else {
                    if (mLayoutState != CollapsingToolbarLayoutState.INTERNEDTATE) {
                        if (mLayoutState == CollapsingToolbarLayoutState.COLLAPSED) {
                            llContent.startAnimation(mShowSet);
                            llContent.setVisibility(View.VISIBLE);
                            llTitle.startAnimation(mHideSet);
                            llTitle.setVisibility(View.GONE);
                        }
                        mLayoutState = CollapsingToolbarLayoutState.INTERNEDTATE;//修改状态标记为中间
                    }

                }
            }
        });
    }

    private void initTablayout() {
        tablayout.addTab(tablayout.newTab().setText(TITLE[0]));
        tablayout.addTab(tablayout.newTab().setText(TITLE[1]));
        tablayout.addTab(tablayout.newTab().setText(TITLE[2]));
        tablayout.addTab(tablayout.newTab().setText(TITLE[3]));
    }

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDTATE,
    }
}
