package com.mytracker.tracker;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.mytracker.tracker.NextWebsite.ApiAggResponse;
import com.mytracker.tracker.NextWebsite.ApiSingleResponse;

@RestController
@CrossOrigin(origins = "https://tracker-dashboard.vercel.app", allowedHeaders = "*", allowCredentials = "true")
public class NextWebsiteApi {

    @GetMapping("/nextWebsite/data/agg")
    public ApiAggResponse getAggregateData() {
        return NextWebsiteDB.getAggregateData();
    }

    @GetMapping("/nextWebsite/data/usersList/{order}")
    public String[][] getUsersList(@PathVariable String order) {
        SortOrdering sortby;

        switch (order) {
            case "TIME_DESC":
                sortby = SortOrdering.TIME_DESC;
                break;
            case "TIME_ASC":
                sortby = SortOrdering.TIME_ASC;
                break;
            default:
                sortby = SortOrdering.ACTIVITY_DESC;
        }

        return NextWebsiteDB.getUsersList(sortby);

    }

    @GetMapping("/nextWebsite/data/single/{uuid}")
    public ApiSingleResponse getSingleData(@PathVariable String uuid) {
        int id = 999;
        try {
            id = Integer.parseInt(uuid);
        } catch (NumberFormatException e) {
            id = 999;
        }
        return NextWebsiteDB.getSingleData(id);
    }
}
