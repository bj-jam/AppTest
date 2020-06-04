package com.app.test.bezier

import android.content.Context
import android.graphics.*
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.app.test.R

/**
 *
 */
class BezierView : FrameLayout {
    private lateinit var paint: Paint
    private lateinit var path: Path

    // 手势坐标
    var initX = 300f
    var initY = 300f

    // 锚点坐标
    var anchorX = 200f
    var anchorY = 300f

    // 起点坐标
    var startX = 100f
    var startY = 100f

    // 定点圆半径
    var radius = DEFAULT_RADIUS

    // 判断动画是否开始
    var isAnimStart = false

    // 判断是否开始拖动
    var isTouch = false
    lateinit var exploredImageView: ImageView
    lateinit var tipImageView: ImageView

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        path = Path()
        paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.strokeWidth = 2f
        paint.color = Color.RED
        val params = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        exploredImageView = ImageView(context)
        exploredImageView.layoutParams = params
        exploredImageView.setImageResource(R.drawable.tip_anim)
        exploredImageView.visibility = View.INVISIBLE
        tipImageView = ImageView(context)
        tipImageView.layoutParams = params
        tipImageView.setImageResource(R.drawable.skin_tips_newmessage_ninetynine)
        addView(tipImageView)
        addView(exploredImageView)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        exploredImageView.x = startX - exploredImageView.width / 2
        exploredImageView.y = startY - exploredImageView.height / 2
        tipImageView.x = startX - tipImageView.width / 2
        tipImageView.y = startY - tipImageView.height / 2
        super.onLayout(changed, left, top, right, bottom)
    }

    private fun calculate() {
        val distance = Math.sqrt(Math.pow(initY - startY.toDouble(), 2.0) + Math.pow(initX - startX.toDouble(), 2.0)).toFloat()
        radius = -distance / 15 + DEFAULT_RADIUS
        if (radius < 9) {
            isAnimStart = true
            exploredImageView.visibility = View.VISIBLE
            exploredImageView.setImageResource(R.drawable.tip_anim)
            (exploredImageView.drawable as AnimationDrawable).stop()
            (exploredImageView.drawable as AnimationDrawable).start()
            tipImageView.visibility = View.GONE
        }

        // 根据角度算出四边形的四个点
        val offsetX = (radius * Math.sin(Math.atan((initY - startY) / (initX - startX).toDouble()))).toFloat()
        val offsetY = (radius * Math.cos(Math.atan((initY - startY) / (initX - startX).toDouble()))).toFloat()
        val x1 = startX - offsetX
        val y1 = startY + offsetY
        val x2 = initX - offsetX
        val y2 = initY + offsetY
        val x3 = initX + offsetX
        val y3 = initY - offsetY
        val x4 = startX + offsetX
        val y4 = startY - offsetY
        path.reset()
        path.moveTo(x1, y1)
        path.quadTo(anchorX, anchorY, x2, y2)
        path.lineTo(x3, y3)
        path.quadTo(anchorX, anchorY, x4, y4)
        path.lineTo(x1, y1)

        // 更改图标的位置
        tipImageView.x = initX - tipImageView.width / 2
        tipImageView.y = initY - tipImageView.height / 2
    }

    override fun onDraw(canvas: Canvas) {
        if (isAnimStart || !isTouch) {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.OVERLAY)
        } else {
            calculate()
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.OVERLAY)
            canvas.drawPath(path, paint)
            canvas.drawCircle(startX, startY, radius, paint)
            canvas.drawCircle(initX, initY, radius, paint)
        }
        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            // 判断触摸点是否在tipImageView中
            val rect = Rect()
            val location = IntArray(2)
            tipImageView.getDrawingRect(rect)
            tipImageView.getLocationOnScreen(location)
            rect.left = location[0]
            rect.top = location[1]
            rect.right = rect.right + location[0]
            rect.bottom = rect.bottom + location[1]
            if (rect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                isTouch = true
            }
        } else if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
            isTouch = false
            tipImageView.x = startX - tipImageView.width / 2
            tipImageView.y = startY - tipImageView.height / 2
        }
        invalidate()
        if (isAnimStart) {
            return super.onTouchEvent(event)
        }
        anchorX = (event.x + startX) / 2
        anchorY = (event.y + startY) / 2
        initX = event.x
        initY = event.y
        return true
    }

    companion object {
        // 默认定点圆半径
        const val DEFAULT_RADIUS = 20f
    }
}