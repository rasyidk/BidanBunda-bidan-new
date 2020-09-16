package com.example.bidanbunda_client.bottomnavigation.konsultasi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class data_ksl {

    @SerializedName("id")
    @Expose
    private String id;


    @SerializedName("chatroom_name")
    @Expose
    private String chatroom_name;

    @SerializedName("chatroom_user_id")
    @Expose
    private String chatroom_user_id;


    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("target_id")
    @Expose
    private String target_id;

    @SerializedName("timestamp")
    @Expose
    private long timestamp;


    public data_ksl(String chatroom_name, String message, String chatroom_user_id, long timestamp) {
        this.chatroom_name = chatroom_name;
        this.message = message;
        this.chatroom_user_id = chatroom_user_id;
        this.timestamp = timestamp;
    }

    public String getTarget_id() {
        return target_id;
    }

    public void setTarget_id(String target_id) {
        this.target_id = target_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getChatroom_name() {
        return chatroom_name;
    }

    public void setChatroom_name(String chatroom_name) {
        this.chatroom_name = chatroom_name;
    }

    public String getChatroom_user_id() {
        return chatroom_user_id;
    }

    public void setChatroom_user_id(String chatroom_user_id) {
        this.chatroom_user_id = chatroom_user_id;
    }
}



