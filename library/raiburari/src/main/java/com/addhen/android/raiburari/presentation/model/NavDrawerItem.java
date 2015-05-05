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

package com.addhen.android.raiburari.presentation.model;

/**
 * @author Henry Addo
 */
public class NavDrawerItem extends Model {

    public String title;

    public int counter;

    public int iconResId;

    public boolean isHeader;

    public int colorSelected = 0;

    public boolean checked = false;

    public boolean removeSelector = false;

    public NavDrawerItem(String title, int iconResId, boolean header, int counter,
            int colorSelected,
            boolean removeSelector) {
        this.title = title;
        this.iconResId = iconResId;
        isHeader = header;
        this.colorSelected = colorSelected;
        this.counter = counter;
        this.removeSelector = removeSelector;
    }

    public NavDrawerItem(String title, int iconResId, boolean header, int counter) {
        this(title, iconResId, header, counter, 0, false);
    }
}
