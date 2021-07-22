/*
 * 
 */
package com.dji.sdk.sample.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Mission.
 */
public class Mission extends DroneFlying {

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
	 * The Heading mode.
	 */
	@SerializedName("headingMode")
	public String headingMode;
	/**
	 * The Poleandpointmission.
	 */
	@SerializedName("poleandpointmission")
	public List<String> poleandpointmission = new ArrayList<>();
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
	 * Gets heading mode.
	 *
	 * @return the heading mode
	 */
	public String getHeadingMode() {
		return headingMode;
	}

	/**
	 * Sets heading mode.
	 *
	 * @param headingMode the heading mode
	 */
	public void setHeadingMode(String headingMode) {
		this.headingMode = headingMode;
	}

	/**
	 * Gets poleandpointmission.
	 *
	 * @return the poleandpointmission
	 */
	public List<String> getPoleandpointmission() {
		return poleandpointmission;
	}

	/**
	 * Sets poleandpointmission.
	 *
	 * @param poleandpointmission the poleandpointmission
	 */
	public void setPoleandpointmission(List<String> poleandpointmission) {
		this.poleandpointmission = poleandpointmission;
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
