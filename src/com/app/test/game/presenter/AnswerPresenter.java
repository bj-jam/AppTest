package com.app.test.game.presenter;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.app.test.game.api.AnswerListener;
import com.app.test.game.bean.Checkpoint;
import com.app.test.game.bean.Question;
import com.app.test.util.ContextUtils;
import com.app.test.util.DataString;
import com.app.test.game.source.AnswerType;
import com.app.test.game.ui.AnswerActivity;
import com.app.test.util.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * @author lcx
 * Created at 2020.3.27
 * Describe:
 */
public class AnswerPresenter implements AnswerListener {

    private int currentPosition = 0;
    private ArrayList<Question> questionList;
    private Checkpoint checkpoint;
    private AnswerActivity activity;

    public AnswerPresenter(AnswerActivity activity) {
        this.activity = activity;
    }


    public void checkAnswer(final int index, final Question question) {
        if (Utils.isEmpty(checkpoint) || Utils.isEmpty(question) || ContextUtils.isDestroyed(getView())) {
            return;
        }
        int questionType = question.getQuestionType();
        if (questionType == AnswerType.NONE) {
            showToast(getView(), "未知类型");
            return;
        }
        onAnswerChecked(index);
    }

    private void showToast(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_LONG).show();

    }


    private void onAnswerChecked(int index) {
        if (Utils.isEmpty(questionList) || ContextUtils.isDestroyed(getView())) {
            return;
        }
        currentPosition = index + 1;
        if (isAnswerEnd()) {
            getView().finish();
            return;
        }
        if (Utils.isIllegalPosition(questionList, currentPosition)) {
            return;
        }
        dispatchQuestion(currentPosition, questionList.get(currentPosition));
        return;
    }

    private AnswerActivity getView() {
        return activity;
    }


    public void getBarrierList() {
        Checkpoint checkpoint = new Gson().fromJson(DataString.QUESTION_STRING, Checkpoint.class);
        if (Utils.isEmpty(checkpoint) || Utils.isEmpty(checkpoint.getQuestionList())) {
            return;
        }
        initGameData(checkpoint);
    }


    private void initGameData(Checkpoint checkpoint) {
        if (ContextUtils.isDestroyed(getView()) || Utils.isEmpty(checkpoint) || Utils.isEmpty(checkpoint.getQuestionList())) {
            return;
        }
        this.checkpoint = checkpoint;
        currentPosition = 0;
        questionList = checkpoint.getQuestionList();
        if (Utils.isIllegalPosition(questionList, currentPosition)) {
            return;
        }
        Question question = questionList.get(currentPosition);
        dispatchQuestion(currentPosition, question);
    }

    private void dispatchQuestion(int currentPosition, Question question) {
        if (Utils.isEmpty(getView())) {
            return;
        }
        getView().changeIndex(currentPosition, questionList.size());
        getView().dispatchQuestion(currentPosition, question);
        resetProverErrorCharacterCount();
    }


    @Override
    public void onUserAnswer(int position, Question question, String answerTime, boolean isAnswerRight) {
        checkAnswer(position, question);
    }


    @Override
    public void onRightCharacter(int count) {
    }

    private void resetProverErrorCharacterCount() {
    }

    @Override
    public void onErrorCharacter() {
    }


    @Override
    public void onRightCharacterViews(View... views) {
    }

    @Override
    public boolean isPbRewardBoxSite() {
        return true;
    }

    private boolean isAnswerEnd() {
        return questionList != null && currentPosition >= questionList.size();
    }

}
