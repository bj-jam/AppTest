package com.app.test.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2018/6/6.
 * qzx
 * <p>
 * RecyclerView---水平排布
 * 具有顶部和底部可延伸一段距离，松开回弹的效果
 */
public class BounceRecyclerView extends RecyclerView {
    private float downTouch; //因为Item设置了点击监听导致RecyclerView收不到ACTION_DOWN的触摸事件
    private boolean firstTouch = true;
    private static final int MOVE_VERTIFY = 10; //可以延伸到屏幕的四分之一
    private static final float DEFAULT_DEVIDE = 0.6f; //recyclerView_thumbnail的padding
    private int paddingTop;
    private int paddingBottom;
    private int oritation;

    public int getOritation() {
        return oritation;
    }

    /**
     * 只能设置0和1
     * <br/><br/>
     * 0表示RecylerView是竖直排布
     * <br/><br/>
     * 1表示RecylerView是水平排布
     * <br/><br/>
     * 否则默认为0---竖直排布
     */
    public void setOritation(int oritation) {
        if (oritation != 0 && oritation != 1) {
            oritation = 0;
        }
        this.oritation = oritation;
    }

    public BounceRecyclerView(Context context) {
        this(context, null);
    }

    public BounceRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BounceRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paddingTop = getPaddingTop();
        paddingBottom = getPaddingBottom();
//        paddingLeft = getPaddingLeft();
//        paddingRight = getPaddingRight();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //                downY = event.getY();
                // //                    Log.d("zbv","downY="+downY);
                break;
            case MotionEvent.ACTION_MOVE: //这样写是因为无法监听到down事件所以第一次move事件的坐标作为down
                if (firstTouch) { //消除第一次downX和moveX不一致
                    if (oritation == 0) {
                        downTouch = event.getY();
                    }
//                    else if (oritation == 1) {
//                        downTouch = event.getX();
//                    }
                    firstTouch = false;
                    return false;
                }
                float moveTouch = 0;
                if (oritation == 0) {
                    moveTouch = event.getY();
                    if (!canScrollVertically(-1)) {
                        if ((moveTouch - downTouch) >= MOVE_VERTIFY) {
                            int deltY = (int) ((int) (moveTouch - downTouch) * DEFAULT_DEVIDE);
                            setPadding(getPaddingLeft(), getPaddingTop() + deltY, getPaddingRight(), getPaddingBottom());
                        } else if ((moveTouch - downTouch) <= -MOVE_VERTIFY) {
                            setPadding(getPaddingLeft(), paddingTop, getPaddingRight(), paddingBottom);
                        }
                    }
//                    else if (!canScrollVertically(1)) {
//                        if ((downTouch - moveTouch) >= MOVE_VERTIFY) {
//                            int deltY = (int) (downTouch - moveTouch) / DEFAULT_DEVIDE;
//                            setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom() + deltY);
//                        } else if ((downTouch - moveTouch) <= -MOVE_VERTIFY) {
//                            setPadding(getPaddingLeft(), paddingTop, getPaddingRight(), paddingBottom);
//                        }
//                    }
                    else {
                        setPadding(getPaddingLeft(), paddingTop, getPaddingRight(), paddingBottom);
                    }
                } else if (oritation == 1) {
//                    moveTouch = event.getX();
//                    if (!canScrollHorizontally(-1)) {
//                        if ((moveTouch - downTouch) >= MOVE_VERTIFY) {
//                            int deltY = (int) (moveTouch - downTouch) / DEFAULT_DEVIDE;
//                            setPadding(getPaddingLeft() + deltY, getPaddingTop(), getPaddingRight(), getPaddingBottom());
//                        } else if ((moveTouch - downTouch) <= -MOVE_VERTIFY) {
//                            setPadding(getPaddingLeft(), paddingTop, getPaddingRight(), paddingBottom);
//                        }
//                    } else if (!canScrollHorizontally(1)) {
//                        if ((downTouch - moveTouch) >= MOVE_VERTIFY) {
//                            int deltY = (int) (downTouch - moveTouch) / DEFAULT_DEVIDE;
//                            setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight() + deltY, getPaddingBottom());
//                        } else if ((downTouch - moveTouch) <= -MOVE_VERTIFY) {
//                            setPadding(getPaddingLeft(), paddingTop, getPaddingRight(), paddingBottom);
//                        }
//                    } else {
//                        setPadding(getPaddingLeft(), paddingTop, getPaddingRight(), paddingBottom);
//                    }
                } //因为Visible都是可见的所以下面注释的三行判断是行不通的
                // rv_adapter.getHolderSparseArray().get(0).pptImageView.getVisibility() == View.VISIBLE
                // rv_adapter.getHolderSparseArray().get(rv_adapter.getHolderSparseArray().size() - 1).footIV != null &&
                // rv_adapter.getHolderSparseArray().get(rv_adapter.getHolderSparseArray().size() - 1).footIV.getVisibility() == View.VISIBLE
                // ---------------
                // linearLayoutManager.findFirstVisibleItemPosition() == 0
                // linearLayoutManager.findLastVisibleItemPosition() == (linearLayoutManager.getItemCount() - 1)
                // !recyclerView_thumbnail.canScrollVertically(-1) //!recyclerView_thumbnail.canScrollVertically(1)
                // ---------------
                // 防止在既不是顶部也不是底部时的闪烁
                downTouch = moveTouch;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (oritation == 0) {
                    setPadding(getPaddingLeft(), paddingTop, getPaddingRight(), paddingBottom);
                }
//                else if (oritation == 1) {
//                    setPadding(paddingLeft, getPaddingTop(), paddingRight, getPaddingBottom());
//                }
                firstTouch = true;
                break;
        }
        return super.onTouchEvent(event);
    }
}
