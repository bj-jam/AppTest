package com.app.test.game.bean

import kotlinx.android.parcel.Parcelize

@Parcelize
class IdiomWrapper(var proverbCharacter: IdiomWord? = null,

        /*一个成语是否全部填充完毕*/
                   private var isAllFill: Boolean = false,

        /*是否全部填充成功*/
                   private var isAllRight: Boolean = false,

        //该字所关联的成语所在集合的下标
                   var proverbIndex: Int = 0) : SuperType() {


    fun isAllFill(): Boolean {
        return isAllFill
    }

    fun setAllFill(allFill: Boolean) {
        if (isAllFill() /*&&isAllRight()*/) {
            //如果某个成语填充完毕，那么就不允许其他成语改变状态
            return
        }
        isAllFill = allFill
    }

    fun isAllRight(): Boolean {
        return isAllRight
    }

    fun setAllRight(allRight: Boolean) {
        if (isAllRight) {
            //如果某个成语填充成功，那么就不允许其他成语改变状态
            return
        }
        isAllRight = allRight
    }

    /*撤下备选词必须将fill改成false*/
    fun setTakeDown() {
        isAllFill = false
        isAllRight = false
    }

    override fun toString(): String {
        return "ProverbCharacterWrapper{" +
                "proverbCharacter=" + proverbCharacter +
                "isAllFill=" + isAllFill +
                "isAllRight=" + isAllRight +
                '}'
    }

}