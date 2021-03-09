package com.app.test.edittext

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.appcompat.widget.AppCompatEditText
import android.text.TextUtils
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import com.app.test.R
import com.app.test.util.DensityUtil

/**
 * @author lcx
 * Created at 2020.6.24
 * Describe:
 */
class PasswordEditText @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : AppCompatEditText(context, attrs) {
    /**
     * 密码的画笔
     */
    private lateinit var mPasswordPaint: Paint

    /**
     * 密码圆点的颜色
     */
    private var mPasswordColor = DEFAULT_PASSWORD_COLOR

    /**
     * 一个密码所占的宽度
     */
    private var mPasswordItemWidth = 0

    /**
     * 密码的个数,默认为4位数
     */
    private var mPasswordNumber = 4

    /**
     * 密码圆点的半径大小，m默认为4像素
     */
    private var mPasswordRadius = 4

    /**
     * 下划线的画笔
     */
    private lateinit var mUnderlinePaint: Paint

    /**
     * 密码底部下划线的宽度
     */
    private var mUnderlineWidth = 0

    /**
     * 密码底部下划线的厚度
     */
    private var mUnderlineSize = 1

    /**
     * 密码底部下划线的宽度
     */
    private var mUnderlineColor = DEFAULT_UNDERLINE_COLOR

    /**
     * 边框的画笔
     */
    private lateinit var mBorderPaint: Paint

    /**
     * 背景边框颜色
     */
    private var mBorderColor = DEFAULT_BORDER_COLOR

    /**
     * 背景边框厚度大小
     */
    private var mBorderStrokeSize = 1

    /**
     * 背景边框圆角大小
     */
    private var mBorderCorner = 0

    /**
     * 分隔线的画笔
     */
    private lateinit var mDivisionLinePaint: Paint

    /**
     * 分割线的颜色，默认跟边框同个颜色
     */
    private var mDivisionLineColor = mBorderColor

    /**
     * 分割线的大小
     */
    private var mDivisionLineSize = 1

    /**
     * 样式类型
     */
    private var mBgStyle = 0

    /**
     * 初始化画笔
     */
    private fun initPaint() {
        // 初始化密码边框的画笔
        mBorderPaint = Paint()
        // 抗锯齿
        mBorderPaint.isAntiAlias = true
        // 防抖动
        mBorderPaint.isDither = true
        // 给画笔设置大小
        mBorderPaint.strokeWidth = mBorderStrokeSize.toFloat()
        // 设置背景的颜色
        mBorderPaint.color = mBorderColor
        // 画空心
        mBorderPaint.style = Paint.Style.STROKE

        // 初始化分隔线的画笔
        mDivisionLinePaint = Paint()
        // 抗锯齿
        mDivisionLinePaint.isAntiAlias = true
        // 防抖动
        mDivisionLinePaint.isDither = true
        // 分割线画笔设置大小
        mDivisionLinePaint.strokeWidth = mDivisionLineSize.toFloat()
        // 设置分割线的颜色
        mDivisionLinePaint.color = mDivisionLineColor

        //初始化密码的画笔
        mPasswordPaint = Paint()
        // 抗锯齿
        mPasswordPaint.isAntiAlias = true
        // 防抖动
        mPasswordPaint.isDither = true
        // 密码绘制是实心
        mPasswordPaint.style = Paint.Style.FILL
        // 设置密码的颜色
        mPasswordPaint.color = mPasswordColor

        //初始化下划线的画笔
        mUnderlinePaint = Paint()
        // 抗锯齿
        mUnderlinePaint.isAntiAlias = true
        // 防抖动
        mUnderlinePaint.isDither = true
        // 设置颜色
        mUnderlinePaint.color = mUnderlineColor
        // 设置画笔的大小
        mUnderlinePaint.strokeWidth = mUnderlineSize.toFloat()
    }

