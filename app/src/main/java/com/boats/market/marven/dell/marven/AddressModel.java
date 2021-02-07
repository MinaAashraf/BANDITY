package com.boats.market.marven.dell.marven;

import java.io.Serializable;

/**
 * Created by dell on 7/1/2019.
 */

public class AddressModel implements Serializable {
    String name , building , street , area , floor ,apartment , state , phone , additional ;

    public AddressModel (){};

    public AddressModel(String name, String building, String street, String area, String floor, String apartment, String state, String phone, String additional) {
        this.name = name;
        this.building = building;
        this.street = street;
        this.area = area;
        this.floor = floor;
        this.apartment = apartment;
        this.state = state;
        this.phone = phone;
        this.additional = additional;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
