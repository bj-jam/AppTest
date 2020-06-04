package com.app.test.lamp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.app.test.R
import java.util.*


/**
 * @author lcx
 * Created at 2020.3.31
 * Describe:
 */
class FlowerView : SurfaceView, SurfaceHolder.Callback, Runnable {
    private var mFlag = true
    private val mFlowers: ArrayList<PointF> by lazy { ArrayList<PointF>() }

    //负责随机数生成
    private val mRandom: Random by lazy {
        Random()
    }

    //小花的图案
    private val mBitmap: Bitmap by lazy { BitmapFactory.decodeResource(resources, R.drawable.ic_game_gold_00) }

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
        holder.addCallback(this)
        //设置背景透明
        setZOrderOnTop(true)
        holder.setFormat(PixelFormat.TRANSLUCENT)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        mFlag = true
        Thread(this).start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        mFlowers.clear()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        mFlag = false
    }

    override fun run() {
        while (mFlag) {
            try {
                Thread.sleep(80)
                val canvas = holder?.lockCanvas()
                var pointF: PointF? = null
                //清屏操作
                if (canvas != null) {
                    canvas.run { drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR) }
                } else {
                    continue
                }
                for (point in mFlowers) {
                    pointF = point
                    canvas.run { drawBitmap(mBitmap, pointF.x, pointF.y, null) }
                    val i = mRandom.nextInt(height / 50).plus(height / 50) //修改雨滴线的纵坐标，使其看起来在下雨
                    pointF.y = pointF.y + i
                }
                holder?.unlockCanvasAndPost(canvas)
                addFlower()
                if (mFlowers.size > 0 && pointF != null && pointF.y >= height) {
                    mFlowers.remove(pointF)
                }
            } catch (e: Exception) {
            }
        }
    }

    private var addTime: Long = 0

    /**
     * 添加花朵
     */
    private fun addFlower() {
        if (System.currentTimeMillis() - addTime < 500) return
        addTime = System.currentTimeMillis()
        val point = PointF()
        point.x = mRandom.nextInt(width).toFloat()
        point.y = -mBitmap.height.toFloat()
        mFlowers.add(point)
    }
}