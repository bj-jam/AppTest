package com.app.test.game.view

import android.content.Context
import android.graphics.Point
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.app.test.R
import com.app.test.game.adapter.IdiomBoardAdapter
import com.app.test.game.adapter.IdiomFreeAdapter
import com.app.test.game.api.AnswerControl
import com.app.test.game.api.AnswerListener
import com.app.test.game.api.ItemTypeListener
import com.app.test.game.api.NoFastClickListener
import com.app.test.game.bean.*
import com.app.test.game.helper.IdiomHelper.Companion.get
import com.app.test.game.source.IdiomType
import com.app.test.util.StringUtils
import com.app.test.util.Utils
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * @author lcx
 * Created at 2020.3.27
 * Describe:
 */
open class IdiomView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr), AnswerControl<AnswerListener?> {
    private var rvSelectIdiomBoard: RecyclerView
    private var rvSelectIdiomFreeWord: RecyclerView
    private var answerListener: AnswerListener? = null

    /*备选词适配器*/
    private var freeWordAdapter: IdiomFreeAdapter? = null

    /*成语棋盘适配器*/
    private var idiomBoardAdapter: IdiomBoardAdapter? = null
    private var intoPosition = 0
    private var intoQuestion: Question? = null
    private var handler: Handler? = null
    override fun getHandler(): Handler {
        if (Utils.isEmpty(handler)) {
            handler = Handler(Looper.getMainLooper())
        }
        return handler!!
    }


    init {
        View.inflate(context, R.layout.layout_idiom_answer, this)
        rvSelectIdiomBoard = findViewById(R.id.rv_select_idiom_board)
        rvSelectIdiomFreeWord = findViewById(R.id.rv_select_idiom_free_word)
        setViewListener()
    }


    fun onDestroy() {
        handler?.removeCallbacksAndMessages(null)
        handler = null
    }

    @Synchronized
    private fun clickIdiomWord(adapter: BaseQuickAdapter<*, *>, position: Int) {
        val charType = adapter.data[position] as SuperType
        //如果选中，则隐藏
        charType.isSelected = true
        adapter.notifyDataSetChanged()
        collectSelectFreeWordCount()
        idiomBoardAdapter?.selectWordToBoard(position, charType)
    }

