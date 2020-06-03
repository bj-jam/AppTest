package com.app.test.game.helper

import android.content.Context
import android.graphics.Point
import android.util.SparseBooleanArray
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import com.app.test.game.api.CheckIdiomListener
import com.app.test.game.bean.*
import com.app.test.game.source.AnswerType
import com.app.test.util.DensityUtil.dp2px
import com.app.test.util.DensityUtil.getScreenWidth
import com.app.test.util.Utils
import java.util.*

class IdiomHelper private constructor() {
    /*获取需要填空的下一个成语(空格从上到下的原则)取最上面空格所在成语的所在下标*/
    fun getNextProverbIndex(list: ArrayList<Idiom>?, allRightProverbIndex: SparseBooleanArray?, listener: CheckIdiomListener): IdiomFillState? {
        var allRightIndex = allRightProverbIndex
        if (Utils.isEmpty(list) || Utils.isEmpty(listener)) {
            return null
        }
        if (Utils.isEmpty(allRightIndex)) {
            allRightIndex = SparseBooleanArray()
        }
        /*记录需要选择的成语所在下标，以及选择框所在坐标*/
        var point = Point(-1, -1)
        var nextPosition = -1
        var tempX = 1000
        var tempY = 1000
        val state = IdiomFillState()
        //需要寻找空格最少的一个成语 ，将红色指示框定位在该成语下面
        list?.also {
            for (i in it.indices) {
                //如果某个成语全部填写正确，则判断下一个
                if (allRightIndex?.get(i) == true) {
                    continue
                }
                val idiom = it[i]
                if (Utils.isEmpty(idiom) || Utils.isEmpty(idiom.proverbCharacterList)) {
                    continue
                }
                for (j in idiom.proverbCharacterList.indices) {
                    val character = idiom.proverbCharacterList[j]
                    if (Utils.isEmpty(character)) {
                        continue
                    }
                    val relativeX = character.relativeX
                    val relativeY = character.relativeY
                    val needFill = listener.currentCharacterNeedFill(relativeX, relativeY)
                    if (needFill) {
                        /*记录进入答题第一个选择的成语所在下标，以及选择框所在坐标,所以需要比较哪个最小*/
                        if (tempX > relativeX || tempX == relativeX && tempY >= relativeY) {
                            tempX = relativeX
                            tempY = relativeY
                            point = Point(relativeX, relativeY)
                            nextPosition = i
                            state.point = point
                            state.position = nextPosition
                        }
                    }
                }
            }
        }

        return if (nextPosition == -1) {
            null
        } else state
    }

    fun getNextCharacterXY(idiom: Idiom, listener: CheckIdiomListener): Point {
        val point = Point(-1, -1)
        if (Utils.isEmpty(idiom)) {
            return point
        }
        for (j in idiom.proverbCharacterList.indices) {
            val character = idiom.proverbCharacterList[j]
            if (Utils.isEmpty(character)) {
                continue
            }
            val relativeX = character.relativeX
            val relativeY = character.relativeY
            val needFill = listener.currentCharacterNeedFill(relativeX, relativeY)
            if (needFill) {
                point[relativeX] = relativeY
                return point
            }
        }
        return point
    }

    fun getItemWidth(context: Context?, column: Int): Int {
        val ignore = 15 * 2 + 5 * 2 + column
        val screenWidth = getScreenWidth(context)
        var itemWidth = (screenWidth - ignore) / column
        itemWidth = Math.min(itemWidth, dp2px(40))
        if (itemWidth <= 0) {
            itemWidth = dp2px(40)
        }
        return itemWidth
    }

    /*生成测试数据*/
    fun produceTestData(): Array<Array<IdiomWord?>>? {
        val question = Question()
        question.questionType = AnswerType.PROVER
        question.relativeRow = 4
        question.relativeColumn = 5
        val idiomList = ArrayList<Idiom>()
        var idiom = Idiom()
        idiom.title = "聚沙成塔"
        idiom.isHorizontal = true
        idiom.startIndex = 39
        idiomList.add(idiom)
        var title = idiom.title
        var strings = arrayOf("聚", "沙", "成", "塔")
        var show = arrayOf(true, true, false, true)
        var y = arrayOf(0, 1, 2, 3)
        var x = arrayOf(1, 1, 1, 1)
        var charList = getCharList(
                title,
                strings,
                show,
                x,
                y
        )
        idiom.proverbCharacterList = charList
        idiom = Idiom()
        idiom.title = "飞沙走石"
        idiom.isHorizontal = false
        idiom.startIndex = 31
        idiomList.add(idiom)
        title = idiom.title
        strings = arrayOf("飞", "沙", "走", "石")
        show = arrayOf(true, true, true, false)
        y = arrayOf(1, 1, 1, 1)
        x = arrayOf(0, 1, 2, 3)
        charList = getCharList(
                title,
                strings,
                show,
                x,
                y
        )
        idiom.proverbCharacterList = charList
        idiom = Idiom()
        idiom.title = "石破天惊"
        idiom.isHorizontal = true
        idiom.startIndex = 58
        idiomList.add(idiom)
        title = idiom.title
        strings = arrayOf("石", "破", "天", "惊")
        show = arrayOf(false, true, false, true)
        y = arrayOf(1, 2, 3, 4)
        x = arrayOf(3, 3, 3, 3)
        charList = getCharList(
                title,
                strings,
                show,
                x,
                y
        )
        idiom.proverbCharacterList = charList
        question.proverbList = idiomList
        if (question.proverbList == null) {
            return null
        }
        val strArray = Array(question.relativeRow) { arrayOfNulls<IdiomWord>(question.relativeColumn) }
        for (i in question.proverbList.indices) {
            val idiom1 = question.proverbList[i]
            for (j in idiom1.proverbCharacterList.indices) {
                val proverbList1 = idiom1.proverbCharacterList[j]
                val relativeX = proverbList1.relativeX
                val relativeY = proverbList1.relativeY
                strArray[relativeX][relativeY] = proverbList1
            }
        }
        return strArray
    }

