package com.mytracker.tracker.NextWebsite;

public class ApiAggResponse {
    private String[][] timePerPage;
    private String[][] hitsPerPage;

    public String[][] getTimePerPage() {
        return this.timePerPage;
    }

    public String[][] getHitsPerPage() {
        return this.hitsPerPage;
    }

    public void setHitsPerPage(String[][] hpp) {
        this.hitsPerPage = hpp;
    }

    public void setTimePerPage(String[][] tpp) {
        this.timePerPage = tpp;
    }
}
