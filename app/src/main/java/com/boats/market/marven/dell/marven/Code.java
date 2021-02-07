package com.boats.market.marven.dell.marven;

/**
 * Created by dell on 8/18/2019.
 */

public class Code {
    String code  ;
    int value;
    int discountType;
    String scope ;

    public Code (){}
    public Code(String code, int value, int discountType, String scope) {
        this.code = code;
        this.value = value;
        this.discountType = discountType;
        this.scope = scope;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getDiscountType() {
        return discountType;
    }

    public void setDiscountType(int discountType) {
        this.discountType = discountType;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
