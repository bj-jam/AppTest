package com.app.test.game.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 成语提示
 */
public class CharacterTips implements Parcelable {
    private String title;
    private String shortTitle;


    public CharacterTips() {
        super();
    }

    public CharacterTips(Parcel sourceParcel) {
        title = sourceParcel.readString();
        shortTitle = sourceParcel.readString();
    }

    public static final Creator<IdiomWrapper> CREATOR = new Creator<IdiomWrapper>() {
        @Override
        public IdiomWrapper[] newArray(int size) {
            return new IdiomWrapper[size];
        }

        @Override
        public IdiomWrapper createFromParcel(Parcel source) {
            return new IdiomWrapper(source);
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeValue(shortTitle);
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
}
