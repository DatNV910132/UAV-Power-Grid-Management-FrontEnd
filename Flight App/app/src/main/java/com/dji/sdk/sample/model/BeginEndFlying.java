/*
 * 
 */
package com.dji.sdk.sample.model;

import com.google.gson.annotations.SerializedName;

/**
 * The type Begin end flying.
 */
public class BeginEndFlying{

	/**
	 * The Finished action.
	 */
	@SerializedName("finishedAction")
	public String finishedAction;
	/**
	 * The Goto first waypoint mode.
	 */
	@SerializedName("gotoFirstWaypointMode")
	public String gotoFirstWaypointMode;
	/**
	 * The Repeat times.
	 */
	@SerializedName("repeatTimes")
	public int repeatTimes;

	/**
	 * Gets finished action.
	 *
	 * @return the finished action
	 */
	public String getFinishedAction() {
		return finishedAction;
	}

	/**
	 * Sets finished action.
	 *
	 * @param finishedAction the finished action
	 */
	public void setFinishedAction(String finishedAction) {
		this.finishedAction = finishedAction;
	}

	/**
	 * Gets goto first waypoint mode.
	 *
	 * @return the goto first waypoint mode
	 */
	public String getGotoFirstWaypointMode() {
		return gotoFirstWaypointMode;
	}

	/**
	 * Sets goto first waypoint mode.
	 *
	 * @param gotoFirstWaypointMode the goto first waypoint mode
	 */
	public void setGotoFirstWaypointMode(String gotoFirstWaypointMode) {
		this.gotoFirstWaypointMode = gotoFirstWaypointMode;
	}

	/**
	 * Gets repeat times.
	 *
	 * @return the repeat times
	 */
	public int getRepeatTimes() {
		return repeatTimes;
	}

	/**
	 * Sets repeat times.
	 *
	 * @param repeatTimes the repeat times
	 */
	public void setRepeatTimes(int repeatTimes) {
		this.repeatTimes = repeatTimes;
	}
}
