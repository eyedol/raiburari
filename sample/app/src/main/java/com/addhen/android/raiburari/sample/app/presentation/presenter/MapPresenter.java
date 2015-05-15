package com.addhen.android.raiburari.sample.app.presentation.presenter;

import com.addhen.android.raiburari.presentation.presenter.Presenter;
import com.addhen.android.raiburari.sample.app.presentation.ui.view.MapsView;
import com.addhen.android.raiburari.sample.app.presentation.util.GeoJsonLoadUtils;
import com.cocoahero.android.geojson.FeatureCollection;

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
            FeatureCollection features = GeoJsonLoadUtils
                    .loadGeoJSONFromAssets(mMapsView.getAppContext(), "geojson/geojsons.json");
            ArrayList<Object> uiObjects = GeoJsonLoadUtils.createUIObjectsFromGeoJSONObjects(
                    features, null);
            mMapsView.renderGeoJson(uiObjects);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
