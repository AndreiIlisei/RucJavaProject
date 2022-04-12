package com.encryption.KeyStores.NoUse;

import java.io.Serializable;

public class PasswordRecord implements Serializable {

    public PasswordRecord(String site, String url, String username, String password) {
        this.site = site;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    String site, url, username, password;

    public static void printLabels() {
        System.out.println("Site           URL            username       password");
        System.out.println("-----------------------------------------------------");
    }

    public void print() {
        // site
        System.out.print(site);
        int spaces = 15 - site.length();
        int s;
        for (s = 0; s < spaces; s++) System.out.print(" ");
        // url
        System.out.print(url);
        spaces = 15 - url.length();
        for (s = 0; s < spaces; s++) System.out.print(" ");
        // username
        System.out.print(username);
        spaces = 15 - username.length();
        for (s = 0; s < spaces; s++) System.out.print(" ");
        // password
        System.out.print(password);
        spaces = 15 - password.length();
        for (s = 0; s < spaces; s++) System.out.print(" ");
        System.out.println("");
    }
}
