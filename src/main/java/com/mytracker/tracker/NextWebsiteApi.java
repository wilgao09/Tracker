package com.mytracker.tracker;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mytracker.tracker.NextWebsite.ApiAggResponse;

@RestController
public class NextWebsiteApi {

    // TODO: this
    @CrossOrigin(origins = "*", allowedHeaders = "*", allowCredentials = "true")
    @PostMapping("/nextWebsite/data/agg")
    public ApiAggResponse getAggregateData() {
        return NextWebsiteDB.getAggregateData();
    }
}