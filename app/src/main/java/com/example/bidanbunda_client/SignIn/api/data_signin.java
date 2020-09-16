package com.example.bidanbunda_client.SignIn.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.net.Inet4Address;

public class data_signin {

    @SerializedName("user_id")
    @Expose
    private Integer user_id;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("user_type")
    @Expose
    private String user_type;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("username")
    @Expose
    private String user_name;

    @SerializedName("full_address")
    @Expose
    private String full_address;

    @SerializedName("profile_image")
    @Expose
    private String profile_image;

    @SerializedName("telephone")
    @Expose
    private String telephone;

    @SerializedName("pus_id")
    @Expose
    private String pus_id;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getFull_address() {
        return full_address;
    }

    public void setFull_address(String full_address) {
        this.full_address = full_address;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getPus_id() {
        return pus_id;
    }

    public void setPus_id(String pus_id) {
        this.pus_id = pus_id;
    }
}
