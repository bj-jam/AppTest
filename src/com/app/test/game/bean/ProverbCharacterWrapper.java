package com.app.test.game.bean;

import android.os.Parcel;

public class ProverbCharacterWrapper extends SuperType {
    private ProverbCharacter proverbCharacter;
    /*一个成语是否全部填充完毕*/
    private boolean isAllFill;
    /*是否全部填充成功*/
    private boolean isAllRight;
    //该字所关联的成语所在集合的下标
    private int proverbIndex;

    public ProverbCharacterWrapper() {
        super();
    }

    public ProverbCharacterWrapper(Parcel sourceParcel) {
        super(sourceParcel);
        proverbCharacter = (ProverbCharacter) sourceParcel.readParcelable(ProverbCharacter.class.getClassLoader());

        isAllFill = (Boolean) sourceParcel.readValue(ClassLoader.getSystemClassLoader());
        isAllRight = (Boolean) sourceParcel.readValue(ClassLoader.getSystemClassLoader());
        proverbIndex = sourceParcel.readInt();
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
        super.writeToParcel(dest, flags);
        dest.writeParcelable(proverbCharacter, PARCELABLE_WRITE_RETURN_VALUE);

        dest.writeValue(isAllFill);
        dest.writeValue(isAllRight);
        dest.writeInt(proverbIndex);
    }

    public ProverbCharacter getProverbCharacter() {
        return proverbCharacter;
    }

    public void setProverbCharacter(ProverbCharacter proverbCharacter) {
        this.proverbCharacter = proverbCharacter;
    }

    public boolean isAllFill() {
        return isAllFill;
    }

    public void setAllFill(boolean allFill) {
        if (isAllFill()/*&&isAllRight()*/) {
            //如果某个成语填充完毕，那么就不允许其他成语改变状态
            return;
        }
        isAllFill = allFill;
    }

    public boolean isAllRight() {
        return isAllRight;
    }

    public void setAllRight(boolean allRight) {
        if (isAllRight) {
            //如果某个成语填充成功，那么就不允许其他成语改变状态
            return;
        }
        isAllRight = allRight;
    }

    /*撤下备选词必须将fill改成false*/
    public void setTakeDown() {
        isAllFill = false;
        isAllRight = false;
    }

    public int getProverbIndex() {
        return proverbIndex;
    }

    public void setProverbIndex(int proverbIndex) {
        this.proverbIndex = proverbIndex;
    }

    @Override
    public String toString() {
        return "ProverbCharacterWrapper{" +
                "proverbCharacter=" + proverbCharacter +
                "isAllFill=" + isAllFill +
                "isAllRight=" + isAllRight +
                '}';
    }
}
