package com.example.bidanbunda_client.bottomnavigation.videomateri.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Pagination
 * Created by Suleiman19 on 10/27/16.
 * Copyright (c) 2016. Suleiman Ali Shakir. All rights reserved.
 */

public class data_vm {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("week")
    @Expose
    private String week;

    @SerializedName("content")
    @Expose
    private String content;


    @SerializedName("thumbnail_url")
    @Expose
    private String thumbnail_url;


    @SerializedName("author.name")
    @Expose
    private String authorname;

    @SerializedName("videos.total")
    @Expose
    private String videostotal;

    @SerializedName("videos.duration_str")
    @Expose
    private String videosduration;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }


    public String getVideostotal() {
        return videostotal;
    }

    public void setVideostotal(String videostotal) {
        this.videostotal = videostotal;
    }

    public String getVideosduration() {
        return videosduration;
    }

    public void setVideosduration(String videosduration) {
        this.videosduration = videosduration;
    }
}



