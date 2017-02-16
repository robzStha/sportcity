package com.app.sportcity.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LinksActiveMenu {

    @SerializedName("collection")
    @Expose
    private String collection;
    @SerializedName("self")
    @Expose
    private String self;

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

}