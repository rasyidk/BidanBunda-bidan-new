package com.example.bidanbunda_client.bottomnavigation.profile.extend.jadwalDiskusi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class data_jd {

//    @SerializedName("poster_path")
//    @Expose
//    private String posterPath;
//    @SerializedName("adult")
//    @Expose
//    private Boolean adult;
//    @SerializedName("overview")
//    @Expose
//    private String overview;
//    @SerializedName("release_date")
//    @Expose
//    private String releaseDate;
//    @SerializedName("genre_ids")
//    @Expose
//    private List<Integer> genreIds = new ArrayList<Integer>();
//    @SerializedName("id")
//    @Expose
//    private Integer id;
//
//    @SerializedName("original_title")
//    @Expose
//    private String originalTitle;
//    @SerializedName("original_language")
//    @Expose
//    private String originalLanguage;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("timestamp")
    @Expose
    private long timestamp_st;

    @SerializedName("timestamp_end")
    @Expose
    private long timestamp_en;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public data_jd(String title) {
        this.title = title;
    }

    public long getTimestamp_st() {
        return timestamp_st;
    }

    public void setTimestamp_st(long timestamp_st) {
        this.timestamp_st = timestamp_st;
    }

    public long getTimestamp_en() {
        return timestamp_en;
    }

    public void setTimestamp_en(long timestamp_en) {
        this.timestamp_en = timestamp_en;
    }



}



