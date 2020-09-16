package com.example.bidanbunda_client.bottomnavigation.konsultasi.extend.chatkonsultasi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class response_ck {

    @SerializedName("data")
    @Expose
    private List<data_ck> results = new ArrayList<data_ck>();

    @SerializedName("pages")
    @Expose
    private Integer pages;






    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public List<data_ck> getResults() {
        return results;
    }


    public void setResults(List<data_ck> results) {
        this.results = results;
    }


}
