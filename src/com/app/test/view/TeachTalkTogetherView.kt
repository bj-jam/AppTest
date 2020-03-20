package com.app.test.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.RelativeLayout
import com.app.test.R
import com.app.test.utils.DisplayUtil
import com.makeramen.roundedimageview.RoundedImageView
import kotlin.math.roundToInt

/**
 * Created by able on 2019/9/23.
 * description:
 */


class TeachTalkTogetherView : RelativeLayout {
    private val dp10: Int by lazy { DisplayUtil.dip2px(context, 10f) }
    private val dp15: Int by lazy { DisplayUtil.dip2px(context, 15f) }
    private val dp20: Int by lazy { DisplayUtil.dip2px(context, 20f) }
    private val dp65: Int by lazy { DisplayUtil.dip2px(context, 65f) }
    private val valueAnimator: ValueAnimator
    private var picIndex = 3

    private val picArr = arrayOf(   R.color.red,
                                    R.color.green,
                                    R.color.red,
                                    R.color.color_primary_dark,
                                    R.color.sys_bg,
                                    R.color.color_primary_dark,
                                    R.color.red,
                                    R.color.text_colord,
                                    R.color.bebebe,
                                    R.color.color_primary_dark,
                                    R.color.bg_color,
                                    R.color.dynamic_background,
                                    R.color.pink_color,
                                    R.color.blue,
                                    R.color.orange_color,
                                    R.color.light_black,
                                    R.color.light_black,
                                    R.color.line,
                                    R.color.green,
                                    R.color.color_primary_dark)

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        isChildrenDrawingOrderEnabled = true
        layoutParams = layoutParams?.also { it.width = dp65 } ?: LayoutParams(dp65,LayoutParams.WRAP_CONTENT)

        //初始化圆形控件函数变量
        val initRoundImg: (roundImg:RoundedImageView, order:Int)->Unit = {it,order->
            it.layoutParams = LayoutParams(dp20,dp20)
            it.setCornerRadius(dp10.toFloat(),dp10.toFloat(),dp10.toFloat(),dp10.toFloat())

            //todo
            if(order == 5){it.setImageResource(R.color.white)}else{it.setImageResource(picArr[order-1])}


//            it.borderWidth = 2f
            it.setBorderColor(ColorStateList.valueOf(resources.getColor(R.color.white)))

            addView(it)

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            it.z = (6 - order).toFloat()
        }

        //根据order 获取初始translationX值 函数变量
        val getTranslationXbyOrder: (order: Int)->Float = {
            when(it){
                1->0f
                2->0f
                3->dp15.toFloat()
                4->dp15.toFloat() * 2
                5->dp15.toFloat() * 3
                else->0f
            }
        }



        //最上面用来缩放的
        val pic1 = RoundedImageView(context).also { initRoundImg(it,1);it.scaleX=0f;it.scaleY=0f}
        //平移1号
        val pic2 = RoundedImageView(context).also { initRoundImg(it,2); }
        //平移2号
        val pic3 = RoundedImageView(context).also { initRoundImg(it,3);it.translationX = getTranslationXbyOrder(3) }
        //平移3号
        val pic4 = RoundedImageView(context).also { initRoundImg(it,4);it.translationX = getTranslationXbyOrder(4)}
        //最后一个 固定显示省略号
        val pic5 = RoundedImageView(context).also { initRoundImg(it,5);it.translationX = getTranslationXbyOrder(5)}


        valueAnimator = ValueAnimator.ofFloat(0f,dp15.toFloat())
        valueAnimator.interpolator = DecelerateInterpolator()
        valueAnimator.duration = 2000
//        valueAnimator.repeatCount = ValueAnimator.INFINITE

        valueAnimator.addUpdateListener { animator->
            for (i in 0 until childCount) {
                val childView = getChildAt(i)

                val childOrder = 6 - childView.getZifCan() //从左到右 1-5

                //进行平移
                if (childOrder in 2..4) {
                    childView.translationX = animator.animatedValue as Float + getTranslationXbyOrder(childOrder)
                }

                if (childOrder == 1) {
                    childView.scaleX =  (animator.animatedValue as Float / dp15.toFloat()) * 1f
                    childView.scaleY = (animator.animatedValue as Float / dp15.toFloat()) * 1f
                }

            }
        }

        valueAnimator.addListener(object: Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                for (i in 0 until childCount){
                    val childView = getChildAt(i)

                    val childOrder = 6 - childView.getZifCan() //从左到右 1-5

                    if (childOrder == 4) {
                        childView.scaleX = 0f
                        childView.scaleY = 0f
                        childView.setZifCan(5)
                        childView.translationX = 0f

                        picIndex+=1
                        picIndex%=20

                        (childView as? RoundedImageView)?.setImageResource(picArr[picIndex])
                    }

                    if (childOrder in 1..3) {
                        childView.setZifCan(childView.getZifCan() - 1)

                        childView.setZifCan(childView.getZifCan() - 1)
                    }
                }

                valueAnimator.startDelay = 500
                valueAnimator.start()
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }
        })


    }


    fun startAnima() {
        valueAnimator.start()
    }


}

private fun View.getZifCan(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        z.roundToInt()
    }else{
        tag as? Int ?: 0
    }
}

private fun View.setZifCan(z: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        setZ(z.toFloat())
    }else{
        tag = z
    }
}
