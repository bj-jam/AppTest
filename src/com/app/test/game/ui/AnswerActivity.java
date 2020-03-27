package com.app.test.game.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.app.test.R;
import com.app.test.game.bean.Question;
import com.app.test.game.presenter.AnswerPresenter;
import com.app.test.game.source.AnswerType;
import com.app.test.game.view.IdiomView;
import com.app.test.game.view.OpenDoorView;
import com.app.test.game.view.SingleView;
import com.app.test.util.Utils;

/**
 * @author lcx
 * Created at 2020.3.27
 * Describe:
 */
public class AnswerActivity extends Activity {
    private AnswerPresenter answerPresenter;
    private View ivFinish;
    private SingleView singleView;
    private IdiomView idiomView;
    private OpenDoorView openDoorView;
    private TextView tvProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);
        initView();
        initData();
    }

    private void initView() {
        ivFinish = findViewById(R.id.iv_finish);
        singleView = findViewById(R.id.sgv_single);
        idiomView = findViewById(R.id.sgv_proverb);
        openDoorView = findViewById(R.id.godv_layout);
        tvProgress = findViewById(R.id.tv_answer_progress);
        setViewListener();
    }

    private void initData() {
        Intent intent = getIntent();
        if (Utils.isEmpty(intent)) {
            finish();
            return;
        }
        getPresenter().getCheckpointList();
    }

    private AnswerPresenter getPresenter() {
        if (answerPresenter == null)
            answerPresenter = new AnswerPresenter(this);
        return answerPresenter;
    }


    private void setViewListener() {
        ivFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        idiomView.setAnswerListener(getPresenter());
        singleView.setAnswerListener(getPresenter());
    }


    public void dispatchQuestion(final int index, final Question question) {
        if (Utils.isEmpty(openDoorView)) {
            _dispatchQuestion(index, question);
            return;
        }
        openDoorView.startCloseAnim();
        openDoorView.setOpenDoorListen(new OpenDoorView.OpenDoorListen() {
            @Override
            public void openAnimStart() {
                _dispatchQuestion(index, question);
            }
        });
    }


    public void _dispatchQuestion(int index, Question question) {
        if (checkIllegal(index, question)) {
            return;
        }
        int questionType = question.getQuestionType();
        singleView.setVisibility(View.GONE);
        idiomView.setVisibility(View.GONE);
        switch (questionType) {
            case AnswerType.NORMAL:
                singleView.setVisibility(View.VISIBLE);
                singleView.onNextQuestion(index, question);
                break;
            case AnswerType.PROVER:
                idiomView.setVisibility(View.VISIBLE);
                idiomView.onNextQuestion(index, question);
                break;
        }
    }

    public void changeIndex(int index, int amount) {
        if (Utils.isEmpty(tvProgress) || index + 1 > amount) {
            return;
        }
        tvProgress.setText(String.format("本轮答题:%1$s/%2$s题", String.valueOf(index + 1), String.valueOf(amount)));
    }


    @Override
    protected void onDestroy() {
        if (!Utils.isEmpty(singleView))
            singleView.onDestroy();
        if (!Utils.isEmpty(idiomView))
            idiomView.onDestroy();
        super.onDestroy();
    }

    private boolean checkIllegal(int index, Question question) {
        return Utils.isEmpty(singleView) || Utils.isEmpty(idiomView) || index < 0 || Utils.isEmpty(question) || question.getQuestionType() == AnswerType.NONE;
    }
}
