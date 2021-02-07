package com.boats.market.marven.dell.marven;

import java.io.Serializable;

/**
 * Created by dell on 10/19/2019.
 */

public class Rating implements Serializable {

    float rate ;
    int rateNum;

    public Rating ()
    {

    }

    public Rating(float rate, int rateNum) {
        this.rate = rate;
        this.rateNum = rateNum;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getRateNum() {
        return rateNum;
    }

    public void setRateNum(int rateNum) {
        this.rateNum = rateNum;
    }
}
