package com.example.demo;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class JavaPostgreSql {

    public static void writeToDatabase(String userName, String userEmail, String userPassword) {

        String url = "jdbc:postgresql://localhost:5432/PFSExam?currentSchema=PFSExam";
        Properties prop = new Properties();
        prop.setProperty("user", "postgres");
        prop.setProperty("password", Global.ACCOUNT_SID);

        String username = userName;
        String useremail = userEmail;
        String userpassword = userPassword;
        DatabaseConnection dbCon = new DatabaseConnection();
        dbCon.DatabaseConnection();
        // query
        String query = "INSERT INTO public.\"WriteToDataBase\" (username, useremail, userpassword) VALUES(?, ?, ?)";
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(url, prop);
            PreparedStatement stmt = con.prepareStatement(query);

            stmt.setString(1, username);
            stmt.setString(2, useremail);
            stmt.setString(3, userpassword);
            stmt.executeUpdate();
            System.out.println("Sucessfully created.");

        } catch (Exception e) {
            System.out.println("Sucessfully not.");
            Logger lgr = Logger.getLogger(JavaPostgreSql.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}