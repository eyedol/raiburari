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

package com.addhen.android.raiburari.sample.app.ui.activity;

import com.addhen.android.raiburari.di.HasComponent;
import com.addhen.android.raiburari.sample.app.R;
import com.addhen.android.raiburari.sample.app.di.components.DaggerUserComponent;
import com.addhen.android.raiburari.sample.app.di.components.UserComponent;
import com.addhen.android.raiburari.ui.activity.BaseActivity;

import android.os.Bundle;

public class MainActivity extends BaseActivity implements HasComponent<UserComponent> {

    private UserComponent userComponent;

    public MainActivity() {
        super(R.layout.activity_main, R.menu.menu_main);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injector();
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
}
