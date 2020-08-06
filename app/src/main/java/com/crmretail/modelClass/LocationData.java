package com.crmretail.modelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LocationData implements Serializable {


    private String userId;
    private String userLAT;
    private String userLONG;
    private String datetime;

    public String getuserId() {
        return userId;
    }

    public void setuserId(String userId) {
        this.userId = userId;
    }

    public String getuserLAT() {
        return userLAT;
    }

    public void setuserLAT(String userLAT) {
        this.userLAT = userLAT;
    }

    public String getuserLONG() {
        return userLONG;
    }

    public void setuserLONG(String userLONG) {
        this.userLONG = userLONG;
    }


    public String getdatetime() {
        return datetime;
    }

    public void setdatetime(String datetime) {
        this.datetime = datetime;
    }

}
