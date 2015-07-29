package org.csie.cheertour.cheertour.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;

import org.csie.cheertour.cheertour.R;


/**
 * Created by rose-pro on 2015/7/12.
 */
public class TravelMapFragment extends Fragment {
    private View rootView;
    private MapView mapView;
    private GoogleMap map;
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
