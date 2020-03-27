package com.app.test.game.bean;

import android.os.Parcel;

public class IdiomWrapper extends SuperType {
    private IdiomWord proverbCharacter;
    /*一个成语是否全部填充完毕*/
    private boolean isAllFill;
    /*是否全部填充成功*/
    private boolean isAllRight;
    //该字所关联的成语所在集合的下标
    private int proverbIndex;

    public IdiomWrapper() {
        super();
    }

    public IdiomWrapper(Parcel sourceParcel) {
        super(sourceParcel);
        proverbCharacter = (IdiomWord) sourceParcel.readParcelable(IdiomWord.class.getClassLoader());

        isAllFill = (Boolean) sourceParcel.readValue(ClassLoader.getSystemClassLoader());
        isAllRight = (Boolean) sourceParcel.readValue(ClassLoader.getSystemClassLoader());
        proverbIndex = sourceParcel.readInt();
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
        super.writeToParcel(dest, flags);
        dest.writeParcelable(proverbCharacter, PARCELABLE_WRITE_RETURN_VALUE);

        dest.writeValue(isAllFill);
        dest.writeValue(isAllRight);
        dest.writeInt(proverbIndex);
    }

    public IdiomWord getProverbCharacter() {
        return proverbCharacter;
    }

    public void setProverbCharacter(IdiomWord idiomWord) {
        this.proverbCharacter = idiomWord;
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
