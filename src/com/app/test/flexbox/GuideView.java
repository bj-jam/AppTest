package com.app.test.flexbox;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.app.test.util.StatusBarUtils;
import com.app.test.util.Utils;

/**
 * @author lcx
 * Created at 2020.4.10
 * Describe:
 */
class GuideView extends ViewGroup {

    /**
     * 圆角矩形&矩形
     */
    public final static int ROUNDRECT = 0;

    /**
     * 圆形
     */
    public final static int CIRCLE = 1;

    /**
     * 高亮区域
     */
    private final RectF mTargetRect = new RectF();
    /**
     * 蒙层区域
     */
    private final RectF mOverlayRect = new RectF();

    /**
     * 中间变量
     */
    private final RectF mChildTmpRect = new RectF();
    /**
     * 蒙层背景画笔
     */
    private final Paint mFullingPaint;
    private int mPadding = 0;
    private int mPaddingLeft = 0;
    private int mPaddingTop = 0;
    private int mPaddingRight = 0;
    private int mPaddingBottom = 0;
    /**
     * 是否覆盖目标区域
     */
    private boolean mOverlayTarget = false;
    /**
     * 圆角大小
     */
    private int mCorner = 0;
    /**
     * 目标区域样式，默认为矩形
     */
    private int mStyle = ROUNDRECT;
    /**
     * 挖空画笔
     */
    private Paint mEraser;
    /**
     * 橡皮擦Bitmap
     */
    private Bitmap mEraserBitmap;
    /**
     * 橡皮擦Cavas
     */
    private Canvas mEraserCanvas;

    private boolean ignoreRepadding;

    private int mInitHeight;
    private int mChangedHeight = 0;
    private boolean mFirstFlag = true;
    GuideClickListen guideClickListen;

    public GuideView(Context context) {
        this(context, null, 0);
    }

    public GuideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuideView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //自我绘制
        setWillNotDraw(false);

//        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//
//        wm.getDefaultDisplay().getRealMetrics(displayMetrics);
        int width = StatusBarUtils.getScreenRealWidth(getContext());
        int height = StatusBarUtils.getScreenRealHeight(getContext());
        mOverlayRect.set(0, 0, width, height);
        mEraserBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mEraserCanvas = new Canvas(mEraserBitmap);
        mFullingPaint = new Paint();
        mEraser = new Paint();
        mEraser.setColor(0xFFFFFFFF);
        //图形重叠时的处理方式，擦除效果
        mEraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        //位图抗锯齿设置
        mEraser.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try {
            clearFocus();
            mEraserCanvas.setBitmap(null);
            mEraserBitmap = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mTargetRect.left < x && mTargetRect.right > x
                        && mTargetRect.top < y && mTargetRect.bottom > y
                        && !Utils.isEmpty(guideClickListen)) {
                    guideClickListen.clickTarget();
                }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int w = MeasureSpec.getSize(widthMeasureSpec);
        final int h = MeasureSpec.getSize(heightMeasureSpec);
        if (mFirstFlag) {
            mInitHeight = h;
            mFirstFlag = false;
        }
        if (mInitHeight > h) {
            mChangedHeight = h - mInitHeight;
        } else if (mInitHeight < h) {
            mChangedHeight = h - mInitHeight;
        } else {
            mChangedHeight = 0;
        }
        setMeasuredDimension(w, h);
        mOverlayRect.set(0, 0, w, h);
        resetOutPath();

        final int count = getChildCount();
        View child;
        for (int i = 0; i < count; i++) {
            child = getChildAt(i);
            if (child != null) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        View child;
        for (int i = 0; i < count; i++) {
            child = getChildAt(i);
            if (child == null) {
                continue;
            }
            calculate(child);
            child.layout((int) mChildTmpRect.left, (int) mChildTmpRect.top, (int) mChildTmpRect.right, (int) mChildTmpRect.bottom);
        }
    }

    public void setGuideClickListen(GuideClickListen guideClickListen) {
        this.guideClickListen = guideClickListen;
    }

