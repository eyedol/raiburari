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

package com.addhen.android.raiburari.sample.app.presentation.ui.fragment;

import com.addhen.android.raiburari.presentation.ui.fragment.BaseFragment;
import com.addhen.android.raiburari.sample.app.R;
import com.addhen.android.raiburari.sample.app.presentation.di.components.UserComponent;
import com.addhen.android.raiburari.sample.app.presentation.presenter.MapPresenter;
import com.addhen.android.raiburari.sample.app.presentation.ui.view.MapsView;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.overlay.PathOverlay;
import com.mapbox.mapboxsdk.views.MapView;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends BaseFragment implements MapsView {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @InjectView(R.id.mapview)
    MapView mMapView;

    @Inject
    MapPresenter mMapPresenter;

    public MapFragment() {
        super(R.layout.fragment_map, R.menu.menu_main);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    @Override
    public void renderGeoJson(ArrayList<Object> uiObjects) {
        for (Object obj : uiObjects) {
            if (obj instanceof Marker) {
                mMapView.addMarker((Marker) obj);
            } else if (obj instanceof PathOverlay) {
                mMapView.getOverlays().add((PathOverlay) obj);
            }
        }
        if (uiObjects.size() > 0) {
            mMapView.invalidate();
        }
    }

    private void initialize() {
        getComponent(UserComponent.class).inject(this);
        mMapPresenter.setView(this);
        mMapView.setClusteringEnabled(true, null, 18);
        mMapView.setMinZoomLevel(mMapView.getTileProvider().getMinimumZoomLevel());
        mMapView.setMaxZoomLevel(mMapView.getTileProvider().getMaximumZoomLevel());
        mMapView.setCenter(mMapView.getTileProvider().getCenterCoordinate());
        mMapView.setZoom(0);
        mMapView.setScrollableAreaLimit(mMapView.getTileProvider().getBoundingBox());
        mMapView.setZoom(mMapView.getTileProvider().getCenterZoom());
        mMapView.zoomToBoundingBox(mMapView.getTileProvider().getBoundingBox());
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapPresenter.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapPresenter.destroy();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public Context getAppContext() {
        return getActivity().getApplication();
    }
}
