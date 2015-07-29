package org.csie.cheertour.cheertour.Search;

/**
 * Created by rose-pro on 2015/6/15.
 */
public class RowItem {
    private int imageId;
    private String text;
    public static final int TYPE_CT_LOCATION = 0;
    public static final int TYPE_GOOGLE_PLACE = 1;
    private static final String typeString[] = {"(Cheertour location)","(Google Place)"};
    private int type;

    public RowItem(int imageId, String text, int type){
        this.imageId = imageId;
        this.text = text;
        this.type = type;
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
    public int getType(){
        return type;
    }
    public void setType(int type){
        this.type = type;
    }
    @Override
    public String toString() {
        return text;
    }
}
