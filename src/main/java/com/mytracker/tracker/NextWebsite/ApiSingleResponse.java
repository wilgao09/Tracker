package com.mytracker.tracker.NextWebsite;

public class ApiSingleResponse {
    private VisitData[] history;
    private int UUID;

    public VisitData[] getHistory() {
        return this.history;
    }

    public void setHistory(VisitData[] history) {
        this.history = history;
    }

    public int getUUID() {
        return this.UUID;
    }

    public void setUUID(int UUID) {
        this.UUID = UUID;
    }

}
