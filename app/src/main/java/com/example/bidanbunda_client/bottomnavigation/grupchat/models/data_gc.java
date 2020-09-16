package com.example.bidanbunda_client.bottomnavigation.grupchat.models;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

/**
 * Pagination
 * Created by Suleiman19 on 10/27/16.
 * Copyright (c) 2016. Suleiman Ali Shakir. All rights reserved.
 */

public class data_gc {

    @SerializedName("id")
    @Expose
    private String id;


    @SerializedName("sender.name")
    @Expose
    private String sender_name;


    @SerializedName("sender_id")
    @Expose
    private String sender_id;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("timestamp")
    @Expose
    private long timestamp;


    public data_gc(String sender_name, String sender_id, String message,long timestamp) {
        this.sender_name = sender_name;
        this.sender_id = sender_id;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
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
}



