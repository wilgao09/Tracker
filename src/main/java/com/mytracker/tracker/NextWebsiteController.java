package com.mytracker.tracker;

// import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mytracker.tracker.NextWebsite.VisitBody;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class NextWebsiteController {
    private final String remoteHost = "https://next-website-wilgao09.vercel.app";

    // @CrossOrigin(origins="https://next-website-wilgao09.vercel.app")
    @CrossOrigin(origins = remoteHost, allowedHeaders = "*", allowCredentials = "true")
    @PostMapping("/nextWebsite")
    public String visitLocation(HttpServletRequest request,
            HttpServletResponse response, @RequestBody VisitBody body,
            @CookieValue(value = "userid", defaultValue = "-1") String userid) {
        int UUID;
        try {
            UUID = Integer.parseInt(userid);
        } catch (NumberFormatException e) {
            return "failed";
        }

        System.out.println("user visited " + body.getLocation() + " and had a user id of " + UUID);

        String cookieval = Integer.toString(UUID);
        if (UUID == -1) {
            int newId = NextWebsiteDB.createNextUser();
            if (newId == -1) {
                newId = 999;
            }
            cookieval = Integer.toString(newId);

            response.setHeader("Set-Cookie", "userid=" + cookieval + "; HttpOnly; SameSite=strict; Secure; Path=/");
            UUID = newId;
        }

        NextWebsiteDB.createNextWebsiteVisit(UUID, body.getLocation());

        return cookieval;
    }
    /**
     * @CrossOrigin(origins="https://next-website-wilgao09.vercel.app")
     *                                                                  @GetMapping("/nextWebsite/{location}")
     *                                                                  public int
     *                                                                  visitLocationGet(HttpServletRequest
     *                                                                  request,
     *                                                                  HttpServletResponse
     *                                                                  response,
     * @CookieValue(value = "userid", defaultValue = "-1") String userid,
     * @PathVariable(name="location", required=true) String location) {
     *                                int UUID;
     *                                try {
     *                                UUID = Integer.parseInt(userid);
     *                                } catch (NumberFormatException e) {
     *                                return 1;
     *                                }
     * 
     *                                location = location.replaceAll("-", "/");
     * 
     *                                System.out.println("user visited " + location
     *                                + " and had a user id of " + UUID);
     * 
     *                                if (UUID == -1) {
     *                                int newId = DBConn.createNextUser();
     *                                if (newId == -1) {
     *                                newId = 999;
     *                                }
     * 
     *                                Cookie c = (new Cookie("userid",
     *                                Integer.toString(newId)));
     *                                // c.setHttpOnly(true);
     *                                c.setPath("/");
     *                                c.setMaxAge(2147483647);
     * 
     * 
     *                                // TODO: set secure
     *                                response.addCookie(c);
     *                                UUID = newId;
     *                                }
     * 
     *                                DBConn.createNextWebsiteVisit(UUID, location);
     *                                return 5;
     *                                }
     * 
     **/
}
