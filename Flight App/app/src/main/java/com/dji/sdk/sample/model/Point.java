package com.dji.sdk.sample.model;

import java.util.Arrays;

/**
 * The type Point.
 */
public class Point {

    /**
     * The Latitude.
     */
    public double latitude;
    /**
     * The Longitude.
     */
    public double longitude;
    /**
     * The Height.
     */
    public float height;
    /**
     * The Action.
     */
    public int action;
    /**
     * The Tags.
     */
    public String[] tags;

    /**
     * Gets latitude.
     *
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets latitude.
     *
     * @param latitude the latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets longitude.
     *
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets longitude.
     *
     * @param longitude the longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets height.
     *
     * @return the height
     */
    public float getHeight() {
        return height;
    }

    /**
     * Sets height.
     *
     * @param height the height
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * Gets action.
     *
     * @return the action
     */
    public int getAction() {
        return action;
    }

    /**
     * Sets action.
     *
     * @param action the action
     */
    public void setAction(int action) {
        this.action = action;
    }

    /**
     * Get tags string [ ].
     *
     * @return the string [ ]
     */
    public String[] getTags() {
        return tags;
    }

    /**
     * Sets tags.
     *
     * @param tags the tags
     */
    public void setTags(String[] tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Point{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", height=" + height +
                ", action=" + action +
                ", tags=" + Arrays.toString(tags) +
                '}';
    }
}
