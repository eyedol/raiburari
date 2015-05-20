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

package com.addhen.android.raiburari.sample.app.presentation.presenter;

import com.addhen.android.raiburari.presentation.presenter.Presenter;
import com.addhen.android.raiburari.sample.app.presentation.ui.view.MapsView;
import com.addhen.android.raiburari.sample.app.presentation.util.GeoJsonLoadUtils;
import com.cocoahero.android.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.overlay.Icon;

import org.json.JSONException;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class MapPresenter implements Presenter {

    private MapsView mMapsView;

    @Inject
    public MapPresenter() {

    }

    public void setView(@NonNull MapsView mapsView) {
        mMapsView = mapsView;
    }

    @Override
    public void resume() {
        loadGeoJson();
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    private void loadGeoJson() {
        try {
            Icon icon = new Icon(mMapsView.getAppContext(), Icon.Size.LARGE, "town-hall", "FF0000");
            FeatureCollection features = GeoJsonLoadUtils
                    .loadGeoJSONFromAssets(mMapsView.getAppContext(), "geojson/geojsons.json");
            ArrayList<Object> uiObjects = GeoJsonLoadUtils.createUIObjectsFromGeoJSONObjects(
                    features, icon);
            mMapsView.renderGeoJson(uiObjects);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
