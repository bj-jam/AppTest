package com.app.test.all.scrollview

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.*
import com.app.test.R
import com.app.test.all.scrollview.IdeaScrollView.OnScrollChangedColorListener
import com.app.test.all.scrollview.IdeaScrollView.OnSelectedIndicateChangedListener
import java.util.*
import kotlin.math.abs

open class MoveScrollView : FrameLayout {
    private var viewPager: IdeaViewPager? = null
    private lateinit var ideaScrollView: IdeaScrollView
    private val text: TextView? = null
    private lateinit var header: LinearLayout
    private lateinit var radioGroup: RadioGroup
    private lateinit var headerParent: LinearLayout
    private lateinit var icon: ImageView
    private var layer: View? = null
    private var currentPercentage = 0f
    private val radioGroupListener = RadioGroup.OnCheckedChangeListener { group, checkedId ->
        for (i in 0 until radioGroup.childCount) {
            val radioButton = radioGroup.getChildAt(i) as RadioButton
            radioButton.setTextColor(if (radioButton.isChecked) getRadioCheckedAlphaColor(currentPercentage) else getRadioAlphaColor(currentPercentage))
            if (radioButton.isChecked && isNeedScrollTo) {
                ideaScrollView.position = i
            }
        }
    }
    private var isNeedScrollTo = true

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView()
    }

    protected fun initView() {
        View.inflate(context, R.layout.view_scroll_view, this)
        //        StatusBarCompat.translucentStatusBar(this);
        header = findViewById<View>(R.id.header) as LinearLayout
        headerParent = findViewById<View>(R.id.headerParent) as LinearLayout
        icon = findViewById<View>(R.id.icon) as ImageView
        radioGroup = findViewById<View>(R.id.radioGroup) as RadioGroup
        ideaScrollView = findViewById<View>(R.id.ideaScrollView) as IdeaScrollView
        viewPager = findViewById<View>(R.id.viewPager) as IdeaViewPager
        layer = findViewById(R.id.layer)
        val rectangle = Rect()
        (context as Activity).window.decorView.getWindowVisibleDisplayFrame(rectangle)
        ideaScrollView.setViewPager(viewPager, getMeasureHeight(headerParent) - rectangle.top)
        icon.imageAlpha = 0
        radioGroup.alpha = 0f
        radioGroup.check(radioGroup.getChildAt(0).id)
        val one = findViewById<View>(R.id.one)
        val two = findViewById<View>(R.id.two)
        val four = findViewById<View>(R.id.four)
        val three = findViewById<View>(R.id.three)
        val araryDistance = ArrayList<Int>()
        araryDistance.add(0)
        araryDistance.add(getMeasureHeight(one) - getMeasureHeight(headerParent))
        araryDistance.add(getMeasureHeight(one) + getMeasureHeight(two) - getMeasureHeight(headerParent))
        araryDistance.add(getMeasureHeight(one) + getMeasureHeight(two) + getMeasureHeight(three) - getMeasureHeight(headerParent))
        ideaScrollView.setArrayDistance(araryDistance)
        ideaScrollView.onScrollChangedColorListener = object : OnScrollChangedColorListener {
            override fun onChanged(percentage: Float) {
                val color = getAlphaColor(if (percentage > 0.9f) 1.0f else percentage)
                header.setBackgroundDrawable(ColorDrawable(color))
                radioGroup.setBackgroundDrawable(ColorDrawable(color))
                icon.imageAlpha = ((if (percentage > 0.9f) 1.0f else percentage) * 255).toInt()
                radioGroup.alpha = (if (percentage > 0.9f) 1.0f else percentage) * 255
                setRadioButtonTextColor(percentage)
            }

            override fun onChangedFirstColor(percentage: Float) {}
            override fun onChangedSecondColor(percentage: Float) {}
        }
        ideaScrollView.onSelectedIndicateChangedListener = OnSelectedIndicateChangedListener { position ->
            isNeedScrollTo = false
            radioGroup.check(radioGroup.getChildAt(position).id)
            isNeedScrollTo = true
        }
        radioGroup.setOnCheckedChangeListener(radioGroupListener)
    }

    fun setRadioButtonTextColor(percentage: Float) {
        if (abs(percentage - currentPercentage) >= 0.1f) {
            for (i in 0 until radioGroup.childCount) {
                val radioButton = radioGroup.getChildAt(i) as RadioButton
                radioButton.setTextColor(if (radioButton.isChecked) getRadioCheckedAlphaColor(percentage) else getRadioAlphaColor(percentage))
            }
            currentPercentage = percentage
        }
    }

    private fun getMeasureHeight(view: View?): Int {
        val width = MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED)
        val height = MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED)
        view?.measure(width, height)
        return view?.measuredHeight ?: 0
    }

    fun getAlphaColor(f: Float): Int {
        return Color.argb((f * 255).toInt(), 0x09, 0xc1, 0xf4)
    }

    fun getLayerAlphaColor(f: Float): Int {
        return Color.argb((f * 255).toInt(), 0x09, 0xc1, 0xf4)
    }

    private fun getRadioCheckedAlphaColor(f: Float): Int {
        return Color.argb((f * 255).toInt(), 0x44, 0x44, 0x44)
    }

    private fun getRadioAlphaColor(f: Float): Int {
        return Color.argb((f * 255).toInt(), 0xFF, 0xFF, 0xFF)
    }
}