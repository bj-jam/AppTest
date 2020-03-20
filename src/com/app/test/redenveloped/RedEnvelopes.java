package com.app.test.redenveloped;

import android.os.Parcel;
import android.os.Parcelable;

import com.app.test.util.Utils;

public class RedEnvelopes implements Parcelable {
    private int index;
    private String redPacketId;
    private int currentX;
    private int currentY;
    private int rotate;
    private int money;
    private String redPacketPath;
    private int resId = -1;

    public boolean hasRedEnvelopes() {
        return resId > 0 || !Utils.trimToEmpty(redPacketPath);
    }

    public RedEnvelopes() {
        super();
    }

    protected RedEnvelopes(Parcel in) {
        index = in.readInt();
        redPacketId = in.readString();
        currentX = in.readInt();
        currentY = in.readInt();
        rotate = in.readInt();
        money = in.readInt();
        redPacketPath = in.readString();
        resId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(index);
        dest.writeString(redPacketId);
        dest.writeInt(currentX);
        dest.writeInt(currentY);
        dest.writeInt(rotate);
        dest.writeInt(money);
        dest.writeString(redPacketPath);
        dest.writeInt(resId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RedEnvelopes> CREATOR = new Creator<RedEnvelopes>() {
        @Override
        public RedEnvelopes createFromParcel(Parcel in) {
            return new RedEnvelopes(in);
        }

        @Override
        public RedEnvelopes[] newArray(int size) {
            return new RedEnvelopes[size];
        }
    };

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getRedPacketId() {
        return redPacketId;
    }

    public void setRedPacketId(String redPacketId) {
        this.redPacketId = redPacketId;
    }

    public int getCurrentX() {
        return currentX;
    }

    public void setCurrentX(int currentX) {
        this.currentX = currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    public void setCurrentY(int currentY) {
        this.currentY = currentY;
    }

    public int getRotate() {
        return rotate;
    }

    public void setRotate(int rotate) {
        this.rotate = rotate;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getRedPacketPath() {
        return redPacketPath;
    }

    public void setRedPacketPath(String redPacketPath) {
        this.redPacketPath = redPacketPath;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

}
