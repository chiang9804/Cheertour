package org.csie.cheertour.cheertour.Location;

/**
 * Created by rose-pro on 2015/7/30.
 */
public class InstagramItem {
    private String content;
    private String image_url;
    private String instagram_url;

    public InstagramItem(String content, String image_url, String instagram_url) {
        this.content = content;
        this.image_url = image_url;
        this.instagram_url = instagram_url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getInstagram_url() {
        return instagram_url;
    }

    public void setInstagram_url(String instagram_url) {
        this.instagram_url = instagram_url;
    }

    @Override
    public String toString() {
        return content;
    }
}
