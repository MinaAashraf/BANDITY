package com.boats.market.marven.dell.marven;

/**
 * Created by dell on 6/30/2020.
 */

public class Location {
    String lat , lang ;
    public Location(){}

    public Location(String lat, String lang) {
        this.lat = lat;
        this.lang = lang;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
