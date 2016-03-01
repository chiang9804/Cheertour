package org.csie.cheertour.cheertour.Recommend;

import org.json.JSONArray;

/**
 * Created by rose-pro on 2015/7/27.
 */
public class RecommendListItem {
    private String location_name;
    private String location_description;
    private String img_url;
    private JSONArray img_urls;
    private String rank; // 打卡數
    private Boolean isFavorite;


    public long getLocation_id() {
        return location_id;
    }

    public void setLocation_id(long location_id) {
        this.location_id = location_id;
    }

    private long location_id;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private String category;

    public RecommendListItem(String location_name,String location_description, String img_url,JSONArray img_urls, String rank, Boolean isFavorite, String category, long location_id){
        this.location_name = location_name;
        this.location_description = location_description;
        this.img_url = img_url;
        this.img_urls = img_urls;
        this.rank = rank;
        this.isFavorite = isFavorite;
        this.category = category;
        this.location_id = location_id;
    }

    public RecommendListItem(String location_name,String location_description, String img_url,JSONArray img_urls, int rank, Boolean isFavorite, String category, long location_id){
        this.location_name = location_name;
        this.location_description = location_description;
        this.img_url = img_url;
        this.img_urls = img_urls;
        if(rank < 10) {
            this.rank = "  "+Integer.toString(rank);
        } else if (rank < 100){
            this.rank = " "+Integer.toString(rank);
        } else {
            this.rank = Integer.toString(rank);
        }
        this.isFavorite = isFavorite;
        this.category = category;
        this.location_id = location_id;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getLocation_description() {
        return location_description;
    }

    public void setLocation_description(String location_description) {
        this.location_description = location_description;
    }

    public String getImg_url() {
        return img_url;
    }
    public JSONArray getImg_urls(){
        return img_urls;
    }
    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    @Override
    public String toString() {
        return "name:"+location_name+", img:"+img_url+", rank:"+rank;
    }
}
