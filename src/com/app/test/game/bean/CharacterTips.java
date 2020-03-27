package com.app.test.game.bean;

import android.os.Parcel;
import android.os.Parcelable;

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
