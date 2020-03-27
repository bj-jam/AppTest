package com.app.test.game.bean;

import android.os.Parcel;

import com.app.test.game.source.AAnswerType;
import com.app.test.game.source.AnswerType;
import com.app.test.util.Utils;

import java.util.ArrayList;

/**
 * 具体每道题
 */

public class Question extends SuperType {
    private ArrayList<Proverb> proverbList = new ArrayList<>();

    @AAnswerType
    private int questionType = AnswerType.NONE;
    /**
     * 答题时间
     */
    private long during;
    /**
     * 该字段为保留字段,为对照字段 请勿做逻辑
     */
    private String originJson;

    /**
     * 原始棋盘行数
     */
    private int row;
    /**
     * 原始棋盘列数
     */
    private int column;

    /**
     * 有效显示行数
     */
    private int relativeRow;
    /**
     * 有效显示列数
     */
    private int relativeColumn;
    /**
     * 成语备选词
     */
    private ArrayList<SuperType> proverbDisturbWordList = new ArrayList<>();

    /**
     * 答案备选列表
     */
    private ArrayList<String> answerList = new ArrayList<>();

    /**
     * 正确答案角标列表
     */
    private ArrayList<String> answerIndex = new ArrayList<>();

    private int clicks;
    private int clicktips;
    private int retrycount;
    private String diomicosttime;
    private long costtime;


    public Question() {
        super();
    }

    public Question(Parcel sourceParcel) {
        super(sourceParcel);
        proverbList = sourceParcel.readArrayList(Proverb.class.getClassLoader());
        questionType = sourceParcel.readInt();
        during = sourceParcel.readLong();
        originJson = sourceParcel.readString();

        row = sourceParcel.readInt();
        column = sourceParcel.readInt();
        relativeRow = sourceParcel.readInt();
        relativeColumn = sourceParcel.readInt();

        proverbDisturbWordList = sourceParcel.readArrayList(SuperType.class.getClassLoader());
        answerList = sourceParcel.readArrayList(String.class.getClassLoader());
        answerIndex = sourceParcel.readArrayList(String.class.getClassLoader());

        clicks = sourceParcel.readInt();
        clicktips = sourceParcel.readInt();
        retrycount = sourceParcel.readInt();
        diomicosttime = sourceParcel.readString();
        costtime = sourceParcel.readLong();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }

        @Override
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeList(proverbList);
        dest.writeInt(questionType);
        dest.writeLong(during);
        dest.writeString(originJson);

        dest.writeInt(row);
        dest.writeInt(column);
        dest.writeInt(relativeRow);
        dest.writeInt(relativeColumn);

        dest.writeList(proverbDisturbWordList);
        dest.writeList(answerList);
        dest.writeList(answerIndex);

        dest.writeInt(clicks);
        dest.writeInt(clicktips);
        dest.writeInt(retrycount);
        dest.writeString(diomicosttime);
        dest.writeLong(costtime);
    }

    public static boolean isIllegalQuestionType(Question question) {
        return Utils.isEmpty(question)
                || !(AnswerType.PROVER == question.getQuestionType() || AnswerType.NORMAL == question.getQuestionType());
    }

    public ArrayList<Proverb> getProverbList() {
        return proverbList;
    }

    public void setProverbList(ArrayList<Proverb> proverbList) {
        this.proverbList = proverbList;
    }

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    public long getDuring() {
        return during;
    }

    public void setDuring(long during) {
        this.during = during;
    }

    public String getOriginJson() {
        return originJson;
    }

    public void setOriginJson(String originJson) {
        this.originJson = originJson;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRelativeRow() {
        return relativeRow;
    }

    public void setRelativeRow(int relativeRow) {
        this.relativeRow = relativeRow;
    }

    public int getRelativeColumn() {
        return relativeColumn;
    }

    public void setRelativeColumn(int relativeColumn) {
        this.relativeColumn = relativeColumn;
    }

    public ArrayList<SuperType> getProverbDisturbWordList() {
        return proverbDisturbWordList;
    }

    public void setProverbDisturbWordList(ArrayList<SuperType> proverbDisturbWordList) {
        this.proverbDisturbWordList = proverbDisturbWordList;
    }

    public ArrayList<String> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(ArrayList<String> answerList) {
        this.answerList = answerList;
    }

    public ArrayList<String> getAnswerIndex() {
        return answerIndex;
    }

    public void setAnswerIndex(ArrayList<String> answerIndex) {
        this.answerIndex = answerIndex;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public int getClicktips() {
        return clicktips;
    }

    public void setClicktips(int clicktips) {
        this.clicktips = clicktips;
    }

    public int getRetrycount() {
        return retrycount;
    }

    public void setRetrycount(int retrycount) {
        this.retrycount = retrycount;
    }

    public String getDiomicosttime() {
        return diomicosttime;
    }

    public void setDiomicosttime(String diomicosttime) {
        this.diomicosttime = diomicosttime;
    }

    public long getCosttime() {
        return costtime;
    }

    public void setCosttime(long costtime) {
        this.costtime = costtime;
    }

    @Override
    public String toString() {
        return "Question{" +
                "proverbList=" + proverbList +
                ", questionType=" + questionType +
                ", during=" + during +
                ", originJson='" + originJson + '\'' +
                ", row=" + row +
                ", column=" + column +
                ", relativeRow=" + relativeRow +
                ", relativeColumn=" + relativeColumn +
                ", proverbDisturbWordList=" + proverbDisturbWordList +
                ", answerList=" + answerList +
                ", answerIndex=" + answerIndex +
                "} " + super.toString();
    }

}
