package com.app.test.game.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ProverbDisturbMapCharacter implements Parcelable {
    private ProverbCharacter proverbCharacter;
    private SuperType proverbDisturbWord;
    private int proverbDisturbIndex;
    private String disturb;

    public ProverbDisturbMapCharacter() {
        super();
    }

    public ProverbDisturbMapCharacter(Parcel sourceParcel) {
        proverbCharacter = (ProverbCharacter) sourceParcel.readParcelable(ProverbCharacter.class.getClassLoader());
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


    public ProverbCharacter getProverbCharacter() {
        return proverbCharacter;
    }

    public void setProverbCharacter(ProverbCharacter proverbCharacter) {
        this.proverbCharacter = proverbCharacter;
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
