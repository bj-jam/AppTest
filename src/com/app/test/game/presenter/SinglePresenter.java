package com.app.test.game.presenter;

import android.view.View;

import com.app.test.game.bean.Question;
import com.app.test.game.view.SingleView;
import com.app.test.util.Utils;

import java.util.ArrayList;

/**
 * @author lcx
 * Created at 2020.3.27
 * Describe:
 */
public class SinglePresenter {
    //用户选中的
    private ArrayList<String> selectData;
    private Question question;
    //是否回答过了
    private boolean isAnswer;
    //是否单选
    private boolean isSingleChoice;
    private SingleView singleView;


    public SinglePresenter(SingleView singleView) {
        this.singleView = singleView;
    }


    public void clearData() {
        if (selectData != null)
            selectData.clear();
        question = null;
        isAnswer = false;
        isSingleChoice = false;
    }

    /**
     * 点击了选项
     *
     * @param position 位置
     */
    public void clickItem(int position, View view) {
        if (Utils.isEmpty(question) || Utils.isEmpty(question.getAnswerList())
                || position < 0 || position >= question.getAnswerList().size())
            return;
        StringBuilder sb = new StringBuilder();
        sb.append(position + 1);
        if (isSingleChoice)
            handleRadio(sb.toString(), view);
        else
            handleCheckbox(sb.toString(), view);

    }

    /**
     * 单选逻辑处理
     *
     * @param position 集合的下标加1
     */
    private void handleRadio(String position, View view) {
        if (isAnswer) {
            return;
        }
        if (selectData == null)
            selectData = new ArrayList<>();
        selectData.clear();
        selectData.add(position);
        if (!Utils.isEmpty(getView()))
            getView().selectData();
        if (question.getAnswerIndex().contains(position)) {
            isAnswer = true;
            getView().answerResult(question, true, view);
        } else {
            isAnswer = false;
        }
    }

    private SingleView getView() {
        return singleView;
    }

    /**
     * 错选逻辑处理
     *
     * @param position 集合的下标加1
     */
    private void handleCheckbox(String position, View view) {
        if (isAnswer)
            return;
        if (selectData == null)
            selectData = new ArrayList<>();
        if (selectData.contains(position))
            selectData.remove(position);
        else
            selectData.add(position);
        if (!Utils.isEmpty(getView()))
            getView().selectData();
        if (selectData.size() != question.getAnswerIndex().size())
            return;
        for (String option : selectData) {
            if (!question.getAnswerIndex().contains(option)) {
                return;
            }
        }
        isAnswer = true;
        //多选正确
        getView().answerResult(question, true, view);
    }

    /**
     * 切换了题目
     *
     * @param question 题目信息
     */
    public void setNewProblem(Question question) {
        if (Utils.isEmpty(question)
                || Utils.isEmpty(question.getAnswerList())
                || Utils.isEmpty(question.getAnswerIndex()))
            return;
        this.question = question;
        isAnswer = false;
        if (selectData == null)
            selectData = new ArrayList<>();
        selectData.clear();
        //有多个答案
        //只有一个答案
        isSingleChoice = question.getAnswerIndex().size() <= 1;
        if (!Utils.isEmpty(getView()))
            getView().setProblemData(question, question.getAnswerIndex(), selectData);
    }

    public void onDestroy() {
        clearData();
    }
}
