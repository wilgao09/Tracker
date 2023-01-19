package com.mytracker.tracker;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.mytracker.tracker.NextWebsite.ApiAggResponse;
import com.mytracker.tracker.NextWebsite.ApiSingleResponse;
import com.mytracker.tracker.NextWebsite.VisitData;

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

    public static String[][] getUsersList(SortOrdering sortby) {
        var conn = DBConn.getConnection();
        final String timeStmt = "WITH next AS (SELECT _id, lead(visitedAt, 1, visitedAt) OVER w AS nextClick from nextWebsite_visits WINDOW w AS (PARTITION BY user ORDER BY _id ASC))SELECT user, SUM(IFNULL(NULLIF(TIMESTAMPDIFF(SECOND,  visitedAt, nextClick),0),30))  as seconds FROM nextWebsite_visits LEFT OUTER JOIN next ON next._id=nextWebsite_visits._id GROUP BY user ORDER BY user ";

        String stmtString = "";
        switch (sortby) {
            case TIME_ASC:
                stmtString = timeStmt + "ASC;";
                break;
            case TIME_DESC:
                stmtString = timeStmt + "DESC;";
                break;
            default: // default to activity
                stmtString = " select user , MAX(visitedAt) as last_activity from nextWebsite_visits GROUP BY user ORDER BY last_activity DESC;";
                break;

        }

        try {
            Statement stmt = conn.createStatement();
            ResultSet r = stmt.executeQuery(stmtString);

            ArrayList<String[]> res = new ArrayList<String[]>();

            while (r.next()) {
                res.add(new String[] { Integer.toString(r.getInt(1)), r.getString(2) });
            }
            return res.toArray(new String[res.size()][2]);
        } catch (SQLException e) {
            return null;
        }

    }

    public static ApiSingleResponse getSingleData(int id) {
        var conn = DBConn.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    " WITH next AS (SELECT _id, lead(visitedAt, 1, visitedAt) OVER w AS nextClick from nextWebsite_visits WINDOW w AS (PARTITION BY user ORDER BY _id ASC))SELECT location, visitedAt, IFNULL(NULLIF(TIMESTAMPDIFF(SECOND,  visitedAt, nextClick),0),30)  as seconds FROM nextWebsite_visits LEFT OUTER JOIN next ON next._id=nextWebsite_visits._id WHERE user= ? ;");
            stmt.setInt(1, id);

            ResultSet r = stmt.executeQuery();

            ArrayList<VisitData> res = new ArrayList<VisitData>();
            VisitData ptr;
            while (r.next()) {
                ptr = new VisitData();
                ptr.setLocation(r.getString(1));
                ptr.setTimeEntered(r.getTimestamp(2).toString());
                ptr.setSecondsSpent(r.getInt(3));
                res.add(ptr);
            }

            ApiSingleResponse ans = new ApiSingleResponse();
            ans.setHistory(res.toArray(new VisitData[res.size()]));
            ans.setUUID(id);

            return ans;
        } catch (SQLException e) {
            return null;
        }

    }
}