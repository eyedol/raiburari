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

package com.addhen.android.data.pref;

import android.content.SharedPreferences;

/**
 * Preference for saving Integer values
 *
 * @author Henry Addo
 */
public class IntPreference extends BasePreference<Integer> {

    /**
     * Constructs a new {@link com.addhen.android.data.pref.IntPreference}
     *
     * @param sharedPreferences SharedPreferences to be used for storing the value.
     * @param key               The key for the preference
     */
    public IntPreference(SharedPreferences sharedPreferences, String key) {
        this(sharedPreferences, key, 0);
    }

    /**
     * Constructs a new {@link com.addhen.android.data.pref.IntPreference}
     *
     * @param sharedPreferences SharedPreferences to be used for storing the value.
     * @param key               The key for the preference
     * @param defaultValue      The default value
     */
    public IntPreference(SharedPreferences sharedPreferences, String key,
            Integer defaultValue) {
        super(sharedPreferences, key, defaultValue);
    }

    @Override
    public Integer get() {
        return getSharedPreferences().getInt(getKey(), getDefaultValue());
    }

    /**
     * Sets the Integer to be saved
     *
     * @param value The Integer value to be saved
     */
    @Override
    public void set(Integer value) {
        this.set((int) value);
    }

    /**
     * Convenient method for setting the Integer to be saved and to avoid clumsy autoboxing.
     *
     * @param value The Integer value to be saved
     */
    public void set(int value) {
        getSharedPreferences().edit().putInt(getKey(), value).apply();
    }
}
