package com.boats.market.marven.dell.marven;

import java.io.Serializable;

/**
 * Created by dell on 6/25/2020.
 */

public class SecondryProduct implements Serializable {

    String name, url, price, totalPrice,color,size;
    int quantity;

    public SecondryProduct() {
    }

    public SecondryProduct(String name, String url, String price, String totalPrice , String color , String size, int quantity) {
        this.name = name;
        this.url = url;
        this.price = price;
        this.totalPrice = totalPrice;
        this.color = color;
        this.size = size;
        this.quantity = quantity;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
