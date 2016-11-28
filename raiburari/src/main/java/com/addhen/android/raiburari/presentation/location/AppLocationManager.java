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

package com.addhen.android.raiburari.presentation.location;


import com.addhen.android.raiburari.presentation.location.geocoder.GeocodeObservable;
import com.addhen.android.raiburari.presentation.location.geocoder.ReverseGeocodeObservable;

import android.content.Context;
import android.location.Address;
import android.location.Location;

import java.util.List;

import rx.Observable;


/**
 * Location manager that returns observables for getting the location of the user's device.
 * You can reverse geocode to get the location name based on latitude and longitude and vice versa
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class AppLocationManager {

    private final Context mContext;

    public AppLocationManager(Context context) {
        mContext = context;
    }

    public Observable<Location> getLastKnownLocation() {
        return LastKnownLocationObservable.createObservable(mContext);
    }

    public Observable<Location> getUpdatedLocation() {
        return LocationUpdatesObservable.createObservable(mContext);
    }

    public Observable<List<Address>> getReverseGeocode(double latitude, double longitude,
            int maxResults) {
        return ReverseGeocodeObservable.createObservable(mContext, latitude, longitude, maxResults);
    }

    public Observable<List<Address>> getGeocode(String locationName, int maxResults) {
        return GeocodeObservable.createObservable(mContext, locationName, maxResults);
    }
}
