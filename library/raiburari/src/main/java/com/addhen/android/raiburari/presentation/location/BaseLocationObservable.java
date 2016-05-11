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

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.util.Log;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import timber.log.Timber;

/**
 * Base class to
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
// Implementing app should request for the needed permissions
@SuppressWarnings("MissingPermission")
public abstract class BaseLocationObservable<T> implements Observable.OnSubscribe<T> {

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 30; // 30 meters

    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 2; // 2 minutes

    protected LocationManager mLocationManager;

    private final Context mContext;

    private LocationListener mLocationListener;


    protected BaseLocationObservable(Context context) {
        mContext = context;
    }

    protected abstract void onLocationFixed(Observer<? super T> observer);

    public void setLocationListener(LocationListener locationListener) {
        mLocationListener = locationListener;
    }

    @Override
    public void call(Subscriber<? super T> subscriber) {
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        onLocationFixed(subscriber);
    }

    protected boolean isGPSEnabled() {
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    protected boolean isNetworkEnabled() {
        return mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    protected Location getLasKnowLocation(Observer<? super T> observer) {
        Location location = null;
        try {
            if (isNetworkEnabled()) {
                location = mLocationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Timber.i("NETWORK_PROVIDER %s", "Enabled");
            }

            if (isGPSEnabled() && location == null) {
                location = mLocationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Timber.i("GPS_PROVIDER %s", "Enabled");
            }
        } catch (Exception e) {
            observer.onError(e);
        }
        return location;
    }

    protected void getLocationUpdates(Observer<? super T> observer) {
        if (mLocationListener == null) {
            throw new IllegalArgumentException("LocationListener cannot be null");
        }
        try {
            if (isNetworkEnabled()) {
                mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, mLocationListener, Looper.getMainLooper());
                Timber.i("NETWORK_PROVIDER %s", "Enabled");
                return;
            }
            if (isGPSEnabled()) {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, mLocationListener, Looper.getMainLooper());
                Timber.i("GPS_PROVIDER %s", "Enabled");
            }
        } catch (Exception e) {
            observer.onError(e);
        }
    }

    public void stopLocating() {
        if (mLocationManager != null) {
            try {
                mLocationManager.removeUpdates(mLocationListener);
            } catch (Exception ex) {
                Log.e(getClass().getSimpleName(), "stopLocating", ex);
            }
            mLocationManager = null;
        }
    }
}
