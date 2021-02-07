package com.boats.market.marven.dell.marven;

import java.util.ArrayList;

/**
 * Created by dell on 8/1/2019.
 */

public class CategoryModel2 {
    ArrayList <SubCategoryModel> arrayList = new ArrayList<>();
   UrlAndPublish urlAndPublish;

    public CategoryModel2(){}

    public CategoryModel2(ArrayList<SubCategoryModel> arrayList, UrlAndPublish urlAndPublish) {
        this.arrayList = arrayList;
        this.urlAndPublish = urlAndPublish;
    }

    public ArrayList<SubCategoryModel> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<SubCategoryModel> arrayList) {
        this.arrayList = arrayList;
    }

    public UrlAndPublish getUrlAndPublish() {
        return urlAndPublish;
    }

    public void setUrlAndPublish(UrlAndPublish urlAndPublish) {
        this.urlAndPublish = urlAndPublish;
    }
}
