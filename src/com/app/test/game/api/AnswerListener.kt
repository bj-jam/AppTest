package com.app.test.game.api

import android.view.View
import com.app.test.game.bean.Question

/**
 *
 */
interface AnswerListener {
    /**
     * 填字游戏正确填入的字数
     */
    fun onRightCount(count: Int)

    /**
     * 填字游戏 填写错误
     */
    fun onPutError()

    /**
     * 用户答完单题
     *
     * @param position
     * @param question
     * @param answerTime
     * @param isAnswerRight
     */
    fun onUserAnswer(position: Int, question: Question?, answerTime: String?, isAnswerRight: Boolean)

    /**
     * 正确答案所在view
     */
    fun onRightCharacterViews(vararg views: View?)
}