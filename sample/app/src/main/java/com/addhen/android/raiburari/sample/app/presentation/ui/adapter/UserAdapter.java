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

package com.addhen.android.raiburari.sample.app.presentation.ui.adapter;

import com.addhen.android.raiburari.sample.app.R;
import com.addhen.android.raiburari.sample.app.presentation.model.UserModel;
import com.addhen.android.raiburari.presentation.ui.adapter.BaseRecyclerViewAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author Henry Addo
 */
public class UserAdapter extends BaseRecyclerViewAdapter<UserModel> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new Widgets(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_user_item, parent, false));
    }

    @Override
    public int getAdapterItemCount() {
        return getItems().size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((Widgets) viewHolder).title.setText(getItem(position).fullName);
        ((Widgets) viewHolder).email.setText(getItem(position).email);
    }

    public class Widgets extends RecyclerView.ViewHolder {

        TextView title;

        TextView email;

        public Widgets(View convertView) {
            super(convertView);
            title = (TextView) convertView.findViewById(R.id.title);
            email = (TextView) convertView.findViewById(R.id.email);
        }

    }

}
