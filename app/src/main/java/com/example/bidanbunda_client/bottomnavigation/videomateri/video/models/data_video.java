package com.example.bidanbunda_client.bottomnavigation.videomateri.video.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Pagination
 * Created by Suleiman19 on 10/27/16.
 * Copyright (c) 2016. Suleiman Ali Shakir. All rights reserved.
 */

public class data_video {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("week")
    @Expose
    private String week;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("title")
    @Expose
    private String title;


    @SerializedName("thumbnail_url")
    @Expose
    private String thumbnail_url;

    @SerializedName("yt_video_id")
    @Expose
    private String yt_video_id;

    @SerializedName("video_duration_str")
    @Expose
    private String video_duration_str;


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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getYt_video_id() {
        return yt_video_id;
    }

    public void setYt_video_id(String yt_video_id) {
        this.yt_video_id = yt_video_id;
    }

    public String getVideo_duration_str() {
        return video_duration_str;
    }

    public void setVideo_duration_str(String video_duration_str) {
        this.video_duration_str = video_duration_str;
    }
}



