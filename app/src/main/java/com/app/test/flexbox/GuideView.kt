package com.app.test.flexbox

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.app.test.util.StatusBarUtils
import com.app.test.util.Utils

/**
 * @author lcx
 * Created at 2020.4.10
 * Describe:
 */
internal class GuideView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyle: Int = 0) : ViewGroup(context, attrs, defStyle) {
    /**
     * 高亮区域
     */
    private val mTargetRect = RectF()

    /**
     * 蒙层区域
     */
    private val mOverlayRect = RectF()

    /**
     * 中间变量
     */
    private val mChildTmpRect = RectF()

    /**
     * 蒙层背景画笔
     */
    private val mFullingPaint: Paint
    private var mPadding = 0
    private var mPaddingLeft = 0
    private var mPaddingTop = 0
    private var mPaddingRight = 0
    private var mPaddingBottom = 0

    /**
     * 是否覆盖目标区域
     */
    private var mOverlayTarget = false

    /**
     * 圆角大小
     */
    private var mCorner = 0

    /**
     * 目标区域样式，默认为矩形
     */
    private var mStyle = ROUNDRECT

    /**
     * 挖空画笔
     */
    private val mEraser: Paint

    /**
     * 橡皮擦Bitmap
     */
    private var mEraserBitmap: Bitmap?

