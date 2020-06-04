package com.app.test.game.presenter

import android.view.View
import com.app.test.game.bean.Question
import com.app.test.game.view.SingleView
import com.app.test.util.Utils

/**
 * @author lcx
 * Created at 2020.3.27
 * Describe:
 */
class SinglePresenter(private val singleView: SingleView) {
    //用户选中的
    private val selectData: ArrayList<String> by lazy { ArrayList<String>() }
    private var question: Question? = null

    //是否回答过了
    private var isAnswer = false

    //是否单选
    private var isSingleChoice = false
    fun clearData() {
        selectData.clear()
        question = null
        isAnswer = false
        isSingleChoice = false
    }

    /**
     * 点击了选项
     *
     * @param position 位置
     */
    fun clickItem(position: Int, view: View) {
        if (Utils.isEmpty(question) || Utils.isEmpty(question?.answerList)
                || position < 0 || position >= question?.answerList?.size ?: 0) return
        val sb = StringBuilder()
        sb.append(position + 1)
        if (isSingleChoice) handleRadio(sb.toString(), view) else handleCheckbox(sb.toString(), view)
    }

    /**
     * 单选逻辑处理
     *
     * @param position 集合的下标加1
     */
    private fun handleRadio(position: String, view: View) {
        if (isAnswer) {
            return
        }
        selectData.clear()
        selectData.add(position)
        if (!Utils.isEmpty(singleView)) singleView.selectData()
        if (question!!.answerIndex.contains(position)) {
            isAnswer = true
            singleView.answerResult(question, true, view)
        } else {
            isAnswer = false
        }
    }

    /**
     * 错选逻辑处理
     *
     * @param position 集合的下标加1
     */
    private fun handleCheckbox(position: String, view: View) {
        if (isAnswer) return
        if (selectData.contains(position)) selectData.remove(position) else selectData.add(position)
        singleView.selectData()
        if (selectData.size != question?.answerIndex?.size) return
        for (option in selectData) {
            if (question?.answerIndex?.contains(option) != true) {
                return
            }
        }
        isAnswer = true
        //多选正确
        singleView.answerResult(question, true, view)
    }

    /**
     * 切换了题目
     *
     * @param question 题目信息
     */
    fun setNewProblem(question: Question?) {
        if (Utils.isEmpty(question)
                || Utils.isEmpty(question?.answerList)
                || Utils.isEmpty(question?.answerIndex)) return
        this.question = question
        isAnswer = false
        selectData.clear()
        //有多个答案
        //只有一个答案
        isSingleChoice = question?.answerIndex?.size ?: 0 <= 1
        singleView.setProblemData(question, question?.answerIndex, selectData)
    }

    fun onDestroy() {
        clearData()
    }

}
