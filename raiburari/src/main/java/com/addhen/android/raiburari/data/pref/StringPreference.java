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

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import javax.inject.Inject;

/**
 * Preference for saving String values
 *
 * @author Henry Addo
 */
public class StringPreference extends BasePreference<String> {

  /**
   * Constructs a new {@link StringPreference}
   *
   * @param sharedPreferences SharedPreferences to be used for storing the value.
   * @param key The key for the preference
   */
  @Inject public StringPreference(SharedPreferences sharedPreferences, String key) {
    this(sharedPreferences, key, null);
  }

  /**
   * Constructs a new {@link StringPreference}
   *
   * @param sharedPreferences SharedPreferences to be used for storing the value.
   * @param key The key for the preference
   * @param defaultValue The default value
   */
  public StringPreference(SharedPreferences sharedPreferences, String key, String defaultValue) {
    super(sharedPreferences, key, defaultValue);
  }

  /**
   * Get the saved String
   *
   * @return The saved string
   */
  @Override public String get() {
    return getSharedPreferences().getString(getKey(), getDefaultValue());
  }

  /** Set the value for the preference */
  @SuppressLint("CommitPrefEdits") @Override public void set(String value) {
    SharedPreferences.Editor editor = getSharedPreferences().edit().putString(getKey(), value);
    PREF_SAVER.save(editor);
  }
}
