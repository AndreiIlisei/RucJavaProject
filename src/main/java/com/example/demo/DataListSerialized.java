package com.example.demo;

import java.io.Serializable;

public class DataListSerialized implements Serializable {
    int id;
    String userName;
    String email;
    String website;
    String password;
    String notes;

    public DataListSerialized(int id, String userName, String email, String website, String password, String notes) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.website = website;
        this.password = password;
        this.notes = notes;
    }

    public static void printLabels() {
        System.out.println("Id           URL            username       password");
        System.out.println("-----------------------------------------------------");
    }

    public void print() {
        // site
        System.out.print(id);
        int spaces = 15 - id;
        int s;
        for (s = 0; s < spaces; s++) System.out.print(" ");
        // url
        System.out.print(userName);
        spaces = 15 - userName.length();
        for (s = 0; s < spaces; s++) System.out.print(" ");
        // username
        System.out.print(email);
        spaces = 15 - email.length();
        for (s = 0; s < spaces; s++) System.out.print(" ");
        // password
        System.out.print(password);
        spaces = 15 - password.length();
        for (s = 0; s < spaces; s++) System.out.print(" ");
        System.out.println("");
        //notes
        System.out.print(notes);
        spaces = 15 - notes.length();
        for (s = 0; s < spaces; s++) System.out.print(" ");
        System.out.println("");
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}