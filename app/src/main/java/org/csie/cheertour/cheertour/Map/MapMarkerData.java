package org.csie.cheertour.cheertour.Map;

/**
 * Created by johntsai on 2015/11/29.
 */
public class MapMarkerData {
    private int id;
    private String name;
    private String category;

    public MapMarkerData(int id, String name, String category){
        setID(id);
        setName(name);
        setCategory(category);
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
}
