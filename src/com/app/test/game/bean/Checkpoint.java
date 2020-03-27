package com.app.test.game.bean;

import android.os.Parcel;

import java.util.ArrayList;

/**
 * 关卡
 */

public class Checkpoint extends SuperType {
    /**
     * 每个关卡的题目
     */
    private ArrayList<Question> questionList = new ArrayList<>();

    public Checkpoint() {
        super();
    }

    public Checkpoint(Parcel sourceParcel) {
        super(sourceParcel);
        questionList = sourceParcel.readArrayList(Question.class.getClassLoader());
    }


    public static final Creator<Checkpoint> CREATOR = new Creator<Checkpoint>() {

        @Override
        public Checkpoint[] newArray(int size) {
            return new Checkpoint[size];
        }

        @Override
        public Checkpoint createFromParcel(Parcel source) {
            return new Checkpoint(source);
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeList(questionList);
    }

    public ArrayList<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(ArrayList<Question> questionList) {
        this.questionList = questionList;
    }


    @Override
    public String toString() {
        return "Barrier{" +
                "questionList=" + questionList +
                '}';
    }
}
