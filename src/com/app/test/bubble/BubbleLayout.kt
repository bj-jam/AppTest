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
package com.app.test.bubble

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.app.test.R

/**
 * @author lcx
 * Created at 2020.3.26
 * Describe:
 */
class BubbleLayout
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : RelativeLayout(context, attrs, defStyleAttr) {
    private lateinit var mAnimator: AbstractPathAnimator

    init {
        val a = context.obtainStyledAttributes(
                attrs, R.styleable.BubbleLayout, defStyleAttr, 0)
        mAnimator = PathAnimator(AbstractPathAnimator.Config.fromTypeArray(a))
        a.recycle()
    }

    var animator: AbstractPathAnimator?
        get() = mAnimator
        set(animator) {
            clearAnimation()
            if (animator != null) {
                mAnimator = animator
            }
        }

    override fun clearAnimation() {
        for (i in 0 until childCount) {
            getChildAt(i).clearAnimation()
        }
        removeAllViews()
    }

    fun addHeart(color: Int) {
        val bubbleView = BubbleView(context)
        bubbleView.setColor(color)
        mAnimator.start(bubbleView, this)
    }

    fun addHeart(color: Int, heartResId: Int, heartBorderResId: Int) {
        val bubbleView = BubbleView(context)
        bubbleView.setColorAndDrawables(color, heartResId, heartBorderResId)
        mAnimator.start(bubbleView, this)
    }
}