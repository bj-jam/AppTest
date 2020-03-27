package com.app.test.game.api;

import android.view.View;

import com.app.test.game.bean.Question;

/**
 *
 */
public interface AnswerListener {

    /**
     * 填字游戏正确填入的字数
     */
    void onRightCharacter(int count);

    /**
     * 填字游戏 填写错误
     */

    void onErrorCharacter();

    /**
     * 用户答完单题
     *
     * @param position
     * @param question
     * @param answerTime
     * @param isAnswerRight
     */
    void onUserAnswer(int position, Question question, String answerTime, boolean isAnswerRight);


    /**
     * 正确答案所在view
     */
    void onRightCharacterViews(View... views);

    /**
     * 是否宝箱位置的题目
     */
    boolean isPbRewardBoxSite();
}
