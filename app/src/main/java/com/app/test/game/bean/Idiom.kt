package com.app.test.game.bean

import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * 每个成语
 */
@Parcelize
class Idiom(var proverbCharacterList: ArrayList<IdiomWord> = ArrayList<IdiomWord>(),

            /**
             * 分别表示各个字的出现与否 0代表需要用户填入  1代表正常的显示
             */
            var blankIndex: ArrayList<String> = ArrayList<String>(),

            /**
             * 成语第一个字的起始位置
             */
            var startIndex: Int = 0,

            /**
             * 0 代表横向  1代表竖向
             */
            var isHorizontal: Boolean = false) : SuperType() {


    override fun toString(): String {
        return "Proverb{" +
                "proverbCharacterList=" + proverbCharacterList +
                ", blankIndex=" + blankIndex +
                ", startIndex=" + startIndex +
                ", horizontal=" + isHorizontal +
                "} " + super.toString()
    }

}