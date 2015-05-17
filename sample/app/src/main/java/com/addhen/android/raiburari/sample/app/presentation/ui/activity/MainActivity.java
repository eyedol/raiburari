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

package com.addhen.android.raiburari.sample.app.presentation.ui.activity;

import com.addhen.android.raiburari.presentation.di.HasComponent;
import com.addhen.android.raiburari.presentation.ui.activity.BaseActivity;
import com.addhen.android.raiburari.sample.app.R;
import com.addhen.android.raiburari.sample.app.presentation.di.components.DaggerUserComponent;
import com.addhen.android.raiburari.sample.app.presentation.di.components.UserComponent;
import com.addhen.android.raiburari.sample.app.presentation.ui.fragment.MainFragment;
import com.addhen.android.raiburari.sample.app.presentation.ui.fragment.MapFragment;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import butterknife.InjectView;

public class MainActivity extends BaseActivity implements HasComponent<UserComponent>,
        Drawer.OnDrawerItemClickListener {

    private UserComponent userComponent;

    private static int HOME_FRAGMENT_POSITION = 0;

    private static int MAP_FRAGMENT_POSITION = 1;

    private static int FRAG_CONTAINER_ID = R.id.fragment_container;

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    private Drawer.Result result = null;

    public MainActivity() {
        super(R.layout.activity_navdrawer, 0);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injector();
        initNavDrawerItems(savedInstanceState);
    }

    private void initNavDrawerItems(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        if (savedInstanceState == null) {
            addFragment(FRAG_CONTAINER_ID, MainFragment.newInstance());
        }
        result = new Drawer()
                .withActivity(this)
                .withToolbar(mToolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.home).withIcon(
                                FontAwesome.Icon.faw_home).withIdentifier(HOME_FRAGMENT_POSITION),
                        new PrimaryDrawerItem().withName(R.string.map)
                                .withIcon(FontAwesome.Icon.faw_map_marker)
                                .withIdentifier(MAP_FRAGMENT_POSITION)

                )
                .withOnDrawerItemClickListener(this)
                .build();
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l,
            IDrawerItem iDrawerItem) {
        if (iDrawerItem != null) {
            if (iDrawerItem.getIdentifier() == MAP_FRAGMENT_POSITION) {
                replaceFragment(R.id.fragment_container, MapFragment.newInstance());
            } else {
                replaceFragment(R.id.fragment_container, MainFragment.newInstance());
            }
        }
    }
}
