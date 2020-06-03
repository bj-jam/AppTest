package com.app.test.bubble

import android.animation.ObjectAnimator
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import com.app.test.R
import java.util.*

/**
 * @author lcx
 * Created at 2020.3.26
 * Describe:
 */
class BubbleActivity : Activity() {
    private val mRandom = Random()
    private val mTimer = Timer()
    private lateinit var mBubbleLayout: BubbleLayout
    private lateinit var ivAnim: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bubble)
        mBubbleLayout = findViewById<View>(R.id.heart_layout) as BubbleLayout
        ivAnim = findViewById<View>(R.id.iv_anim) as ImageView
        mTimer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                mBubbleLayout.post { mBubbleLayout.addHeart(randomColor()) }
            }
        }, 500, 200)
        startAnim()
    }

    override fun onDestroy() {
        super.onDestroy()
        mTimer.cancel()
    }

    private fun randomColor(): Int {
        return Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255))
    }

    private fun startAnim() {
        val anim = ObjectAnimator.ofFloat(ivAnim, "rotation", 0f, -15f, 0f, 15f, 0f, -15f, 0f, 15f, 0f, -15f, 0f, 15f, 0f, 0f)
        anim.repeatCount = -1
        anim.duration = 800
        anim.interpolator = LinearInterpolator()
        anim.start()
    }
}