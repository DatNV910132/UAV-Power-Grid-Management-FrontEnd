package com.dji.sdk.sample.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * The type Electric pole.
 */
public class ElectricPole {

    /**
     * The Id.
     */
    @SerializedName("_id")
    public String _id;
    /**
     * The Pole name.
     */
    @SerializedName("pole_Name")
    public String pole_Name;
    /**
     * The Pole latitude.
     */
    @SerializedName("pole_Latitude")
    public Double pole_Latitude;
    /**
     * The Pole longitude.
     */
    @SerializedName("pole_Longitude")
    public Double pole_Longitude;
    /**
     * The Description.
     */
    @SerializedName("description")
    public String description;
    /**
     * The Build time.
     */
    @SerializedName("buildTime")
    public String buildTime;
    /**
     * The Ep maintenance time.
     */
    @SerializedName("ep_MaintenanceTime")
    public String ep_MaintenanceTime;

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
     * Gets pole name.
     *
     * @return the pole name
     */
    public String getPole_Name() {
        return pole_Name;
    }

    /**
     * Sets pole name.
     *
     * @param pole_Name the pole name
     */
    public void setPole_Name(String pole_Name) {
        this.pole_Name = pole_Name;
    }

    /**
     * Gets pole latitude.
     *
     * @return the pole latitude
     */
    public Double getPole_Latitude() {
        return pole_Latitude;
    }

    /**
     * Sets pole latitude.
     *
     * @param pole_Latitude the pole latitude
     */
    public void setPole_Latitude(Double pole_Latitude) {
        this.pole_Latitude = pole_Latitude;
    }

    /**
     * Gets pole longitude.
     *
     * @return the pole longitude
     */
    public Double getPole_Longitude() {
        return pole_Longitude;
    }

    /**
     * Sets pole longitude.
     *
     * @param pole_Longitude the pole longitude
     */
    public void setPole_Longitude(Double pole_Longitude) {
        this.pole_Longitude = pole_Longitude;
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
     * Gets build time.
     *
     * @return the build time
     */
    public String getBuildTime() {
        return buildTime;
    }

    /**
     * Sets build time.
     *
     * @param buildTime the build time
     */
    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }

    /**
     * Gets ep maintenance time.
     *
     * @return the ep maintenance time
     */
    public String getEp_MaintenanceTime() {
        return ep_MaintenanceTime;
    }

    /**
     * Sets ep maintenance time.
     *
     * @param ep_MaintenanceTime the ep maintenance time
     */
    public void setEp_MaintenanceTime(String ep_MaintenanceTime) {
        this.ep_MaintenanceTime = ep_MaintenanceTime;
    }

    /**
     * Instantiates a new Electric pole.
     *
     * @param pole_Name          the pole name
     * @param pole_Latitude      the pole latitude
     * @param pole_Longitude     the pole longitude
     * @param description        the description
     * @param buildTime          the build time
     * @param ep_MaintenanceTime the ep maintenance time
     */
    public ElectricPole(String pole_Name, Double pole_Latitude, Double pole_Longitude, String description, String buildTime, String ep_MaintenanceTime) {
        this.pole_Name = pole_Name;
        this.pole_Latitude = pole_Latitude;
        this.pole_Longitude = pole_Longitude;
        this.description = description;
        this.buildTime = buildTime;
        this.ep_MaintenanceTime = ep_MaintenanceTime;
    }

    @Override
    public String toString() {
        return "ElectricPole{" +
                "_id='" + _id + '\'' +
                ", pole_Name='" + pole_Name + '\'' +
                ", pole_Latitude=" + pole_Latitude +
                ", pole_Longitude=" + pole_Longitude +
                ", description='" + description + '\'' +
                ", buildTime=" + buildTime +
                ", ep_MaintenanceTime=" + ep_MaintenanceTime +
                '}';
    }
}
