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

package com.addhen.android.raiburari.ui.adapter;

import com.addhen.android.raiburari.R;
import com.addhen.android.raiburari.model.NavDrawerItem;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * @author Henry Addo
 */
public class NavDrawerAdapter extends BaseAdapter {

    private int mColorIcon = 0;

    private int mColorName = 0;

    private int mColorSeparator = 0;

    private int mNewSelector = 0;

    private int mColorDefault = 0;

    private final Context mcontext;

    private boolean mRemoveAlpha = false;

    private List<NavDrawerItem> mNavDrawerItems;

    public NavDrawerAdapter(Context context, List<NavDrawerItem> navDrawerItems,
            boolean removeAlpha, List<Integer> extra) {
        mcontext = context;
        mNavDrawerItems = navDrawerItems;
        mRemoveAlpha = removeAlpha;
        mNewSelector = extra.get(0);
        mColorDefault = extra.get(1);
        mColorIcon = extra.get(2);
        mColorName = extra.get(3);
        mColorSeparator = extra.get(4);
    }

    @Override
    public int getCount() {
        return mNavDrawerItems.size();
    }

    @Override
    public NavDrawerItem getItem(int position) {
        return mNavDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEnabled(int position) {
        return !getItem(position).isHeader;
    }

    public void setChecked(int position, boolean checked) {
        mNavDrawerItems.get(position).checked = checked;
        notifyDataSetChanged();
    }

    public void setTitle(int position, String name) {
        mNavDrawerItems.get(position).title = name;
        notifyDataSetChanged();
    }

    public void setIcon(int position, int iconResId) {
        mNavDrawerItems.get(position).iconResId = iconResId;
        notifyDataSetChanged();
    }

    public void setCounter(int position, int counter) {
        mNavDrawerItems.get(position).counter = counter;
        notifyDataSetChanged();
    }

    public void setNavDrawerItem(int position, String title, int iconResId, int counter) {
        mNavDrawerItems.get(position).title = title;
        mNavDrawerItems.get(position).iconResId = iconResId;
        mNavDrawerItems.get(position).counter = counter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Widgets widget;
        if (convertView == null) {
            convertView = LayoutInflater.from(mcontext)
                    .inflate(R.layout.navdrawer_item_layout, parent, false);
            widget = new Widgets(convertView);
        } else {
            widget = (Widgets) convertView.getTag();
        }
        NavDrawerItem navDrawerItem = getItem(position);
        widget.title.setText(navDrawerItem.title);
        if (navDrawerItem.isHeader) {
            widget.separatorLayout.setVisibility(View.GONE);
            widget.itemLayout.setVisibility(View.VISIBLE);
            widget.setAlpha(widget.title, (navDrawerItem.checked ? 1f : 0.07f));
            widget.title.setTextColor((!navDrawerItem.isHeader && navDrawerItem.checked &&
                            navDrawerItem.colorSelected > 0 ?
                            mcontext.getResources().getColor(navDrawerItem.colorSelected) :
                            mColorName > 0 ? mcontext.getResources().getColor(mColorName) :
                                    mcontext.getResources().getColor(R.color.navdrawer_text_color))
            );
        } else {
            widget.separatorLayout.setVisibility(View.VISIBLE);
            if (mColorSeparator > 0) {
                widget.separator.setBackgroundResource(mColorSeparator);
            }

            if (TextUtils.isEmpty(navDrawerItem.title)) {
                widget.title.setVisibility(View.GONE);
                widget.itemLayout.setVisibility(View.GONE);
            } else {
                widget.itemLayout.setVisibility(View.VISIBLE);
            }
        }

        // Set Counter
        if (widget.counter != null) {
            if (navDrawerItem.counter >= 1) {
                widget.counter.setVisibility(View.VISIBLE);
                widget.counter
                        .setText((navDrawerItem.counter > 99) ? "99+" : navDrawerItem.counter + "");
            } else {
                widget.counter.setVisibility(View.GONE);
            }
        }

        // Set Icon
        if (widget.icon != null) {
            if (navDrawerItem.iconResId > 0) {
                widget.icon.setVisibility(View.VISIBLE);
                widget.icon.setImageResource(navDrawerItem.iconResId);
                widget.setAlpha(widget.icon,
                        (!navDrawerItem.isHeader && navDrawerItem.checked ? 1f : 0.54f));
                widget.icon.setColorFilter((!navDrawerItem.isHeader && navDrawerItem.checked &&
                        navDrawerItem.colorSelected > 0 ?
                        mcontext.getResources().getColor(navDrawerItem.colorSelected) :
                        (mColorDefault != 0 ? mcontext.getResources().getColor(mColorDefault) :
                                mColorIcon > 0 ? mcontext.getResources().getColor(mColorIcon) :
                                        mcontext.getResources()
                                                .getColor(R.color.navdrawer_text_color))));
            } else {
                widget.icon.setVisibility(View.GONE);
            }
        }

        // Set item checked
        convertView.setBackgroundResource(R.drawable.navdrawer_item_selector_unchecked);
        if (!navDrawerItem.isHeader) {
            if (navDrawerItem.checked) {
                int backgroundResId = (!navDrawerItem.removeSelector ? (mNewSelector == 0 ?
                        R.drawable.navdrawer_item_selector_check : mNewSelector) :
                        R.drawable.navdrawer_item_selector_unchecked);
                convertView.setBackgroundResource(backgroundResId);
            }
        }
        return convertView;
    }

    private class Widgets {

        public TextView title;

        public TextView counter;

        public View separator;

        public ImageView icon;

        public RelativeLayout itemLayout;

        public LinearLayout separatorLayout;

        public Widgets(View convertView) {
            title = (TextView) convertView.findViewById(R.id.navdrawer_title);
            counter = (TextView) convertView.findViewById(R.id.navdrawer_counter);
            separator = convertView.findViewById(R.id.navdrawer_separator);
            itemLayout = (RelativeLayout) convertView.findViewById(R.id.navdrawer_item_layout);
            separatorLayout = (LinearLayout) convertView
                    .findViewById(R.id.navdrawer_separator_layout);
            icon = (ImageView) convertView.findViewById(R.id.navdrawer_icon);
        }

        public void setAlpha(View v, float alpha) {
            if (!mRemoveAlpha) {
                v.setAlpha(alpha);
            } else {
                v.setAlpha(1f);
            }
        }
    }
}
