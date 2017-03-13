package com.app.sportcity.pushnotification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseToken {

    @SerializedName("isError")
    @Expose
    public String isError;
    @SerializedName("error")
    @Expose
    public String error;
    @SerializedName("SuccessMessage")
    @Expose
    public String SuccessMessage;

}