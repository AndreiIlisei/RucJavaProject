package com.encryption.KeyStores.NoUse;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test extends Application {
    public static char[] getUserPassword()
            throws IOException
    {
        // Enter data using BufferReader
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // Reading data using readLine
        String password = reader.readLine();

        // Printing the read line
        System.out.println("Password is:" + password);
        return new char[0];
    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}
