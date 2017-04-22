package com.app.sportcity.objects;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartDetails {

    @SerializedName("total_count")
    @Expose
    private int totalCount = 0;
    @SerializedName("total_amount")
    @Expose
    private float totalAmount=0;
    @SerializedName("items_detail")
    @Expose
    private List<ItemDetail> itemDetail = new ArrayList<>();

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<ItemDetail> getItemDetail() {
        return itemDetail;
    }

    public void setItemDetail(List<ItemDetail> itemDetail) {
        this.itemDetail = itemDetail;
    }
}