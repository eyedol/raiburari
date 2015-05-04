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

package com.addhen.android.raiburari.sample.app.di.components;

import com.addhen.android.raiburari.di.component.ActivityComponent;
import com.addhen.android.raiburari.di.module.ActivityModule;
import com.addhen.android.raiburari.di.qualifier.ForActivity;
import com.addhen.android.raiburari.sample.app.di.modules.UserModule;
import com.addhen.android.raiburari.sample.app.ui.fragment.MainFragment;

import dagger.Component;

/**
 * @author Henry Addo
 */
@ForActivity
@Component(dependencies = SampleApplicationComponent.class, modules = {ActivityModule.class,
        UserModule.class})
public interface UserComponent extends ActivityComponent {

    void inject(MainFragment mainFragment);
}
