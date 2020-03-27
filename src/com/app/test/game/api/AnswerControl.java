package com.app.test.game.api;

import com.app.test.game.bean.Question;

public interface AnswerControl<T> {
    void setAnswerListener(T listener);

    void onNextQuestion(int position, Question question);
}
