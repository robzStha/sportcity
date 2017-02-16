package com.app.sportcity.objects;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActiveMenuList {

@SerializedName("ID")
@Expose
private Integer iD;
@SerializedName("name")
@Expose
private String name;
@SerializedName("slug")
@Expose
private String slug;
@SerializedName("description")
@Expose
private String description;
@SerializedName("count")
@Expose
private Integer count;
@SerializedName("items")
@Expose
private List<Item> items = null;
@SerializedName("meta")
@Expose
private Meta meta;

public Integer getID() {
return iD;
}

public void setID(Integer iD) {
this.iD = iD;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getSlug() {
return slug;
}

public void setSlug(String slug) {
this.slug = slug;
}

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

public Integer getCount() {
return count;
}

public void setCount(Integer count) {
this.count = count;
}

public List<Item> getItems() {
return items;
}

public void setItems(List<Item> items) {
this.items = items;
}

public Meta getMeta() {
return meta;
}

public void setMeta(Meta meta) {
this.meta = meta;
}

}