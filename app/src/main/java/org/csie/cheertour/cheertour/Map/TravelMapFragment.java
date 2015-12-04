package org.csie.cheertour.cheertour.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import org.csie.cheertour.cheertour.Location.LocationInfoAcvtivity;
import org.csie.cheertour.cheertour.R;

import java.util.HashMap;


/**
 * Created by rose-pro on 2015/7/12.
 */
public class TravelMapFragment extends Fragment{
    private View rootView;
    private MapView mapView;
    private GoogleMap map;
    private UiSettings uiSettings;
    private HashMap <Marker, MapMarkerData> markerDataMap;
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

        // Add a marker and set map center to its position
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(25.02, 121.38), 15));

        addMarker(123, "Cheertour", "other", 25.02, 121.38);

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
            }
        });
    }
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
    private Marker addMarker(int id, String name, String category, double lat, double lng){
        return addMarker(id, name, category, new LatLng(lat, lng) );
    }
}
