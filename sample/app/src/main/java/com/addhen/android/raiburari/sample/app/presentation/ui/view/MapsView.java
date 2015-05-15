package com.addhen.android.raiburari.sample.app.presentation.ui.view;

import com.addhen.android.raiburari.presentation.ui.view.LoadDataView;

import java.util.ArrayList;

/**
 * @author Henry Addo
 */
public interface MapsView extends LoadDataView {

    void renderGeoJson(ArrayList<Object> uiObjects);
}
