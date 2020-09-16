package com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.chatkonsultasi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class data_ck {

    @SerializedName("target_id")
    @Expose
    private String target_id;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("sender_id")
    @Expose
    private String sender_id;

    @SerializedName("timestamp")
    @Expose
    private long timestamp;

    ///=====


    public data_ck(String message, String sender_id, long timestamp) {
        this.message = message;
        this.sender_id = sender_id;
        this.timestamp = timestamp;
    }

    ///=======
    public String getTarget_id() {
        return target_id;
    }

    public void setTarget_id(String target_id) {
        this.target_id = target_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }
}
