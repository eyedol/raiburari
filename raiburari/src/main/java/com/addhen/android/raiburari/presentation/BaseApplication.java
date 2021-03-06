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

package com.addhen.android.raiburari.presentation;

import android.app.Application;
import com.addhen.android.raiburari.presentation.di.component.ApplicationComponent;
import com.addhen.android.raiburari.presentation.di.component.DaggerApplicationComponent;
import com.addhen.android.raiburari.presentation.di.module.ApplicationModule;

/**
 * Base {@link Application} class that must be subclassed by the inherited app to setup the App for
 * Dagger Dependency Injections.
 *
 * @author Henry Addo
 */
// This should registered by the implementing class
@SuppressWarnings("Registered") public class BaseApplication extends Application {

  private ApplicationComponent mApplicationComponent;

  protected BaseApplication() {
    // No-op
  }

  @Override public void onCreate() {
    super.onCreate();
    this.initializeInjector();
  }

  private void initializeInjector() {
    mApplicationComponent =
        DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
  }

  /**
   * Get the {@link ApplicationComponent}
   *
   * @return The {@link ApplicationComponent}
   */
  public ApplicationComponent getApplicationComponent() {
    return mApplicationComponent;
  }
}
