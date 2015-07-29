package org.csie.cheertour.cheertour.Rank;

/**
 * Created by rose-pro on 2015/7/29.
 */
public class RankListItem {
    private String location_name;
    private String img_url;
    private Boolean isFavorite;
    public static final int TYPE_SCENCE = 0;
    public static final int TYPE_FOOD = 1;

    public Long getLocation_id() {
        return location_id;
    }

    public void setLocation_id(Long location_id) {
        this.location_id = location_id;
    }

    private Long location_id;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type;
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public RankListItem(String location_name, String img_url,  Boolean isFavorite, String category, Long location_id){
        this.location_name = location_name;
        this.img_url = img_url;
        this.isFavorite = isFavorite;
        this.category = category;
        if(category.equals("food")){
            setType(TYPE_FOOD);
        } else {
            setType(TYPE_SCENCE);
        }
        this.location_id = location_id;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    @Override
    public String toString() {
        return "name:"+location_name+", img:"+img_url;
    }
}
