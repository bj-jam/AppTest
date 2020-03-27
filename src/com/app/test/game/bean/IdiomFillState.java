package com.app.test.game.bean;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;

public class IdiomFillState implements Parcelable {
    private boolean allFill;
    private boolean allRight;
    private int rightCount;

    private int position;
    private int needFillCount;
    private Point point;

    public IdiomFillState() {
        super();
    }

    public IdiomFillState(Parcel sourceParcel) {
        allFill = (Boolean) sourceParcel.readValue(ClassLoader.getSystemClassLoader());
        allRight = (Boolean) sourceParcel.readValue(ClassLoader.getSystemClassLoader());
        rightCount = sourceParcel.readInt();
        position = sourceParcel.readInt();
        needFillCount = sourceParcel.readInt();
        point = (Point) sourceParcel.readParcelable(Point.class.getClassLoader());
    }

    public static final Creator<ProverbCharacterWrapper> CREATOR = new Creator<ProverbCharacterWrapper>() {
        @Override
        public ProverbCharacterWrapper[] newArray(int size) {
            return new ProverbCharacterWrapper[size];
        }

        @Override
        public ProverbCharacterWrapper createFromParcel(Parcel source) {
            return new ProverbCharacterWrapper(source);
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(allFill);
        dest.writeValue(allRight);
        dest.writeInt(rightCount);
        dest.writeInt(position);
        dest.writeInt(needFillCount);

        dest.writeParcelable(point, PARCELABLE_WRITE_RETURN_VALUE);
    }

    public boolean isAllFill() {
        return allFill;
    }

    public void setAllFill(boolean allFill) {
        this.allFill = allFill;
    }

    public boolean isAllRight() {
        return allRight;
    }

    public void setAllRight(boolean allRight) {
        this.allRight = allRight;
    }

    public int getRightCount() {
        return rightCount;
    }

    public void setRightCount(int rightCount) {
        this.rightCount = rightCount;
    }

    public Point getPoint() {
        return point==null?new Point(-1,-1):point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public int getNeedFillCount() {
        return needFillCount;
    }

    public void setNeedFillCount(int needFillCount) {
        this.needFillCount = needFillCount;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
