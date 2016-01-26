package org.csie.cheertour.cheertour.Map;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by johntsai on 2016/1/13.
 */
public class MapMarker implements ClusterItem {
    private final LatLng position;
    public final int id;
    public final String name;
    public final String category;

    public MapMarker(double lat, double lng, int id, String name, String category){
        this(new LatLng(lat, lng), id, name, category);
    }
    public MapMarker(LatLng latlng, int id, String name, String category){
        this.position = latlng;
        this.id = id;
        this.name = name;
        this.category = category;
    }
    /*
    public MapMarker(double lat, double lng){
        this(new LatLng(lat, lng));
    }
    public MapMarker(LatLng latlng){
        this.position = latlng;
    }*/

    @Override
    public LatLng getPosition() {
        return position;
    }
}
