package com.boats.market.marven.dell.marven;

/**
 * Created by dell on 6/30/2020.
 */

public class DeliveringLocation {
    double latitude ,longitude;

   public DeliveringLocation (){}

    public DeliveringLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
