package com.dji.sdk.sample.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

/**
 * The type Link.
 */
public class Link {

    /**
     * The Id.
     */
    @SerializedName("_id")
    public String _id;
    /**
     * The Url.
     */
    @SerializedName("url")
    public String url;
    /**
     * The Description.
     */
    @SerializedName("description")
    public String description;
    /**
     * The Createat.
     */
    @SerializedName("createat")
    public String createat;
    /**
     * The Updateat.
     */
    @SerializedName("updateat")
    public String updateat;

    /**
     * Gets id.
     *
     * @return the id
     */
    public String get_id() {
        return _id;
    }

    /**
     * Sets id.
     *
     * @param _id the id
     */
    public void set_id(String _id) {
        this._id = _id;
    }

    /**
     * Gets url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets url.
     *
     * @param url the url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets createat.
     *
     * @return the createat
     */
    public String getCreateat() {
        return createat;
    }

    /**
     * Sets createat.
     *
     * @param createat the createat
     */
    public void setCreateat(String createat) {
        this.createat = createat;
    }

    /**
     * Gets updateat.
     *
     * @return the updateat
     */
    public String getUpdateat() {
        return updateat;
    }

    /**
     * Sets updateat.
     *
     * @param updateat the updateat
     */
    public void setUpdateat(String updateat) {
        this.updateat = updateat;
    }
}