    fun setProverbData(question: Question?, proverbCharacters: Array<Array<IdiomWrapper?>>?, point: Point?, firstSelectPosition: Int) {
        var mPoint = point
        if (Utils.isEmpty(mPoint)) {
            mPoint = Point()
        }
        if (Utils.isEmpty(proverbCharacters)) {
            return
        }
        question?.also {
            val row = it.relativeRow
            val column = it.relativeColumn
            val data: List<SuperType> = it.proverbDisturbWordList
            if (!Utils.isEmpty(rvSelectIdiomFreeWord)) {
                freeWordAdapter = IdiomFreeAdapter(data)
                freeWordAdapter?.onItemClickListener = object : NoFastClickListener() {
                    override fun onNoDoubleClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                        clickIdiomWord(adapter, position)
                    }
                }
                val spanSize = data.size.coerceAtMost(6)
                rvSelectIdiomFreeWord.layoutManager = GridLayoutManager(context, spanSize)
                rvSelectIdiomFreeWord.adapter = freeWordAdapter
            }
            if (!Utils.isEmpty(rvSelectIdiomBoard)) {
                //构造辅助数据
                val selectIdiomFreeWords = ArrayList<Int>()
                val count = row * column
                for (i in 0 until count) {
                    selectIdiomFreeWords.add(i)
                }
                idiomBoardAdapter = IdiomBoardAdapter(selectIdiomFreeWords, proverbCharacters, row, column)
                idiomBoardAdapter?.setFirstSelectPoint(mPoint, firstSelectPosition)
                idiomBoardAdapter?.setProverList(it.proverbList)
                idiomBoardAdapter?.setItemTypeListener(object : ItemTypeListener<Int> {
                    override fun onItemType(currentIndex: Int, type: Int, obj: Any?) {
                        when (type) {
                            IdiomType.TAKE_DOWN_WORD -> {
                                if (Utils.isEmpty(freeWordAdapter) || Utils.isEmpty(obj)) {
                                    return
                                }
                                freeWordAdapter?.also { it ->
                                    val disturb = obj as SuperType?
                                    val disturbList = it.data
                                    if (Utils.isEmpty(disturbList)) {
                                        return
                                    }
                                    val index = disturbList.indexOf(disturb)
                                    disturb?.isSelected = false
                                    it.notifyItemChanged(index)
                                }
                            }
                            IdiomType.ALL_FILL_RIGHT -> {
                                setUploadData(intoQuestion)
                                answerListener?.onUserAnswer(intoPosition, intoQuestion, "0", true)
                            }
                            IdiomType.EACH_PROVERB_RIGHT_COUNT -> if (obj is Int) {
                                val rightCount = obj.toInt()
                                answerListener?.onRightCount(rightCount)
                            }
                            IdiomType.EACH_PROVERB_RIGHT_COLLECT -> if (obj is String) {
                                collectAnswerProverbIntervalTime(obj)
                            }
                            IdiomType.ANSWER_RIGHT_VIEWS_POSITION -> getViewForRightProverbLastChar(obj)
                            IdiomType.ANSWER_RIGHT_IDIOM_VIEWS_POSITION -> getHandler().post { getViewForRightProverbEachCharView(obj, true) }
                            IdiomType.ANSWER_ERROR_IDIOM_VIEWS_POSITION -> getHandler().post { getViewForRightProverbEachCharView(obj, false) }
                            IdiomType.AUTO_FILL_CHAR -> findTypeByTitle(obj)
                            IdiomType.PROVERB_FILL_ERROR -> answerListener?.onPutError()
                            IdiomType.SHOW_TOAST -> if (!Utils.isEmpty(obj) && obj is String) {
                                Toast.makeText(context, obj as String?, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                })
                rvSelectIdiomBoard.layoutManager = GridLayoutManager(context, column)
                rvSelectIdiomBoard.adapter = idiomBoardAdapter
            }
        }

    }

    /*正确的成语中，获取最后一个字所在view*/
    private fun getViewForRightProverbLastChar(obj: Any?) {
        if (!Utils.isEmpty(answerListener)) {
            if (!Utils.isEmpty(obj) && obj is IdiomViewPosition) {
                /*获取成语尾部最后一个view*/
                val viewsPosition = obj.lastViewPosition
                val views = arrayOfNulls<View>(viewsPosition.size)
                for (i in viewsPosition.indices) {
                    val viewPosition = viewsPosition[i]
                    views[i] = rvSelectIdiomBoard.getChildAt(viewPosition)
                    if (views[i] == null) {
                        continue
                    }
                }
                answerListener?.onRightCharacterViews(*views)
            }
        }
    }

    private var animIntervalTime = 0
    private fun getViewForRightProverbEachCharView(obj: Any?, isAllRight: Boolean) {
        if (!Utils.isEmpty(answerListener)) {
            if (!Utils.isEmpty(obj) && obj is IdiomViewPosition) {
                val eachViewPosition = obj.eachViewPosition
                /*获取成语每个view*/
                for (idiomItem in eachViewPosition) {
                    if (Utils.isEmpty(idiomItem)) {
                        continue
                    }
                    animIntervalTime = 0
                    for (viewPosition in idiomItem) {
                        val view = rvSelectIdiomBoard.getChildAt(viewPosition)
                        if (Utils.isEmpty(view)) {
                            continue
                        }
                        val flGameProverb = view.findViewById<View>(R.id.fl_game_proverb)
                        getHandler().postDelayed({
                            if (isAllRight) {
                                get()?.playRightAnim(flGameProverb)
                            } else {
                                get()?.playErrorAnim(flGameProverb)
                            }
                        }, animIntervalTime.toLong())
                        if (isAllRight) {
                            animIntervalTime += 50
                        }
                    }
                    /*因为同时填错多组成语只需要移动其中一组成语，所以加了个break,如果多组成语都需要移动，则去掉break,这样改动最少*/break
                }
            }
        }
    }

    //自动选词，根据当前选择框获取正确title,然后在备选词中获取
    private fun findTypeByTitle(obj: Any?) {
        if (Utils.isEmpty(obj) || obj !is CharacterTips) {
            return
        }
        val title = obj.title
        freeWordAdapter?.also {
            val data = it.data
            if (Utils.isEmpty(data)) {
                return
            }
            var isFindByFreeWord = false
            for (i in data.indices) {
                val charType = data[i]
                if (Utils.isEmpty(charType)) {
                    continue
                }
                //必须是找备选面板还没选到棋盘上面的字
                if (StringUtils.equals(title, charType?.title) && !charType.isSelected) {
                    //如果选中，则隐藏
                    charType.isSelected = true
                    it.notifyItemChanged(i)
                    idiomBoardAdapter?.selectWordToBoard(i, charType)
                    isFindByFreeWord = true
                    return
                }
            }
            if (!isFindByFreeWord) {
                //如果备选面板没找到字，则往棋盘上面找，并且把棋盘上面的错的字拿下来，棋盘正确的字移到需要填写的地方
                if (Utils.isEmpty(idiomBoardAdapter)) {
                    return
                }
                idiomBoardAdapter?.also { it ->
                    val sourceWrapperData = it.sourceWrapperData
                    if (Utils.isEmpty(sourceWrapperData)) {
                        return
                    }
                    val length = sourceWrapperData.size
                    for (i in 0 until length) {
                        val sourceWrapper = sourceWrapperData[i]
                        if (Utils.isEmpty(sourceWrapper)) {
                            continue
                        }
                        val size = sourceWrapper.size
                        for (j in 0 until size) {
                            val wrapper = sourceWrapper[j]
                            if (Utils.isEmpty(wrapper)) {
                                continue
                            }
                            val idiomWord = wrapper.proverbCharacter
                            if (Utils.isEmpty(idiomWord)) {
                                continue
                            }
                            //如果不是需要填写的字，直接忽略
                            if (idiomWord.isShow) {
                                continue
                            }
                            val boardFillWord = idiomWord.shortTitle
                            if (StringUtils.equals(title, boardFillWord)) {
                                //假设棋盘需要自动填写的地方需要A ，但是A在B处，则把B处的字隐藏掉，然后把A拿到此处，把此处错误的字放到备选面板
                                /*下面分两步完成，第一部，取下备选词，更改备选词未被选中属性，第二部，自动填充，将刚刚取下的词，替换上去*/
                                idiomWord.isFilled = false
                                idiomWord.shortTitle = ""
                                it.notifyByXY(idiomWord.relativeX, idiomWord.relativeY)
                                val needRetrieveFreeWord = it.getNeedRetrieveFreeWord(idiomWord)
                                if (!Utils.isEmpty(needRetrieveFreeWord)) {
                                    //设置备选面板未被选中属性，方便下面的补充
                                    needRetrieveFreeWord?.isSelected = false
                                }
                                it.autoFillNextChar()
                                return
                            }
                        }
                    }
                }

            }
        }

    }

    protected fun initData() {}
    private fun setViewListener() {}
    private fun setUploadData(intoQuestion: Question?) {
        if (Utils.isEmpty(logHelper)) {
            return
        }
        collectAnswerEndTime()
        collectAnswerTime()
        intoQuestion?.retrycount = logHelper.retrycount
        intoQuestion?.clicks = logHelper.clicks
        intoQuestion?.clicktips = logHelper.clicktips
        intoQuestion?.costtime = logHelper.costtime
        intoQuestion?.diomicosttime = logHelper.stringBuilder
    }

    /*收集重试次数*/
    private fun collectResetCount() {
        logHelper.retrycount += 1
    }

    /*收集选词次数*/
    private fun collectSelectFreeWordCount() {
        logHelper.clicks += 1
    }

    /*收集点击提示次数*/
    private fun collectTipsCount() {
        logHelper.clicktips += 1
    }

    /*收集每题答题时间*/
    private fun collectAnswerTime() {
        if (Utils.isEmpty(logHelper)) {
            return
        }
        logHelper.costtime = logHelper.endTime - logHelper.startIntoTime
        if (logHelper.costtime <= 0) {
            logHelper.costtime = 10000
        }
    }

    /*记录每题答完的时间*/
    private fun collectAnswerEndTime() {
        logHelper.endTime = System.currentTimeMillis()
    }

    /*收集每题答对成语间隔*/
    private fun collectAnswerProverbIntervalTime(str: String) {
        logHelper.appendRightTime(str)
    }

    /*每开始一题就初始化答题统计帮助类*/
    private fun initGameUploadHelper() {
        logHelper = LogHelper()
        logHelper.startIntoTime = System.currentTimeMillis()
        logHelper.startSelectWordTime = System.currentTimeMillis()
    }

    override fun setAnswerListener(listener: AnswerListener?) {
        this.answerListener = listener
    }

    private lateinit var logHelper: LogHelper
    override fun onNextQuestion(position: Int, question: Question) {
        initGameUploadHelper()
        intoPosition = position
        intoQuestion = question
        if (Utils.isEmpty(question.proverbList)) {
            return
        }
        val strArray = Array(question.relativeRow) { arrayOfNulls<IdiomWrapper>(question.relativeColumn) }

        /*记录进入答题第一个选择的成语所在下标，以及选择框所在坐标*/
        var firstPosition = -1
        var point = Point(-1, -1)
        var tempX = 1000
        var tempY = 1000
        for (i in question.proverbList.indices) {
            val idiom = question.proverbList[i]
            if (Utils.isEmpty(idiom)) {
                continue
            }
            for (j in idiom.proverbCharacterList.indices) {
                val proverbList = idiom.proverbCharacterList[j]
                if (Utils.isEmpty(proverbList)) {
                    continue
                }
                val relativeX = proverbList.relativeX
                val relativeY = proverbList.relativeY
                var existWrapper = strArray[relativeX][relativeY]
                if (Utils.isEmpty(existWrapper)) {
                    existWrapper = IdiomWrapper()
                    existWrapper.proverbCharacter = proverbList
                    strArray[relativeX][relativeY] = existWrapper
                } else {
                    existWrapper?.proverbCharacter = proverbList
                }
                //记录当前字对应的默认一个成语下标
                existWrapper?.proverbIndex = i
                if (!proverbList.isShow && !proverbList.isFilled) {
                    /*记录进入答题第一个选择的成语所在下标，以及选择框所在坐标,所以需要比较哪个最小*/
                    if (tempX > relativeX || tempX == relativeX && tempY >= relativeY) {
                        tempX = relativeX
                        tempY = relativeY
                        point = Point(relativeX, relativeY)
                        firstPosition = i
                    }
                }
            }
        }
        setProverbData(question, strArray, point, firstPosition)
    }
}