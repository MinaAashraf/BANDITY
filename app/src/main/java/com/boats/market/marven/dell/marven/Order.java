package com.boats.market.marven.dell.marven;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by dell on 6/25/2020.
 */

public class Order implements Serializable {
    ArrayList <SecondryProduct> arrayList ;
    AddressModel addressModel ;
    int status;
    String date;
    String price ;
    String paymentType ;
    String userId;
    public Order (){}



    public Order(ArrayList<SecondryProduct> arrayList, AddressModel addressModel, int status, String date, String price,String paymentType,String userId) {
        this.arrayList = arrayList;
        this.addressModel = addressModel;
        this.status = status;
        this.date = date;
        this.price = price;
        this.paymentType = paymentType;
        this.userId = userId;

    }

    public ArrayList<SecondryProduct> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<SecondryProduct> arrayList) {
        this.arrayList = arrayList;
    }

    public AddressModel getAddressModel() {
        return addressModel;
    }

    public void setAddressModel(AddressModel addressModel) {
        this.addressModel = addressModel;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getuserId() {
        return userId;
    }

    public void setuserId(String userId) {
        this.userId = userId;
    }
}
