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

package com.addhen.android.raiburari.sample.app.presentation.ui.activity;

import com.addhen.android.raiburari.presentation.di.HasComponent;
import com.addhen.android.raiburari.presentation.model.NavDrawerItem;
import com.addhen.android.raiburari.presentation.ui.activity.BaseActivity;
import com.addhen.android.raiburari.presentation.ui.listener.NavDrawerListener;
import com.addhen.android.raiburari.sample.app.R;
import com.addhen.android.raiburari.sample.app.presentation.di.components.DaggerUserComponent;
import com.addhen.android.raiburari.sample.app.presentation.di.components.UserComponent;
import com.addhen.android.raiburari.sample.app.presentation.ui.fragment.MainFragment;
import com.addhen.android.raiburari.sample.app.presentation.ui.fragment.MapFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements HasComponent<UserComponent>,
        NavDrawerListener {

    private UserComponent userComponent;

    private static int HOME_FRAGMENT_POSITION = 0;

    private static int MAP_FRAGMENT_POSITION = 1;

    public MainActivity() {
        super(R.layout.activity_navdrawer, 0, R.id.drawerLayout);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injector();
        //addFragment(R.id.container, MainFragment.newInstance());
    }

    @Override
    protected void initNavDrawerItems(Bundle savedInstanceState) {
        setNavDrawerListener(this);
        NavDrawerItem homeNavDrawerItem = new NavDrawerItem("Home", R.drawable.ic_action_add, false,
                HOME_FRAGMENT_POSITION);
        NavDrawerItem mapNavDrawerItem = new NavDrawerItem("Map", R.drawable.ic_action_add, false,
                MAP_FRAGMENT_POSITION);
        List<NavDrawerItem> itemList = new ArrayList<>();
        itemList.add(homeNavDrawerItem);
        itemList.add(mapNavDrawerItem);

        setColorDefaultItemNavigation(R.color.navdrawer_background);
        setColorIconItemNavigation(R.color.navdrawer_icon_tint);
        setColorSelectedItemNavigation(R.color.navdrawer_text_color);
        setNavDrawerAdapterItems(itemList);

        setNavDrawerFooterItem("Settings", R.drawable.abc_btn_check_material);
        if (savedInstanceState == null) {
            setNavDrawerDefaultStartPosition(0);
        }
    }

    private void injector() {

        this.userComponent = DaggerUserComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
    }

    @Override
    public UserComponent getComponent() {
        return userComponent;
    }

    @Override
    public void onNavDrawerItemClick(int position, int layoutContainerId) {
        Fragment fragment;
        if (position == MAP_FRAGMENT_POSITION) {
            fragment = MapFragment.newInstance();
            replaceFragment(layoutContainerId, fragment, "map");
        } else {
            fragment = MainFragment.newInstance();
            replaceFragment(layoutContainerId, fragment, "list");
        }

    }

    @Override
    public void onPrepareOptionsMenusNavDrawer(Menu menu, int position, boolean visible) {

    }

    @Override
    public void onNavDrawerFooterClick(View v) {

    }

    @Override
    public void onNavDrawerUserAvatarClick(View v) {

    }
}
