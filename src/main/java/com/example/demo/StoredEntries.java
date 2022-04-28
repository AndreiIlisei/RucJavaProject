package com.example.demo;

import java.io.Serializable;

public class StoredEntries implements Serializable {

    private String domain;
    private String userName;
    private String password;

    public StoredEntries(String domain, String userName, String password) {
        this.domain = domain;
        this.userName = userName;
        this.password = password;
    }

    public StoredEntries() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}