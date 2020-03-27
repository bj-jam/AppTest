package com.app.test.game.source;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({AnswerType.NONE
        , AnswerType.NORMAL
        , AnswerType.PROVER})
@Retention(RetentionPolicy.SOURCE)
public @interface AAnswerType {
}
