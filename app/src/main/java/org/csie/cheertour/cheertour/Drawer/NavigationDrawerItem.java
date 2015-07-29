package org.csie.cheertour.cheertour.Drawer;

/**
 * Created by rose-pro on 2015/6/20.
 */
public class NavigationDrawerItem {
    private int imageId;
    private String text;
    public NavigationDrawerItem(int imageId, String text){
        this.imageId = imageId;
        this.text = text;
    }
    public int getImageId(){
        return imageId;
    }
    public void setImageId(int imageId){
        this.imageId = imageId;
    }
    public String getText(){
        return text;
    }
    public void setText(String text){
        this.text = text;
    }

}
