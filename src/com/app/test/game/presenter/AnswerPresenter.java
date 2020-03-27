package com.app.test.game.presenter;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.app.test.game.api.AnswerListener;
import com.app.test.game.bean.Checkpoint;
import com.app.test.game.bean.Question;
import com.app.test.game.source.AnswerType;
import com.app.test.game.ui.AnswerActivity;
import com.app.test.util.ContextUtils;
import com.app.test.util.DataString;
import com.app.test.util.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * @author lcx
 * Created at 2020.3.27
 * Describe:
 */
public class AnswerPresenter implements AnswerListener {
    /**
     * 当前题目位置
     */
    private int currentIndex = 0;
    /**
     * 题目集合
     */
    private ArrayList<Question> questionList;
    /**
     * 关卡
     */
    private Checkpoint checkpoint;
    private AnswerActivity activity;

    public AnswerPresenter(AnswerActivity activity) {
        this.activity = activity;
    }

    private void checkAnswer(final int index, final Question question) {
        if (Utils.isEmpty(checkpoint) || Utils.isEmpty(question) || ContextUtils.isDestroyed(getView())) {
            return;
        }
        int questionType = question.getQuestionType();
        if (questionType == AnswerType.NONE) {
            showToast(getView(), "题目类型错误");
            return;
        }
        onAnswerChecked(index);
    }

    private void showToast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();

    }


    private void onAnswerChecked(int index) {
        if (Utils.isEmpty(questionList) || ContextUtils.isDestroyed(getView())) {
            return;
        }
        currentIndex = index + 1;
        if (isAnswerEnd()) {
            getView().finish();
            return;
        }
        if (Utils.isIllegalPosition(questionList, currentIndex)) {
            return;
        }
        dispatchQuestion(currentIndex, questionList.get(currentIndex));
    }

    private AnswerActivity getView() {
        return activity;
    }

    /**
     * 获取题目关卡信息
     */
    public void getCheckpointList() {
        Checkpoint checkpoint = new Gson().fromJson(DataString.QUESTION_STRING, Checkpoint.class);
        if (Utils.isEmpty(checkpoint) || Utils.isEmpty(checkpoint.getQuestionList())) {
            return;
        }
        initAnswerData(checkpoint);
    }


    private void initAnswerData(Checkpoint checkpoint) {
        if (ContextUtils.isDestroyed(getView()) || Utils.isEmpty(checkpoint) || Utils.isEmpty(checkpoint.getQuestionList())) {
            return;
        }
        this.checkpoint = checkpoint;
        currentIndex = 0;
        questionList = checkpoint.getQuestionList();
        if (Utils.isIllegalPosition(questionList, currentIndex)) {
            return;
        }
        Question question = questionList.get(currentIndex);
        dispatchQuestion(currentIndex, question);
    }

    /**
     * 设置题目信息
     *
     * @param currentPosition
     * @param question
     */
    private void dispatchQuestion(int currentPosition, Question question) {
        if (Utils.isEmpty(getView())) {
            return;
        }
        getView().changeIndex(currentPosition, questionList.size());
        getView().dispatchQuestion(currentPosition, question);
    }


    @Override
    public void onUserAnswer(int position, Question question, String answerTime, boolean isAnswerRight) {
        checkAnswer(position, question);
    }


    @Override
    public void onRightCharacter(int count) {
    }


    @Override
    public void onErrorCharacter() {
    }


    @Override
    public void onRightCharacterViews(View... views) {
    }

    @Override
    public boolean isPbRewardBoxSite() {
        return false;
    }

    private boolean isAnswerEnd() {
        return questionList != null && currentIndex >= questionList.size();
    }

}
