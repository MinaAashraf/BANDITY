package com.boats.market.marven.dell.marven;

import java.util.ArrayList;

/**
 * Created by dell on 8/1/2019.
 */

public class CategoryModel {

    ArrayList <ProductModel> arrayList = new ArrayList<>();
   UrlAndPublish urlAndPublish;
public CategoryModel (){}

    public CategoryModel(ArrayList<ProductModel> arrayList, UrlAndPublish urlAndPublish) {
        this.arrayList = arrayList;
        this.urlAndPublish = urlAndPublish;
    }

    public ArrayList<ProductModel> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<ProductModel> arrayList) {
        this.arrayList = arrayList;
    }

    public UrlAndPublish getUrlAndPublish() {
        return urlAndPublish;
    }

    public void setUrlAndPublish(UrlAndPublish urlAndPublish) {
        this.urlAndPublish = urlAndPublish;
    }
}
