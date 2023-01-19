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
            System.out.println("Successfully connected to database");
        } catch (SQLException ex) {
            System.err.println("SQLException encountered while connecting to database");
            System.err.println(ex);

        } catch (ClassNotFoundException e) {
            System.err.println("ClassNotFoundException encountered while connecting to database");
            System.err.println(e);
        }
    }



}
