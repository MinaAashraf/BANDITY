package com.boats.market.marven.dell.marven;

import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dell on 6/19/2019.
 */

public class ProductModel implements Serializable {
    String url, sort;
    long date;
    long negativedate;
    String sale, price;
    String totalprice = "0";
    int addicon = R.drawable.add_icon, itemnum = 1;
    ArrayList<String> urlimages = new ArrayList<>();
    ArrayList<String> sizes = new ArrayList<>();
    ArrayList<String> colores = new ArrayList<>();

    String description;
    String colorr, sizee;
    float price2;
    float negativeprice2;

    Rating rating = new Rating() ;
    String categoryName ;
    String subkey;
    public ProductModel() {
    }

    public ProductModel(String url, String sort, String sale, String price, ArrayList<String> urlimages, ArrayList<String> sizes, ArrayList<String> colores, String description, long date, long negativedate, float price2, float negativeprice2,String categoryName,String subkey,float rate , int ratingNum) {
        this.url = url;
        this.sort = sort;
        this.sale = sale;
        this.price = price;
        this.urlimages = urlimages;
        this.sizes = sizes;
        this.colores = colores;
        this.description = description;
        this.date = date;
        this.negativedate = negativedate;
        this.price2 = price2;
        this.negativeprice2 = negativeprice2;
        this.rating.setRate(rate);
        this.rating.setRateNum(ratingNum);
        this.categoryName = categoryName;
        this.subkey = subkey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSort() {
        return sort;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubkey() {
        return subkey;
    }

    public void setSubkey(String subkey) {
        this.subkey = subkey;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }



    public int getAddicon() {
        return addicon;
    }

    public void setItemnum(int itemnum) {
        this.itemnum = itemnum;
    }

    public ArrayList<String> getUrlimages() {
        return urlimages;
    }


    public ArrayList<String> getSizes() {
        return sizes;
    }


    public ArrayList<String> getColores() {
        return colores;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public int getItemnum() {
        return itemnum;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getColorr() {
        return colorr;
    }

    public void setColorr(String colorr) {
        this.colorr = colorr;
    }

    public String getSizee() {
        return sizee;
    }

    public void setSizee(String sizee) {
        this.sizee = sizee;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getNegativedate() {
        return negativedate;
    }

    public Rating getRating() {
        return rating;
    }

    public void setNegativedate(long negativedate) {
        this.negativedate = negativedate;
    }

    public float getPrice2() {
        return price2;
    }

    public void setPrice2(float price2) {
        this.price2 = price2;
    }

    public float getNegativeprice2() {
        return negativeprice2;
    }

    public void setNegativeprice2(float negativeprice2) {
        this.negativeprice2 = negativeprice2;
    }
}
