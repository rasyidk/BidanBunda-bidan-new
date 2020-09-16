package com.example.bidanbunda_client.bottomnavigation.profile.extend.catatanKhusus.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class data_catk {

    @SerializedName("user_id")
    @Expose
    private String user_id;

    @SerializedName("patient_note")
    @Expose
    private String patient_note;

    @SerializedName("medical_record_id")
    @Expose
    private String medical_record_id;

    @SerializedName("user.name")
    @Expose
    private String username;

    @SerializedName("user.profile_image")
    @Expose
    private String userprofile_image;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPatient_note() {
        return patient_note;
    }

    public void setPatient_note(String patient_note) {
        this.patient_note = patient_note;
    }

    public String getMedical_record_id() {
        return medical_record_id;
    }

    public void setMedical_record_id(String medical_record_id) {
        this.medical_record_id = medical_record_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserprofile_image() {
        return userprofile_image;
    }

    public void setUserprofile_image(String userprofile_image) {
        this.userprofile_image = userprofile_image;
    }
}



