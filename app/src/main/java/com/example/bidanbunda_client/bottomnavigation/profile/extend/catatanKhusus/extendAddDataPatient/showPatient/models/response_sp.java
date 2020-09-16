package com.example.bidanbunda_client.bottomnavigation.profile.extend.catatanKhusus.extendAddDataPatient.showPatient.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class response_sp {

    @SerializedName("data")
    @Expose
    private List<data_sp> results = new ArrayList<data_sp>();

    @SerializedName("pages")
    @Expose
    private Integer pages;






    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }
    /**
     *
     * @return
     * The results
     */
    public List<data_sp> getResults() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setResults(List<data_sp> results) {
        this.results = results;
    }

    /**
     *
     * @return
     * The totalResults
     */


}
