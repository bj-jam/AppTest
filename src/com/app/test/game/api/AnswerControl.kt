package com.app.test.game.api

import com.app.test.game.bean.Question

interface AnswerControl<T> {
    open fun setAnswerListener(listener: T): Unit
    open fun onNextQuestion(position: Int, question: Question?): Unit
}