package com.app.test.game.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 干扰
 */
public class IdiomDisturbWord implements Parcelable {
    private IdiomWord proverbCharacter;
    private SuperType proverbDisturbWord;
    private int proverbDisturbIndex;
    private String disturb;

    public IdiomDisturbWord() {
        super();
    }

    public IdiomDisturbWord(Parcel sourceParcel) {
        proverbCharacter = (IdiomWord) sourceParcel.readParcelable(IdiomWord.class.getClassLoader());
        proverbDisturbWord = (SuperType) sourceParcel.readParcelable(SuperType.class.getClassLoader());
        proverbDisturbIndex = sourceParcel.readInt();
        disturb = sourceParcel.readString();
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
        dest.writeParcelable(proverbCharacter, PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeParcelable(proverbDisturbWord, PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeInt(proverbDisturbIndex);
        dest.writeString(disturb);
    }


    public IdiomWord getProverbCharacter() {
        return proverbCharacter;
    }

    public void setProverbCharacter(IdiomWord idiomWord) {
        this.proverbCharacter = idiomWord;
    }

    public SuperType getProverbDisturbWord() {
        return proverbDisturbWord;
    }

    public void setProverbDisturbWord(SuperType proverbDisturbWord) {
        this.proverbDisturbWord = proverbDisturbWord;
    }

    public String getDisturb() {
        return disturb;
    }

    public void setDisturb(String disturb) {
        this.disturb = disturb;
    }

    public int getProverbDisturbIndex() {
        return proverbDisturbIndex;
    }

    public void setProverbDisturbIndex(int proverbDisturbIndex) {
        this.proverbDisturbIndex = proverbDisturbIndex;
    }

}
