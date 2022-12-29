package com.mytracker.tracker;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CookieValue;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class NextWebsiteController {

    @PostMapping("/nextWebsite")
    public int visitLocation(HttpServletRequest request,
            HttpServletResponse response, @RequestBody NextWebsiteBody body,
            @CookieValue(value = "userid", defaultValue = "-1") String userid) {
        int UUID;
        try {
            UUID = Integer.parseInt(userid);
        } catch (NumberFormatException e) {
            return 1;
        }

        System.out.println("user visited " + body.getLocation() + " and had a user id of " + userid);

        if (UUID == -1) {
            int newId = DBConn.createNextUser();
            if (newId == -1) {
                newId = 999;
            }

            Cookie c = new Cookie("userid", Integer.toString(newId));
            c.setHttpOnly(true);
            c.setPath("/");
            // TODO: set secure
            response.addCookie(c);
        }

        DBConn.createNextWebsiteVisit(UUID, body.getLocation());

        return 5;
    }
}
