package com.dji.sdk.sample.model;

import com.google.gson.annotations.SerializedName;


/**
 * The type Employee.
 */
public class Employee {

    /**
     * The Id.
     */
    @SerializedName("_id")
    public String id;
    /**
     * The Username.
     */
    @SerializedName("username")
    public String username;
    /**
     * The Password.
     */
    @SerializedName("password")
    public String password;
    /**
     * The Idrole.
     */
    @SerializedName("idrole")
    public String idrole;
    /**
     * The Name.
     */
    @SerializedName("name")
    public String name;
    /**
     * The Birth.
     */
    @SerializedName("birth")
    public String birth;
    /**
     * The Sex.
     */
    @SerializedName("sex")
    public String sex;
    /**
     * The Nationid.
     */
    @SerializedName("nationality")
    public String nationality;
    /**
     * The Address.
     */
    @SerializedName("address")
    public String address;
    /**
     * The Mail.
     */
    @SerializedName("mail")
    public String mail;
    /**
     * The Phone.
     */
    @SerializedName("phone")
    public String phone;

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
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets idrole.
     *
     * @return the idrole
     */
    public String getIdrole() {
        return idrole;
    }

    /**
     * Sets idrole.
     *
     * @param idrole the idrole
     */
    public void setIdrole(String idrole) {
        this.idrole = idrole;
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
     * Gets birth.
     *
     * @return the birth
     */
    public String getBirth() {
        return birth;
    }

    /**
     * Sets birth.
     *
     * @param birth the birth
     */
    public void setBirth(String birth) {
        this.birth = birth;
    }

    /**
     * Gets sex.
     *
     * @return the sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * Sets sex.
     *
     * @param sex the sex
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * Gets nationality.
     *
     * @return the nationality
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * Sets nationality.
     *
     * @param nationality the nationality
     */
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    /**
     * Gets address.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets address.
     *
     * @param address the address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets mail.
     *
     * @return the mail
     */
    public String getMail() {
        return mail;
    }

    /**
     * Sets mail.
     *
     * @param mail the mail
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * Gets phone.
     *
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets phone.
     *
     * @param phone the phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", idrole='" + idrole + '\'' +
                ", name='" + name + '\'' +
                ", birth='" + birth + '\'' +
                ", sex='" + sex + '\'' +
                ", nationid='" + nationality + '\'' +
                ", address='" + address + '\'' +
                ", mail='" + mail + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
