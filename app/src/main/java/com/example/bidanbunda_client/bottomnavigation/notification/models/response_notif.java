package com.example.bidanbunda_client.bottomnavigation.notification.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class response_notif {





    @SerializedName("data")
    @Expose
    private List<data_notif> results = new ArrayList<data_notif>();


    @SerializedName("pages")
    @Expose
    private Integer pages;

    public List<data_notif> getResults() {
        return results;
    }


    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }


}
