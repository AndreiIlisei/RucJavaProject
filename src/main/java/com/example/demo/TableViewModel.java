package com.example.demo;

public class TableViewModel {

    private String userNameColumn;
    private String emailColumn;
    private String password;
    private String website;
    private String notes;


    public TableViewModel(String username, String email, String password, String website, String notes) {
        this.userNameColumn = username;
        this.emailColumn = email;
        this.password = password;
        this.website = website;
        this.notes = notes;
    }

    public String getUsername() {
        return userNameColumn;
    }

    public void setUsername(String username) {
        this.userNameColumn = username;
    }

    public String getEmail() {
        return emailColumn;
    }

    public void setEmail(String email) {
        this.emailColumn = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
