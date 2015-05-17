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

package com.addhen.android.raiburari.presentation.ui.adapter;

import com.addhen.android.raiburari.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for managing context menu items
 *
 * @author Henry Addo
 */
public class ContextMenuAdapter extends ArrayAdapter<ContextMenuAdapter.ContextMenuItem> {

    private static final int TYPE_MENU_ITEM = 0;

    private static final int TYPE_DIVIDER = 1;

    private final LayoutInflater inflater;

    private final List<ContextMenuItem> mMenuItems = new ArrayList<>();

    public ContextMenuAdapter(Context context) {
        super(context, 0);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mMenuItems.size();
    }

    public void setContextMenuItems(List<ContextMenuItem> contextMenuItems) {
        mMenuItems.clear();
        mMenuItems.addAll(contextMenuItems);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public ContextMenuItem getItem(int position) {
        return mMenuItems.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return mMenuItems.get(position).isDivider ? TYPE_DIVIDER : TYPE_MENU_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == TYPE_MENU_ITEM) {
            MenuItemViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.context_menu_item, parent, false);
                holder = new MenuItemViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (MenuItemViewHolder) convertView.getTag();
            }

            ContextMenuItem item = getItem(position);
            holder.label.setText(item.label);
            holder.icon.setImageResource(item.iconResId);
            holder.icon.setVisibility(item.iconResId == 0 ? View.GONE : View.VISIBLE);

            return convertView;
        } else {
            return inflater.inflate(R.layout.context_menu_item_divider, parent, false);
        }
    }

    @Override
    public boolean isEnabled(int position) {
        return !mMenuItems.get(position).isDivider;
    }

    public static class MenuItemViewHolder {

        ImageView icon;

        TextView label;

        public MenuItemViewHolder(View view) {
            icon = (ImageView) view.findViewById(R.id.context_menu_icon);
            label = (TextView) view.findViewById(R.id.context_menu_label);
        }
    }

    public static class ContextMenuItem {

        public int iconResId;

        public String label;

        public boolean isDivider;

        private ContextMenuItem() {

        }

        public ContextMenuItem(int iconResId, String label) {
            this.iconResId = iconResId;
            this.label = label;
            this.isDivider = false;
        }

        public static ContextMenuItem dividerMenuItem() {
            ContextMenuItem globalMenuItem = new ContextMenuItem();
            globalMenuItem.isDivider = true;
            return globalMenuItem;
        }
    }
}