    fun getCharList(pro: String?, strings: Array<String>?, show: Array<Boolean>?, x: Array<Int>, y: Array<Int>): ArrayList<IdiomWord?> {
        val charList = ArrayList<IdiomWord?>()
        if (strings == null || show == null) {
            return charList
        }
        val length = strings.size
        for (i in 0 until length) {
            val character = IdiomWord()
            character.title = strings[i]
            character.isShow = show[i]
            //            character.setTitle(pro);
            character.relativeX = x[i]
            character.relativeY = y[i]
            //            character.setRawX(rawX[i]);
//            character.setRawY(rawY[i]);
            val proverbRelationList = ArrayList<SuperType>()
            if ("沙" == strings[i]) {
                proverbRelationList.add(getType("聚沙成塔"))
                proverbRelationList.add(getType("飞沙走石"))
            } else if ("石" == strings[i]) {
                proverbRelationList.add(getType("飞沙走石"))
                proverbRelationList.add(getType("石破天惊"))
            } else {
                proverbRelationList.add(getType(pro))
            }
            character.proverbRelationList = proverbRelationList
            character.isShow = show[i]
            charList.add(character)
        }
        return charList
    }

    fun getType(str: String?): SuperType {
        val type = SuperType()
        type.title = str
        return type
    }

    fun logData(idiomWords: Array<Array<IdiomWord?>>?) {
        if (idiomWords == null) {
            return
        }
        val length = idiomWords.size
        val sb = StringBuilder()
        for (i in 0 until length) {
            sb.append("stem.out: \n")
            val size: Int = idiomWords[i].size
            for (j in 0 until size) {
                if (idiomWords[i][j] == null) {
                    sb.append("口")
                } else {
                    sb.append(idiomWords[i][j]!!.title + "")
                }
            }
        }
        println(sb.toString())
    }

    fun playRightAnim(viewList: List<View?>) {
        if (Utils.isEmpty(viewList)) {
            return
        }
        for (i in viewList.indices) {
            val view = viewList[i]
            playRightAnim(view)
        }
    }

    fun playRightAnim(view: View?) {
        if (Utils.isEmpty(view)) {
            return
        }
        if (view?.visibility != View.VISIBLE) {
            return
        }
        val animation: Animation = ScaleAnimation(1f, 1.2f, 1f, 1.2f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f)
        animation.repeatMode = ScaleAnimation.REVERSE
        animation.repeatCount = 1
        animation.duration = 200
        animation.interpolator = LinearInterpolator()
        view.startAnimation(animation)
    }

    fun playErrorAnim(viewList: List<View?>) {
        if (Utils.isEmpty(viewList)) {
            return
        }
        for (i in viewList.indices) {
            val view = viewList[i]
            playErrorAnim(view)
        }
    }

    fun playErrorAnim(view: View?) {
        if (Utils.isEmpty(view)) {
            return
        }
        val animation: Animation = TranslateAnimation(ScaleAnimation.RELATIVE_TO_SELF, 0f, ScaleAnimation.RELATIVE_TO_SELF, 0.11f, ScaleAnimation.RELATIVE_TO_SELF, 0f, ScaleAnimation.RELATIVE_TO_SELF, 0f)
        animation.repeatMode = ScaleAnimation.REVERSE
        animation.repeatCount = 1
        animation.duration = 130
        animation.interpolator = LinearInterpolator()
        view!!.startAnimation(animation)
    }

    companion object {
        private var singleObj: IdiomHelper? = null

        @JvmStatic
        fun get(): IdiomHelper? {
            if (singleObj == null) {
                synchronized(IdiomHelper::class.java) {
                    if (singleObj == null) {
                        singleObj = IdiomHelper()
                    }
                }
            }
            return singleObj
        }
    }
}