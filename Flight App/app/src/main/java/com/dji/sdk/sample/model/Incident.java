package com.dji.sdk.sample.model;

import com.google.gson.annotations.SerializedName;

/**
 * The type Incident.
 */
public class Incident {

    /**
     * The Id.
     */
    @SerializedName("_id")
    public String id;
    /**
     * The Name.
     */
    @SerializedName("name")
    public String name;
    /**
     * The Des.
     */
    @SerializedName("des")
    public String des;
    /**
     * The Date.
     */
    @SerializedName("date")
    public String date;
    /**
     * The Status.
     */
    @SerializedName("status")
    public String status;
    /**
     * The Level.
     */
    @SerializedName("level")
    public String level;
    /**
     * The Idfix.
     */
    @SerializedName("idfix")
    public String idfix;
    /**
     * The Iddetect.
     */
    @SerializedName("iddetect")
    public String iddetect;
    /**
     * The Idpole.
     */
    @SerializedName("idpole")
    public String idpole;
    /**
     * The Image.
     */
    @SerializedName("image")
    public String image;

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets des.
     *
     * @return the des
     */
    public String getDes() {
        return des;
    }

    /**
     * Sets des.
     *
     * @param des the des
     */
    public void setDes(String des) {
        this.des = des;
    }

    /**
     * Gets date.
     *
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets date.
     *
     * @param date the date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets level.
     *
     * @return the level
     */
    public String getLevel() {
        return level;
    }

    /**
     * Sets level.
     *
     * @param level the level
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * Gets idfix.
     *
     * @return the idfix
     */
    public String getIdfix() {
        return idfix;
    }

    /**
     * Sets idfix.
     *
     * @param idfix the idfix
     */
    public void setIdfix(String idfix) {
        this.idfix = idfix;
    }

    /**
     * Gets iddetect.
     *
     * @return the iddetect
     */
    public String getIddetect() {
        return iddetect;
    }

    /**
     * Sets iddetect.
     *
     * @param iddetect the iddetect
     */
    public void setIddetect(String iddetect) {
        this.iddetect = iddetect;
    }

    /**
     * Gets idpole.
     *
     * @return the idpole
     */
    public String getIdpole() {
        return idpole;
    }

    /**
     * Sets idpole.
     *
     * @param idpole the idpole
     */
    public void setIdpole(String idpole) {
        this.idpole = idpole;
    }

    /**
     * Gets image.
     *
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets image.
     *
     * @param image the image
     */
    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Incident{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", des='" + des + '\'' +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                ", level='" + level + '\'' +
                ", idfix='" + idfix + '\'' +
                ", iddetect='" + iddetect + '\'' +
                ", idpole='" + idpole + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
