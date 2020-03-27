package com.app.test.game.view;


import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.app.test.R;
import com.app.test.game.adapter.IdiomBoardAdapter;
import com.app.test.game.adapter.IdiomFreeAdapter;
import com.app.test.game.api.AnswerListener;
import com.app.test.game.api.AnswerControl;
import com.app.test.game.api.ItemTypeListener;
import com.app.test.game.api.NoFastClickListener;
import com.app.test.game.bean.CharacterTips;
import com.app.test.game.bean.LogHelper;
import com.app.test.game.bean.IdiomViewPosition;
import com.app.test.game.bean.Proverb;
import com.app.test.game.bean.ProverbCharacter;
import com.app.test.game.bean.IdiomWrapper;
import com.app.test.game.bean.Question;
import com.app.test.game.bean.SuperType;
import com.app.test.game.helper.IdiomHelper;
import com.app.test.game.source.IdiomType;
import com.app.test.util.StringUtils;
import com.app.test.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lcx
 * Created at 2020.3.27
 * Describe:
 */
public class IdiomView extends FrameLayout implements AnswerControl<AnswerListener> {
    private RecyclerView rvSelectIdiomBoard;
    private RecyclerView rvSelectIdiomFreeWord;
    private AnswerListener answerListener;
    /*备选词适配器*/
    private IdiomFreeAdapter freeWordAdapter;
    /*成语棋盘适配器*/
    private IdiomBoardAdapter idiomBoardAdapter;
    private int intoPosition;
    private Question intoQuestion;
    private Handler handler;

    @Override
    public Handler getHandler() {
        if (Utils.isEmpty(handler)) {
            handler = new Handler(Looper.getMainLooper());
        }
        return handler;
    }

    public IdiomView(Context context) {
        super(context);
        initView();
    }

    public IdiomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public IdiomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    protected void initView() {
        View.inflate(getContext(), R.layout.layout_idiom_answer, this);
        rvSelectIdiomBoard = findViewById(R.id.rv_select_idiom_board);
        rvSelectIdiomFreeWord = findViewById(R.id.rv_select_idiom_free_word);
        setViewListener();
    }

