package com.app.test.game.presenter

import android.content.Context
import android.view.View
import android.widget.Toast
import com.app.test.game.api.AnswerListener
import com.app.test.game.bean.Checkpoint
import com.app.test.game.bean.Question
import com.app.test.game.source.AnswerType
import com.app.test.game.ui.AnswerActivity
import com.app.test.util.ContextUtils
import com.app.test.util.DataString
import com.app.test.util.Utils
import com.google.gson.Gson
import java.util.*

/**
 * @author lcx
 * Created at 2020.3.27
 * Describe:
 */
class AnswerPresenter(private val answerActivity: AnswerActivity) : AnswerListener {
    /**
     * 当前题目位置
     */
    private var currentIndex = 0

    /**
     * 题目集合
     */
    private var questionList: ArrayList<Question?>? = null

    /**
     * 关卡
     */
    private var checkpoint: Checkpoint? = null

    private fun checkAnswer(index: Int, question: Question?) {
        if (Utils.isEmpty(checkpoint) || Utils.isEmpty(question) || ContextUtils.isDestroyed(answerActivity)) {
            return
        }
        val questionType = question!!.questionType
        if (questionType == AnswerType.NONE) {
            showToast(answerActivity, "题目类型错误")
            return
        }
        onAnswerChecked(index)
    }

    private fun showToast(context: Context, str: String) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show()
    }

    private fun onAnswerChecked(index: Int) {
        if (Utils.isEmpty(questionList) || ContextUtils.isDestroyed(answerActivity)) {
            return
        }
        currentIndex = index + 1
        if (isAnswerEnd) {
            answerActivity.finish()
            return
        }
        if (Utils.isIllegalPosition(questionList, currentIndex)) {
            return
        }
        dispatchQuestion(currentIndex, questionList!![currentIndex])
    }

    /**
     * 获取题目关卡信息
     */
    val checkpointList: Unit
        get() {
            val checkpoint = Gson().fromJson(DataString.QUESTION_STRING, Checkpoint::class.java)
            if (Utils.isEmpty(checkpoint) || Utils.isEmpty(checkpoint.questionList)) {
                return
            }
            initAnswerData(checkpoint)
        }

    private fun initAnswerData(checkpoint: Checkpoint) {
        if (ContextUtils.isDestroyed(answerActivity) || Utils.isEmpty(checkpoint) || Utils.isEmpty(checkpoint.questionList)) {
            return
        }
        this.checkpoint = checkpoint
        currentIndex = 0
        questionList = checkpoint.questionList
        if (Utils.isIllegalPosition(questionList, currentIndex)) {
            return
        }
        val question = questionList?.get(currentIndex)
        dispatchQuestion(currentIndex, question)
    }

    /**
     * 设置题目信息
     *
     * @param currentPosition
     * @param question
     */
    private fun dispatchQuestion(currentPosition: Int, question: Question?) {
        if (Utils.isEmpty(answerActivity)) {
            return
        }
        answerActivity.changeIndex(currentPosition, questionList!!.size)
        answerActivity.dispatchQuestion(currentPosition, question!!)
    }

    override fun onUserAnswer(position: Int, question: Question?, answerTime: String?, isAnswerRight: Boolean) {
        checkAnswer(position, question)
    }

    override fun onRightCount(count: Int) {}
    override fun onPutError() {}
    override fun onRightCharacterViews(vararg views: View?) {}
    private val isAnswerEnd: Boolean
        get() = questionList != null && currentIndex >= questionList!!.size

}