package org.csie.cheertour.cheertour.Search;

/**
 * Created by rose-pro on 2015/6/16.
 */
public class CheertourLocation {
    private String category;
    private String location_name;
    private String location_id;
    private double lat;
    private double lng;

    public CheertourLocation(String category, String location_name, String location_id, double lat, double lng){
        this.category = category;
        this.location_name = location_name;
        this.location_id = location_id;
        this.lat = lat;
        this.lng = lng;
    }

    public String getCategory(){
        return category;
    }
    public String getLocation_name(){
        return location_name;
    }
    public String getLocation_id(){
        return location_id;
    }
    public double getLat(){
        return lat;
    }
    public double getLng(){
        return lng;
    }

}
