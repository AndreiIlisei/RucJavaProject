package com.example.demo;

public class StoredInfo {
    int id;
    String userName;
     String email;
     String website;
     String password;
     String notes;

    public StoredInfo(int id, String userName, String email, String website, String password, String notes) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.website = website;
        this.password = password;
        this.notes = notes;
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