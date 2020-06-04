package com.app.test.game.view

import android.content.Context
import android.os.Build
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.method.ScrollingMovementMethod
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.LinearLayout
import android.widget.TextView
import com.app.test.R
import com.app.test.game.adapter.SingleAdapter
import com.app.test.game.api.AnswerControl
import com.app.test.game.api.AnswerListener
import com.app.test.game.bean.Question
import com.app.test.game.presenter.SinglePresenter
import com.app.test.util.DensityUtil.dp2px
import com.app.test.util.Utils
import com.chad.library.adapter.base.BaseQuickAdapter
import java.util.*

/**
 * @author lcx
 * Created at 2020.3.27
 * Describe:
 */
open class SingleView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr), AnswerControl<AnswerListener> {
    private val presenter: SinglePresenter by lazy { SinglePresenter(this) }
    private var tvSingleContent: TextView
    private var rvSingleOption: RecyclerView
    private val optionAdapter: SingleAdapter by lazy { SingleAdapter() }
    private var currentPosition = 0
    private var answerListener: AnswerListener? = null
    private var isAlreadyGet = false

    //单选标题的底部距离
    private val marginBottom = dp2px(5)
    private var startTime: Long = 0

    //提示的次数
    private var tipsCount = 0

    //单机的次数
    private var clicks = 0

    //选项的时间选择的时间
    private var optionTime: Long = 0
    var sb = StringBuilder()


    init {
        View.inflate(context, R.layout.view_single_answer, this)
        tvSingleContent = findViewById(R.id.tv_single_content)
        rvSingleOption = findViewById(R.id.rv_single_option)
        rvSingleOption.layoutManager = LinearLayoutManager(context)
        tvSingleContent.movementMethod = ScrollingMovementMethod.getInstance()
        rvSingleOption.adapter = optionAdapter
        setViewListener()
    }

    private fun setViewListener() {
        optionAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, view, position ->
            clicks++
            if (Utils.isEmpty(sb)) sb = StringBuilder()
            sb.append(clicks)
            sb.append("_")
            sb.append(position + 1)
            sb.append("_")
            sb.append(System.currentTimeMillis() - optionTime)
            sb.append(",")
            optionTime = System.currentTimeMillis()
            presenter.clickItem(position, view)
        }
    }

    /**
     * 设置答题完之后的监听
     *
     * @param answerListener
     */
    override fun setAnswerListener(answerListener: AnswerListener) {
        this.answerListener = answerListener
    }

    /**
     * 设置单选标题
     *
     * @param title
     */
    private fun setProblemTitle(title: String?) {
        if (Utils.trimToEmptyNull(title)) return
        tvSingleContent.text = title
    }

    /**
     * 有选择之后更新UI
     */
    fun selectData() {
        optionAdapter.dataChange()
    }

    fun answerResult(question: Question?, isAnswerRight: Boolean, vararg view: View?) {
        if (answerListener != null) {
            question?.costtime = System.currentTimeMillis() - startTime
            question?.clicktips = tipsCount
            question?.clicks = clicks
            if (!Utils.isEmpty(sb)) question?.diomicosttime = sb.toString()
            answerListener?.onUserAnswer(currentPosition, question, "100", isAnswerRight)
            answerListener?.onRightCharacterViews(*view)
        }
    }

    /**
     * 设置选项
     *
     * @param question
     * @param answerData
     * @param selectData
     */
    fun setProblemData(question: Question?, answerData: ArrayList<String>?, selectData: ArrayList<String>?) {
        if (Utils.isEmpty(question)) return
        optionAdapter.setProblemData(question?.answerList, selectData, answerData)
    }

    override fun onNextQuestion(position: Int, question: Question) {
        if (Utils.isEmpty(question) || Utils.isEmpty(presenter)) return
        clearInfo()
        currentPosition = position
        fitHeight()
        setProblemTitle(question.title)
        presenter.setNewProblem(question)
    }

    /**
     * 获取view的实际高度高度
     */
    private fun fitHeight() {
        if (isAlreadyGet) return
        viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                } else {
                    viewTreeObserver.removeGlobalOnLayoutListener(this)
                }
                //标题高度
                val contentHeight = tvSingleContent.height
                val selfHeight: Int = this@SingleView.height
                if (contentHeight > 0 && selfHeight > 0) {
                    isAlreadyGet = true
                    optionAdapter.setOptionHeight(selfHeight - contentHeight - marginBottom)
                }
            }
        })
    }

    private fun clearInfo() {
        startTime = System.currentTimeMillis()
        optionTime = System.currentTimeMillis()
        if (Utils.isEmpty(sb)) sb = StringBuilder()
        sb.setLength(0)
        currentPosition = -1
        tipsCount = 0
        clicks = 0
        tvSingleContent.text = ""
        optionAdapter.setProblemData(null, null, null)
        presenter.clearData()
    }

    fun onDestroy() {
        presenter.onDestroy()
    }
}