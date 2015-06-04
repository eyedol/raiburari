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

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private ListView mListView;

    private ContextMenuAdapter mContextMenuAdapter;

    private OnContextMenuItemClickListener mOnContextMenuItemClickListener;

    private int mContextMenuItem = -1;

    public ContextMenu(Context context) {
        super(context);
        initView(context);
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
        mListView.setOnItemClickListener(this);
        mContextMenuAdapter = new ContextMenuAdapter(context);
        mListView.setAdapter(mContextMenuAdapter);
        setBackgroundResource(R.drawable.bg_container_shadow);
        setOrientation(VERTICAL);
        setLayoutParams(
                new LayoutParams(DEFAULT_CONTEXT_MENU_WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void bindToItem(int contextMenuItem) {
        mContextMenuItem = contextMenuItem;
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
