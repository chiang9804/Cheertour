package org.csie.cheertour.cheertour.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Paint;
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
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.ui.IconGenerator;

import org.csie.cheertour.cheertour.R;


/**
 * Created by rose-pro on 2015/7/12.
 */
public class TravelMapFragment extends Fragment{
    private View rootView;
    private MapView mapView;
    private GoogleMap map;
    private UiSettings uiSettings;
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

        map = mapView.getMap();

        // Show Zoom control
        uiSettings = map.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        // Add a marker and set map center to its position
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(25.02, 121.38), 15));

        IconGenerator iconGenerator = new IconGenerator( this.getActivity() );
        Bitmap iconBitmap = iconGenerator.makeIcon("Cheertour");
        map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(iconBitmap))
                .position(new LatLng(25.02, 121.38))
                .title("visit cheertour.info now")
        );
        // map.setOnMarkerClickListener();

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
}