    /**
     * 根据目标view计算提示的view的位置
     *
     * @param child
     */
    public void calculate(View child) {
        if (mTargetRect.top - child.getMeasuredHeight() > 0) {
            mChildTmpRect.bottom = mTargetRect.top;
            mChildTmpRect.top = mChildTmpRect.bottom - child.getMeasuredHeight();
        } else {
            mChildTmpRect.top = mTargetRect.bottom;
            mChildTmpRect.bottom = mChildTmpRect.top + child.getMeasuredHeight();
        }
        if (((int) mTargetRect.left - child.getMeasuredWidth() / 2) > 0 && (getWidth() - (int) mTargetRect.right) > child.getMeasuredWidth() / 2) {
            mChildTmpRect.left = (mTargetRect.width() - child.getMeasuredWidth()) / 2;
            mChildTmpRect.right = (mTargetRect.width() + child.getMeasuredWidth()) / 2;
            mChildTmpRect.offset(mTargetRect.left, 0);
        } else if (((int) mTargetRect.left - child.getMeasuredWidth() / 2) < 0) {
            mChildTmpRect.left = 0;
            mChildTmpRect.right = child.getMeasuredWidth();
        } else if ((getWidth() - (int) mTargetRect.right) < child.getMeasuredWidth() / 2) {
            mChildTmpRect.left = getWidth() - child.getMeasuredWidth();
            mChildTmpRect.right = getWidth();
        }
    }

    private void resetOutPath() {
        resetPadding();
    }

    /**
     * 设置padding
     */
    private void resetPadding() {
        if (!ignoreRepadding) {
            if (mPadding != 0 && mPaddingLeft == 0) {
                mTargetRect.left -= mPadding;
            }
            if (mPadding != 0 && mPaddingTop == 0) {
                mTargetRect.top -= mPadding;
            }
            if (mPadding != 0 && mPaddingRight == 0) {
                mTargetRect.right += mPadding;
            }
            if (mPadding != 0 && mPaddingBottom == 0) {
                mTargetRect.bottom += mPadding;
            }
            if (mPaddingLeft != 0) {
                mTargetRect.left -= mPaddingLeft;
            }
            if (mPaddingTop != 0) {
                mTargetRect.top -= mPaddingTop;
            }
            if (mPaddingRight != 0) {
                mTargetRect.right += mPaddingRight;
            }
            if (mPaddingBottom != 0) {
                mTargetRect.bottom += mPaddingBottom;
            }
            ignoreRepadding = true;
        }
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        final long drawingTime = getDrawingTime();
        try {
            View child;
            for (int i = 0; i < getChildCount(); i++) {
                child = getChildAt(i);
                drawChild(canvas, child, drawingTime);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mChangedHeight != 0) {
            mTargetRect.offset(0, mChangedHeight);
            mInitHeight = mInitHeight + mChangedHeight;
            mChangedHeight = 0;
        }
        mEraserBitmap.eraseColor(Color.TRANSPARENT);
        mEraserCanvas.drawColor(mFullingPaint.getColor());
        if (!mOverlayTarget) {
            if (mStyle == CIRCLE) {
                mEraserCanvas.drawCircle(mTargetRect.centerX(), mTargetRect.centerY(), mTargetRect.width() / 2, mEraser);
            } else {
                mEraserCanvas.drawRoundRect(mTargetRect, mCorner, mCorner, mEraser);
            }
        }
        canvas.drawBitmap(mEraserBitmap, mOverlayRect.left, mOverlayRect.top, null);
    }

    public void setTargetRect(Rect rect) {
        mTargetRect.set(rect);
    }

    public void setFullingAlpha(int alpha) {
        mFullingPaint.setAlpha(alpha);
    }

    public void setFullingColor(int color) {
        mFullingPaint.setColor(color);
    }

    public void setHighTargetCorner(int corner) {
        this.mCorner = corner;
    }

    public void setHighTargetGraphStyle(int style) {
        this.mStyle = style;
    }

    public void setOverlayTarget(boolean b) {
        mOverlayTarget = b;
    }

    public void setPadding(int padding) {
        this.mPadding = padding;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.mPaddingLeft = paddingLeft;
    }

    public void setPaddingTop(int paddingTop) {
        this.mPaddingTop = paddingTop;
    }

    public void setPaddingRight(int paddingRight) {
        this.mPaddingRight = paddingRight;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.mPaddingBottom = paddingBottom;
    }


    /**
     * Rect在屏幕上去掉状态栏高度的绝对位置
     */
    static Rect getViewAbsRect(View view, int parentX, int parentY) {
        int[] loc = new int[2];
        view.getLocationInWindow(loc);
        Rect rect = new Rect();
        rect.set(loc[0], loc[1], loc[0] + view.getMeasuredWidth(), loc[1] + view.getMeasuredHeight());
        rect.offset(-parentX, -parentY);
        return rect;
    }

    public interface GuideClickListen {
        void clickTarget();
    }

}
