package com.example.bidanbunda_client.bottomnavigation.grupchat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class response_gc {





    @SerializedName("data")
    @Expose
    private List<data_gc> results = new ArrayList<data_gc>();


    @SerializedName("pages")
    @Expose
    private Integer pages;

    public List<data_gc> getResults() {
        return results;
    }


    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }


}
