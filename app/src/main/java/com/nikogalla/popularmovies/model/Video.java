package com.nikogalla.popularmovies.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nicola on 2016-04-08.
 */
public class Video {
    @SerializedName("id")
    String id;
    @SerializedName("iso_639_1")
    String iso_639_1;
    @SerializedName("iso_3166_1")
    String iso_3166_1;
    @SerializedName("key")
    String key;
    @SerializedName("name")
    String name;
    @SerializedName("site")
    String site;
    @SerializedName("size")
    Integer size;
    @SerializedName("type")
    String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    public void setIso_3166_1(String iso_3166_1) {
        this.iso_3166_1 = iso_3166_1;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
