package com.dji.sdk.sample.model;

import com.google.gson.annotations.SerializedName;

import org.bson.types.ObjectId;

import java.util.Date;

/**
 * The type Drone.
 */
public class Drone {

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
     * The Idman.
     */
    @SerializedName("idman")
    public String idman;
    /**
     * The Online.
     */
    @SerializedName("online")
    public boolean online;
    /**
     * The Battery.
     */
    @SerializedName("battery")
    public int battery;
    /**
     * The Dateuse.
     */
    @SerializedName("dateuse")
    public Date dateuse;
    /**
     * The Status.
     */
    @SerializedName("status")
    public String status;
    /**
     * The Maintenancetime.
     */
    @SerializedName("maintenancetime")
    public Date maintenancetime;

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
     * Gets idman.
     *
     * @return the idman
     */
    public String getIdman() {
        return idman;
    }

    /**
     * Sets idman.
     *
     * @param idman the idman
     */
    public void setIdman(String idman) {
        this.idman = idman;
    }

    /**
     * Is online boolean.
     *
     * @return the boolean
     */
    public boolean isOnline() {
        return online;
    }

    /**
     * Sets online.
     *
     * @param online the online
     */
    public void setOnline(boolean online) {
        this.online = online;
    }

    /**
     * Gets battery.
     *
     * @return the battery
     */
    public int getBattery() {
        return battery;
    }

    /**
     * Sets battery.
     *
     * @param battery the battery
     */
    public void setBattery(int battery) {
        this.battery = battery;
    }

    /**
     * Gets dateuse.
     *
     * @return the dateuse
     */
    public Date getDateuse() {
        return dateuse;
    }

    /**
     * Sets dateuse.
     *
     * @param dateuse the dateuse
     */
    public void setDateuse(Date dateuse) {
        this.dateuse = dateuse;
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
     * Gets maintenancetime.
     *
     * @return the maintenancetime
     */
    public Date getMaintenancetime() {
        return maintenancetime;
    }

    /**
     * Sets maintenancetime.
     *
     * @param maintenancetime the maintenancetime
     */
    public void setMaintenancetime(Date maintenancetime) {
        this.maintenancetime = maintenancetime;
    }

    @Override
    public String toString() {
        return "Drone{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", des='" + des + '\'' +
                ", idman='" + idman + '\'' +
                ", online=" + online +
                ", battery=" + battery +
                ", dateuse=" + dateuse +
                ", status='" + status + '\'' +
                ", maintenancetime=" + maintenancetime +
                '}';
    }
}
