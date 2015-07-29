package org.csie.cheertour.cheertour.Search;

/**
 * Created by rose-pro on 2015/6/16.
 */
public class GooglePlace {
    private String description;
    private String place_id;
    public GooglePlace(String description, String place_id){
        this.description = description;
        this.place_id = place_id;
    }
    public String getDescription(){
        return description;
    }
    public String getPlace_id(){
        return place_id;
    }

}
