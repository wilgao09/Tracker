package com.mytracker.tracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
// import com.mysql.cj.jdbc.Driver;

public class DBConn {
    private static boolean initialized = false;
    private static Connection conn = null;

    public static boolean isInitialized() {
        return initialized;
    }

    public static Connection getConnection() {
        if (!initialized)
            connect();
        return conn;
    }

    public static void connect() {
        if (initialized)
            return;

        try {

            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/tracker?useSSL=false&serverRsaPublicKeyFile=/var/lib/mysql/public_key.pem", "tracker", "toor" );
            initialized = true;
        } catch (SQLException ex) {
            System.err.println("SQLException encountered while connecting to database");
            System.err.println(ex);

        } catch (ClassNotFoundException e) {
            System.err.println("ClassNotFoundExceptino encoutnered while connecting to database");
            System.err.println(e);
        }
    }

    public static int createNextUser() {
        if (!initialized) {
            return -1;
        }
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
        if (!initialized) {
            return;
        }
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

}
