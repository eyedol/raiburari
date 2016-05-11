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

import com.addhen.android.raiburari.R;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import rx.Observable;
import rx.Observer;

public class LocationUpdatesObservable extends BaseLocationObservable<Location> {

    private final Context mContext;

    protected LocationUpdatesObservable(Context context) {
        super(context);
        mContext = context;
    }

    public static Observable<Location> createObservable(Context context) {
        return Observable.create(new LocationUpdatesObservable(context));
    }

    @Override
    protected void onLocationFixed(final Observer<? super Location> observer) {
        setLocationListener(new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    observer.onNext(location);
                    stopLocating();
                    observer.onCompleted();
                } else {
                    observer.onError(new Exception(mContext.getString(R.string.no_location_found)));
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // No-op
            }

            @Override
            public void onProviderEnabled(String provider) {
                // No-op
            }

            @Override
            public void onProviderDisabled(String provider) {
                // No-op
            }
        });
        getLocationUpdates(observer);
    }
}
