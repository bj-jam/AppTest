package com.app.test.circle

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.app.test.util.DensityUtil.dp2px
import java.text.DecimalFormat
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * 饼状图
 */
class PieGraph @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    /**
     * 饼状图中心x的位置
     */
    private var mCX = 0f

    /**
     * 饼状图的半径
     */
    private var mCY = 0f

    /**
     * 饼状图的半径
     */
    private var mPieRadius = 0f

    /**
     * 文字大小
     */
    private var mTextSize = 0

    /**
     * 文字大小
     */
    private var mBigSize = 0

    /**
     * 文字大小
     */
    private var mSmallSize = 0

    /**
     * 文字颜色
     */
    private var mTextColor = 0

    /**
     * 文字的高度
     */
    private var mTextHeight = 0f

    /**
     * 文字的基准线
     */
    private var mTextBottom = 0f

    /**
     * 画文字的时候先在原有的圆上面延伸出来的长度
     */
    private var mMarkerLine1 = 0f

    /**
     * mMarkerLine1的基础上延伸出来的水平线的长度
     */
    private var mMarkerLine2: Float = 0f

    /**
     * 绘制类型字体的画笔
     */
    private lateinit var mTextPaint: TextPaint

    /**
     * 绘制类型字体的画笔
     */
    private lateinit var mBigPaint: TextPaint

    /**
     * 绘制类型字体的画笔
     */
    private lateinit var mSmallPaint: TextPaint

    /**
     * 画文字连接线的画笔
     */
    private lateinit var mLinePaint: Paint

    /**
     * 饼状图信息列表
     */
    private lateinit var pieDataHolders: MutableList<PieDataHolder>

    /**
     * 饼状图正常时候那个矩形区域
     */
    private lateinit var mPieNormalRectF: RectF

    /**
     * 饼状图的画笔
     */
    private lateinit var mPiePaint: Paint

    /**
     * 分割线的画笔
     */
    private lateinit var mCutLinePaint: Paint

    /**
     * 当前要画的文字的区域
     */
    private lateinit var mCurrentTextRect: Rect

    /**
     * 确定比例保存几位小数
     */
    private lateinit var mDecimalFormat: DecimalFormat

    /**
     * 文字矩形区域
     */
    private lateinit var mBigTextRect: Rect

    /**
     * 文字矩形区域
     */
    private lateinit var mSmallTextRect: Rect

    private fun initData() {
        mTextPaint = TextPaint()
        mTextPaint.flags = Paint.ANTI_ALIAS_FLAG
        mTextPaint.textAlign = Paint.Align.LEFT

        mBigPaint = TextPaint()
        mBigPaint.flags = Paint.ANTI_ALIAS_FLAG
        mBigPaint.color = -0xcccccd
        mBigPaint.textSize = mBigSize.toFloat()

        mSmallPaint = TextPaint()
        mSmallPaint.flags = Paint.ANTI_ALIAS_FLAG
        mSmallPaint.color = -0xcccccd
        mSmallPaint.textSize = mSmallSize.toFloat()

        pieDataHolders = ArrayList()

        mPieNormalRectF = RectF()

        mPiePaint = Paint()
        mPiePaint.flags = Paint.ANTI_ALIAS_FLAG
        mPiePaint.style = Paint.Style.STROKE
        mPiePaint.strokeWidth = 80f

        mCutLinePaint = Paint()
        mCutLinePaint.flags = Paint.ANTI_ALIAS_FLAG
        mCutLinePaint.style = Paint.Style.STROKE
        mCutLinePaint.strokeWidth = 10f
        mCutLinePaint.color = -0x1

        mCurrentTextRect = Rect()

        mLinePaint = Paint()
        mLinePaint.flags = Paint.ANTI_ALIAS_FLAG
        mLinePaint.style = Paint.Style.FILL

        mDecimalFormat = DecimalFormat("0.00")

        mBigTextRect = Rect()

        mSmallTextRect = Rect()
    }

    /**
     * 获取xml里面定义的属性
     */
    private fun initAttrs() {
        mPieRadius = dp2px(80).toFloat()
        mTextSize = dp2px(14)
        mBigSize = dp2px(16)
        mSmallSize = dp2px(10)
        mTextColor = -0x99999a
        mMarkerLine1 = dp2px(10).toFloat()
        mMarkerLine2 = dp2px(65).toFloat()
    }

    /**
     * 得到位置的高度，基准线啥啥的
     */
    private fun initTextMetrics() {
        mTextPaint.textSize = mTextSize.toFloat()
        val fontMetrics = mTextPaint.fontMetrics
        mTextHeight = fontMetrics.descent - fontMetrics.ascent
        mTextBottom = fontMetrics.bottom
    }

    /**
     * 测量控件大小,这里宽度我们不测了，只是去测量高度，宽度直接用父控件传过来的大小
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }

    /**
     * 具体的绘制
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        initPieRectF()
        drawPie(canvas)
        drawText(canvas)
    }

    /**
     * 这里呢，去得到画饼状图的时候的那个矩形
     */
    private fun initPieRectF() {
        mPieNormalRectF.left = width / 2 - mPieRadius
        mPieNormalRectF.top = height / 2 - mPieRadius
        mPieNormalRectF.right = mPieNormalRectF.left + mPieRadius * 2
        mPieNormalRectF.bottom = mPieNormalRectF.top + mPieRadius * 2
        mCX = width / 2.toFloat()
        mCY = height / 2.toFloat()
    }

    /**
     * 画饼状图
     */
    private fun drawPie(canvas: Canvas) {
        if (pieDataHolders.size <= 0) {
            return
        }
        for (i in pieDataHolders.indices) {
            val pieDataHolder = pieDataHolders[i]
            mPiePaint.color = pieDataHolder.mColor
            if (pieDataHolder.mSweepAngel == 0f) {
                // 0度的不画
                continue
            }
            // 正常画圆弧
            canvas.drawArc(mPieNormalRectF, pieDataHolder.mStartAngel, pieDataHolder.mSweepAngel, false, mPiePaint)
        }
        mCutLinePaint.color = -0x1
        for (i in pieDataHolders.indices) {
            val pieDataHolder = pieDataHolders[i]
            if (pieDataHolder.mSweepAngel == 0f) {
                // 0度的不画
                continue
            }
            var middle = pieDataHolder.mStartAngel % 360
            if (middle < 0) {
                middle += 360f
            }
            val linePath = Path()
            linePath.close()
            linePath.moveTo(mCX, mCY)
            // 找到圆边缘上的点
            val startX = (width / 2 + (mPieRadius + 50) * cos(Math.toRadians(middle.toDouble()))).toFloat()
            val startY = (height / 2 + (mPieRadius + 50) * sin(Math.toRadians(middle.toDouble()))).toFloat()
            linePath.lineTo(startX, startY)
            canvas.drawPath(linePath, mCutLinePaint)
        }
        mCutLinePaint.color = -0x9090a
        canvas.drawCircle(mCX, mCY, mPieRadius - 40, mCutLinePaint)
    }

    /**
     * 画文字
     */
    private fun drawText(canvas: Canvas) {
        mTextPaint.color = mTextColor
        mCurrentTextRect.setEmpty()
        for (index in pieDataHolders.indices) {
            val pieDataHolder = pieDataHolders[index]
            mLinePaint.style = Paint.Style.STROKE
            mLinePaint.color = pieDataHolder.mColor
            if (pieDataHolder.mSweepAngel == 0f) {
                // 没有比例的不画
                continue
            }
            val textMarker = pieDataHolder.mMarker ?: continue
            val textWidth = mTextPaint.measureText(textMarker)
            // 找到圆弧一半的位置，要往这个方向拉出去
            var middle = (pieDataHolder.mStartAngel + pieDataHolder.mSweepAngel / 2) % 360
            if (middle < 0) {
                middle += 360f
            }
            //画折线
            val linePath = Path()
            linePath.close()
            // 找到圆边缘上的点
            val startX = (width / 2 + (mPieRadius + 60) * cos(Math.toRadians(middle.toDouble()))).toFloat()
            val startY = (height / 2 + (mPieRadius + 60) * sin(Math.toRadians(middle.toDouble()))).toFloat()
            linePath.moveTo(startX, startY)
            val x = (width / 2 + (mMarkerLine1 + mPieRadius + 60) * cos(Math.toRadians(middle.toDouble()))).toFloat()
            val y = (height / 2 + (mMarkerLine1 + mPieRadius + 60) * sin(Math.toRadians(middle.toDouble()))).toFloat()
            linePath.lineTo(x, y)
            var landLineX: Float
            // 左边 右边的判断
            landLineX = if (270f > middle && middle > 90f) {
                x - mMarkerLine2
            } else {
                x + mMarkerLine2
            }
            linePath.lineTo(landLineX, y) // 画文字线先确定了
            canvas.drawPath(linePath, mLinePaint)
            mLinePaint.style = Paint.Style.FILL
            canvas.drawCircle(startX, startY, 6f, mLinePaint)

            //画浅颜色的线
            var bStartX: Float
            // 左边 右边的判断
            bStartX = if (270f > middle && middle > 90f) {
                x - 10
            } else {
                x + 10
            }
            mLinePaint.style = Paint.Style.STROKE
            mLinePaint.color = pieDataHolder.mBottomColor
            val linePath1 = Path()
            linePath1.close()
            linePath1.moveTo(bStartX, y + 4)
            linePath1.lineTo(landLineX, y + 4) // 画文字线先确定了
            canvas.drawPath(linePath1, mLinePaint)

            // 继续去确定文字的位置
            if (270f > middle && middle > 90f) {
                // 圆的右边
                // 文字的区域
                mCurrentTextRect.top = (y + mTextHeight / 3).toInt()
                mCurrentTextRect.left = landLineX.toInt()
                mCurrentTextRect.bottom = (mCurrentTextRect.top + mTextHeight).toInt()
                mCurrentTextRect.right = (mCurrentTextRect.left + textWidth).toInt()
            } else {
                // 圆的左边
                // 文字的区域
                mCurrentTextRect.top = (y + mTextHeight / 3).toInt()
                mCurrentTextRect.left = (landLineX - textWidth).toInt()
                mCurrentTextRect.bottom = (mCurrentTextRect.top + mTextHeight).toInt()
                mCurrentTextRect.right = mCurrentTextRect.left
            }
            canvas.drawText(textMarker, mCurrentTextRect.left.toFloat(), mCurrentTextRect.top + mTextHeight - mTextBottom, mTextPaint)
            mBigPaint.getTextBounds(pieDataHolder.bigInfo, 0, pieDataHolder.bigInfo.length
                    ?: 0, mBigTextRect)
            mSmallPaint.getTextBounds(pieDataHolder.smallInfo, 0, pieDataHolder.smallInfo.length, mSmallTextRect)
            var textX: Float
            val textY = y - mBigTextRect.height() / 3
            textX = if (270f > middle && middle > 90f) {
                // 圆的左边文字的区域
                landLineX
            } else {
                if (pieDataHolder.type == -1) {
                    landLineX - mBigTextRect.width() - mSmallTextRect.width() - 5
                } else landLineX - mBigTextRect.width()
            }
            var sTextX: Float
            sTextX = if (270f > middle && middle > 90f) {
                // 圆的左边文字的区域
                landLineX + mBigTextRect.width() + 5
            } else {
                landLineX - mSmallTextRect.width()
            }
            canvas.drawText(pieDataHolder.bigInfo, textX, textY, mBigPaint)
            if (pieDataHolder.type == -1) {
                canvas.drawText(pieDataHolder.smallInfo, sTextX, textY, mSmallPaint)
            }
        }
    }

    /**
     * 设置饼状图数据(给外部调用的)
     */
    fun setPieData(pieDataList: List<PieDataHolder>?) {
        if (pieDataList == null || pieDataList.isEmpty()) {
            return
        }
        pieDataHolders.clear()
        pieDataHolders.addAll(pieDataList)
        // 计算每个饼状图的比例，开始角度，扫过的角度
        var sum = 0f
        for (pieDataHolder in pieDataHolders) {
            sum += pieDataHolder.mAllScore
        }
        var preSum = 0f // 当前位置之前的总的值，算开始角度用的，总共360
        for (index in pieDataList.indices) {
            val pieDataHolder = pieDataHolders[index]
            pieDataHolder.mPosition = index
            pieDataHolder.mRatio = mDecimalFormat.format(pieDataHolder.mAllScore / sum.toDouble()).toFloat()
            pieDataHolder.mStartAngel = preSum / sum * 360f
            preSum += pieDataHolder.mAllScore
            if (index == pieDataList.size - 1) {
                // 如果是最后一个 目的是避免精度的问题
                pieDataHolder.mSweepAngel = 360 - pieDataHolder.mStartAngel
            } else {
                pieDataHolder.mSweepAngel = pieDataHolder.mRatio * 360
            }
        }
        // 这里要调整一下比例，因为精度的原因，有的时候可能加起来不是100%，解决办法呢就是最大的比例直接用100减掉其他的
        var maxRatioPosition = 0
        var maxRatioValue = 0f
        for (pieDataHolder in pieDataHolders) {
            if (maxRatioValue < pieDataHolder.mRatio) {
                maxRatioValue = pieDataHolder.mRatio
                maxRatioPosition = pieDataHolder.mPosition
            }
        }
        var sumWithOutMax = 0f
        var maxHolder: PieDataHolder? = null
        for (pieDataHolder in pieDataHolders) {
            if (pieDataHolder.mPosition != maxRatioPosition) {
                sumWithOutMax += pieDataHolder.mRatio
            } else {
                maxHolder = pieDataHolder
            }
        }
        if (maxHolder != null) {
            maxHolder.mRatio = 1 - mDecimalFormat.format(sumWithOutMax.toDouble()).toFloat()
        }
        invalidate()
    }

    /**
     * 饼状图里面每个饼的信息
     */
    class PieDataHolder(allScore: Int, color: Int,
                        /**
                         * 颜色
                         */
                        var mBottomColor: Int, label: String, studentScore: Int, var type: Int) {
        /**
         * 具体的值
         */
        var mAllScore: Float = allScore.toFloat()

        /**
         * 比例
         */
        var mRatio = 0f

        /**
         * 颜色
         */
        var mColor: Int = color

        /**
         * 文字标记
         */
        var mMarker: String = label

        /**
         * 起始弧度
         */
        var mStartAngel = 0f

        /**
         * 扫过的弧度
         */
        var mSweepAngel = 0f

        /**
         * 位置下标
         */
        var mPosition = 0
        var bigInfo: String = ""
        var smallInfo: String

        init {
            when (type) {
                -1 -> {
                    bigInfo = studentScore.toString() + ""
                }
                1 -> {
                    bigInfo = "???"
                }
                2 -> {
                    bigInfo = "????"
                }
                3 -> {
                    bigInfo = "????"
                }
            }
            smallInfo = "分/" + allScore + "分"
        }
    }

    init {
        initAttrs()
        initData()
        initTextMetrics()
    }
}