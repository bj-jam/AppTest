package com.app.test.game.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.app.test.R
import com.app.test.game.bean.Question
import com.app.test.game.presenter.AnswerPresenter
import com.app.test.game.source.AnswerType
import com.app.test.game.view.IdiomView
import com.app.test.game.view.OpenDoorView
import com.app.test.game.view.OpenDoorView.OpenDoorListen
import com.app.test.game.view.SingleView
import com.app.test.util.Utils

/**
 * @author lcx
 * Created at 2020.3.27
 * Describe:
 */
class AnswerActivity : Activity() {
    private val answerPresenter: AnswerPresenter by lazy { AnswerPresenter(this) }
    private lateinit var ivFinish: View
    private lateinit var singleView: SingleView
    private lateinit var idiomView: IdiomView
    private lateinit var openDoorView: OpenDoorView
    private lateinit var tvProgress: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer_question)
        initView()
        initData()
    }

    private fun initView() {
        ivFinish = findViewById(R.id.iv_finish)
        singleView = findViewById(R.id.sgv_single)
        idiomView = findViewById(R.id.sgv_proverb)
        openDoorView = findViewById(R.id.godv_layout)
        tvProgress = findViewById(R.id.tv_answer_progress)
        setViewListener()
    }

    private fun initData() {
        val intent = intent
        if (Utils.isEmpty(intent)) {
            finish()
            return
        }
        answerPresenter.checkpointList
    }


    private fun setViewListener() {
        ivFinish.setOnClickListener { finish() }
        idiomView.setAnswerListener(answerPresenter)
        singleView.setAnswerListener(answerPresenter)
    }

    fun dispatchQuestion(index: Int, question: Question) {
        if (Utils.isEmpty(openDoorView)) {
            _dispatchQuestion(index, question)
            return
        }
        openDoorView.startCloseAnim()
        openDoorView.setOpenDoorListen(object : OpenDoorListen() {
            override fun openAnimStart() {
                _dispatchQuestion(index, question)
            }
        })
    }

    fun _dispatchQuestion(index: Int, question: Question) {
        if (checkIllegal(index, question)) {
            return
        }
        val questionType = question.questionType
        singleView.visibility = View.GONE
        idiomView.visibility = View.GONE
        when (questionType) {
            AnswerType.NORMAL -> {
                singleView.visibility = View.VISIBLE
                singleView.onNextQuestion(index, question)
            }
            AnswerType.PROVER -> {
                idiomView.visibility = View.VISIBLE
                idiomView.onNextQuestion(index, question)
            }
        }
    }

    fun changeIndex(index: Int, amount: Int) {
        if (index + 1 > amount) {
            return
        }
        tvProgress.text = String.format("本轮答题:%1\$s/%2\$s题", (index + 1).toString(), amount.toString())
    }

    override fun onDestroy() {
        singleView.onDestroy()
        idiomView.onDestroy()
        super.onDestroy()
    }

    private fun checkIllegal(index: Int, question: Question): Boolean {
        return Utils.isEmpty(singleView) || Utils.isEmpty(idiomView) || index < 0 || Utils.isEmpty(question) || question.questionType == AnswerType.NONE
    }
}