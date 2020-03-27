package com.app.test.game.bean;

import android.os.Parcel;

import java.util.ArrayList;

/**
 * 每个成语题目的 字

 */

public class IdiomWord extends SuperType {
    /**
     * 改字关联的第几条成语及成语内容
     */
    private ArrayList<SuperType> proverbRelationList = new ArrayList<>();
    /**
     * 原始坐标位置 x
     */
    private int rawX;
    /**
     * 原始坐标位置 y
     */
    private int rawY;
    /**
     * 相对坐标位置 x
     */
    private int relativeX;
    /**
     * 相对坐标位置 y
     */
    private int relativeY;
    /**
     * 是否显示
     */
    private boolean isShow;
    /**
     * 是否已填充
     */
    private boolean isFilled;

    public IdiomWord() {
        super();
    }

    public IdiomWord(Parcel sourceParcel) {
        super(sourceParcel);
        rawX = sourceParcel.readInt();
        rawY = sourceParcel.readInt();
        relativeX = sourceParcel.readInt();
        relativeY = sourceParcel.readInt();

        isShow = (Boolean) sourceParcel.readValue(ClassLoader.getSystemClassLoader());
        isFilled = (Boolean) sourceParcel.readValue(ClassLoader.getSystemClassLoader());

        proverbRelationList = sourceParcel.readArrayList(SuperType.class.getClassLoader());
    }


    public static final Creator<IdiomWord> CREATOR = new Creator<IdiomWord>() {

        @Override
        public IdiomWord[] newArray(int size) {
            return new IdiomWord[size];
        }

        @Override
        public IdiomWord createFromParcel(Parcel source) {
            return new IdiomWord(source);
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(rawX);
        dest.writeInt(rawY);
        dest.writeInt(relativeX);
        dest.writeInt(relativeY);

        dest.writeValue(isShow);
        dest.writeValue(isFilled);

        dest.writeList(proverbRelationList);
    }

    public ArrayList<SuperType> getProverbRelationList() {
        return proverbRelationList;
    }

    public void setProverbRelationList(ArrayList<SuperType> proverbRelationList) {
        this.proverbRelationList = proverbRelationList;
    }

    public int getRawX() {
        return rawX;
    }

    public void setRawX(int rawX) {
        this.rawX = rawX;
    }

    public int getRawY() {
        return rawY;
    }

    public void setRawY(int rawY) {
        this.rawY = rawY;
    }

    public int getRelativeX() {
        return relativeX;
    }

    public void setRelativeX(int relativeX) {
        this.relativeX = relativeX;
    }

    public int getRelativeY() {
        return relativeY;
    }

    public void setRelativeY(int relativeY) {
        this.relativeY = relativeY;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public void setFilled(boolean filled) {
        isFilled = filled;
    }


    @Override
    public String toString() {
        return "ProverbCharacter{" +
                "proverbRelationList=" + proverbRelationList +
                ", rawX=" + rawX +
                ", rawY=" + rawY +
                ", relativeX=" + relativeX +
                ", relativeY=" + relativeY +
                ", isShow=" + isShow +
                ", isFilled=" + isFilled +
                "} " + super.toString();
    }
}
