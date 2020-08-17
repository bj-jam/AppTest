package com.app.test.paint

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.app.test.R


/**
 * @author lcx
 * Created at 2020.8.14
 * Describe:
 */
class XfermodeView : View {
    private var mBgBitmap: Bitmap? = null
    private var mFgBitmap: Bitmap? = null
    private var mPaint: Paint? = null
    private var mCanvas: Canvas? = null
    private var mPath: Path? = null

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(mBgBitmap, 0f, 0f, null)
        canvas.drawBitmap(mFgBitmap, 0f, 0f, null)
    }

    private fun init() {
        mPaint = Paint()
        mPaint?.alpha = 0
        mPaint?.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        mPaint?.style = Paint.Style.STROKE
        mPaint?.strokeJoin = Paint.Join.ROUND
        mPaint?.strokeWidth = 50f
        mPaint?.strokeCap = Paint.Cap.ROUND
        mPath = Path()
        mBgBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_splash_bg)
        mFgBitmap = Bitmap.createBitmap(mBgBitmap?.width ?: 0, mBgBitmap?.height
                ?: 0, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mFgBitmap)
        mCanvas?.drawColor(Color.GRAY)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mPath?.reset()
                mPath?.moveTo(event.x, event.y)
            }
            MotionEvent.ACTION_MOVE -> mPath?.lineTo(event.x, event.y)
        }
        mCanvas?.drawPath(mPath, mPaint)
        invalidate()
        return true
    }

//    private val mRunnable: Runnable = object : Runnable {
//        private var mPixels: IntArray
//        override fun run() {
//            var wipeArea = 0f
//            val totalArea: Float = screenWidth * screenHeight
//            val mBitmap: Bitmap = bitmap
//            mPixels = IntArray(screenWidth * screenHeight)
//            /**
//             * 拿到所有的像素信息
//             */
//            mBitmap.getPixels(mPixels, 0, screenWidth, 0, 0, screenWidth,
//                    screenHeight)
//            /**
//             * 遍历统计擦除的区域
//             */
//            for (i in 0 until screenWidth) {
//                for (j in 0 until screenHeight) {
//                    val index: Int = i + j * screenWidth
//                    if (mPixels[index] == 0) {
//                        wipeArea++
//                    }
//                }
//            }
//            /**
//             * 根据所占百分比，进行一些操作
//             */
//            if (wipeArea > 0 && totalArea > 0) {
//                val percent = (wipeArea * 100 / totalArea).toInt()
//                /**
//                 * 设置达到多少百分比的时候，弹窗提醒是否中奖此处设置为20
//                 */
//                if (percent > 20) {
//                    /**
//                     * 刮开奖以后的操作，此处在子线程toast，可能会发生线程阻塞，只为测试使用
//                     */
//                    Looper.prepare()
//                    Toast.makeText(context, "已刮开$percent%",
//                            Toast.LENGTH_LONG).show()
//                    Looper.loop()
//                }
//            }
//        }
//    }
}