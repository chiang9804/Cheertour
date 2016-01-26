package org.csie.cheertour.cheertour.Map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import org.csie.cheertour.cheertour.JSONParser;
import org.csie.cheertour.cheertour.R;
import org.csie.cheertour.cheertour.util.SingleShotLocationProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import static org.csie.cheertour.cheertour.ConstantVariables.GET_LOCATION_URL;
import static org.csie.cheertour.cheertour.ConstantVariables.TAG_MAP;
import static org.csie.cheertour.cheertour.Location.LocationInfoAcvtivity.openLocationInfo;


/**
 * Created by rose-pro on 2015/7/12.
 */
public class TravelMapFragment extends Fragment{
    private View rootView;
    private MapView mapView;
    private GoogleMap map;
    UiSettings uiSettings;
    private ClusterManager<MapMarker> mClusterManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(container==null)
            return null;
        rootView = inflater.inflate(R.layout.fragment_travel_map, container,false);

        mapView = (MapView) rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        try{
            MapsInitializer.initialize(getActivity());
        } catch (Exception e){
            e.printStackTrace();
        }

        setMap(mapView);

        return rootView;
    }

    @Override
    // TODO: onReseme()
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setMap(MapView mapView){
        map = mapView.getMap();

        // Show Zoom control
        uiSettings = map.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        // Set the center of the map to current position
        Activity activity = getActivity();
        // TODO: Decide a default position
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(25.04, 121.53), 15));
        // TODO: Ask for GPS position
        SingleShotLocationProvider.requestSingleUpdate(activity,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override
                    public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates
                                                               location) {
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        // new LatLng(location.latitude, location.longitude), 15)
                                        // johntsai (2015/12/22): It's weird that I need to exchange the
                                        // order of lat and lng
                                        new LatLng(location.latitude, location.longitude), 15)
                        );
                    }
                });

        setUpClusterer();
    }
    private void setUpClusterer() {
        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MapMarker>(getActivity(), map){
            // Set listeners
            /*
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.setSnippet(data.getName());
                // Open the location info View
                openLocationInfo(getActivity(), data.getID(), data.getName());
                return true;
            }
           */

            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                super.onCameraChange(cameraPosition);
                LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
                loadMapLocation(bounds);
                mClusterManager.cluster();
            }
        };
        // TODO: change the marker style
        mClusterManager.setRenderer(new MapMarkerRenderer());

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        map.setOnCameraChangeListener(mClusterManager);
        map.setOnMarkerClickListener(mClusterManager);
    }

    /**
     * Load 20 locations in map bounds and show on the map.
     * @param bounds bounds of the map
     */
    private void loadMapLocation(LatLngBounds bounds) {
        // Default number = 20 locations
        loadMapLocation(bounds, 20);
    }

    /**
     * Load locations in map bounds with number limit and show on the map.
     * @param bounds bounds of the map
     * @param number maximum number of locations to show.
     */
    private void loadMapLocation(LatLngBounds bounds, int number) {
        // Test Bound
        final String[] bound_param = {"up=" + bounds.northeast.latitude,
            "down=" + bounds.southwest.latitude,
            "left=" + bounds.southwest.longitude,
            "right=" + bounds.northeast.longitude
        };
        String number_param = "number=" + number;
        new LoadMapLocation().execute(bound_param[0], bound_param[1], bound_param[2],
                bound_param[3], number_param);
    }

    /**
     * Add a marker onto the map.
     * @param id        location id
     * @param name      location name.
     * @param category  category ("food", "other")
     * @param latlng    LatLng
     */
    private void addMarker(int id, String name, String category, LatLng latlng){
        // Add marker through the clusterManager
        mClusterManager.addItem(new MapMarker(latlng, id, name, category));
    }

    /**
     * Add a marker onto the map.
     * @param id        location id
     * @param name      location name.
     * @param category  category ("food", "other")
     * @param lat       latitude
     * @param lng       longitude
     */
    private void addMarker(int id, String name, String category, double lat, double lng){
        addMarker(id, name, category, new LatLng(lat, lng));
    }

    /** Private Classes **/
    private class LoadMapLocation extends AsyncTask<String, Void, JSONArray> {
        // TODO: Keep loaded locations instead of dropping them each drag action.
        // TODO: Prevent loading if the drag distance is too short.
        @Override
        protected JSONArray doInBackground(String... params) {
            JSONArray jsonArray = new JSONArray();
            try {
                // Create url
                String url = GET_LOCATION_URL
                        + TextUtils.join("&", params);
                Log.d(TAG_MAP, "get_location url:" + url);

                // Show all locations on map
                jsonArray = JSONParser.getJSONArrayFromURL(url);
            } catch (IOException e) {
                Log.e(TAG_MAP, "URL not found");
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e(TAG_MAP, "get location json error.");
                e.printStackTrace();
            }
            Log.d(TAG_MAP, "get json with length " + jsonArray.length());
            return jsonArray;
        }
        protected void onPostExecute(JSONArray markerJSONArray) {
            try {
                // Clear map markers
                mClusterManager.clearItems();
                // Add markers
                for (int i = 0; i < markerJSONArray.length(); ++i) {
                    JSONObject json = markerJSONArray.getJSONObject(i);
                    addMarker(json.getInt("location_id"),
                            json.getString("location_name"), json.getString("category"),
                            json.getDouble("lat"), json.getDouble("lon")
                    );
                }
                mClusterManager.cluster();  // Refresh the map to show markers.
            } catch (JSONException e) {
                Log.e(TAG_MAP, "get location json error.");
                e.printStackTrace();
            }
        }
    }
    private class MapMarkerRenderer extends DefaultClusterRenderer<MapMarker>{
        private final IconGenerator mIconGenerator = new IconGenerator(getActivity());
        private static final int MIN_CLUSTER_SIZE = 2;

        public MapMarkerRenderer() {
            super(getActivity(), map, mClusterManager);
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster<MapMarker> cluster) {
            return cluster.getSize() >= MIN_CLUSTER_SIZE;
        }

        /*
        @Override
        protected void onBeforeClusterItemRendered(MapMarker item, MarkerOptions markerOptions) {
            // Draw a single marker.
            // Set the info window to show location name.
            Bitmap iconBitmap = mIconGenerator.makeIcon("123");
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconBitmap));
        }*/
    }
}
