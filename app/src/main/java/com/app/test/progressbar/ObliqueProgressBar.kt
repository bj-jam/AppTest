package com.app.test.progressbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

/**
 * @author lcx
 * Created at 2020.6.9
 * Describe:
 */
class ObliqueProgressBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private var mPercent = 50
    private var mBackColor = -0x777778
    private var mPercentColor = -0xf75106
    var paint: Paint = Paint().also { it.isAntiAlias = true }
    var path: Path = Path()

    public fun setPerCent(percent: Int?) {
        mPercent = percent ?: 0
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        // 绘制背景
        path.reset()
        val backLeftTop = 2 * width / 100f
        path.moveTo(backLeftTop, 0f)
        val backRightTop = width.toFloat()
        path.lineTo(backRightTop, 0f)
        val backRightBottom = (100 - 2) * width / 100f
        path.lineTo(backRightBottom, height.toFloat())
        path.lineTo(0f, height.toFloat())
        path.close()
        paint.color = mBackColor
        canvas.drawPath(path, paint)

        // 绘制已完成的进度
        path.reset()
        val leftTop = 2 * width / 100f
        path.moveTo(leftTop, 0f)
        if (mPercent < 0) {
            mPercent = 0
        }
        if (mPercent > 100) {
            mPercent = 100
        }
        val rightTop = mPercent * width / 100f
        path.lineTo(rightTop, 0f)
        val rightBottom = (mPercent - 2) * width / 100f
        path.lineTo(rightBottom, height.toFloat())
        path.lineTo(0f, height.toFloat())
        path.close()
        paint.color = mPercentColor
        canvas.drawPath(path, paint)
        invalidate()
    }
}