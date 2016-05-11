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

import rx.Observable;
import rx.Observer;

/**
 * Fetches the users known last location
 */
public class LastKnownLocationObservable extends BaseLocationObservable<Location> {

    private final Context mContext;

    protected LastKnownLocationObservable(Context context) {
        super(context);
        mContext = context;
    }

    public static Observable<Location> createObservable(Context ctx) {
        return Observable.create(new LastKnownLocationObservable(ctx));
    }

    @Override
    protected void onLocationFixed(Observer<? super Location> observer) {
        Location location = getLasKnowLocation(observer);
        if (location != null) {
            observer.onNext(location);
        } else {
            observer.onError(new Exception(mContext.getString(R.string.no_location_found)));
        }
        observer.onCompleted();
    }
}
