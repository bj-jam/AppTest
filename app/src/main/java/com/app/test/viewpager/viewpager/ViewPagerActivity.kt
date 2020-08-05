package com.app.test.viewpager.viewpager

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.app.test.R

class ViewPagerActivity : AppCompatActivity() {
    private var mViewPager: VerticalViewPager? = null
    private val mImgSource = arrayListOf<String>(
            "http://e.hiphotos.baidu.com/image/pic/item/a1ec08fa513d2697e542494057fbb2fb4316d81e.jpg",
            "http://c.hiphotos.baidu.com/image/pic/item/30adcbef76094b36de8a2fe5a1cc7cd98d109d99.jpg",
            "http://h.hiphotos.baidu.com/image/pic/item/7c1ed21b0ef41bd5f2c2a9e953da81cb39db3d1d.jpg",
            "http://g.hiphotos.baidu.com/image/pic/item/55e736d12f2eb938d5277fd5d0628535e5dd6f4a.jpg",
            "http://e.hiphotos.baidu.com/image/pic/item/4e4a20a4462309f7e41f5cfe760e0cf3d6cad6ee.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/9d82d158ccbf6c81b94575cfb93eb13533fa40a2.jpg",
            "http://e.hiphotos.baidu.com/image/pic/item/4bed2e738bd4b31c1badd5a685d6277f9e2ff81e.jpg",
            "http://g.hiphotos.baidu.com/image/pic/item/0d338744ebf81a4c87a3add4d52a6059252da61e.jpg",
            "http://a.hiphotos.baidu.com/image/pic/item/f2deb48f8c5494ee5080c8142ff5e0fe99257e19.jpg",
            "http://f.hiphotos.baidu.com/image/pic/item/4034970a304e251f503521f5a586c9177e3e53f9.jpg",
            "http://a.hiphotos.baidu.com/image/pic/item/e824b899a9014c087eb617650e7b02087af4f464.jpg",
            "http://d.hiphotos.baidu.com/image/pic/item/b58f8c5494eef01f119945cbe2fe9925bc317d2a.jpg",
            "http://d.hiphotos.baidu.com/image/pic/item/b58f8c5494eef01f119945cbe2fe9925bc317d2a.jpg",
            "http://h.hiphotos.baidu.com/image/pic/item/902397dda144ad340668b847d4a20cf430ad851e.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/359b033b5bb5c9ea5c0e3c23d139b6003bf3b374.jpg",
            "http://a.hiphotos.baidu.com/image/pic/item/8d5494eef01f3a292d2472199d25bc315d607c7c.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/e824b899a9014c08878b2c4c0e7b02087af4f4a3.jpg"
    )
    private var mAdapter: HorizonAdapter? = null
    private var mImgClick: ImageView? = null
    private var mImgAdd: ImageView? = null
    private var mTvAdd: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_viewpager1)
        initView()
        initData()
        initEvent()
    }

    private fun initEvent() {
        mAdapter?.setmOnImageClikListener {
            mImgClick?.visibility = View.VISIBLE
            val imgClickAnimation = AnimationUtils.loadAnimation(this@ViewPagerActivity
                    , R.anim.collect_click)
            mImgClick?.startAnimation(imgClickAnimation)
            val bigAnimationX: Animator = ObjectAnimator.ofFloat(mImgAdd, "scaleX", 1.0f, 1.3f, 1.0f)
            val bigAnimationY: Animator = ObjectAnimator.ofFloat(mImgAdd, "scaleY", 1.0f, 1.3f, 1.0f)
            val set = AnimatorSet()
            set.play(bigAnimationX).with(bigAnimationY)
            set.start()
            val tvClickAnimation = AnimationUtils.loadAnimation(this@ViewPagerActivity, R.anim.tv_collect_add)
            mTvAdd?.visibility = View.VISIBLE
            mTvAdd?.startAnimation(tvClickAnimation)
        }
    }

    private fun initView() {
        mViewPager = findViewById<View>(R.id.viewpager) as VerticalViewPager
        mImgClick = findViewById<View>(R.id.iv_collect_click) as ImageView
        mImgAdd = findViewById<View>(R.id.iv_collect_add) as ImageView
        mTvAdd = findViewById<View>(R.id.tv_collect_add) as TextView
    }

    private fun initData() {
        mAdapter = HorizonAdapter(this, mImgSource, mViewPager)
        mViewPager?.adapter = mAdapter
        mViewPager?.offscreenPageLimit = 3
        mViewPager?.currentItem = mImgSource.size
    }
}