    public void onDestroy() {
        if (!Utils.isEmpty(handler)) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    private synchronized void clickIdiomWord(BaseQuickAdapter adapter, int position) {
        SuperType charType = (SuperType) adapter.getData().get(position);
        //如果选中，则隐藏
        charType.setSelected(true);
        adapter.notifyDataSetChanged();

        collectSelectFreeWordCount();

        if (!Utils.isEmpty(idiomBoardAdapter)) {
            idiomBoardAdapter.selectWordToBoard(position, charType);
        }
    }

    public void setProverbData(Question question, IdiomWrapper[][] proverbCharacters, Point point, int firstSelectPosition) {
        if (Utils.isEmpty(point)) {
            point = new Point();
        }
        if (Utils.isEmpty(question) || Utils.isEmpty(proverbCharacters)) {
            return;
        }
        int row = question.getRelativeRow();
        int column = question.getRelativeColumn();
        List<SuperType> data = question.getProverbDisturbWordList();
        if (!Utils.isEmpty(rvSelectIdiomFreeWord)) {
            freeWordAdapter = new IdiomFreeAdapter(data);
            freeWordAdapter.setOnItemClickListener(new NoFastClickListener() {
                @Override
                public void onNoDoubleClick(BaseQuickAdapter adapter, View view, int position) {
                    clickIdiomWord(adapter, position);
                }
            });

            int spanSize = Math.min(data.size(), 6);
            rvSelectIdiomFreeWord.setLayoutManager(new GridLayoutManager(getContext(), spanSize));
            rvSelectIdiomFreeWord.setAdapter(freeWordAdapter);
        }


        if (!Utils.isEmpty(rvSelectIdiomBoard)) {
            //构造辅助数据
            ArrayList<Integer> selectIdiomFreeWords = new ArrayList<>();
            int count = row * column;
            for (int i = 0; i < count; i++) {
                selectIdiomFreeWords.add(i);
            }


            idiomBoardAdapter = new IdiomBoardAdapter(selectIdiomFreeWords, proverbCharacters, row, column);
            idiomBoardAdapter.setFirstSelectPoint(point, firstSelectPosition);
            idiomBoardAdapter.setProverList(question.getProverbList());
            idiomBoardAdapter.setItemTypeListener(new ItemTypeListener<Integer>() {
                @Override
                public void onItemType(int currentIndex, Integer type, final Object obj) {
                    switch (type) {
                        case IdiomType.TAKE_DOWN_WORD:
                            if (Utils.isEmpty(freeWordAdapter) || Utils.isEmpty(obj)) {
                                break;
                            }
                            SuperType disturb = (SuperType) obj;
                            List<SuperType> disturbList = freeWordAdapter.getData();
                            if (Utils.isEmpty(disturbList)) {
                                break;
                            }
                            int index = disturbList.indexOf(disturb);
                            disturb.setSelected(false);

                            freeWordAdapter.notifyItemChanged(index);

                            break;
                        case IdiomType.ALL_FILL_RIGHT:
                            if (!Utils.isEmpty(answerListener)) {
                                setUploadData(intoQuestion);
                                if (answerListener.isPbRewardBoxSite()) {
                                    answerListener.onUserAnswer(intoPosition, intoQuestion, "0", true);
                                } else {
                                    postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            answerListener.onUserAnswer(intoPosition, intoQuestion, "0", true);
                                        }
                                    }, 400);
                                }
                            }
                            break;
                        case IdiomType.EACH_PROVERB_RIGHT_COUNT:

                            if (!Utils.isEmpty(answerListener)) {
                                if (obj instanceof Integer) {
                                    int rightCount = ((Integer) obj).intValue();
                                    answerListener.onRightCharacter(rightCount);
                                }
                            }
                            break;
                        case IdiomType.EACH_PROVERB_RIGHT_COLLECT:
                            if (obj instanceof String) {
                                collectAnswerProverbIntervalTime((String) obj);
                            }
                            break;
                        case IdiomType.ANSWER_RIGHT_VIEWS_POSITION:
                            getViewForRightProverbLastChar(obj);
                            break;
                        case IdiomType.ANSWER_RIGHT_IDIOM_VIEWS_POSITION:
                            getHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    getViewForRightProverbEachCharView(obj, true);
                                }
                            });
                            break;
                        case IdiomType.ANSWER_ERROR_IDIOM_VIEWS_POSITION:
                            getHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    getViewForRightProverbEachCharView(obj, false);
                                }
                            });
                            break;
                        case IdiomType.AUTO_FILL_CHAR:
                            findTypeByTitle(obj);
                            break;
                        case IdiomType.PROVERB_FILL_ERROR:

                            if (!Utils.isEmpty(answerListener)) {
                                answerListener.onErrorCharacter();
                            }
                            break;
                        case IdiomType.SHOW_TOAST:
                            if (!Utils.isEmpty(obj) && obj instanceof String) {
                                Toast.makeText(getContext(), (String) obj, Toast.LENGTH_LONG).show();
                                ;
                            }
                            break;
                    }
                }
            });

            rvSelectIdiomBoard.setLayoutManager(new GridLayoutManager(getContext(), column));
            rvSelectIdiomBoard.setAdapter(idiomBoardAdapter);


        }
    }


    /*正确的成语中，获取最后一个字所在view*/
    private void getViewForRightProverbLastChar(Object obj) {
        if (!Utils.isEmpty(answerListener)) {
            if (!Utils.isEmpty(obj) && obj instanceof IdiomViewPosition) {
                IdiomViewPosition idiomViewPosition = (IdiomViewPosition) obj;
                /*获取成语尾部最后一个view*/
                List<Integer> viewsPosition = idiomViewPosition.getLastViewPosition();
                View[] views = new View[viewsPosition.size()];
                for (int i = 0; i < viewsPosition.size(); i++) {
                    Integer viewPosition = viewsPosition.get(i);
                    views[i] = rvSelectIdiomBoard.getChildAt(viewPosition);
                    if (views[i] == null) {
                        continue;
                    }
                }
                answerListener.onRightCharacterViews(views);
            }
        }
    }

    private int animIntervalTime = 0;

    private void getViewForRightProverbEachCharView(Object obj, final boolean isAllRight) {
        if (!Utils.isEmpty(answerListener)) {
            if (!Utils.isEmpty(obj) && obj instanceof IdiomViewPosition) {
                IdiomViewPosition idiomViewPosition = (IdiomViewPosition) obj;
                List<List<Integer>> eachViewPosition = idiomViewPosition.getEachViewPosition();
                /*获取成语每个view*/
                for (List<Integer> idiomItem : eachViewPosition) {
                    if (Utils.isEmpty(idiomItem)) {
                        continue;
                    }
                    animIntervalTime = 0;
                    for (Integer viewPosition : idiomItem) {
                        final View view = rvSelectIdiomBoard.getChildAt(viewPosition);
                        if (Utils.isEmpty(view)) {
                            continue;
                        }
                        final View flGameProverb = view.findViewById(R.id.fl_game_proverb);
                        getHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (isAllRight) {
                                    IdiomHelper.get().playRightAnim(flGameProverb);
                                } else {
                                    IdiomHelper.get().playErrorAnim(flGameProverb);
                                }
                            }
                        }, animIntervalTime);
                        if (isAllRight) {
                            animIntervalTime += 50;
                        }
                    }
                    /*因为同时填错多组成语只需要移动其中一组成语，所以加了个break,如果多组成语都需要移动，则去掉break,这样改动最少*/
                    break;
                }
            }
        }
    }

    //自动选词，根据当前选择框获取正确title,然后在备选词中获取
    private void findTypeByTitle(Object obj) {
        if (Utils.isEmpty(obj) || !(obj instanceof CharacterTips)) {
            return;
        }
        CharacterTips characterTips = (CharacterTips) obj;
        String title = characterTips.getTitle();
//        String shortTitle = characterTips.getShortTitle();

        if (Utils.isEmpty(freeWordAdapter)) {
            return;
        }
        List<SuperType> data = freeWordAdapter.getData();
        if (Utils.isEmpty(data)) {
            return;
        }

        boolean isFindByFreeWord = false;
        for (int i = 0; i < data.size(); i++) {
            SuperType charType = data.get(i);
            if (Utils.isEmpty(charType)) {
                continue;
            }
            //必须是找备选面板还没选到棋盘上面的字
            if (StringUtils.equals(title, charType.getTitle()) && !charType.isSelected()) {
                //如果选中，则隐藏
                charType.setSelected(true);
                freeWordAdapter.notifyItemChanged(i);
                if (!Utils.isEmpty(idiomBoardAdapter)) {
                    idiomBoardAdapter.selectWordToBoard(i, charType);
                }
                isFindByFreeWord = true;
                return;
            }
        }
        if (!isFindByFreeWord) {
            //如果备选面板没找到字，则往棋盘上面找，并且把棋盘上面的错的字拿下来，棋盘正确的字移到需要填写的地方
            if (Utils.isEmpty(idiomBoardAdapter)) {
                return;
            }
            IdiomWrapper[][] sourceWrapperData = idiomBoardAdapter.getSourceWrapperData();
            if (Utils.isEmpty(sourceWrapperData)) {
                return;
            }
            int length = sourceWrapperData.length;
            for (int i = 0; i < length; i++) {
                IdiomWrapper[] sourceWrapper = sourceWrapperData[i];
                if (Utils.isEmpty(sourceWrapper)) {
                    continue;
                }
                int size = sourceWrapper.length;
                for (int j = 0; j < size; j++) {
                    IdiomWrapper wrapper = sourceWrapper[j];
                    if (Utils.isEmpty(wrapper)) {
                        continue;
                    }
                    ProverbCharacter proverbCharacter = wrapper.getProverbCharacter();
                    if (Utils.isEmpty(proverbCharacter)) {
                        continue;
                    }
                    //如果不是需要填写的字，直接忽略
                    if (proverbCharacter.isShow()) {
                        continue;
                    }
                    String boardFillWord = proverbCharacter.getShortTitle();
                    if (StringUtils.equals(title, boardFillWord)) {
                        //假设棋盘需要自动填写的地方需要A ，但是A在B处，则把B处的字隐藏掉，然后把A拿到此处，把此处错误的字放到备选面板
                        /*下面分两步完成，第一部，取下备选词，更改备选词未被选中属性，第二部，自动填充，将刚刚取下的词，替换上去*/
                        proverbCharacter.setFilled(false);
                        proverbCharacter.setShortTitle("");
                        idiomBoardAdapter.notifyByXY(proverbCharacter.getRelativeX(), proverbCharacter.getRelativeY());

                        SuperType needRetrieveFreeWord = idiomBoardAdapter.getNeedRetrieveFreeWord(proverbCharacter);
                        if (!Utils.isEmpty(needRetrieveFreeWord)) {
                            //设置备选面板未被选中属性，方便下面的补充
                            needRetrieveFreeWord.setSelected(false);
                        }
                        idiomBoardAdapter.autoFillNextChar();
                        return;
                    }
                }
            }
        }

    }

    protected void initData() {

    }

    protected void setViewListener() {

    }

    private void setUploadData(Question intoQuestion) {
        if (Utils.isEmpty(logHelper)) {
            return;
        }
        collectAnswerEndTime();
        collectAnswerTime();
        intoQuestion.setRetrycount(logHelper.retrycount);
        intoQuestion.setClicks(logHelper.clicks);
        intoQuestion.setClicktips(logHelper.clicktips);
        intoQuestion.setCosttime(logHelper.costtime);
        intoQuestion.setDiomicosttime(logHelper.getStringBuilder());
    }

    /*收集重试次数*/
    private void collectResetCount() {
        if (Utils.isEmpty(logHelper)) {
            return;
        }
        logHelper.retrycount = logHelper.retrycount + 1;
    }

    /*收集选词次数*/
    private void collectSelectFreeWordCount() {
        if (Utils.isEmpty(logHelper)) {
            return;
        }
        logHelper.clicks = logHelper.clicks + 1;
    }

    /*收集点击提示次数*/
    private void collectTipsCount() {
        if (Utils.isEmpty(logHelper)) {
            return;
        }
        logHelper.clicktips = logHelper.clicktips + 1;
    }

    /*收集每题答题时间*/
    private void collectAnswerTime() {
        if (Utils.isEmpty(logHelper)) {
            return;
        }
        logHelper.costtime = logHelper.endTime - logHelper.startIntoTime;
        if (logHelper.costtime <= 0) {
            logHelper.costtime = 10000;
        }
    }

    /*记录每题答完的时间*/
    private void collectAnswerEndTime() {
        if (Utils.isEmpty(logHelper)) {
            return;
        }
        logHelper.endTime = System.currentTimeMillis();
    }

    /*收集每题答对成语间隔*/
    private void collectAnswerProverbIntervalTime(String str) {
        if (Utils.isEmpty(logHelper)) {
            return;
        }
        logHelper.appendRightTime(str);
    }

    /*每开始一题就初始化答题统计帮助类*/
    private void initGameUploadHelper() {
        logHelper = new LogHelper();
        logHelper.startIntoTime = System.currentTimeMillis();
        logHelper.startSelectWordTime = System.currentTimeMillis();
    }

    @Override
    public void setAnswerListener(AnswerListener answerListener) {
        this.answerListener = answerListener;
    }

    private LogHelper logHelper;

    @Override
    public void onNextQuestion(int position, Question question) {
        initGameUploadHelper();
        intoPosition = position;
        intoQuestion = question;
        if (Utils.isEmpty(question.getProverbList())) {
            return;
        }

        IdiomWrapper[][] strArray = new IdiomWrapper[question.getRelativeRow()][question.getRelativeColumn()];

        /*记录进入答题第一个选择的成语所在下标，以及选择框所在坐标*/
        int firstPosition = -1;
        Point point = new Point(-1, -1);
        int tempX = 1000, tempY = 1000;
        for (int i = 0; i < question.getProverbList().size(); i++) {
            Proverb proverb = question.getProverbList().get(i);
            if (Utils.isEmpty(proverb)) {
                continue;
            }
            for (int j = 0; j < proverb.getProverbCharacterList().size(); j++) {
                ProverbCharacter proverbList = proverb.getProverbCharacterList().get(j);
                if (Utils.isEmpty(proverbList)) {
                    continue;
                }
                int relativeX = proverbList.getRelativeX();
                int relativeY = proverbList.getRelativeY();

                IdiomWrapper existWrapper = strArray[relativeX][relativeY];

                if (Utils.isEmpty(existWrapper)) {
                    existWrapper = new IdiomWrapper();
                    existWrapper.setProverbCharacter(proverbList);
                    strArray[relativeX][relativeY] = existWrapper;
                } else {
                    existWrapper.setProverbCharacter(proverbList);
                }
                //记录当前字对应的默认一个成语下标
                existWrapper.setProverbIndex(i);

                if (!proverbList.isShow() && !proverbList.isFilled()) {
                    /*记录进入答题第一个选择的成语所在下标，以及选择框所在坐标,所以需要比较哪个最小*/
                    if ((tempX > relativeX) || (tempX == relativeX && tempY >= relativeY)) {
                        tempX = relativeX;
                        tempY = relativeY;
                        point = new Point(relativeX, relativeY);
                        firstPosition = i;
                    }
                }
            }
        }
        setProverbData(question, strArray, point, firstPosition);
    }
}