    /**
     * 初始化属性
     */
    private fun initAttributeSet(context: Context, attrs: AttributeSet?) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.PasswordEditText)
        // 密码的颜色
        mPasswordColor = array.getColor(R.styleable.PasswordEditText_passwordColor, mPasswordColor)
        // 密码圆点的半径
        mPasswordRadius = array.getDimension(R.styleable.PasswordEditText_passwordRadius, DensityUtil.dp2px(mPasswordRadius).toFloat()).toInt()
        // 密码的个数
        mPasswordNumber = array.getInteger(R.styleable.PasswordEditText_passwordNumber, mPasswordNumber)

        // 间隔线大小
        mDivisionLineSize = array.getDimension(R.styleable.PasswordEditText_divisionLineSize, DensityUtil.dp2px(mDivisionLineSize).toFloat()).toInt()
        // 间隔线的颜色
        mDivisionLineColor = array.getColor(R.styleable.PasswordEditText_divisionLineColor, mDivisionLineColor)

        // 边框的厚度
        mBorderStrokeSize = array.getDimension(R.styleable.PasswordEditText_bgSize, DensityUtil.dp2px(mBorderStrokeSize).toFloat()).toInt()
        // 边框的圆角
        mBorderCorner = array.getDimension(R.styleable.PasswordEditText_bgCorner, 0f).toInt()
        // 获取边框的颜色
        mBorderColor = array.getColor(R.styleable.PasswordEditText_bgColor, mBorderColor)

        // 下划线的颜色
        mUnderlineColor = array.getColor(R.styleable.PasswordEditText_textUnderlineColor, mUnderlineColor)
        // 下划线的厚度
        mUnderlineSize = array.getDimension(R.styleable.PasswordEditText_underlineSize, DensityUtil.dp2px(mUnderlineSize).toFloat()).toInt()

        // 样式类型
        mBgStyle = array.getInteger(R.styleable.PasswordEditText_bgStyle, BACKGROUND_STYLE_BORDER)
        array.recycle()
    }


    override fun onDraw(canvas: Canvas) {
        // 一个密码的宽度
        mPasswordItemWidth = ((width - 2 * mBorderStrokeSize - (mPasswordNumber - 1) * mDivisionLineSize)
                / mPasswordNumber)
        mUnderlineWidth = mPasswordItemWidth - 8 * mBorderStrokeSize
        if (mBgStyle == BACKGROUND_STYLE_UNDERLINE) {
            //绘制下划线
            drawUnderLine(canvas)
        } else {
            // 画背景
            drawBg(canvas)
            // 画分割线
            drawDivisionLine(canvas)
        }
        // 画密码
        drawPassword(canvas)
        // 当前密码是不是满了
        val password = text.toString().trim { it <= ' ' }
        if (password.length >= mPasswordNumber) {
            mListener?.passwordFull(password)
        } else {
            mListener?.passwordChanged(password)
        }
    }

    /**
     * 绘制密码
     */
    private fun drawPassword(canvas: Canvas) {
        // 获取当前text
        val text = text.toString().trim { it <= ' ' }
        // 获取密码的长度
        val passwordLength = text.length
        // 不断的绘制密码
        for (i in 0 until passwordLength) {
            val cy = height / 2f
            val cx = (mBorderStrokeSize
                    + i * mPasswordItemWidth + i * mDivisionLineSize + mPasswordItemWidth / 2)
            canvas.drawCircle(cx.toFloat(), cy, mPasswordRadius.toFloat(), mPasswordPaint)
        }
    }

    /**
     * 绘制分割线
     */
    private fun drawDivisionLine(canvas: Canvas) {
        for (i in 0 until mPasswordNumber - 1) {
            val startX = mBorderStrokeSize + (i + 1) * mPasswordItemWidth + i * mDivisionLineSize
            val startY = mBorderStrokeSize
            val endY = height - mBorderStrokeSize
            canvas.drawLine(startX.toFloat(), startY.toFloat(), startX.toFloat(), endY.toFloat(), mDivisionLinePaint)
        }
    }

    /**
     * 绘制背景
     */
    private fun drawBg(canvas: Canvas) {
        val rect = RectF(mBorderStrokeSize.toFloat(),
                mBorderStrokeSize.toFloat(), (width - mBorderStrokeSize).toFloat(), (height - mBorderStrokeSize).toFloat())
        // 绘制背景  drawRect , drawRoundRect  ,
        // 如果有圆角那么就绘制drawRoundRect，否则绘制drawRect
        if (mBorderCorner == 0) {
            canvas.drawRect(rect, mBorderPaint)
        } else {
            canvas.drawRoundRect(rect, mBorderCorner.toFloat(), mBorderCorner.toFloat(), mBorderPaint)
        }
    }

    /**
     * 绘制每个密码项的底部下划线
     */
    private fun drawUnderLine(canvas: Canvas) {
        for (i in 0 until mPasswordNumber) {
            val startX = mBorderStrokeSize * 4 + i * mPasswordItemWidth
            val startY = height - mBorderStrokeSize
            val endX = startX + mUnderlineWidth
            val endY = height - mBorderStrokeSize
            canvas.drawLine(startX.toFloat(), startY.toFloat(), endX.toFloat(), endY.toFloat(), mUnderlinePaint)
        }
    }

    /**
     * 添加一个密码
     */
    fun addPassword(number: String) {
        // 把之前的密码取出来
        var password = text.toString().trim()
        if (password.length >= mPasswordNumber) {
            // 密码不能超过当前密码个输
            return
        }
        // 密码叠加
        password += number
        setText(password)
    }

    /**
     * 删除最后一位密码
     */
    fun deleteLastPassword() {
        var password = text.toString().trim()
        // 判断当前密码是不是空
        if (TextUtils.isEmpty(password)) {
            return
        }
        password = password.substring(0, password.length - 1)
        setText(password)
    }

    // 设置当前密码是否已满的接口回掉
    private var mListener: PasswordFullListener? = null
    fun setOnPasswordFullListener(listener: PasswordFullListener?) {
        mListener = listener
    }

    /**
     * 密码已经全部填满
     */
    interface PasswordFullListener {
        fun passwordFull(password: String?)
        fun passwordChanged(password: String?)
    }

    companion object {
        /**
         * 默认的密码颜色
         */
        private val DEFAULT_PASSWORD_COLOR = Color.parseColor("#333333")

        /**
         * 默认边框的颜色
         */
        private val DEFAULT_BORDER_COLOR = Color.parseColor("#d1d2d6")

        /**
         * 默认密码下划线的颜色
         */
        private val DEFAULT_UNDERLINE_COLOR = Color.parseColor("#666666")
        private const val BACKGROUND_STYLE_BORDER = 0
        private const val BACKGROUND_STYLE_UNDERLINE = 1
    }

    init {
        initAttributeSet(context, attrs)
        initPaint()
        // 默认只能够设置数字
        inputType = EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
    }
}