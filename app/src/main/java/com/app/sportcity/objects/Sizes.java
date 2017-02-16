package com.app.sportcity.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sizes {

    @SerializedName("thumbnail")
    @Expose
    private Thumbnail thumbnail;
    @SerializedName("medium")
    @Expose
    private Medium medium;
    @SerializedName("medium_large")
    @Expose
    private MediumLarge mediumLarge;
    @SerializedName("featured_post_thumbnail")
    @Expose
    private FeaturedPostThumbnail featuredPostThumbnail;
    @SerializedName("latest_post_thumbnail")
    @Expose
    private LatestPostThumbnail latestPostThumbnail;
    @SerializedName("FB_thumbnail")
    @Expose
    private FBThumbnail fBThumbnail;
    @SerializedName("full")
    @Expose
    private Full full;

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Medium getMedium() {
        return medium;
    }

    public void setMedium(Medium medium) {
        this.medium = medium;
    }

    public MediumLarge getMediumLarge() {
        return mediumLarge;
    }

    public void setMediumLarge(MediumLarge mediumLarge) {
        this.mediumLarge = mediumLarge;
    }

    public FeaturedPostThumbnail getFeaturedPostThumbnail() {
        return featuredPostThumbnail;
    }

    public void setFeaturedPostThumbnail(FeaturedPostThumbnail featuredPostThumbnail) {
        this.featuredPostThumbnail = featuredPostThumbnail;
    }

    public LatestPostThumbnail getLatestPostThumbnail() {
        return latestPostThumbnail;
    }

    public void setLatestPostThumbnail(LatestPostThumbnail latestPostThumbnail) {
        this.latestPostThumbnail = latestPostThumbnail;
    }

    public FBThumbnail getFBThumbnail() {
        return fBThumbnail;
    }

    public void setFBThumbnail(FBThumbnail fBThumbnail) {
        this.fBThumbnail = fBThumbnail;
    }

    public Full getFull() {
        return full;
    }

    public void setFull(Full full) {
        this.full = full;
    }

}
