package com.app.test.game.bean;

import android.os.Parcel;

import java.util.ArrayList;

/**
 * 每个成语
 */

public class Idiom extends SuperType {
    private ArrayList<IdiomWord> proverbCharacterList = new ArrayList<>();
    /**
     * 分别表示各个字的出现与否 0代表需要用户填入  1代表正常的显示
     */
    private ArrayList<String> blankIndex = new ArrayList<>();
    /**
     * 成语第一个字的起始位置
     */
    private int startIndex;
    /**
     * 0 代表横向  1代表竖向
     */
    private boolean horizontal;

    public Idiom() {
        super();
    }

    public Idiom(Parcel sourceParcel) {
        super(sourceParcel);
        proverbCharacterList = sourceParcel.readArrayList(IdiomWord.class.getClassLoader());
        blankIndex = sourceParcel.readArrayList(String.class.getClassLoader());
        startIndex = sourceParcel.readInt();
        horizontal = (Boolean) sourceParcel.readValue(ClassLoader.getSystemClassLoader());
    }


    public static final Creator<Idiom> CREATOR = new Creator<Idiom>() {

        @Override
        public Idiom[] newArray(int size) {
            return new Idiom[size];
        }

        @Override
        public Idiom createFromParcel(Parcel source) {
            return new Idiom(source);
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeList(proverbCharacterList);
        dest.writeList(blankIndex);
        dest.writeInt(startIndex);
        dest.writeValue(horizontal);
    }

    public ArrayList<IdiomWord> getProverbCharacterList() {
        return proverbCharacterList;
    }

    public void setProverbCharacterList(ArrayList<IdiomWord> idiomWordList) {
        this.proverbCharacterList = idiomWordList;
    }

    public ArrayList<String> getBlankIndex() {
        return blankIndex;
    }

    public void setBlankIndex(ArrayList<String> blankIndex) {
        this.blankIndex = blankIndex;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    @Override
    public String toString() {
        return "Proverb{" +
                "proverbCharacterList=" + proverbCharacterList +
                ", blankIndex=" + blankIndex +
                ", startIndex=" + startIndex +
                ", horizontal=" + horizontal +
                "} " + super.toString();
    }
}
