package com.app.test.viewpager;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.app.test.R;
import com.app.test.util.DensityUtil;

/**
 * Created by jam on 16/9/29.
 */

public class ViewPagerActivity extends Activity {
    private ViewPager mViewPager;
    private StudyTopCenterView studyTopCenter;
    private ViewPagerAdapter adapter;
    private String[] date;
    private View[] viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        intDate();
        intView();
    }

    private void intView() {
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        studyTopCenter = (StudyTopCenterView) findViewById(R.id.studyTopCenter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageMargin(DensityUtil.dp2px(-70));
        mViewPager.setPageTransformer(true, new ScalePageTransformer());
        mViewPager.setAdapter(adapter);
        studyTopCenter.initValue(viewList);
    }

    private void intDate() {
        adapter = new ViewPagerAdapter();
        date = new String[10];
        date[0] = "";
        viewList = new View[2];
        View view = View.inflate(this, R.layout.view_brush_course_info, null);
        final BrushCourseCircleBar brushCourseCircleBar = (BrushCourseCircleBar) view.findViewById(R.id.brushCourseCircleBar);
        brushCourseCircleBar.startCustomAnimation();
        brushCourseCircleBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brushCourseCircleBar.startCustomAnimation();
            }
        });

        View view1 = View.inflate(this, R.layout.view_brush_course_count_info, null);
        final BrushCourseMissionBar brushCourseMissionBar = (BrushCourseMissionBar) view1.findViewById(R.id.brushCourseMissionBar);
//        brushCourseMissionBar.startCustomAnimation();
        brushCourseMissionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brushCourseMissionBar.startCustomAnimation();
            }
        });

        viewList[0] = view1;
        viewList[1] = view;

    }


    private class ViewPagerAdapter extends PagerAdapter {

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(ViewPagerActivity.this, R.layout.item_viewpager, null);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return date.length;
        }
    }


    /**
     * viewPager实现中间放大,两边缩小
     */

    public class ScalePageTransformer implements ViewPager.PageTransformer {
        /**
         * 最大的item
         */
        public static final float MAX_SCALE = 1f;
        /**
         * 最小的item
         */
        public static final float MIN_SCALE = 0.8f;

        /**
         * 核心就是实现transformPage(View page, float position)这个方法
         **/
        @Override
        public void transformPage(View page, float position) {

            if (position < -1) {
                position = -1;
            } else if (position > 1) {
                position = 1;
            }

            float tempScale = position < 0 ? 1 + position : 1 - position;

            float slope = (MAX_SCALE - MIN_SCALE) / 1;
            //一个公式
            float scaleValue = MIN_SCALE + tempScale * slope;
            page.setScaleX(scaleValue);
            page.setScaleY(scaleValue);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                page.getParent().requestLayout();
            }
        }
    }
}
