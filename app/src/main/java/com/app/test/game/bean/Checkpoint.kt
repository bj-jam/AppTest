package com.app.test.game.bean

import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * 关卡
 */
@Parcelize
class Checkpoint(var questionList: ArrayList<Question> = ArrayList<Question>()) : SuperType() {
    /**
     * 每个关卡的题目
     */


    override fun toString(): String {
        return "Barrier{" +
                "questionList=" + questionList +
                '}'
    }


}