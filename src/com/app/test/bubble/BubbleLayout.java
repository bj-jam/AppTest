/*
 * Copyright (C) 2015 tyrantgit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.app.test.bubble;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.app.test.R;


/**
 * @author lcx
 * Created at 2020.3.26
 * Describe:
 */
public class BubbleLayout extends RelativeLayout {

    private AbstractPathAnimator mAnimator;

    public BubbleLayout(Context context) {
        super(context);
        init(null, 0);
    }

    public BubbleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BubbleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.BubbleLayout, defStyleAttr, 0);

        mAnimator = new PathAnimator(AbstractPathAnimator.Config.fromTypeArray(a));

        a.recycle();
    }

    public AbstractPathAnimator getAnimator() {
        return mAnimator;
    }

    public void setAnimator(AbstractPathAnimator animator) {
        clearAnimation();
        mAnimator = animator;
    }

    public void clearAnimation() {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).clearAnimation();
        }
        removeAllViews();
    }

    public void addHeart(int color) {
        BubbleView bubbleView = new BubbleView(getContext());
        bubbleView.setColor(color);
        mAnimator.start(bubbleView, this);
    }

    public void addHeart(int color, int heartResId, int heartBorderResId) {
        BubbleView bubbleView = new BubbleView(getContext());
        bubbleView.setColorAndDrawables(color, heartResId, heartBorderResId);
        mAnimator.start(bubbleView, this);
    }

}
