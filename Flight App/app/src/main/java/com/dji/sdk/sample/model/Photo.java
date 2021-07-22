package com.dji.sdk.sample.model;

import com.google.gson.annotations.SerializedName;

import org.bson.types.Binary;

/**
 * The type Photo.
 */
public class Photo {

    /**
     * The Id.
     */
    @SerializedName("_id")
    public String id;
    /**
     * The Title.
     */
    @SerializedName("title")
    public String title;
    /**
     * The Type.
     */
    @SerializedName("type")
    public String type;
    /**
     * The Date create.
     */
    @SerializedName("dateCreate")
    public String dateCreate;
    /**
     * The Date import.
     */
    @SerializedName("dateImport")
    public String dateImport;
    /**
     * The Description.
     */
    @SerializedName("description")
    public String description;
    /**
     * The Idpole.
     */
    @SerializedName("idpole")
    public String idpole;
    /**
     * The Iduser.
     */
    @SerializedName("iduser")
    public String iduser;
    /**
     * The Iddrone.
     */
    @SerializedName("iddrone")
    public String iddrone;
    /**
     * The Crop.
     */
    @SerializedName("crop")
    public boolean crop;
    /**
     * The Image.
     */
    @SerializedName("image")
    public Binary image;

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
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets date create.
     *
     * @return the date create
     */
    public String getDateCreate() {
        return dateCreate;
    }

    /**
     * Sets date create.
     *
     * @param dateCreate the date create
     */
    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    /**
     * Gets date import.
     *
     * @return the date import
     */
    public String getDateImport() {
        return dateImport;
    }

    /**
     * Sets date import.
     *
     * @param dateImport the date import
     */
    public void setDateImport(String dateImport) {
        this.dateImport = dateImport;
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
     * Gets iduser.
     *
     * @return the iduser
     */
    public String getIduser() {
        return iduser;
    }

    /**
     * Sets iduser.
     *
     * @param iduser the iduser
     */
    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    /**
     * Gets iddrone.
     *
     * @return the iddrone
     */
    public String getIddrone() {
        return iddrone;
    }

    /**
     * Sets iddrone.
     *
     * @param iddrone the iddrone
     */
    public void setIddrone(String iddrone) {
        this.iddrone = iddrone;
    }

    /**
     * Is crop boolean.
     *
     * @return the boolean
     */
    public boolean isCrop() {
        return crop;
    }

    /**
     * Sets crop.
     *
     * @param crop the crop
     */
    public void setCrop(boolean crop) {
        this.crop = crop;
    }

    /**
     * Gets image.
     *
     * @return the image
     */
    public Binary getImage() {
        return image;
    }

    /**
     * Sets image.
     *
     * @param image the image
     */
    public void setImage(Binary image) {
        this.image = image;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", dateCreate='" + dateCreate + '\'' +
                ", dateImport='" + dateImport + '\'' +
                ", description='" + description + '\'' +
                ", idpole='" + idpole + '\'' +
                ", iduser='" + iduser + '\'' +
                ", iddrone='" + iddrone + '\'' +
                ", crop=" + crop +
                ", image=" + image +
                '}';
    }
}
