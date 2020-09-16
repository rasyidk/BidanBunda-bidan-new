package com.example.bidanbunda_client.bottomnavigation.profile.extend.catatanKhusus.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class response_catk {

    @SerializedName("data")
    @Expose
    private List<data_catk> results = new ArrayList<data_catk>();

    @SerializedName("pages")
    @Expose
    private Integer pages;






    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public List<data_catk> getResults() {
        return results;
    }


    public void setResults(List<data_catk> results) {
        this.results = results;
    }



}
