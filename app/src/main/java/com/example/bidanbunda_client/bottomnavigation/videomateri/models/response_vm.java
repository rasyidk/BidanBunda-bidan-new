package com.example.bidanbunda_client.bottomnavigation.videomateri.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class response_vm {





    @SerializedName("data")
    @Expose
    private List<data_vm> results = new ArrayList<data_vm>();


    @SerializedName("pages")
    @Expose
    private Integer pages;

    public List<data_vm> getResults() {
        return results;
    }


    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }


}
