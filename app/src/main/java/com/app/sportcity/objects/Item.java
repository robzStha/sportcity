package com.app.sportcity.objects;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("parent")
    @Expose
    private Integer parent;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("attr")
    @Expose
    private String attr;
    @SerializedName("target")
    @Expose
    private String target;
    @SerializedName("classes")
    @Expose
    private String classes;
    @SerializedName("xfn")
    @Expose
    private String xfn;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("object_id")
    @Expose
    private Integer objectId;
    @SerializedName("object")
    @Expose
    private String object;
    @SerializedName("object_slug")
    @Expose
    private String objectSlug;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("type_label")
    @Expose
    private String typeLabel;
    @SerializedName("children")
    @Expose
    private List<Child> children = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getXfn() {
        return xfn;
    }

    public void setXfn(String xfn) {
        this.xfn = xfn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getObjectSlug() {
        return objectSlug;
    }

    public void setObjectSlug(String objectSlug) {
        this.objectSlug = objectSlug;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeLabel() {
        return typeLabel;
    }

    public void setTypeLabel(String typeLabel) {
        this.typeLabel = typeLabel;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }

}