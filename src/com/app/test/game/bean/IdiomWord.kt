package com.app.test.game.bean

import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * 每个成语题目的 字
 *
 */
@Parcelize
class IdiomWord(
        /**
         * 改字关联的第几条成语及成语内容
         */
        var proverbRelationList: ArrayList<SuperType> = ArrayList<SuperType>(),

        /**
         * 原始坐标位置 x
         */
        var rawX: Int = 0,

        /**
         * 原始坐标位置 y
         */
        var rawY: Int = 0,

        /**
         * 相对坐标位置 x
         */
        var relativeX: Int = 0,

        /**
         * 相对坐标位置 y
         */
        var relativeY: Int = 0,

        /**
         * 是否显示
         */
        var isShow: Boolean = false,

        /**
         * 是否已填充
         */
        var isFilled: Boolean = false) : SuperType() {


    override fun toString(): String {
        return "ProverbCharacter{" +
                "proverbRelationList=" + proverbRelationList +
                ", rawX=" + rawX +
                ", rawY=" + rawY +
                ", relativeX=" + relativeX +
                ", relativeY=" + relativeY +
                ", isShow=" + isShow +
                ", isFilled=" + isFilled +
                "} " + super.toString()
    }

}