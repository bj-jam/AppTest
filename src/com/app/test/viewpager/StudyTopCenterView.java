package com.app.test.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.test.R;
import com.app.test.util.DisplayUtil;

public class StudyTopCenterView extends RelativeLayout implements OnPageChangeListener {

    private View[] views;
    private ViewPager viewPager;
    private Context context;
    /**
     * 圆点的父容器
     */
    private LinearLayout pointLayout;
    private ViewPagerAdapter viewPagerAdapter;

    private View[] viewList;

    public StudyTopCenterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.context = context;
        if (isInEditMode()) {
            return;
        }
        initView();
    }

    /**
     * 设置值
     */
    public void initValue(View[] viewList) {
        this.viewList = viewList;
        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        notifyDateChanged();
    }

    /**
     * 初始化View
     */
    private void initView() {
        View.inflate(getContext(), R.layout.view_video_exam, this);
        pointLayout = (LinearLayout) findViewById(R.id.pointLayout);
        viewPager = (ViewPager) findViewById(R.id.examViewPager);
        viewPager.setOnPageChangeListener(this);
    }

    /**
     * 数据变化
     */
    private void notifyDateChanged() {
        viewPagerAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(0);// 默认选中第一
        setPoint(viewList.length);
    }

    /**
     * 设置圆点
     */
    private void setPoint(int size) {
        int width = DisplayUtil.getInstance().dip2px(9);// 设置原点大小
        // 滑动的指示点图片
        views = new View[viewList.length];
        if (size != 0) {
            pointLayout.removeAllViews();
            for (int i = 0; i < size; i++) {
                views[i] = new View(context);
                views[i].setLayoutParams(new LayoutParams(width, width));
                LayoutParams params = (LayoutParams) views[i]
                        .getLayoutParams();
                params.setMargins(0, 0, width, 0);// 设置两点见的间距
                views[i].setBackgroundResource(R.drawable.code6);
                pointLayout.addView(views[i]);
            }
            // 默认选择第一个
            views[0].setBackgroundResource(R.drawable.shape_item_dynamic);
        }

    }


    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    // 当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    // 当新的页面被选中时调用
    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < viewList.length; i++) {
            if (i != position) {// 未选中
                views[i].setBackgroundResource(R.drawable.shadow_selecter);
            } else {// 选中
                views[position].setBackgroundResource(R.drawable.shape_black_cricle);
            }
        }
    }


    /**
     * pagerView的适配器
     */
    private class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return viewList == null ? 0 : viewList.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {
            arg0.removeView((View) arg2);
        }

        @Override
        public Object instantiateItem(ViewGroup arg0, int arg1) {
            View view = viewList[arg1];
            arg0.addView(view);
            return view;
        }
    }
}
