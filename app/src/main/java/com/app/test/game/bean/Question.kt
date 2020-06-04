package com.app.test.game.bean

import com.app.test.game.source.AAnswerType
import com.app.test.game.source.AnswerType
import com.app.test.util.Utils
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * 具体每道题
 */
@Parcelize
class Question(@AAnswerType var questionType: Int = AnswerType.NONE,

               /**
                * 答题时间
                */
               var during: Long = 0,

               /**
                * 该字段为保留字段,为对照字段 请勿做逻辑
                */
               var originJson: String? = null,

               /**
                * 原始棋盘行数
                */
               var row: Int = 0,

               /**
                * 原始棋盘列数
                */
               var column: Int = 0,

               /**
                * 有效显示行数
                */
               var relativeRow: Int = 0,

               /**
                * 有效显示列数
                */
               var relativeColumn: Int = 0,

               /**
                * 成语备选词
                */
               var proverbDisturbWordList: ArrayList<SuperType> = ArrayList<SuperType>(),

               /**
                * 答案备选列表
                */
               var answerList: ArrayList<String> = ArrayList<String>(),

               /**
                * 正确答案角标列表
                */
               var answerIndex: ArrayList<String> = ArrayList<String>(),
               var clicks: Int = 0,
               var clicktips: Int = 0,
               var retrycount: Int = 0,
               var diomicosttime: String? = null,
               var costtime: Long = 0,
               var proverbList: ArrayList<Idiom> = ArrayList<Idiom>()) : SuperType() {


    override fun toString(): String {
        return "Question{" +
                "proverbList=" + proverbList +
                ", questionType=" + questionType +
                ", during=" + during +
                ", originJson='" + originJson + '\'' +
                ", row=" + row +
                ", column=" + column +
                ", relativeRow=" + relativeRow +
                ", relativeColumn=" + relativeColumn +
                ", proverbDisturbWordList=" + proverbDisturbWordList +
                ", answerList=" + answerList +
                ", answerIndex=" + answerIndex +
                "} " + super.toString()
    }

    companion object {

        fun isIllegalQuestionType(question: Question): Boolean {
            return (Utils.isEmpty(question)
                    || !(AnswerType.PROVER == question.questionType || AnswerType.NORMAL == question.questionType))
        }
    }
}