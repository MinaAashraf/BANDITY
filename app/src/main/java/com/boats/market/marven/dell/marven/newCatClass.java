package com.boats.market.marven.dell.marven;

import java.util.ArrayList;

/**
 * Created by dell on 8/28/2019.
 */

public class newCatClass {

    ArrayList <SubCategoryModel> subCat = new ArrayList<>();
    UrlAndPublish urlAndPublish ;

    public newCatClass(ArrayList<SubCategoryModel> subCat, UrlAndPublish urlAndPublish) {
        this.subCat = subCat;
        this.urlAndPublish = urlAndPublish;
    }

    public newCatClass (){}

    public ArrayList<SubCategoryModel> getSubCat() {
        return subCat;
    }

    public void setSubCat(ArrayList<SubCategoryModel> subCat) {
        this.subCat = subCat;
    }

    public UrlAndPublish getUrlAndPublish() {
        return urlAndPublish;
    }

    public void setUrlAndPublish(UrlAndPublish urlAndPublish) {
        this.urlAndPublish = urlAndPublish;
    }
}
