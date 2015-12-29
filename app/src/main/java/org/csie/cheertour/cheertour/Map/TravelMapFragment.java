package org.csie.cheertour.cheertour.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.google.maps.android.ui.IconGenerator;

import org.csie.cheertour.cheertour.JSONParser;
import org.csie.cheertour.cheertour.Location.LocationInfoAcvtivity;
import org.csie.cheertour.cheertour.R;
import org.csie.cheertour.cheertour.util.SingleShotLocationProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import static org.csie.cheertour.cheertour.ConstantVariables.GET_LOCATION_URL;
import static org.csie.cheertour.cheertour.ConstantVariables.TAG_MAP;


/**
 * Created by rose-pro on 2015/7/12.
 */
public class TravelMapFragment extends Fragment{
    private View rootView;
    private MapView mapView;
    private GoogleMap map;
    private HashMap <Marker, MapMarkerData> markerDataMap;
    UiSettings uiSettings;

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
        markerDataMap = new HashMap<Marker, MapMarkerData>();

        return rootView;
    }

    @Override
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
        // Default position
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(25.02, 121.38), 15));
        SingleShotLocationProvider.requestSingleUpdate(activity,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override
                    public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates
                                                               location) {
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        // new LatLng(location.latitude, location.longitude), 15)
                                        // johntsai (2015/12/22): It's weird that I need to exchange the
                                        // order of lat and lng
                                        new LatLng(location.longitude, location.latitude), 15)
                        );
                    }
                });

        // Set listeners
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Get corresponding markerData
                MapMarkerData data = markerDataMap.get(marker);
                marker.setSnippet(data.getName());
                // Open the location info View
                Context context = getActivity();
                Intent intent = new Intent(context, LocationInfoAcvtivity.class);
                Bundle b = new Bundle();
                b.putLong("id", data.getID());
                intent.putExtras(b);
                context.startActivity(intent);
                return false;
                //TODO: Test it
            }
        });
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
                LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
                loadMapLocation(bounds);
            }
        });
    }
    private void loadMapLocation(LatLngBounds bounds) {
        // Default number = 20 locations
        loadMapLocation(bounds, 20);
    }
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

    private class LoadMapLocation extends AsyncTask<String, Void, JSONArray> {
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
            return jsonArray;
        }
        protected void onPostExecute(JSONArray markerJSONArray) {
            try {
                // Clear map markers
                map.clear();
                // Add markers
                for (int i = 0; i < markerJSONArray.length(); ++i) {
                    JSONObject json = markerJSONArray.getJSONObject(i);
                    addMarker(json.getInt("location_id"),
                            json.getString("location_name"), json.getString("category"),
                            json.getDouble("lat"), json.getDouble("lon")
                    );
                }
            } catch (JSONException e) {
                Log.e(TAG_MAP, "get location json error.");
                e.printStackTrace();
            }
        }
    }

    /**
     * Add a marker onto the map. And save the markerData to markerDataMap.
     * @param id        location id
     * @param name      location name.
     * @param category  category ("food", "other")
     * @param latlng    LatLng
     * @return          The marker
     */
    private Marker addMarker(int id, String name, String category, LatLng latlng){
        IconGenerator iconGenerator = new IconGenerator( this.getActivity() );
        Bitmap iconBitmap = iconGenerator.makeIcon(name);
        Marker marker = map.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(iconBitmap))
                        .position(latlng)
        );
        MapMarkerData markerData = new MapMarkerData(id, name, category, latlng);
        markerDataMap.put(marker, markerData);

        return marker;
    }

    /**
     * Add a marker onto the map. And save the markerData to markerDataMap.
     * @param id        location id
     * @param name      location name.
     * @param category  category ("food", "other")
     * @param lat       latitude
     * @param lng       longitude
     * @return          The marker
     */
    private Marker addMarker(int id, String name, String category, double lat, double lng){
        return addMarker(id, name, category, new LatLng(lat, lng) );
    }
}
