/*
 * @author DatNV 154484
 * @version 1.0
 * @copyright dom
 */
package com.dji.sdk.sample.model;

// TODO: Auto-generated Javadoc

import com.google.gson.annotations.SerializedName;

/**
 * The Class DroneFlying.
 */
public class DroneFlying extends BeginEndFlying {

	/**
	 * The Auto flight speed.
	 */
	@SerializedName("autoFlightSpeed")
    public float autoFlightSpeed;
	/**
	 * The Max flight speed.
	 */
	@SerializedName("maxFlightSpeed")
    public float maxFlightSpeed;
	/**
	 * The Set exit mission on rc signal lost enabled.
	 */
	@SerializedName("setExitMissionOnRCSignalLostEnabled")
    public boolean setExitMissionOnRCSignalLostEnabled;
	/**
	 * The Flight path mode.
	 */
	@SerializedName("flightPathMode")
    public String flightPathMode;

	/**
	 * Gets auto flight speed.
	 *
	 * @return the auto flight speed
	 */
	public float getAutoFlightSpeed() {
		return autoFlightSpeed;
	}

	/**
	 * Sets auto flight speed.
	 *
	 * @param autoFlightSpeed the auto flight speed
	 */
	public void setAutoFlightSpeed(float autoFlightSpeed) {
		this.autoFlightSpeed = autoFlightSpeed;
	}

	/**
	 * Gets max flight speed.
	 *
	 * @return the max flight speed
	 */
	public float getMaxFlightSpeed() {
		return maxFlightSpeed;
	}

	/**
	 * Sets max flight speed.
	 *
	 * @param maxFlightSpeed the max flight speed
	 */
	public void setMaxFlightSpeed(float maxFlightSpeed) {
		this.maxFlightSpeed = maxFlightSpeed;
	}

	/**
	 * Is set exit mission on rc signal lost enabled boolean.
	 *
	 * @return the boolean
	 */
	public boolean isSetExitMissionOnRCSignalLostEnabled() {
		return setExitMissionOnRCSignalLostEnabled;
	}

	/**
	 * Sets set exit mission on rc signal lost enabled.
	 *
	 * @param setExitMissionOnRCSignalLostEnabled the set exit mission on rc signal lost enabled
	 */
	public void setSetExitMissionOnRCSignalLostEnabled(boolean setExitMissionOnRCSignalLostEnabled) {
		this.setExitMissionOnRCSignalLostEnabled = setExitMissionOnRCSignalLostEnabled;
	}

	/**
	 * Gets flight path mode.
	 *
	 * @return the flight path mode
	 */
	public String getFlightPathMode() {
		return flightPathMode;
	}

	/**
	 * Sets flight path mode.
	 *
	 * @param flightPathMode the flight path mode
	 */
	public void setFlightPathMode(String flightPathMode) {
		this.flightPathMode = flightPathMode;
	}
}
