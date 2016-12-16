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

package com.addhen.android.raiburari.data.pref;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;

/**
 * Base class for creating Typed base Preference
 *
 * @author Henry Addo
 */
public abstract class BasePreference<T> implements AppPreference<T> {

  /**
   * Wrapper for saving the preference. Switches between
   * {@link android.content.SharedPreferences.Editor#commit()}, and
   * {@link android.content.SharedPreferences.Editor#apply()} based of of the api level
   */
  static final PrefSaver PREF_SAVER;

  static {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
      PREF_SAVER = new PrefSaverGingerbread();
    } else {
      PREF_SAVER = new PrefSaverDefault();
    }
  }

  private final SharedPreferences mSharedPreferences;

  private final String mKey;

  private final T mDefaultValue;

  public BasePreference(SharedPreferences sharedPreferences, String key, T defaultValue) {
    mSharedPreferences = sharedPreferences;
    mKey = key;
    mDefaultValue = defaultValue;
  }

  @Override public boolean isSet() {
    return getSharedPreferences().contains(mKey);
  }

  @Override public void delete() {
    getSharedPreferences().edit().remove(mKey).apply();
  }

  /**
   * Gets the key of the preference.
   *
   * @return The key
   */
  protected String getKey() {
    return mKey;
  }

  /**
   * Gets the default value of the preference
   *
   * @return The default value
   */
  protected T getDefaultValue() {
    return mDefaultValue;
  }

  /**
   * Gets the {@link SharedPreferences}
   *
   * @return The SharedPreferences
   */
  protected SharedPreferences getSharedPreferences() {
    return mSharedPreferences;
  }

  /** Get the implementation to save shared prefereneces based on api level */
  protected PrefSaver getPrefSaver() {
    return PREF_SAVER;
  }

  interface PrefSaver {

    void save(SharedPreferences.Editor editor);
  }

  static class PrefSaverDefault implements PrefSaver {

    @Override public void save(SharedPreferences.Editor editor) {
      editor.commit();
    }
  }

  static class PrefSaverGingerbread implements PrefSaver {

    @TargetApi(Build.VERSION_CODES.GINGERBREAD) @Override
    public void save(SharedPreferences.Editor editor) {
      editor.apply();
    }
  }
}
