package com.app.test.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by able on 2017/12/1.
 * 解决和scrollView华东冲突
 */

public class CreditSwipeRefreshLayout extends SwipeRefreshLayout {
    private RecyclerView recyclerView;
    private RelativeLayout mChildRootView;
    private float mTouchY;
    private static final float SCROLL_RATIO = 0.6f;
    private boolean firstTouch = true;


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 1) {
            if (getChildAt(1) instanceof RelativeLayout) {
                mChildRootView = (RelativeLayout) getChildAt(1);
                if (mChildRootView != null && mChildRootView.getChildCount() > 0 && mChildRootView.getChildAt(0) instanceof RecyclerView) {
                    recyclerView = (RecyclerView) mChildRootView.getChildAt(0);
                }
            }
        }
    }

    public CreditSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (firstTouch) { //消除第一次downX和moveX不一致
                    mTouchY = ev.getY();
                    firstTouch = false;
                    return false;
                }
                float nowY = ev.getY();
                if (!recyclerView.canScrollVertically(-1) && !isRefreshing()) {
                    int deltaY = (int) (nowY - mTouchY);
                    int moveY = (int) (mChildRootView.getPaddingTop() + deltaY * SCROLL_RATIO);
                    try {
                        if (moveY > 0) {
                            mChildRootView.setPadding(0, moveY, 0, 0);
                            float alphadata = 1 - moveY / (float) 1080;
                            if (0 != alphadata) {
                                mChildRootView.setAlpha(alphadata);
                            }
                        } else {
                            mChildRootView.setPadding(0, 0, 0, 0);
                            mChildRootView.setAlpha(1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    mChildRootView.setPadding(0, 0, 0, 0);
                    mChildRootView.setAlpha(1);
                }
                mTouchY = nowY;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                firstTouch = true;
                mChildRootView.setPadding(0, 0, 0, 0);
                mChildRootView.setAlpha(1);

                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

}
