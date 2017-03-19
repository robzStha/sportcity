package com.app.sportcity.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ACF {

    @SerializedName("show_in_store")
    @Expose
    private Boolean showInStore;
//    @SerializedName("image_link")
//    @Expose
//    private String imageLink;
    @SerializedName("price")
    @Expose
    private String price;

    public Boolean getShowInStore() {
        return showInStore;
    }

    public void setShowInStore(Boolean showInStore) {
        this.showInStore = showInStore;
    }

//    public String getImageLink() {
//        return imageLink;
//    }
//
//    public void setImageLink(String imageLink) {
//        this.imageLink = imageLink;
//    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}