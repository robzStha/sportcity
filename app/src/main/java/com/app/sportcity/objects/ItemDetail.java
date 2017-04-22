package com.app.sportcity.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemDetail {

    @SerializedName("item_id")
    @Expose
    private int itemId;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("item_qty")
    @Expose
    private int itemQty;
    @SerializedName("item_price")
    @Expose
    private float itemPrice;
    @SerializedName("item_total")
    @Expose
    private float itemTotal;

    public void setItemImgUrl(String itemImgUrl) {
        this.itemImgUrl = itemImgUrl;
    }

    @SerializedName("item_img_url")
    @Expose
    private String itemImgUrl;

    @SerializedName("item_org_img_url")
    @Expose
    private String itemOrgImgUrl;

    public String getItemImgUrl() {
        return itemImgUrl;
    }

    public String getItemOrgImgUrl() {
        return itemOrgImgUrl;
    }

    public void setItemOrgImgUrl(String itemOrgImgUrl) {
        this.itemOrgImgUrl = itemOrgImgUrl;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemQty() {
        return itemQty;
    }

    public void setItemQty(int itemQty) {
        this.itemQty = itemQty;
    }

    public float getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(float itemPrice) {
        this.itemPrice = itemPrice;
    }

    public float getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(float itemTotal) {
        this.itemTotal = itemTotal;
    }

    public String getImageUrl() {
        return itemImgUrl;
    }
}