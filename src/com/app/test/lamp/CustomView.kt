package com.app.test.lamp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.app.test.R

/**
 * @author lcx
 * Created at 2020.3.31
 * Describe:
 */
class CustomView : View {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var path: Path? = null
    private var drawLeftTop = false
    private var drawLeftBottom = false
    private var drawRightTop = false
    private var drawRightBottom = false
    private var radius = 0f

    constructor(context: Context?) : super(context) {
        initDraw()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomView)
        val position = typedArray.getInt(R.styleable.CustomView_round_position, 0)
        radius = typedArray.getDimension(R.styleable.CustomView_round_radius, 0f)
        val LEFT_TOP = 0x1
        drawLeftTop = position and LEFT_TOP == LEFT_TOP
        val LEFT_BOTTOM = 0x2
        drawLeftBottom = position and LEFT_BOTTOM == LEFT_BOTTOM
        val RIGHT_TOP = 0x4
        drawRightTop = position and RIGHT_TOP == RIGHT_TOP
        val RIGHT_BOTTOM = 0x8
        drawRightBottom = position and RIGHT_BOTTOM == RIGHT_BOTTOM
        typedArray.recycle()
        initDraw()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initDraw()
    }

    private fun initDraw() {
        path = Path()
        paint.color = Color.GREEN
        paint.isAntiAlias = true
        paint.strokeWidth = 5.toFloat()
        paint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        //重置path
        path?.reset()
        path?.moveTo(radius, 0f)
        if (drawRightTop) {
            path?.lineTo(width - radius, 0f)
            path?.cubicTo(width - radius / 2, 0f, width.toFloat(), radius / 2, width.toFloat(), radius)
        } else {
            path?.lineTo(width.toFloat(), 0f)
        }
        path?.lineTo(width.toFloat(), height - radius)
        if (drawRightBottom) {
            path?.cubicTo(width.toFloat(), height - radius / 2, width - radius / 2, height.toFloat(), width - radius, height.toFloat())
        } else {
            path?.lineTo(width.toFloat(), height.toFloat())
        }
        path?.lineTo(radius, height.toFloat())
        if (drawLeftBottom) {
            path?.cubicTo(radius / 2, height.toFloat(), 0f, height - radius / 2, 0f, height - radius)
        } else {
            path?.lineTo(0f, height.toFloat())
        }
        path?.lineTo(0f, radius)
        if (drawLeftTop) {
            path?.cubicTo(0f, radius / 2, radius / 2, 0f, radius, 0f)
        } else {
            path?.lineTo(0f, 0f)
            path?.lineTo(radius, 0f)
        }
        canvas.drawPath(path, paint)
        super.onDraw(canvas)
    }

    fun setRadius(radius: Float) {
        this.radius = radius
        Log.e("jam", radius.toString())
        invalidate()
    }
}