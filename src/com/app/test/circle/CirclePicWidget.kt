package com.app.test.circle

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import com.app.test.R
import com.app.test.util.DensityUtil
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

/**
 * 圆形布局
 */
class CirclePicWidget : ConstraintLayout {
    private var circleImageView: CircleImageView? = null
    private var circleImageView1: CircleImageView? = null
    private var circleImageView2: CircleImageView? = null
    private var circleImageView3: CircleImageView? = null
    private var circleImageView4: CircleImageView? = null
    private var layoutParams: LayoutParams? = null
    private var layoutParams1: LayoutParams? = null
    private var layoutParams2: LayoutParams? = null
    private var layoutParams3: LayoutParams? = null
    private var layoutParams4: LayoutParams? = null
    private var initSize = 0
    private var initAngle = 0
    private var firstAngle = 0f
    private val intWidth = DensityUtil.dp2px(40)
    var radius = 0

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    /**
     * 初始化view
     */
    private fun initView() {
        View.inflate(context, R.layout.view_circel_pic, this)
        circleImageView = findViewById(R.id.civ1)
        circleImageView1 = findViewById(R.id.civ2)
        circleImageView2 = findViewById(R.id.civ3)
        circleImageView3 = findViewById(R.id.civ4)
        circleImageView4 = findViewById(R.id.civ5)
        layoutParams = circleImageView?.layoutParams as LayoutParams
        layoutParams1 = circleImageView1?.layoutParams as LayoutParams
        layoutParams2 = circleImageView2?.layoutParams as LayoutParams
        layoutParams3 = circleImageView3?.layoutParams as LayoutParams
        layoutParams4 = circleImageView4?.layoutParams as LayoutParams
    }

    /**
     * 设置数据
     *
     * @param picList
     */
    fun setPicList(picList: List<String?>?) {
        setViewHide()
        handleCount(picList?.size ?: 0)
        if (picList != null && picList.isNotEmpty()) {
            for (i in picList.indices) {
                if (i > 4)
                    continue
                var view: CircleImageView? = null
                var params: LayoutParams? = null
                when (i) {
                    0 -> {
                        view = circleImageView
                        params = layoutParams
                    }
                    1 -> {
                        view = circleImageView1
                        params = layoutParams1
                    }
                    2 -> {
                        view = circleImageView2
                        params = layoutParams2
                    }
                    3 -> {
                        view = circleImageView3
                        params = layoutParams3
                    }
                    4 -> {
                        view = circleImageView4
                        params = layoutParams4
                    }
                }
                view?.visibility = View.VISIBLE
                val url = picList[i]
                params?.width = initSize
                params?.height = initSize
                params?.circleRadius = radius
                params?.circleAngle = firstAngle + i * initAngle
                view?.layoutParams = params
                Glide.with(context).load(url).asBitmap().placeholder(R.drawable.icon_project_pic).centerCrop().into(view)
            }
        } else {
            val view = circleImageView
            val params = layoutParams
            view?.visibility = View.VISIBLE
            params?.width = initSize
            params?.height = initSize
            params?.circleRadius = radius
            params?.circleAngle = firstAngle
            view?.layoutParams = params
            Glide.with(context).load(R.drawable.icon_project_pic)
                    .asBitmap()
                    .placeholder(R.drawable.icon_project_pic)
                    .centerCrop().into(view)
        }
        postInvalidate()
    }

    /**
     * 处理数据
     *
     * @param count
     */
    private fun handleCount(count: Int) {
        if (count == 0 || count == 1) {
            initSize =  DensityUtil.dp2px( 40)
            initAngle = 0
            firstAngle = 0f
        } else if (count == 2) {
            initSize =  DensityUtil.dp2px(22)
            initAngle = 180
            firstAngle = 90f
        } else if (count == 3) {
            initSize =  DensityUtil.dp2px(22)
            initAngle = 120
            firstAngle = 120f
        } else if (count == 4) {
            initSize =  DensityUtil.dp2px( 20)
            initAngle = 90
            firstAngle = 45f
        } else {
            initSize =  DensityUtil.dp2px( 18)
            initAngle = 72
            firstAngle = 72f
        }
        radius = if (count == 4) intWidth / 2 - initSize / 2 + 5 else intWidth / 2 - initSize / 2
    }

    private fun setViewHide() {
        circleImageView?.visibility = View.GONE
        circleImageView1?.visibility = View.GONE
        circleImageView2?.visibility = View.GONE
        circleImageView3?.visibility = View.GONE
        circleImageView4?.visibility = View.GONE
    }
}