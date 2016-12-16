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

package com.addhen.android.raiburari.presentation.location.geocoder;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ReverseGeocodeObservable implements ObservableOnSubscribe<List<Address>> {

  private final Context mContext;

  private final double mLatitude;

  private final double mLongitude;

  private final int mMaxResults;

  private ReverseGeocodeObservable(Context context, double latitude, double longitude,
      int maxResults) {
    mContext = context;
    mLatitude = latitude;
    mLongitude = longitude;
    mMaxResults = maxResults;
  }

  public static Observable<List<Address>> createObservable(Context ctx, double latitude,
      double longitude, int maxResults) {
    return Observable.create(new ReverseGeocodeObservable(ctx, latitude, longitude, maxResults));
  }

  @Override public void subscribe(ObservableEmitter<List<Address>> subscriber) {
    Geocoder geocoder = new Geocoder(mContext);
    List<Address> result = new ArrayList<>();
    try {
      result = geocoder.getFromLocation(mLatitude, mLongitude, mMaxResults);
    } catch (IOException e) {
      if (!subscriber.isDisposed()) {
        subscriber.onError(e);
      }
    }
    if (!subscriber.isDisposed()) {
      subscriber.onNext(result);
      subscriber.onComplete();
    }
  }
}
