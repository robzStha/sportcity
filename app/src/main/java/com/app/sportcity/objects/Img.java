package com.app.sportcity.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Img implements Serializable{

    @SerializedName("img_url")
    @Expose
    private String imgUrl;
    @SerializedName("img_price")
    @Expose
    private String imgPrice;
    @SerializedName("img_id")
    @Expose
    private String imgId;
    @SerializedName("is_fav")
    @Expose
    private String isFav;

    public String getIsPurchased() {
        return isPurchased;
    }

    public void setIsPurchased(String isPurchased) {
        this.isPurchased = isPurchased;
    }

    @SerializedName("is_purchased")
    @Expose
    private String isPurchased;

    /**
     * @return The imgUrl
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * @param imgUrl The img_url
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    /**
     * @return The imgPrice
     */
    public String getImgPrice() {
        return imgPrice;
    }

    /**
     * @param imgPrice The img_price
     */
    public void setImgPrice(String imgPrice) {
        this.imgPrice = imgPrice;
    }

    /**
     * @return The imgId
     */
    public String getImgId() {
        return imgId;
    }

    /**
     * @param imgId The img_id
     */
    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getIsFav() {
        return isFav;
    }

    public void setIsFav(String isFav) {
        this.isFav = isFav;
    }
}