    /**
     * 橡皮擦Cavas
     */
    private val mEraserCanvas: Canvas
    private var ignoreRepadding = false
    private var mInitHeight = 0
    private var mChangedHeight = 0
    private var mFirstFlag = true
    var guideClickListen: GuideClickListen? = null
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        try {
            clearFocus()
            mEraserCanvas.setBitmap(null)
            mEraserBitmap = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> if (mTargetRect.left < x && mTargetRect.right > x && mTargetRect.top < y && mTargetRect.bottom > y && !Utils.isEmpty(guideClickListen)) {
                guideClickListen!!.clickTarget()
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)
        if (mFirstFlag) {
            mInitHeight = h
            mFirstFlag = false
        }
        mChangedHeight = if (mInitHeight > h) {
            h - mInitHeight
        } else if (mInitHeight < h) {
            h - mInitHeight
        } else {
            0
        }
        setMeasuredDimension(w, h)
        mOverlayRect[0f, 0f, w.toFloat()] = h.toFloat()
        resetOutPath()
        val count = childCount
        var child: View?
        for (i in 0 until count) {
            child = getChildAt(i)
            child?.let { measureChild(it, widthMeasureSpec, heightMeasureSpec) }
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val count = childCount
        var child: View?
        for (i in 0 until count) {
            child = getChildAt(i)
            if (child == null) {
                continue
            }
            calculate(child)
            child.layout(mChildTmpRect.left.toInt(), mChildTmpRect.top.toInt(), mChildTmpRect.right.toInt(), mChildTmpRect.bottom.toInt())
        }
    }

    /**
     * 根据目标view计算提示的view的位置
     *
     * @param child
     */
    fun calculate(child: View) {
        if (mTargetRect.top - child.measuredHeight > 0) {
            mChildTmpRect.bottom = mTargetRect.top
            mChildTmpRect.top = mChildTmpRect.bottom - child.measuredHeight
        } else {
            mChildTmpRect.top = mTargetRect.bottom
            mChildTmpRect.bottom = mChildTmpRect.top + child.measuredHeight
        }
        if (mTargetRect.left.toInt() - child.measuredWidth / 2 > 0 && width - mTargetRect.right.toInt() > child.measuredWidth / 2) {
            mChildTmpRect.left = (mTargetRect.width() - child.measuredWidth) / 2
            mChildTmpRect.right = (mTargetRect.width() + child.measuredWidth) / 2
            mChildTmpRect.offset(mTargetRect.left, 0f)
        } else if (mTargetRect.left.toInt() - child.measuredWidth / 2 < 0) {
            mChildTmpRect.left = 0f
            mChildTmpRect.right = child.measuredWidth.toFloat()
        } else if (width - mTargetRect.right.toInt() < child.measuredWidth / 2) {
            mChildTmpRect.left = width - child.measuredWidth.toFloat()
            mChildTmpRect.right = width.toFloat()
        }
    }

    private fun resetOutPath() {
        resetPadding()
    }

    /**
     * 设置padding
     */
    private fun resetPadding() {
        if (!ignoreRepadding) {
            if (mPadding != 0 && mPaddingLeft == 0) {
                mTargetRect.left -= mPadding.toFloat()
            }
            if (mPadding != 0 && mPaddingTop == 0) {
                mTargetRect.top -= mPadding.toFloat()
            }
            if (mPadding != 0 && mPaddingRight == 0) {
                mTargetRect.right += mPadding.toFloat()
            }
            if (mPadding != 0 && mPaddingBottom == 0) {
                mTargetRect.bottom += mPadding.toFloat()
            }
            if (mPaddingLeft != 0) {
                mTargetRect.left -= mPaddingLeft.toFloat()
            }
            if (mPaddingTop != 0) {
                mTargetRect.top -= mPaddingTop.toFloat()
            }
            if (mPaddingRight != 0) {
                mTargetRect.right += mPaddingRight.toFloat()
            }
            if (mPaddingBottom != 0) {
                mTargetRect.bottom += mPaddingBottom.toFloat()
            }
            ignoreRepadding = true
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        val drawingTime = drawingTime
        try {
            var child: View?
            for (i in 0 until childCount) {
                child = getChildAt(i)
                drawChild(canvas, child, drawingTime)
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mChangedHeight != 0) {
            mTargetRect.offset(0f, mChangedHeight.toFloat())
            mInitHeight = mInitHeight + mChangedHeight
            mChangedHeight = 0
        }
        mEraserBitmap!!.eraseColor(Color.TRANSPARENT)
        mEraserCanvas.drawColor(mFullingPaint.color)
        if (!mOverlayTarget) {
            if (mStyle == CIRCLE) {
                mEraserCanvas.drawCircle(mTargetRect.centerX(), mTargetRect.centerY(), mTargetRect.width() / 2, mEraser)
            } else {
                mEraserCanvas.drawRoundRect(mTargetRect, mCorner.toFloat(), mCorner.toFloat(), mEraser)
            }
        }
        canvas.drawBitmap(mEraserBitmap, mOverlayRect.left, mOverlayRect.top, null)
    }

    fun setTargetRect(rect: Rect?) {
        mTargetRect.set(rect)
    }

    fun setFullingAlpha(alpha: Int) {
        mFullingPaint.alpha = alpha
    }

    fun setFullingColor(color: Int) {
        mFullingPaint.color = color
    }

    fun setHighTargetCorner(corner: Int) {
        mCorner = corner
    }

    fun setHighTargetGraphStyle(style: Int) {
        mStyle = style
    }

    fun setOverlayTarget(b: Boolean) {
        mOverlayTarget = b
    }

    fun setPadding(padding: Int) {
        mPadding = padding
    }

    fun setPaddingLeft(paddingLeft: Int) {
        mPaddingLeft = paddingLeft
    }

    fun setPaddingTop(paddingTop: Int) {
        mPaddingTop = paddingTop
    }

    fun setPaddingRight(paddingRight: Int) {
        mPaddingRight = paddingRight
    }

    fun setPaddingBottom(paddingBottom: Int) {
        mPaddingBottom = paddingBottom
    }

    interface GuideClickListen {
        fun clickTarget()
    }

    companion object {
        /**
         * 圆角矩形&矩形
         */
        const val ROUNDRECT = 0

        /**
         * 圆形
         */
        const val CIRCLE = 1

        /**
         * Rect在屏幕上去掉状态栏高度的绝对位置
         */
        @JvmStatic
        fun getViewAbsRect(view: View, parentX: Int, parentY: Int): Rect {
            val loc = IntArray(2)
            view.getLocationInWindow(loc)
            val rect = Rect()
            rect[loc[0], loc[1], loc[0] + view.measuredWidth] = loc[1] + view.measuredHeight
            rect.offset(-parentX, -parentY)
            return rect
        }
    }

    init {
        //自我绘制
        setWillNotDraw(false)

//        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//
//        wm.getDefaultDisplay().getRealMetrics(displayMetrics);
        val width = StatusBarUtils.getScreenRealWidth(getContext())
        val height = StatusBarUtils.getScreenRealHeight(getContext())
        mOverlayRect[0f, 0f, width.toFloat()] = height.toFloat()
        mEraserBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        mEraserCanvas = Canvas(mEraserBitmap)
        mFullingPaint = Paint()
        mEraser = Paint()
        mEraser.color = -0x1
        //图形重叠时的处理方式，擦除效果
        mEraser.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        //位图抗锯齿设置
        mEraser.flags = Paint.ANTI_ALIAS_FLAG
    }
}