package com.dji.sdk.sample.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Template pole mission.
 */
public class TemplatePoleMission {

    /**
     * The Id.
     */
    @SerializedName("_id")
    public String _id;
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
     * The Poletype.
     */
    @SerializedName("poletype")
    public String poletype;
    /**
     * The Point.
     */
    @SerializedName("point")
    public List<Point> point = new ArrayList<>();
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
     * Gets poletype.
     *
     * @return the poletype
     */
    public String getPoletype() {
        return poletype;
    }

    /**
     * Sets poletype.
     *
     * @param poletype the poletype
     */
    public void setPoletype(String poletype) {
        this.poletype = poletype;
    }

    /**
     * Gets point.
     *
     * @return the point
     */
    public List<Point> getPoint() {
        return point;
    }

    /**
     * Sets point.
     *
     * @param point the point
     */
    public void setPoint(List<Point> point) {
        this.point = point;
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

    @Override
    public String toString() {
        return "TemplatePoleMission{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", des='" + des + '\'' +
                ", poletype='" + poletype + '\'' +
                ", point=" + point +
                ", createat='" + createat + '\'' +
                ", updateat='" + updateat + '\'' +
                '}';
    }
}
