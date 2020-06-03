package com.app.test.anim

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import com.app.test.R
import com.app.test.util.DensityUtil
import com.app.test.view.TeachTalkTogetherView

/**
 *
 */
class AnimActivity : Activity() {
    private lateinit var ivIsVip: ImageView
    private lateinit var toOpenVipImg: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_anim)
        initView()
        initLineHeadAnima()
        (findViewById<View>(R.id.teach) as TeachTalkTogetherView).startAnima()
    }

    private fun initLineHeadAnima() {
        val offset = DensityUtil.dp2px(15).toFloat()
        val container = findViewById<ViewGroup>(R.id.container)
        val pic0 = findViewById<ImageView>(R.id.pic0)
        val pic1 = findViewById<ImageView>(R.id.pic1)
        val pic2 = findViewById<ImageView>(R.id.pic2)
        val pic3 = findViewById<ImageView>(R.id.pic3)
        val pic4 = findViewById<ImageView>(R.id.pic4)
        pic0.z = 4f
        pic1.z = 3f
        pic2.z = 2f
        pic3.z = 1f
        pic4.z = 0f
        val valueAnimator = ValueAnimator.ofFloat(0f, offset)
        valueAnimator.interpolator = DecelerateInterpolator()
        valueAnimator.duration = 2000
        valueAnimator.addUpdateListener { animation ->
            for (i in 0 until container.childCount) {
                val chilidView = container.getChildAt(i)
                val order = container.childCount - 1 - chilidView.z //0-4
                if (order in 1.0..3.0) {
                    val transX = animation.animatedValue as Float + (order - 1) * offset
                    chilidView.translationX = transX
                }
                if (order == 0f) {
                    //缩放动画
                    chilidView.scaleX = 1.0f * (animation.animatedValue as Float / offset)
                    chilidView.scaleY = 1.0f * (animation.animatedValue as Float / offset)
                } else if (order == 3f) {
                    //缩放动画
//                        chilidView.setScaleX(1.0f * (1.0f - (float)animation.getAnimatedValue() / offset));
//                        chilidView.setScaleY(1.0f * (1.0f - (float)animation.getAnimatedValue() / offset));
                }
                if (animation.currentPlayTime == 3000L) {
                    //动画结束
                    if (order == 3f) {
                        chilidView.scaleX = 0f
                        chilidView.scaleY = 0f
                        chilidView.z = 4f
                        chilidView.translationX = 0f
                    }
                    if (order == 2f || order == 1f) {
                        chilidView.z = chilidView.z - 1
                    }
                }
            }
        }
        findViewById<View>(R.id.bt).setOnClickListener {
            valueAnimator.repeatCount = ValueAnimator.INFINITE
            valueAnimator.start()
        }
    }

    private fun initView() {
        ivIsVip = findViewById<View>(R.id.iv_is_vip) as ImageView
        toOpenVipImg = findViewById<View>(R.id.to_open_vip_img) as ImageView
        val animator1 = ObjectAnimator.ofFloat(toOpenVipImg, "translationY", 0f, -50f, 0f)
        animator1.duration = 1500
        animator1.repeatCount = -1
        animator1.start()
        val set = AnimatorSet()
        set.play(animator1)
        set.start()
    }
}