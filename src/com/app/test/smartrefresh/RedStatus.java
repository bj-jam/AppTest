package com.app.test.smartrefresh;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lcx
 */
public class RedStatus implements Parcelable {
    private int todayEnd;// : 1,  //今日已经结束 1是 0否  2接口失败了
    private long coolingTime;//':0 //冷却时间 单位 s

    public RedStatus() {
        super();
    }

    protected RedStatus(Parcel in) {
        todayEnd = in.readInt();
        coolingTime = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(todayEnd);
        dest.writeLong(coolingTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RedStatus> CREATOR = new Creator<RedStatus>() {
        @Override
        public RedStatus createFromParcel(Parcel in) {
            return new RedStatus(in);
        }

        @Override
        public RedStatus[] newArray(int size) {
            return new RedStatus[size];
        }
    };

    public boolean isEnd() {
        return coolingTime > 0;
    }

    public boolean todayIsEnd() {
        return todayEnd == 1;
    }

    public int getTodayEnd() {
        return todayEnd;
    }

    public void setTodayEnd(int todayEnd) {
        this.todayEnd = todayEnd;
    }

    public long getCoolingTime() {
        return coolingTime;
    }

    public void setCoolingTime(long coolingTime) {
        this.coolingTime = coolingTime;
    }
}
