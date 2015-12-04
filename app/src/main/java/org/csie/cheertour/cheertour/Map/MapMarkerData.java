package org.csie.cheertour.cheertour.Map;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by johntsai on 2015/11/29.
 */
public class MapMarkerData {
    private int id;
    private String name;
    private String category;
    private LatLng latlng;

    public MapMarkerData(int id){
        setID(id);
    }
    public MapMarkerData(int id, String name, String category, LatLng latlng){
        setID(id);
        setName(name);
        setCategory(category);
        setLatLng(latlng);
    }
    public MapMarkerData(int id, String name, String category, double lat, double lng){
        this(id, name, category, new LatLng(lat, lng) );
    }

    public void setID(int id){
        this.id = id;
    }
    public int getID(){
        return this.id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setCategory(String category){
        this.category = category;
    }
    public String getCategory(){
        return this.category;
    }
    public void setLatLng(LatLng latlng){
        this.latlng = latlng;
    }
    public void setLatLng(float lat, float lng){
        this.latlng = new LatLng(lat, lng);
    }
    public LatLng getLatLng(){
        return this.latlng;
    }
}
