package com.boats.market.marven.dell.marven;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dell on 6/22/2019.
 */

public class Users {
    String name, email, pass;
    int defaultAdress;


    public Users() {
    }

    ;

    public Users(String name, String email, String pass, int defaultAdres ) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.defaultAdress = defaultAdress;
    }


    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getDefaultAdress() {
        return defaultAdress;
    }

    public void setDefaultAdress(int defaultAdress) {
        this.defaultAdress = defaultAdress;
    }

}
