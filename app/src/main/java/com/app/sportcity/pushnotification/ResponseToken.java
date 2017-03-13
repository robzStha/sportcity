package com.app.sportcity.pushnotification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseToken {

    @SerializedName("code")
    @Expose
    public String code;
    @SerializedName("msg")
    @Expose
    public String msg;

}