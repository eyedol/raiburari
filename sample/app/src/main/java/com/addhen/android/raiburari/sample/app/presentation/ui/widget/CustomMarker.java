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

package com.addhen.android.raiburari.sample.app.presentation.ui.widget;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.views.MapView;

/**
 * Extended the Stock {@link com.mapbox.mapboxsdk.overlay.Marker } to add ID support to make it
 * load a particular model
 *
 * @author Henry Addo
 */
public class CustomMarker extends Marker {

    public Long markerId;

    public CustomMarker(MapView mv, String aTitle, String aDescription,
            LatLng aLatLng) {
        super(mv, aTitle, aDescription, aLatLng);
    }

    /**
     * Construct a new Marker, given title, description, and place
     *
     * @param title       Marker title
     * @param description Marker description
     * @param latLng      Marker position
     */
    public CustomMarker(String title, String description, LatLng latLng) {
        this(null, title, description, latLng);
    }


}
