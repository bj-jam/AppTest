package com.app.test.game.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.test.R;
import com.app.test.game.adapter.SingleAdapter;
import com.app.test.game.api.AnswerControl;
import com.app.test.game.api.AnswerListener;
import com.app.test.game.bean.Question;
import com.app.test.game.presenter.SinglePresenter;
import com.app.test.util.DensityUtil;
import com.app.test.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;

/**
 * @author lcx
 * Created at 2020.3.27
 * Describe:
 */
public class SingleView extends LinearLayout implements AnswerControl<AnswerListener> {
    SinglePresenter singlePresenter;
    private TextView tvSingleContent;
    private RecyclerView rvSingleOption;
    private SingleAdapter optionAdapter;
    private int currentPosition;
    private AnswerListener answerListener;
    private boolean isAlreadyGet;
    //单选标题的底部距离
    private int marginBottom = DensityUtil.dp2px(5);
    private long startTime;
    //提示的次数
    private int tipsCount;
    //单机的次数
    private int clicks;
    //选项的时间选择的时间
    private long optionTime;
    StringBuilder sb = new StringBuilder();

    public SingleView(Context context) {
        super(context);
        initView();
        initData();
        setViewListener();
    }

    public SingleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        initData();
        setViewListener();
    }

    public SingleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initData();
        setViewListener();
    }

    protected void initView() {
        View.inflate(getContext(), R.layout.view_single_answer, this);
        tvSingleContent = findViewById(R.id.tv_single_content);
        rvSingleOption = findViewById(R.id.rv_single_option);
        rvSingleOption.setLayoutManager(new LinearLayoutManager(getContext()));
        tvSingleContent.setMovementMethod(ScrollingMovementMethod.getInstance());

    }

    protected void initData() {
        optionAdapter = new SingleAdapter();
        if (!Utils.isEmpty(rvSingleOption))
            rvSingleOption.setAdapter(optionAdapter);

    }

    private SinglePresenter getPresenter() {
        if (singlePresenter == null)
            singlePresenter = new SinglePresenter(this);
        return singlePresenter;
    }

    protected void setViewListener() {
        optionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                clicks++;
                if (Utils.isEmpty(sb))
                    sb = new StringBuilder();
                sb.append(clicks);
                sb.append("_");
                sb.append(position + 1);
                sb.append("_");
                sb.append(System.currentTimeMillis() - optionTime);
                sb.append(",");
                optionTime = System.currentTimeMillis();
                getPresenter().clickItem(position, view);

            }


        });
    }

    /**
     * 设置答题完之后的监听
     *
     * @param answerListener
     */


    @Override
    public void setAnswerListener(AnswerListener answerListener) {
        this.answerListener = answerListener;
    }


    /**
     * 设置单选标题
     *
     * @param title
     */
    private void setProblemTitle(String title) {
        if (Utils.trimToEmptyNull(title) || Utils.isEmpty(tvSingleContent))
            return;
        tvSingleContent.setText(title);
    }

    /**
     * 有选择之后更新UI
     */

    public void selectData() {
        if (!Utils.isEmpty(optionAdapter))
            optionAdapter.dataChange();
    }

    public void answerResult(final Question question, final boolean isAnswerRight, View... view) {
        if (answerListener != null) {
            question.setCosttime(System.currentTimeMillis() - startTime);
            question.setClicktips(tipsCount);
            question.setClicks(clicks);
            if (!Utils.isEmpty(sb))
                question.setDiomicosttime(sb.toString());
            answerListener.onUserAnswer(currentPosition, question, "100", isAnswerRight);
            answerListener.onRightCharacterViews(view);
        }
    }

    /**
     * 设置选项
     *
     * @param question
     * @param answerData
     * @param selectData
     */
    public void setProblemData(Question question, ArrayList<String> answerData, ArrayList<String> selectData) {
        if (Utils.isEmpty(question))
            return;
        if (!Utils.isEmpty(optionAdapter))
            optionAdapter.setProblemData(question.getAnswerList(), selectData, answerData);
    }

    @Override
    public void onNextQuestion(int position, Question question) {
        if (Utils.isEmpty(question) || Utils.isEmpty(getPresenter()))
            return;
        clearInfo();
        this.currentPosition = position;
        fitHeight();
        setProblemTitle(question.getTitle());
        getPresenter().setNewProblem(question);
    }


    /**
     * 获取view的实际高度高度
     */
    private void fitHeight() {
        if (isAlreadyGet)
            return;
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                //标题高度
                int contentHeight = 0;
                int selfHeight;
                if (!Utils.isEmpty(tvSingleContent))
                    contentHeight = tvSingleContent.getHeight();
                selfHeight = SingleView.this.getHeight();
                if (contentHeight > 0 && selfHeight > 0) {
                    isAlreadyGet = true;
                    if (!Utils.isEmpty(optionAdapter)) {
                        optionAdapter.setOptionHeight(selfHeight - contentHeight - marginBottom);
                    }
                }
            }
        });
    }

    private void clearInfo() {
        startTime = System.currentTimeMillis();
        optionTime = System.currentTimeMillis();
        if (Utils.isEmpty(sb))
            sb = new StringBuilder();
        sb.setLength(0);
        currentPosition = -1;
        tipsCount = 0;
        clicks = 0;
        if (!Utils.isEmpty(tvSingleContent))
            tvSingleContent.setText("");
        if (!Utils.isEmpty(optionAdapter))
            optionAdapter.setProblemData(null, null, null);
        getPresenter().clearData();
    }

    public void onDestroy() {
        if (!Utils.isEmpty(getPresenter()))
            getPresenter().onDestroy();
    }
}
