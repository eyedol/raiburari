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

import com.addhen.android.raiburari.presentation.ui.adapter.ContextMenuAdapter;
import com.addhen.android.raiburari.presentation.util.Utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import java.util.List;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class ContextMenuManager extends RecyclerView.OnScrollListener
        implements View.OnAttachStateChangeListener {

    private static ContextMenuManager instance;

    private ContextMenu contextMenuView;

    private boolean isContextMenuDismissing;

    private boolean isContextMenuShowing;

    public static ContextMenuManager getInstance() {
        if (instance == null) {
            instance = new ContextMenuManager();
        }
        return instance;
    }

    private ContextMenuManager() {
        // Do nothing
    }

    public void toggleContextMenuFromView(View openingView, int contextMenuItem,
            List<ContextMenuAdapter.ContextMenuItem> cm,
            ContextMenu.OnContextMenuItemClickListener listener) {
        if (contextMenuView == null) {
            showContextMenuFromView(openingView, contextMenuItem, cm, listener);
        } else {
            hideContextMenu();
        }
    }

    private void showContextMenuFromView(final View openingView, int contextMenuItem,
            List<ContextMenuAdapter.ContextMenuItem> cm,
            ContextMenu.OnContextMenuItemClickListener listener) {
        if (!isContextMenuShowing) {
            isContextMenuShowing = true;
            contextMenuView = new ContextMenu(openingView.getContext());
            contextMenuView.bindToItem(contextMenuItem);
            contextMenuView.addOnAttachStateChangeListener(this);
            contextMenuView.setOnContextMenuItemClickListener(listener);
            contextMenuView.setContextMenuItems(cm);
            ((ViewGroup) openingView.getRootView().findViewById(android.R.id.content))
                    .addView(contextMenuView);

            contextMenuView.getViewTreeObserver()
                    .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            contextMenuView.getViewTreeObserver().removeOnPreDrawListener(this);
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
        contextMenuView.setTranslationX(openingViewLocation[0] - contextMenuView.getWidth() / 3);
        contextMenuView.setTranslationY(
                openingViewLocation[1] - contextMenuView.getHeight() - additionalBottomMargin);
    }

    private void performShowAnimation() {
        contextMenuView.setPivotX(contextMenuView.getWidth() / 2);
        contextMenuView.setPivotY(contextMenuView.getHeight());
        contextMenuView.setScaleX(0.1f);
        contextMenuView.setScaleY(0.1f);
        contextMenuView.animate()
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

    public ContextMenu getContextMenu() {
        return contextMenuView;
    }

    private void performDismissAnimation() {
        contextMenuView.setPivotX(contextMenuView.getWidth() / 2);
        contextMenuView.setPivotY(contextMenuView.getHeight());
        contextMenuView.animate()
                .scaleX(0.1f).scaleY(0.1f)
                .setDuration(150)
                .setInterpolator(new AccelerateInterpolator())
                .setStartDelay(100)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (contextMenuView != null) {
                            contextMenuView.dismiss();
                        }
                        isContextMenuDismissing = false;
                    }
                });
    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (contextMenuView != null) {
            hideContextMenu();
            contextMenuView.setTranslationY(contextMenuView.getTranslationY() - dy);
        }
    }

    @Override
    public void onViewAttachedToWindow(View v) {

    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        contextMenuView = null;
    }
}
