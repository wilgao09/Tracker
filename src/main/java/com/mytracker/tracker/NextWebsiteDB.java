package com.mytracker.tracker;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.mytracker.tracker.NextWebsite.ApiAggResponse;

public class NextWebsiteDB {
    public static int createNextUser() {
        var conn = DBConn.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO users() VALUES ()",
                    Statement.RETURN_GENERATED_KEYS);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return -1;
            }
            ResultSet res = stmt.getGeneratedKeys();
            if (res.next()) {
                System.out.println("IT DIDNT FAIL");
                System.out.println("got id " + res.getInt(1));
                return res.getInt(1);
            }
            return -1;

        } catch (SQLException e) {
            System.out.println("failed");
            return -1;
        }

    }

    public static void createNextWebsiteVisit(int UUID, String location) {
        var conn = DBConn.getConnection();
        try {
            PreparedStatement stmt = conn
                    .prepareStatement("INSERT INTO nextWebsite_visits(user, location, visitedAt) VALUES (?, ?, ?)");
            stmt.setInt(1, UUID);
            stmt.setString(2, location);
            stmt.setString(3, (new Timestamp(System.currentTimeMillis())).toString());

            System.out.println(stmt);

            if (stmt.executeUpdate() > 0) {
                System.out.println("succeeded");
            } else {
                System.out.println("failed");
            }

            return;

        } catch (SQLException e) {
            System.out.println("failed");
            return;
        }
    }

    public static ApiAggResponse getAggregateData() {
        var conn = DBConn.getConnection();
        try {
            PreparedStatement timeStmt = conn.prepareStatement(
                    "WITH next AS (SELECT _id, lead(visitedAt, 1, visitedAt) OVER w AS nextClick from nextWebsite_visits WINDOW w AS (PARTITION BY user ORDER BY _id ASC) )SELECT location, SUM(IFNULL(NULLIF(TIMESTAMPDIFF(SECOND,  visitedAt, nextClick),0),30) ) as seconds FROM nextWebsite_visits LEFT OUTER JOIN next ON next._id=nextWebsite_visits._id GROUP BY location;");
            PreparedStatement hitsStmt = conn.prepareStatement(
                    "SELECT location, COUNT(location) as hits FROM nextWebsite_visits GROUP BY location;");

            ArrayList<String[]> times = new ArrayList<String[]>();

            ResultSet r = timeStmt.executeQuery();
            while (r.next()) {
                times.add(new String[] { r.getString(1), Integer.toString(r.getInt(2)) });
            }

            ArrayList<String[]> hits = new ArrayList<String[]>();
            r = hitsStmt.executeQuery();
            while (r.next()) {
                hits.add(new String[] { r.getString(1), Integer.toString(r.getInt(2)) });
            }

            ApiAggResponse res = new ApiAggResponse();
            res.setTimePerPage(times.toArray(new String[times.size()][2]));
            res.setHitsPerPage(hits.toArray(new String[hits.size()][2]));
            return res;

        } catch (SQLException e) {
            return null;
        }
    }

}