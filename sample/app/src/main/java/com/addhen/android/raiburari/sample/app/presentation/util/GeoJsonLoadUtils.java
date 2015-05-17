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

package com.addhen.android.raiburari.sample.app.presentation.util;

import com.addhen.android.raiburari.sample.app.presentation.ui.widget.CustomMarker;
import com.cocoahero.android.geojson.Feature;
import com.cocoahero.android.geojson.FeatureCollection;
import com.cocoahero.android.geojson.GeoJSON;
import com.cocoahero.android.geojson.Geometry;
import com.cocoahero.android.geojson.GeometryCollection;
import com.cocoahero.android.geojson.LineString;
import com.cocoahero.android.geojson.MultiLineString;
import com.cocoahero.android.geojson.MultiPoint;
import com.cocoahero.android.geojson.MultiPolygon;
import com.cocoahero.android.geojson.Point;
import com.cocoahero.android.geojson.Polygon;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Icon;
import com.mapbox.mapboxsdk.overlay.PathOverlay;
import com.mapbox.mapboxsdk.util.NetworkUtils;
import com.mapbox.mapboxsdk.util.constants.UtilConstants;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Locale;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class GeoJsonLoadUtils {

    /**
     * Load GeoJSON from URL (in synchronous manner) and return GeoJSON FeatureCollection
     *
     * @param url URL of GeoJSON data
     * @return Remote GeoJSON parsed into Library objects
     */
    public static FeatureCollection loadGeoJSONFromUrl(final String url) throws IOException,
            JSONException {
        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException("No GeoJSON URL passed in.");
        }

        if (UtilConstants.DEBUGMODE) {
            Log.d(GeoJsonLoadUtils.class.getCanonicalName(),
                    "Mapbox SDK downloading GeoJSON URL: " + url);
        }

        InputStream is;
        if (url.toLowerCase(Locale.US).indexOf("http") == 0) {
            is = NetworkUtils.getHttpURLConnection(new URL(url)).getInputStream();
        } else {
            is = new URL(url).openStream();
        }
        BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String jsonText = readAll(rd);

        FeatureCollection parsed = (FeatureCollection) GeoJSON.parse(jsonText);
        if (UtilConstants.DEBUGMODE) {
            Log.d(GeoJsonLoadUtils.class.getCanonicalName(),
                    "Parsed GeoJSON with " + parsed.getFeatures().size() + " features.");
        }

        return parsed;
    }

    /**
     * Load GeoJSON from URL (in synchronous manner) and return GeoJSON FeatureCollection
     *
     * @param context  Application's Context
     * @param fileName Name of file in assets directory
     * @return Local GeoJSON file parsed into Library objects
     */
    public static FeatureCollection loadGeoJSONFromAssets(final Context context,
            final String fileName) throws IOException, JSONException {
        if (TextUtils.isEmpty(fileName)) {
            throw new NullPointerException("No GeoJSON File Name passed in.");
        }

        if (UtilConstants.DEBUGMODE) {
            Log.d(GeoJsonLoadUtils.class.getCanonicalName(),
                    "Mapbox SDK loading GeoJSON URL: " + fileName);
        }

        InputStream is = context.getAssets().open(fileName);
        BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String jsonText = readAll(rd);

        FeatureCollection parsed = (FeatureCollection) GeoJSON.parse(jsonText);
        if (UtilConstants.DEBUGMODE) {
            Log.d(GeoJsonLoadUtils.class.getCanonicalName(),
                    "Parsed GeoJSON with " + parsed.getFeatures().size() + " features.");
        }

        return parsed;
    }

    public static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    /**
     * Converts GeoJSON objects into Mapbox SDK UI Objects
     *
     * @param featureCollection Parsed GeoJSON Objects
     * @param markerIcon        Optional Icon to use for markers
     * @return Collection of Mapbox SDK UI Objects
     */
    public static ArrayList<Object> createUIObjectsFromGeoJSONObjects(
            final FeatureCollection featureCollection, final Icon markerIcon) throws JSONException {
        ArrayList<Object> uiObjects = new ArrayList<>();

        for (Feature f : featureCollection.getFeatures()) {
            // Parse Into UI Objections
            int j;

            if (f.getGeometry() instanceof Point) {
                JSONArray coordinates = (JSONArray) f.getGeometry().toJSON().get("coordinates");
                double lon = (Double) coordinates.get(0);
                double lat = (Double) coordinates.get(1);
                CustomMarker marker = new CustomMarker(
                        f.getProperties().optString("title"),
                        f.getProperties().optString("description"), new LatLng(lat, lon));
                if (markerIcon != null) {
                    marker.setIcon(markerIcon);
                }
                uiObjects.add(marker);
            } else if (f.getGeometry() instanceof MultiPoint) {
                JSONArray points = (JSONArray) f.getGeometry().toJSON().get("coordinates");
                for (j = 0; j < points.length(); j++) {
                    JSONArray coordinates = (JSONArray) points.get(j);
                    double lon = (Double) coordinates.get(0);
                    double lat = (Double) coordinates.get(1);
                    CustomMarker marker = new CustomMarker(f.getProperties().optString("title"),
                            f.getProperties().optString("description"), new LatLng(lat, lon));
                    if (markerIcon != null) {
                        marker.setIcon(markerIcon);
                    }
                    uiObjects.add(marker);
                }
            } else if (f.getGeometry() instanceof LineString) {
                PathOverlay path = new PathOverlay();
                JSONArray points = (JSONArray) f.getGeometry().toJSON().get("coordinates");
                JSONArray coordinates;
                for (j = 0; j < points.length(); j++) {
                    coordinates = (JSONArray) points.get(j);
                    double lon = (Double) coordinates.get(0);
                    double lat = (Double) coordinates.get(1);
                    path.addPoint(new LatLng(lat, lon));
                }
                uiObjects.add(path);
            } else if (f.getGeometry() instanceof MultiLineString) {
                JSONArray lines = (JSONArray) f.getGeometry().toJSON().get("coordinates");
                for (int k = 0; k < lines.length(); k++) {
                    PathOverlay path = new PathOverlay();
                    JSONArray points = (JSONArray) lines.get(k);
                    JSONArray coordinates;
                    for (j = 0; j < points.length(); j++) {
                        coordinates = (JSONArray) points.get(j);
                        double lon = (Double) coordinates.get(0);
                        double lat = (Double) coordinates.get(1);
                        path.addPoint(new LatLng(lat, lon));
                    }
                    uiObjects.add(path);
                }
            } else if (f.getGeometry() instanceof Polygon) {
                PathOverlay path = new PathOverlay();
                path.getPaint().setStyle(Paint.Style.FILL);
                JSONArray points = (JSONArray) f.getGeometry().toJSON().get("coordinates");

                for (int r = 0; r < points.length(); r++) {
                    JSONArray ring = (JSONArray) points.get(r);
                    JSONArray coordinates;

                    // we re-wind inner rings of GeoJSON polygons in order
                    // to render them as transparent in the canvas layer.

                    // first ring should have windingOrder = true,
                    // all others should have winding order == false
                    if ((r == 0 && !windingOrder(ring)) || (r != 0 && windingOrder(ring))) {
                        for (j = 0; j < ring.length(); j++) {
                            coordinates = (JSONArray) ring.get(j);
                            double lon = (Double) coordinates.get(0);
                            double lat = (Double) coordinates.get(1);
                            path.addPoint(new LatLng(lat, lon));
                        }
                    } else {
                        for (j = ring.length() - 1; j >= 0; j--) {
                            coordinates = (JSONArray) ring.get(j);
                            double lon = (Double) coordinates.get(0);
                            double lat = (Double) coordinates.get(1);
                            path.addPoint(new LatLng(lat, lon));
                        }
                    }
                    uiObjects.add(path);
                }
            } else if (f.getGeometry() instanceof MultiPolygon) {
                PathOverlay path = new PathOverlay();
                path.getPaint().setStyle(Paint.Style.FILL);
                JSONArray polygons = (JSONArray) f.getGeometry().toJSON().get("coordinates");

                for (int p = 0; p < polygons.length(); p++) {
                    JSONArray points = (JSONArray) polygons.get(p);
                    for (int r = 0; r < points.length(); r++) {
                        JSONArray ring = (JSONArray) points.get(r);
                        JSONArray coordinates;

                        // we re-wind inner rings of GeoJSON polygons in order
                        // to render them as transparent in the canvas layer.

                        // first ring should have windingOrder = true,
                        // all others should have winding order == false
                        if ((r == 0 && !windingOrder(ring)) || (r != 0 && windingOrder(ring))) {
                            for (j = 0; j < ring.length(); j++) {
                                coordinates = (JSONArray) ring.get(j);
                                double lon = (Double) coordinates.get(0);
                                double lat = (Double) coordinates.get(1);
                                path.addPoint(new LatLng(lat, lon));
                            }
                        } else {
                            for (j = ring.length() - 1; j >= 0; j--) {
                                coordinates = (JSONArray) ring.get(j);
                                double lon = (Double) coordinates.get(0);
                                double lat = (Double) coordinates.get(1);
                                path.addPoint(new LatLng(lat, lon));
                            }
                        }
                        uiObjects.add(path);
                    }
                }
            } else if (f.getGeometry() instanceof GeometryCollection) {
                GeometryCollection geometryCollection = (GeometryCollection) f.getGeometry();

                for (Geometry geometry : geometryCollection.getGeometries()) {
                    JSONArray coordinates = (JSONArray) geometry.toJSON().get("coordinates");
                    double lon = (Double) coordinates.get(0);
                    double lat = (Double) coordinates.get(1);
                    CustomMarker marker = new CustomMarker(f.getProperties().optString("title"),
                            f.getProperties().optString("description"), new LatLng(lat, lon));
                    marker.markerId = f.getProperties().getLong("id");
                    if (markerIcon != null) {
                        marker.setIcon(markerIcon);
                    }
                    uiObjects.add(marker);
                }
            }
        }

        return uiObjects;
    }

    private static boolean windingOrder(JSONArray ring) throws JSONException {
        float area = 0;

        if (ring.length() > 2) {
            for (int i = 0; i < ring.length() - 1; i++) {
                JSONArray p1 = (JSONArray) ring.get(i);
                JSONArray p2 = (JSONArray) ring.get(i + 1);
                area += rad((Double) p2.get(0) - (Double) p1.get(0)) * (2 + Math.sin(
                        rad((Double) p1.get(1))) + Math.sin(rad((Double) p2.get(1))));
            }
        }

        return area > 0;
    }

    private static double rad(double _) {
        return _ * Math.PI / 180f;
    }
}
