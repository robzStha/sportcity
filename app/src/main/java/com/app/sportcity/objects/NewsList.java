package com.app.sportcity.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class NewsList implements Serializable {

    @SerializedName("cat_id")
    @Expose
    private Integer catId;
    @SerializedName("news_id")
    @Expose
    private Integer newsId;
    @SerializedName("news_title")
    @Expose
    private String newsTile;
    @SerializedName("feat_img_url")
    @Expose
    private String featImgUrl;
    @SerializedName("published_date")
    @Expose
    private String publishedDate;
    @SerializedName("cat_name")
    @Expose
    private String catName;
    @SerializedName("news_desc")
    @Expose
    private String newsDesc;
    @SerializedName("img")
    @Expose
    private List<Img> img = null;
    @SerializedName("is_fav")
    @Expose
    private Boolean isFav;

    /**
     * @return The catId
     */
    public Integer getCatId() {
        return catId;
    }

    /**
     * @param catId The cat_id
     */
    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    /**
     * @return The newsId
     */
    public Integer getNewsId() {
        return newsId;
    }

    /**
     * @param newsId The news_id
     */
    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    /**
     * @return The newsTile
     */
    public String getNewsTile() {
        return newsTile;
    }

    /**
     * @param newsTile The news_tile
     */
    public void setNewsTile(String newsTile) {
        this.newsTile = newsTile;
    }

    /**
     * @return The featImgUrl
     */
    public String getFeatImgUrl() {
        return featImgUrl;
    }

    /**
     * @param featImgUrl The feat_img_url
     */
    public void setFeatImgUrl(String featImgUrl) {
        this.featImgUrl = featImgUrl;
    }

    /**
     * @return The newsDesc
     */
    public String getNewsDesc() {
        return newsDesc;
    }

    /**
     * @param newsDesc The news_desc
     */
    public void setNewsDesc(String newsDesc) {
        this.newsDesc = newsDesc;
    }

    /**
     * @return The img
     */
    public List<Img> getImg() {
        return img;
    }

    /**
     * @param img The img
     */
    public void setImg(List<Img> img) {
        this.img = img;
    }

    /**
     * @return The isFav
     */
    public Boolean getIsFav() {
        return isFav;
    }

    /**
     * @param isFav The is_fav
     */
    public void setIsFav(Boolean isFav) {
        this.isFav = isFav;
    }

}