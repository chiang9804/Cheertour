package org.csie.cheertour.cheertour.Favorite;

/**
 * Created by rose-pro on 15/12/15.
 */
public class FavoriteListItem {
    private String location_name;
    private long location_id;
    private String[] img_url_list;
    private String category;

    public FavoriteListItem(String location_name, long location_id, String[] img_url_list, String category) {
        this.location_name = location_name;
        this.location_id = location_id;
        this.img_url_list = img_url_list;
        this.category = category;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public void setLocation_id(long location_id) {
        this.location_id = location_id;
    }

    public void setImg_url_list(String[] img_url_list) {
        this.img_url_list = img_url_list;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation_name() {
        return location_name;
    }

    public long getLocation_id() {
        return location_id;
    }

    public String[] getImg_url_list() {
        return img_url_list;
    }

    public String getCategory() {
        return category;
    }
}
