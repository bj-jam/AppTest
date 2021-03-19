package com.app.ui.zhibo

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import com.app.ui.R
import com.app.ui.zhibo.utils.MagicTextView

/**
 *
 */
class MagicTextViewActivity : AppCompatActivity() {
    private var inAnim: TranslateAnimation? = null
    private var giftNumberAnim: NumberAnim? = null
    private var mtv_giftNum: MagicTextView? = null
    var count = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_magic)
        inAnim = AnimationUtils.loadAnimation(this, R.anim.gift_in) as TranslateAnimation // 礼物进入时动画
        mtv_giftNum = findViewById(R.id.mtv_giftNum)
        mtv_giftNum?.text = "x$count"
        giftNumberAnim = NumberAnim() // 初始化数字动画
        mtv_giftNum?.setOnClickListener(View.OnClickListener {
            count++
            mtv_giftNum?.text = "x$count"
            giftNumberAnim?.showAnimator(mtv_giftNum)
        })
    }

    class NumberAnim {
        private var lastAnimator: Animator? = null
        fun showAnimator(v: View?) {
            lastAnimator?.removeAllListeners()
            lastAnimator?.cancel()
            lastAnimator?.end()
            val animScaleX = ObjectAnimator.ofFloat(v, "scaleX", 1.3f, 1.0f)
            val animScaleY = ObjectAnimator.ofFloat(v, "scaleY", 1.3f, 1.0f)
            val animSet = AnimatorSet()
            animSet.playTogether(animScaleX, animScaleY)
            animSet.duration = 200
            lastAnimator = animSet
            animSet.start()
        }
    }
}