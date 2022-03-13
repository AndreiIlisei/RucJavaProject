package com.example.demo;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaPostgreSql {

    public static void writeToDatabase(String userName, String userEmail, String userPassword) {

        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "";

        String username = userName;
        String useremail = userEmail;
        String userpassword = userPassword;

        // query
        String query = "INSERT INTO javafx(username, useremail, userpassword) VALUES(?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, username);
            pst.setString(2, useremail);
            pst.setString(3, userpassword);
            pst.executeUpdate();
            System.out.println("Sucessfully created.");

        } catch (SQLException ex) {
            System.out.println("Sucessfully not.");
            Logger lgr = Logger.getLogger(JavaPostgreSql.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

    }
}