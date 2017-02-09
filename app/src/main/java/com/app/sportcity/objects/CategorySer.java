package com.app.sportcity.objects;

import java.io.Serializable;

public class CategorySer implements Serializable {

    private String catId;
    private Boolean isActive;
    private String imgUrl;
    private String catTitle;

    /**
     * @return The catId
     */
    public String getCatId() {
        return catId;
    }

    /**
     * @param catId The catId
     */
    public void setCatId(String catId) {
        this.catId = catId;
    }

    /**
     * @return The isActive
     */
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     * @param isActive The isActive
     */
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * @return The imgUrl
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * @param imgUrl The imgUrl
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    /**
     * @return The catTitle
     */
    public String getCatTitle() {
        return catTitle;
    }

    /**
     * @param catTitle The catTitle
     */
    public void setCatTitle(String catTitle) {
        this.catTitle = catTitle;
    }

}