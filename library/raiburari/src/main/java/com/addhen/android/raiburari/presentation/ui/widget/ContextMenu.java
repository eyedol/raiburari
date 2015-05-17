/*
 * Copyright (c) 2015 Henry Addo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.addhen.android.raiburari.presentation.ui.widget;

import com.addhen.android.raiburari.R;
import com.addhen.android.raiburari.presentation.ui.adapter.ContextMenuAdapter;
import com.addhen.android.raiburari.presentation.util.Utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

/**
 * A View for setting a context menu.
 *
 * @author Henry Addo
 */
public class ContextMenu extends LinearLayout implements AdapterView.OnItemClickListener {

    private static final int DEFAULT_CONTEXT_MENU_WIDTH = Utils.dpToPx(240);

    protected int mWidth;

    protected int mBackgroundResId;

    private ListView mListView;

    private ContextMenuAdapter mContextMenuAdapter;

    private OnContextMenuItemClickListener mOnContextMenuItemClickListener;

    private boolean isContextMenuDismissing;

    private boolean isContextMenuShowing;

    public ContextMenu(Context context) {
        super(context);
        initView(context);
    }

    public ContextMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        initView(context);
    }

    public ContextMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initView(context);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext()
                .obtainStyledAttributes(attrs, R.styleable.ContextMenu);

        try {
            mWidth = (int) typedArray
                    .getDimension(R.styleable.ContextMenu_width, DEFAULT_CONTEXT_MENU_WIDTH);
            mBackgroundResId = typedArray
                    .getResourceId(R.styleable.ContextMenu_backgroundShadowRes,
                            R.drawable.bg_container_shadow);
        } finally {
            typedArray.recycle();
        }
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.context_menu, this, true);
        mListView = (ListView) view.findViewById(R.id.context_menu_listview);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView.setDivider(getResources().getDrawable(android.R.color.transparent));
        mListView.setDividerHeight(0);
        mListView.setBackgroundColor(Color.WHITE);
        setBackgroundResource(mBackgroundResId);
        setOrientation(VERTICAL);
        setLayoutParams(new LayoutParams(mWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
        mContextMenuAdapter = new ContextMenuAdapter(context);
        mListView.setAdapter(mContextMenuAdapter);
        addView(mListView);
    }

    public void setContextMenuItems(List<ContextMenuAdapter.ContextMenuItem> contextMenuItems) {
        mContextMenuAdapter.setContextMenuItems(contextMenuItems);
    }

    public ContextMenuAdapter getContextMenuAdapter() {
        return mContextMenuAdapter;
    }

    public void setOnContextMenuItemClickListener(
            OnContextMenuItemClickListener onContextMenuItemClickListener) {
        mOnContextMenuItemClickListener = onContextMenuItemClickListener;
    }

    public void toggleContextMenuFromView(View openingView, int position) {
        if (!isContextMenuShowing) {
            showContextMenuFromView(openingView, position);
        } else {
            hideContextMenu();
        }
    }

    private void showContextMenuFromView(final View openingView, int position) {
        if (!isContextMenuShowing) {
            isContextMenuShowing = true;

            ((ViewGroup) openingView.getRootView().findViewById(android.R.id.content))
                    .addView(this);

            getViewTreeObserver()
                    .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            getViewTreeObserver().removeOnPreDrawListener(this);
                            setupContextMenuInitialPosition(openingView);
                            performShowAnimation();
                            return false;
                        }
                    });
        }
    }

    private void setupContextMenuInitialPosition(View openingView) {
        final int[] openingViewLocation = new int[2];
        openingView.getLocationOnScreen(openingViewLocation);
        int additionalBottomMargin = Utils.dpToPx(16);
        setTranslationX(openingViewLocation[0] - getWidth() / 3);
        setTranslationY(
                openingViewLocation[1] - getHeight() - additionalBottomMargin);
    }

    private void performShowAnimation() {
        setPivotX(getWidth() / 2);
        setPivotY(getHeight());
        setScaleX(0.1f);
        setScaleY(0.1f);
        animate()
                .scaleX(1f).scaleY(1f)
                .setDuration(150)
                .setInterpolator(new OvershootInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isContextMenuShowing = false;
                    }
                });
    }

    public void hideContextMenu() {
        if (!isContextMenuDismissing) {
            isContextMenuDismissing = true;
            performDismissAnimation();
        }
    }

    private void performDismissAnimation() {
        setPivotX(getWidth() / 2);
        setPivotY(getHeight());
        animate()
                .scaleX(0.1f).scaleY(0.1f)
                .setDuration(150)
                .setInterpolator(new AccelerateInterpolator())
                .setStartDelay(100)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dismiss();
                        isContextMenuDismissing = false;
                    }
                });
    }

    public void dismiss() {
        ((ViewGroup) getParent()).removeView(ContextMenu.this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mOnContextMenuItemClickListener != null) {
            mOnContextMenuItemClickListener.onItemClick(adapterView, view, i, l);
        }
    }

    public interface OnContextMenuItemClickListener {

        void onItemClick(AdapterView<?> adapterView, View view, int i, long l);
    }
}
