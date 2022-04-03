package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
;

public class DatabaseConnection {
    public Connection DatabaseConnection() {
        Connection con = null;
        String url = "jdbc:postgresql://localhost:5432/PFSExam?currentSchema=PFSExam";
        Properties prop = new Properties();
        prop.setProperty("user", "postgres");
        prop.setProperty("password", "Secret");

        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, prop);
            if (con == null) {
                System.out.println("Connection is not established");
            }
            return con;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
