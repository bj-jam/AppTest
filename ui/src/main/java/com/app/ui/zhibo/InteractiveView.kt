package com.app.ui.zhibo

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.app.ui.R
import com.app.ui.zhibo.utils.DisplayUtil
import com.app.ui.zhibo.utils.MagicTextView
import com.app.ui.zhibo.utils.SoftKeyBoardListener
import com.app.ui.zhibo.utils.SoftKeyBoardListener.OnSoftKeyBoardChangeListener
import java.util.*

/**
 *
 */
class InteractiveView : RelativeLayout, View.OnClickListener {
    // 礼物
    private val giftIcon = intArrayOf(R.mipmap.ic_05,
            R.mipmap.ic_15,
            R.mipmap.ic_24,
            R.mipmap.ic_25)
    private var giftNumberAnim: NumberAnim? = null
    private val messageData: MutableList<String> = LinkedList()
    private var messageAdapter: MessageAdapter? = null
    private var lv_message: ListView? = null
    private var ll_gift_group: LinearLayout? = null
    private var outAnim: TranslateAnimation? = null
    private var inAnim: TranslateAnimation? = null
    private var ll_inputparent: LinearLayout? = null
    private var tv_chat: Button? = null
    private var tv_send: TextView? = null
    private var et_chat: EditText? = null
    private var ll_anchor: LinearLayout? = null
    private var rl_num: RelativeLayout? = null
    private var myContext: Context
    private var btn_gift01: Button? = null
    private var btn_gift02: Button? = null
    private var btn_gift03: Button? = null
    private var btn_gift04: Button? = null

    constructor(context: Context) : super(context) {
        myContext = context
        initView()
        initData()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        myContext = context
        initView()
        initData()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        myContext = context
        initView()
        initData()
    }

    fun initView() {
        inflate(myContext, R.layout.view_interactive, this)
        lv_message = findViewById(R.id.lv_message)
        ll_gift_group = findViewById(R.id.ll_gift_group)
        ll_inputparent = findViewById(R.id.ll_inputparent)
        tv_chat = findViewById(R.id.tv_chat)
        tv_send = findViewById(R.id.tv_send)
        et_chat = findViewById(R.id.et_chat)
        ll_anchor = findViewById(R.id.ll_anchor)
        rl_num = findViewById(R.id.rl_num)
        btn_gift01 = findViewById(R.id.btn_gift01)
        btn_gift02 = findViewById(R.id.btn_gift02)
        btn_gift03 = findViewById(R.id.btn_gift03)
        btn_gift04 = findViewById(R.id.btn_gift04)
    }

    private fun initData() {
        initMessage() // 初始化评论
        initListener()
        clearTiming() // 开启定时清理礼物列表
        initAnim() // 初始化动画
    }

    /**
     * 初始化评论列表
     */
    private fun initMessage() {
        for (x in 0..19) {
            messageData.add("小明: 主播好漂亮啊$x")
        }
        messageAdapter = MessageAdapter(myContext, messageData)
        lv_message?.adapter = messageAdapter
        lv_message?.setSelection(messageData.size)
    }

    private fun initListener() {
        btn_gift01?.setOnClickListener(this)
        btn_gift02?.setOnClickListener(this)
        btn_gift03?.setOnClickListener(this)
        btn_gift04?.setOnClickListener(this)
        tv_chat?.setOnClickListener(this)
        tv_send?.setOnClickListener(this)
        setOnClickListener {
            if (ll_inputparent?.visibility == VISIBLE) {
                tv_chat?.visibility = VISIBLE
                ll_inputparent?.visibility = GONE
                hideKeyboard()
            }
        }

        // 软键盘监听
        SoftKeyBoardListener.setListener(myContext as Activity, object : OnSoftKeyBoardChangeListener {
            override fun keyBoardShow(height: Int) { /*软键盘显示：执行隐藏title动画，并修改listview高度和装载礼物容器的高度*/

                // 输入文字时的界面退出动画
                val animatorSetHide = AnimatorSet()
                val leftOutAnim = ObjectAnimator.ofFloat(rl_num, "translationX", 0f, -rl_num!!.width.toFloat())
                val topOutAnim = ObjectAnimator.ofFloat(ll_anchor, "translationY", 0f, -ll_anchor!!.height.toFloat())
                animatorSetHide.playTogether(leftOutAnim, topOutAnim)
                animatorSetHide.duration = 300
                animatorSetHide.start()
                // 改变listview的高度
                dynamicChangeListviewH(90)
                dynamicChangeGiftParentH(true)
            }

            override fun keyBoardHide(height: Int) { /*软键盘隐藏：隐藏聊天输入框并显示聊天按钮，执行显示title动画，并修改listview高度和装载礼物容器的高度*/
                tv_chat?.visibility = VISIBLE
                ll_inputparent?.visibility = GONE
                // 输入文字时的界面进入时的动画
                val animatorSetShow = AnimatorSet()
                val leftInAnim = ObjectAnimator.ofFloat(rl_num, "translationX", -rl_num!!.width.toFloat(), 0f)
                val topInAnim = ObjectAnimator.ofFloat(ll_anchor, "translationY", -ll_anchor!!.height.toFloat(), 0f)
                animatorSetShow.playTogether(leftInAnim, topInAnim)
                animatorSetShow.duration = 300
                animatorSetShow.start()

                // 改变listview的高度
                dynamicChangeListviewH(150)
                dynamicChangeGiftParentH(false)
            }
        })
    }

