package com.example.bidanbunda_client.bottomnavigation.videomateri.video.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class response_video {
    @SerializedName("data")
    @Expose
    private List<data_video> results = new ArrayList<data_video>();

    @SerializedName("pages")
    @Expose
    private Integer pages;

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public List<data_video> getResults() {
        return results;
    }

    public void setResults(List<data_video> results) {
        this.results = results;
    }



}
