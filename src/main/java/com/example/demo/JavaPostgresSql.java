package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Scanner;

public class JavaPostgresSql {

    static File file = new File("data.txt");
    static HashMap<String, String> loginInfo = new HashMap<>();

    static void writeToFile(String userName, String userEmail, String userPassword) throws IOException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));

        writer.write(userName + "," + userPassword + "\n");
        writer.close();
    }

    // Login from the file data
    @FXML
    static void loginHandler(ActionEvent event, String userName, String userPassword) throws IOException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        updateUsernamesAndPasswords();

        String encryptedPassword = loginInfo.get(userName);
        if (userPassword.equals(userPassword)) {
            System.out.println("successfully login!");
        } else {
            System.out.println("successfully not login!");
        }
    }

    private static void updateUsernamesAndPasswords() throws IOException {
        Scanner scanner = new Scanner(file);
        loginInfo.clear();
        loginInfo = new HashMap<>();
        while (scanner.hasNext()) {
            String[] splitInfo = scanner.nextLine().split(",");
            loginInfo.put(splitInfo[0], splitInfo[1]);
        }
    }
}
