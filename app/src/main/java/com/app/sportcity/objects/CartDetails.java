package com.app.sportcity.objects;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartDetails {

    @SerializedName("total_count")
    @Expose
    private String totalCount;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("items_detail")
    @Expose
    private List<ItemsDetail> itemsDetail = null;

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<ItemsDetail> getItemsDetail() {
        return itemsDetail;
    }

    public void setItemsDetail(List<ItemsDetail> itemsDetail) {
        this.itemsDetail = itemsDetail;
    }
}