package com.boats.market.marven.dell.marven;

/**
 * Created by dell on 7/30/2019.
 */

public class UrlAndPublish {
    String url ,description; boolean publish ;


    UrlAndPublish (){}
    public UrlAndPublish(String url ) {
        this.url = url;
        this.description = description;
        this.publish = false;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isPublish() {
        return publish;
    }

    public void setPublish(boolean publish) {
        this.publish = publish;
    }
}
