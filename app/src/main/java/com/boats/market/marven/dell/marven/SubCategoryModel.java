package com.boats.market.marven.dell.marven;

import java.util.ArrayList;

/**
 * Created by dell on 7/13/2019.
 */

public class SubCategoryModel {
String name , url ;

public SubCategoryModel (){}

    public SubCategoryModel(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
