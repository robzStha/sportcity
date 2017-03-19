package com.app.sportcity.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ACF {

    @SerializedName("show_in_store")
    @Expose
    private String showInStore;
    @SerializedName("price")
    @Expose
    private String price;

    public String getShowInStore() {
        return showInStore;
    }

    public void setShowInStore(String showInStore) {
        this.showInStore = showInStore;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}