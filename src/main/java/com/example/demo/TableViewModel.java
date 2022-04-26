package com.example.demo;

import javafx.beans.property.SimpleStringProperty;

public class TableViewModel {

    private SimpleStringProperty userNameColumn;
    private String emailColumn;
    private String password;
    private String website;
    private String notes;


    public TableViewModel(String  userNameColumn, String email, String password, String website, String notes) {
        this.userNameColumn = new SimpleStringProperty(userNameColumn);
        this.emailColumn = email;
        this.password = password;
        this.website = website;
        this.notes = notes;
    }

    public TableViewModel(SimpleStringProperty userNameColumn) {
        this.userNameColumn = userNameColumn;
    }

    public String getUserNameColumn() {
        return userNameColumn.get();
    }

    public SimpleStringProperty userNameColumnProperty() {
        return userNameColumn;
    }

    public void setUserNameColumn(String userNameColumn) {
        this.userNameColumn.set(userNameColumn);
    }
}
