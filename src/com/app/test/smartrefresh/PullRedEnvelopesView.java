package com.app.test.smartrefresh;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;


/**
 * lcx
 */
public class PullRedEnvelopesView extends FrameLayout {


    //recyclerview
    private RecyclerView childView;
    // 在手指滑动的过程中记录是否移动了布局
    private boolean isMoved = false;
    // 如果按下时不能上拉和下拉， 会在手指移动时更新为当前手指的Y值
    private float startY;
    //阻尼
    private static final float OFFSET_RADIO = 0.5f;

    private boolean isRecyclerResult = false;

    private boolean isCanRefresh;

    private int refreshValue = 200;


    public PullRedEnvelopesView(Context context) {
        this(context, null);
    }

    public PullRedEnvelopesView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRedEnvelopesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    /**
     * 加载布局后初始化,这个方法会在加载完布局后调用
     */
    @Override
    protected void onFinishInflate() {
        //此处为容器中的子view   必须有RecyclerView
        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                if (getChildAt(i) instanceof RecyclerView) {
                    if (childView == null) {
                        childView = (RecyclerView) getChildAt(i);
                    } else {
                        throw new RuntimeException("PullWalkView 中只能存在一个RecyclerView");
                    }
                }
            }
        }

        if (childView == null) {
            throw new RuntimeException("PullWalkView 子容器中必须有一个RecyclerView");
        }
        super.onFinishInflate();
    }


    /**
     * 事件分发
     */

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (childView == null) {
            return super.dispatchTouchEvent(ev);
        }
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //记录按下时的Y
                startY = ev.getY();
            case MotionEvent.ACTION_MOVE:
                float nowY = ev.getY();
                int scrollY = (int) (nowY - startY);
                if ((isCanPullDown() && scrollY > 5)) {
                    int offset = (int) (scrollY * OFFSET_RADIO);
                    distanceListen.onDistance(offset);
                    isMoved = true;
                    isRecyclerResult = false;
                    isCanRefresh = offset > refreshValue;
                    return true;
                } else {
                    recoverLayout();
                    startY = ev.getY();
                    isMoved = false;
                    isCanRefresh = false;
                    isRecyclerResult = true;
                    return super.dispatchTouchEvent(ev);
                }
            case MotionEvent.ACTION_UP:
                recoverLayout();
                if (isRecyclerResult) {
                    return super.dispatchTouchEvent(ev);
                } else {
                    return true;
                }
            default:
                return true;
        }
    }

    /**
     * 位置还原
     */
    private void recoverLayout() {
        if (!isMoved) {
            return;//如果没有移动布局，则跳过执行
        }
        distanceListen.onEnd();
        isMoved = false;
        if (isCanRefresh)
            distanceListen.toRefresh();
    }

    /**
     * 容器的的事件都在事件分发中处理，这里处理的是事件分发传递过来的事件，
     * <p>
     * 传递过来的为RecyclerVIew的事件  不拦截，直接交给reyclerview处理
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;  //不拦截  直接传递给子的view
    }


    /**
     * 判断是否可以下拉
     *
     * @return
     */
    private boolean isCanPullDown() {

        final RecyclerView.Adapter adapter = childView.getAdapter();
        if (null == adapter) {
            return true;
        }
        //spf  ========================================================
        if (childView.getLayoutManager() != null && childView.getLayoutManager() instanceof LinearLayoutManager) {
            final int firstVisiblePosition = ((LinearLayoutManager) childView.getLayoutManager()).findFirstVisibleItemPosition();
            if (firstVisiblePosition != 0 && adapter.getItemCount() != 0) {
                return false;
            }
        }
        //===================================================================================
        int mostTop = childView.getChildCount() > 0 ? childView.getChildAt(0).getTop() : 0;
        return mostTop >= 0;
    }

    private DistanceListen distanceListen;

    public void setDistanceListen(DistanceListen distanceListen) {
        this.distanceListen = distanceListen;
    }

    public interface DistanceListen {
        void onDistance(int distance);

        void onEnd();

        void toRefresh();
    }

}