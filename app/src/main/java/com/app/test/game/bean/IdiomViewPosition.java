package com.app.test.game.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class IdiomViewPosition implements Parcelable {
    private boolean isAllRight = false;
    /*每个成语最后一个view所在下标*/
    private List<Integer> lastViewPosition = new ArrayList<>();
    /*记录每个成语每个view所在下标*/
    private List<List<Integer>> eachViewPosition = new ArrayList<>();

    public IdiomViewPosition() {
        super();
    }

    protected IdiomViewPosition(Parcel in) {
        lastViewPosition = in.readArrayList(IdiomViewPosition.class.getClassLoader());
        eachViewPosition = in.readArrayList(IdiomViewPosition.class.getClassLoader());
        isAllRight = (Boolean) in.readValue(ClassLoader.getSystemClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(lastViewPosition);
        dest.writeList(eachViewPosition);
        dest.writeValue(isAllRight);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<IdiomViewPosition> CREATOR = new Creator<IdiomViewPosition>() {
        @Override
        public IdiomViewPosition createFromParcel(Parcel in) {
            return new IdiomViewPosition(in);
        }

        @Override
        public IdiomViewPosition[] newArray(int size) {
            return new IdiomViewPosition[size];
        }
    };

    public synchronized List<Integer> getLastViewPosition() {
        if (lastViewPosition == null) {
            lastViewPosition = new ArrayList<>();
        }
        return lastViewPosition;
    }

    public void setLastViewPosition(List<Integer> lastViewPosition) {
        this.lastViewPosition = lastViewPosition;
    }

    public void addLastViewPosition(int lastViewPosition) {
        getLastViewPosition().add(lastViewPosition);
    }

    public synchronized List<List<Integer>> getEachViewPosition() {
        if (eachViewPosition == null) {
            eachViewPosition = new ArrayList<>();
        }
        return eachViewPosition;
    }

    public void setEachViewPosition(List<List<Integer>> eachViewPosition) {
        this.eachViewPosition = eachViewPosition;
    }

    public void addEachViewPosition(List<Integer> eachViewPosition) {
        getEachViewPosition().add(eachViewPosition);
    }

    public boolean isAllRight() {
        return isAllRight;
    }

    public void setAllRight(boolean allRight) {
        isAllRight = allRight;
    }
}
