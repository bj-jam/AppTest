package com.app.test.paint

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.FrameLayout
import com.app.test.R
import com.app.test.util.DensityUtil.dp2px
import com.app.test.util.Utils
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import kotlin.math.pow

/**
 * @author lcx
 * Created at 2020.4.3
 * Describe:线条闪烁
 */
class LineTwinkleLayout(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : FrameLayout(context, attributeSet, defStyleAttr) {
    //边框宽度
    private var strokeW = 0f

    //边框圆角
    private var strokeRadius = 0f

    //边框颜色
    private var strokeColor = 0
    private var during = DURING
    private var lineSpace = 0f

    //内边框
    private var insideStrokeW = 0f
    private var insideRadius = 0f
    private var insideColor = 0
    private var insideShadowColor = 0

    //外边框
    private var outsideStrokeW = 0f
    private var outsideRadius = 0f
    private var outsideColor = 0
    private var outsideShadowColor = 0
    private var paint: Paint? = null
    private var outsidePaint: Paint? = null
    private var insidePaint: Paint? = null
    private var shadowPaint: Paint? = null
    private var path: Path? = null
    private var next: Long = 0
    private var colorList: List<Int> = CopyOnWriteArrayList()
    private var shadowColorList: List<Int> = CopyOnWriteArrayList()
    private var outsideAlpha = 0
    private var insideAlpha = 0
    private var outsideBlurMaskRatio = 0.33f

    //外面的实线占宽度比率
    private var outsideShadowWidthRatio = 0.25f
    private var insideBlurMaskRatio = 0.25f

    //里面的实线占宽度比率
    private var insideShadowWidthRatio = 0.3f
    private var outsideAnim: ValueAnimator? = null
    private var insideAnim: ValueAnimator? = null
    private var executor: ScheduledExecutorService? = null
    private val insideBgColor = Color.parseColor("#252B30")
    private var insideBgRadius = -1.0f
    private var insideBgWidth = -1f
    private val isShow = true

    @JvmOverloads
    constructor(context: Context, paramAttributeSet: AttributeSet? = null) : this(context, paramAttributeSet, 0) {
    }

    private fun init(paramContext: Context, paramAttributeSet: AttributeSet?) {
        val typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.LineTwinkleLayout)
        during = typedArray.getInt(R.styleable.LineTwinkleLayout_dll_during, during)
        strokeW = typedArray.getDimensionPixelSize(R.styleable.LineTwinkleLayout_dll_stroke_width, dp2px(21)).toFloat()
        strokeColor = typedArray.getColor(R.styleable.LineTwinkleLayout_dll_stroke_color, Color.TRANSPARENT)
        strokeRadius = typedArray.getDimensionPixelSize(R.styleable.LineTwinkleLayout_dll_stroke_radius, dp2px(16)).toFloat()
        lineSpace = typedArray.getDimensionPixelSize(R.styleable.LineTwinkleLayout_dll_line_space, dp2px(2)).toFloat()
        outsideStrokeW = typedArray.getDimensionPixelSize(R.styleable.LineTwinkleLayout_dll_out_stroke_width, dp2px(6)).toFloat()
        outsideRadius = typedArray.getDimensionPixelSize(R.styleable.LineTwinkleLayout_dll_out_radius, dp2px(22)).toFloat()
        outsideColor = typedArray.getColor(R.styleable.LineTwinkleLayout_dll_out_color, Color.parseColor("#ff108c"))
        outsideShadowColor = typedArray.getColor(R.styleable.LineTwinkleLayout_dll_out_shadow_color, Color.parseColor("#ffd2d2"))
        outsideShadowWidthRatio = typedArray.getFloat(R.styleable.LineTwinkleLayout_dll_out_shadow_width_ratio, 0.5f)
        outsideBlurMaskRatio = typedArray.getFloat(R.styleable.LineTwinkleLayout_dll_out_blur_mask_ratio, 1.5f)
        insideStrokeW = typedArray.getDimensionPixelSize(R.styleable.LineTwinkleLayout_dll_inner_stroke_width, dp2px(6)).toFloat()
        insideRadius = typedArray.getDimensionPixelSize(R.styleable.LineTwinkleLayout_dll_inner_radius, dp2px(18)).toFloat()
        insideColor = typedArray.getColor(R.styleable.LineTwinkleLayout_dll_inner_color, Color.parseColor("#ff108c"))
        insideShadowColor = typedArray.getColor(R.styleable.LineTwinkleLayout_dll_inner_shadow_color, Color.parseColor("#ff108c"))
        insideShadowWidthRatio = typedArray.getFloat(R.styleable.LineTwinkleLayout_dll_inner_shadow_width_ratio, 0.5f)
        insideBlurMaskRatio = typedArray.getFloat(R.styleable.LineTwinkleLayout_dll_inner_blur_mask_ratio, 2f)
        if (outsideStrokeW > strokeW) {
            outsideStrokeW = strokeW / 5
        }
        if (insideStrokeW > strokeW) {
            insideStrokeW = strokeW / 5
        }
        if (lineSpace > strokeW) {
            lineSpace = strokeW / 5
        }
        if (during > DURING) {
            during = DURING
        }
        if (outsideBlurMaskRatio <= 0) {
            outsideBlurMaskRatio = 0.8f
        }
        if (insideBlurMaskRatio <= 0) {
            insideBlurMaskRatio = 0.8f
        }
        typedArray.recycle()
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        outsidePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        outsidePaint!!.style = Paint.Style.STROKE
        insidePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        insidePaint!!.style = Paint.Style.STROKE
        shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        shadowPaint!!.style = Paint.Style.STROKE
        path = Path()
        colorList = getColorList(defaultColors, defaultColors)
        shadowColorList = getColorList(defaultShadowColors, defaultShadowColors)
    }

    private fun startAnim() {
        startInsideAnim()
        startOutsideAnim()
        stopTimer()
        executor = Executors.newSingleThreadScheduledExecutor()
        executor?.scheduleAtFixedRate(object : Runnable {
            override fun run() {
                updateNext()
            }

            private fun updateNext() {
                if (next >= Long.MAX_VALUE / 2) {
                    next = 0
                }
                next += 2
            }
        }, during * 2.toLong(), during * 2.toLong(), TimeUnit.MILLISECONDS)
    }

    private fun startOutsideAnim() {
        if (Utils.isEmpty(outsideAnim)) {
            outsideAnim = ValueAnimator.ofInt(0, 255)
        }
        outsideAnim!!.cancel()
        outsideAnim!!.repeatMode = ValueAnimator.REVERSE
        outsideAnim!!.repeatCount = -1
        outsideAnim!!.interpolator = DecelerateInterpolator()
        outsideAnim!!.setDuration((during * 1f).toLong())
        outsideAnim!!.addUpdateListener { animation ->
            outsideAlpha = if (isShow) {
                animation.animatedValue as Int
            } else {
                0
            }
            postInvalidate()
        }
        outsideAnim!!.start()
    }

    private fun startInsideAnim() {
        if (Utils.isEmpty(insideAnim)) {
            insideAnim = ValueAnimator.ofInt(0, 255)
        }
        insideAnim!!.cancel()
        insideAnim!!.repeatMode = ValueAnimator.REVERSE
        insideAnim!!.interpolator = myInterpolator
        insideAnim!!.repeatCount = -1
        insideAnim!!.setDuration((during * 1f).toLong())
        insideAnim!!.addUpdateListener { animation ->
            insideAlpha = if (isShow) {
                animation.animatedValue as Int
            } else {
                0
            }
            postInvalidate()
        }
        insideAnim!!.start()
    }

    private fun getColorList(colors: IntArray, defaultColors: IntArray): List<Int> {
        var colors = colors
        if (Utils.isEmpty(colors)) {
            colors = defaultColors
        }
        val colorList: MutableList<Int> = CopyOnWriteArrayList()
        for (i in colors.indices) {
            colorList.add(colors[i])
        }
        return colorList
    }

    private fun updateDraw(canvas: Canvas) {
        paint!!.color = strokeColor
        paint!!.strokeWidth = strokeW
        paint!!.style = Paint.Style.STROKE
        canvas.drawPath(path, paint)
        drawDoubleLine(canvas)
    }

    public override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        updateDraw(canvas)
    }

    private fun drawDoubleLine(canvas: Canvas) {
        drawInnerBg(canvas)
        drawOutLine(canvas)
        drawInnerLine(canvas)
    }

    private fun drawInnerBg(canvas: Canvas) {
        if (insideBgRadius < 0) {
            insideBgRadius = insideRadius
        }
        val width = width
        val height = height
        if (insideBgWidth < 0) {
            insideBgWidth = strokeW - outsideStrokeW - lineSpace
        }
        val gbCenter = outsideStrokeW + lineSpace + insideBgWidth / 2
        val bgRect = RectF(gbCenter, gbCenter, width - gbCenter, height - gbCenter)
        shadowPaint!!.color = insideBgColor
        shadowPaint!!.strokeWidth = insideBgWidth
        canvas.drawRoundRect(bgRect, insideBgRadius, insideBgRadius, shadowPaint)
    }

    private fun drawOutLine(canvas: Canvas) {
        val width = width
        val height = height
        val widthSpace = outsideStrokeW / 2
        val out = RectF(widthSpace, widthSpace, width - widthSpace, height - widthSpace)
        outsidePaint!!.color = getFitColor(colorList, next, outsideColor)
        if (outsideStrokeW * outsideBlurMaskRatio > 0) {
            outsidePaint!!.maskFilter = BlurMaskFilter(outsideStrokeW * outsideBlurMaskRatio, BlurMaskFilter.Blur.INNER)
        }
        outsidePaint!!.strokeWidth = outsideStrokeW
        outsidePaint!!.alpha = outsideAlpha
        canvas.drawRoundRect(out, outsideRadius, outsideRadius, outsidePaint)
        shadowPaint!!.color = getFitColor(shadowColorList, next, outsideShadowColor)
        shadowPaint!!.alpha = outsideAlpha
        shadowPaint!!.strokeWidth = outsideStrokeW * outsideShadowWidthRatio
        canvas.drawRoundRect(RectF(widthSpace, widthSpace, width - widthSpace, height - widthSpace)
                , outsideRadius, outsideRadius, shadowPaint)
    }

    private fun drawInnerLine(canvas: Canvas) {
        val width = width
        val height = height
        val widthSpace = lineSpace + outsideStrokeW + insideStrokeW / 2
        val index = next + 1
        val inner = RectF(widthSpace, widthSpace, width - widthSpace, height - widthSpace)
        insidePaint!!.color = getFitColor(colorList, index, insideColor)
        if (insideStrokeW * insideBlurMaskRatio > 0) {
            insidePaint!!.maskFilter = BlurMaskFilter(insideStrokeW * insideBlurMaskRatio, BlurMaskFilter.Blur.INNER)
        }
        insidePaint!!.strokeWidth = insideStrokeW
        insidePaint!!.alpha = insideAlpha
        canvas.drawRoundRect(inner, insideRadius, insideRadius, insidePaint)
        val fitColor = getFitColor(shadowColorList, index, insideShadowColor)
        shadowPaint!!.color = fitColor
        shadowPaint!!.strokeWidth = insideStrokeW * insideShadowWidthRatio
        shadowPaint!!.alpha = insideAlpha
        canvas.drawRoundRect(RectF(widthSpace, widthSpace, width - widthSpace, height - widthSpace)
                , insideRadius, insideRadius, shadowPaint)
    }

    /**
     * colorList,next,outColor
     *
     * @param next
     * @return
     */
    private fun getFitColor(colorList: List<Int>, next: Long, defaultColor: Int): Int {
        if (Utils.isEmpty(colorList) || colorList.size == 1) {
            return defaultColor
        }
        val n = colorList.size
        var innerColorIndex = Math.abs((next % n).toInt())
        innerColorIndex = Math.max(0, innerColorIndex)
        return colorList[innerColorIndex]
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w != oldw || h != oldh) {
            initPath()
            startAnim()
        }
    }

    private fun initPath() {
        val width = width
        val height = height
        if (width <= 0 || height <= 0) {
            return
        }
        path!!.reset()
        val v = strokeW / 2
        val rectF = RectF(v, v, width - v, height - v)
        path!!.addRoundRect(rectF, strokeRadius, strokeRadius, Path.Direction.CW)
    }

    var myInterpolator: Interpolator = Interpolator { input ->
        if (input < 0.4) {
            0f
        } else input.toDouble().pow(2.0).toFloat()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnim()
    }

    fun stopAnim() {
        if (!Utils.isEmpty(outsideAnim)) {
            outsideAnim!!.cancel()
            outsideAnim = null
        }
        if (!Utils.isEmpty(insideAnim)) {
            insideAnim!!.cancel()
            insideAnim = null
        }
        stopTimer()
    }

    private fun stopTimer() {
        try {
            if (Utils.isEmpty(executor)) {
                return
            }
            if (!executor!!.isShutdown) {
                executor!!.shutdownNow()
            }
            executor = null
        } catch (e: Exception) {
        }
    }

    companion object {
        private const val DURING = 400
        private val defaultColors = intArrayOf(Color.parseColor("#ff108c"),
                Color.parseColor("#ff108c"))
        private val defaultShadowColors = intArrayOf(Color.parseColor("#ffd2d2")
                , Color.parseColor("#ff108c"))
    }

    init {
        init(context, attributeSet)
    }
}