    /**
     * 初始化动画
     */
    private fun initAnim() {
        giftNumberAnim = NumberAnim() // 初始化数字动画
        inAnim = AnimationUtils.loadAnimation(myContext, R.anim.gift_in) as TranslateAnimation // 礼物进入时动画
        outAnim = AnimationUtils.loadAnimation(myContext, R.anim.gift_out) as TranslateAnimation // 礼物退出时动画
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.btn_gift01) { // 礼物1,送香皂
            showGift("gift01")
        } else if (id == R.id.btn_gift02) { // 礼物2,送玫瑰
            showGift("gift02")
        } else if (id == R.id.btn_gift03) { // 礼物3,送爱心
            showGift("gift03")
        } else if (id == R.id.btn_gift04) { // 礼物4,送蛋糕
            showGift("gift04")
        } else if (id == R.id.tv_chat) { // 聊天
            tv_chat?.visibility = GONE
            ll_inputparent?.visibility = VISIBLE
            ll_inputparent?.requestFocus() // 获取焦点
            showKeyboard()
        } else if (id == R.id.tv_send) { // 发送消息
            val chatMsg = et_chat!!.text.toString()
            if (!TextUtils.isEmpty(chatMsg)) {
                messageData.add("小明: $chatMsg")
                et_chat?.setText("")
                messageAdapter?.notifyAdapter(messageData)
                lv_message?.setSelection(messageData.size)
            }
            hideKeyboard()
        }
    }

    inner class NumberAnim {
        private var lastAnimator: Animator? = null
        fun showAnimator(v: View?) {
            if (lastAnimator != null) {
                lastAnimator?.removeAllListeners()
                lastAnimator?.cancel()
                lastAnimator?.end()
            }
            val animScaleX = ObjectAnimator.ofFloat(v, "scaleX", 1.3f, 1.0f)
            val animScaleY = ObjectAnimator.ofFloat(v, "scaleY", 1.3f, 1.0f)
            val animSet = AnimatorSet()
            animSet.playTogether(animScaleX, animScaleY)
            animSet.duration = 200
            lastAnimator = animSet
            animSet.start()
        }
    }

    /**
     * 刷礼物的方法
     */
    private fun showGift(tag: String) {
        var newGiftView = ll_gift_group?.findViewWithTag<View>(tag)
        // 是否有该tag类型的礼物
        if (newGiftView == null) {
            // 判断礼物列表是否已经有3个了，如果有那么删除掉一个没更新过的, 然后再添加新进来的礼物，始终保持只有3个
            if (ll_gift_group?.childCount ?: 0 >= 3) {
                // 获取前2个元素的最后更新时间
                val giftView01 = ll_gift_group?.getChildAt(0)
                val iv_gift01 = giftView01?.findViewById<ImageView>(R.id.iv_gift)
                val lastTime1 = iv_gift01?.tag as Long
                val giftView02 = ll_gift_group?.getChildAt(1)
                val iv_gift02 = giftView02?.findViewById<ImageView>(R.id.iv_gift)
                val lastTime2 = iv_gift02?.tag as Long
                if (lastTime1 > lastTime2) { // 如果第二个View显示的时间比较长
                    removeGiftView(1)
                } else { // 如果第一个View显示的时间长
                    removeGiftView(0)
                }
            }

            // 获取礼物
            newGiftView = getNewGiftView(tag)
            ll_gift_group?.addView(newGiftView)

            // 播放动画
            newGiftView.startAnimation(inAnim)
            val mtv_giftNum: MagicTextView = newGiftView.findViewById(R.id.mtv_giftNum)
            inAnim?.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationRepeat(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    giftNumberAnim?.showAnimator(mtv_giftNum)
                }
            })
        } else {
            // 如果列表中已经有了该类型的礼物，则不再新建，直接拿出
            // 更新标识，记录最新修改的时间，用于回收判断
            val iv_gift = newGiftView.findViewById<ImageView>(R.id.iv_gift)
            iv_gift.tag = System.currentTimeMillis()

            // 更新标识，更新记录礼物个数
            val mtv_giftNum: MagicTextView = newGiftView.findViewById(R.id.mtv_giftNum)
            val giftCount = mtv_giftNum.tag as Int + 1 // 递增
            mtv_giftNum.text = "x$giftCount"
            mtv_giftNum.tag = giftCount
            giftNumberAnim?.showAnimator(mtv_giftNum)
        }
    }

    /**
     * 获取礼物
     */
    private fun getNewGiftView(tag: String): View {

        // 添加标识, 该view若在layout中存在，就不在生成（用于findViewWithTag判断是否存在）
        val giftView = LayoutInflater.from(myContext).inflate(R.layout.item_gift, null)
        giftView.tag = tag

        // 添加标识, 记录生成时间，回收时用于判断是否是最新的，回收最老的
        val iv_gift = giftView.findViewById<ImageView>(R.id.iv_gift)
        iv_gift.tag = System.currentTimeMillis()

        // 添加标识，记录礼物个数
        val mtv_giftNum: MagicTextView = giftView.findViewById(R.id.mtv_giftNum)
        mtv_giftNum.tag = 1
        mtv_giftNum.text = "x1"
        when (tag) {
            "gift01" -> iv_gift.setImageResource(giftIcon[0])
            "gift02" -> iv_gift.setImageResource(giftIcon[1])
            "gift03" -> iv_gift.setImageResource(giftIcon[2])
            "gift04" -> iv_gift.setImageResource(giftIcon[3])
        }
        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.topMargin = 10
        giftView.layoutParams = lp
        return giftView
    }

    /**
     * 移除礼物列表里的giftView
     */
    private fun removeGiftView(index: Int) {
        // 移除列表，外加退出动画
        val removeGiftView = ll_gift_group?.getChildAt(index)
        outAnim?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                ll_gift_group?.removeViewAt(index)
            }
        })

        // 开启动画，因为定时原因，所以可能是在子线程
        post { removeGiftView?.startAnimation(outAnim) }
    }

    /**
     * 定时清理礼物列表信息
     */
    private fun clearTiming() {
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                val childCount = ll_gift_group?.childCount ?: 0
                val nowTime = System.currentTimeMillis()
                for (i in 0 until childCount) {
                    val childView = ll_gift_group?.getChildAt(i)
                    val iv_gift = childView?.findViewById<View>(R.id.iv_gift) as ImageView
                    val lastUpdateTime = iv_gift.tag as Long

                    // 更新超过3秒就刷新
                    if (nowTime - lastUpdateTime >= 3000) {
                        removeGiftView(i)
                    }
                }
            }
        }, 0, 3000)
    }

    /**
     * 显示软键盘
     */
    private fun showKeyboard() {
        val imm = myContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(et_chat, InputMethodManager.SHOW_FORCED)
    }

    /**
     * 隐藏软键盘
     */
    fun hideKeyboard() {
        val imm = myContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(et_chat?.windowToken, 0)
    }

    /**
     * 动态的修改listview的高度
     */
    private fun dynamicChangeListviewH(heightPX: Int) {
        val layoutParams = lv_message?.layoutParams
        layoutParams?.height = DisplayUtil.dip2px(myContext, heightPX.toFloat())
        lv_message?.layoutParams = layoutParams
    }

    /**
     * 动态修改礼物父布局的高度
     */
    private fun dynamicChangeGiftParentH(showhide: Boolean) {
        if (showhide) { // 如果软键盘显示中
            if (ll_gift_group?.childCount != 0) {

                // 判断是否有礼物显示，如果有就修改父布局高度，如果没有就不作任何操作
                val layoutParams = ll_gift_group?.layoutParams
                layoutParams?.height = ll_gift_group?.getChildAt(0)?.height
                ll_gift_group?.layoutParams = layoutParams
            }
        } else {
            // 如果软键盘隐藏中
            // 就将装载礼物的容器的高度设置为包裹内容
            val layoutParams = ll_gift_group?.layoutParams
            layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
            ll_gift_group?.layoutParams = layoutParams
        }
    }
}