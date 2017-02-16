package com.app.sportcity.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Meta {

    @SerializedName("links")
    @Expose
    private LinksMenu links;

    public LinksMenu getLinks() {
        return links;
    }

    public void setLinks(LinksMenu links) {
        this.links = links;
    }

}
