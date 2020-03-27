package com.app.test.game.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.app.test.util.StringUtils;

public class SuperType implements Parcelable {
    private String title;
    private String shortTitle;
    private boolean selected;
    private int index;

    public SuperType() {
        super();
    }

    public SuperType(Parcel source) {
        title = source.readString();
        shortTitle = source.readString();
        selected = (Boolean) source.readValue(ClassLoader.getSystemClassLoader());
        index = source.readInt();
    }


    public static final Creator<SuperType> CREATOR = new Creator<SuperType>() {

        @Override
        public SuperType[] newArray(int size) {
            return new SuperType[size];
        }

        @Override
        public SuperType createFromParcel(Parcel source) {
            return new SuperType(source);
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(shortTitle);
        dest.writeValue(selected);
        dest.writeInt(index);
    }


    public boolean isSelected() {
        return selected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "SuperType{" +
                ", title='" + title + '\'' +
                ", selected=" + selected +
                '}';
    }

    protected boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }

    protected boolean trimToEmpty(String str) {
        return StringUtils.isEmpty(StringUtils.trimToEmpty(str));
    }
}
