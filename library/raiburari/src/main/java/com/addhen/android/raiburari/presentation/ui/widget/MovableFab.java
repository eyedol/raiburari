/*
 * Copyright (c) 2015. Henry Addo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.addhen.android.raiburari.presentation.ui.widget;

import com.getbase.floatingactionbutton.FloatingActionButton;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * @author Henry Addo
 */
public class MovableFab extends FloatingActionButton {

    private static final int TRANSLATE_DURATION_MILLIS = 500;

    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    private boolean mHidden = false;

    private float mYDisplayed = -1;

    private float mYHidden = -1;

    public MovableFab(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MovableFab(Context context) {
        this(context, null);
    }

    public MovableFab(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void moveUp(int level) {
        int translationY = -level;
        animate().translationY(translationY).setInterpolator(
                new AccelerateInterpolator(2)).start();
    }

    public void moveDown(int level) {
        animate().translationY(level).setInterpolator(
                new DecelerateInterpolator(2)).start();

    }

    private void move(int level) {
        animate().setInterpolator(mInterpolator)
                .setDuration(TRANSLATE_DURATION_MILLIS)
                .translationYBy(level);
    }

    public void hide(boolean hide) {
        // If the hidden state is being updated
        if (mHidden != hide) {

            // Store the new hidden state
            mHidden = hide;

            animate().setInterpolator(mInterpolator)
                    .setDuration(TRANSLATE_DURATION_MILLIS)
                    .translationY(mHidden ? mYHidden : mYDisplayed);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mYDisplayed == -1) {
            mYDisplayed = getY();
        }
    }

}
