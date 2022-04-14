package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

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
import java.sql.*;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaPostgreSql {

    static Encryptor encryptor = new Encryptor();

    static byte[] encryptionKey = {65, 12, 12, 12, 12, 12, 12, 12, 12,
            12, 12, 12, 12, 12, 12, 12 };

    static File file = new File("data.txt");

    static HashMap<String, String> loginInfo = new HashMap<>();
    

    static void writeToFile(String userName, String userEmail, String userPassword) throws IOException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));

       // writer.write(userName + "," + encryptor.encrypt(userPassword,encryptionKey) + "\n");
        writer.close();
    }

    // Creating the account into mysql
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

    // Login from the file data
    @FXML
    static void loginHandler(ActionEvent event, String userName, String userPassword) throws IOException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        updateUsernamesAndPasswords();

//        String encryptedPassword = loginInfo.get(userName);
//        if(userPassword.equals(encryptor.decrypt(encryptedPassword,encryptionKey))){
//            System.out.println("successfully login!");
//        } else {
//            System.out.println("successfully not login!");
//        }
    }

    private static void updateUsernamesAndPasswords() throws IOException {
        Scanner scanner = new Scanner(file);
        loginInfo.clear();
        loginInfo = new HashMap<>();
        while (scanner.hasNext()){
            String[] splitInfo = scanner.nextLine().split(",");
            loginInfo.put(splitInfo[0],splitInfo[1]);
        }
    }


    // Login into my sql Database
    public static void loginUser(ActionEvent event, String userName, String userPassword) throws IOException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Main m = new Main();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/PFSExam?currentSchema=PFSExam", "postgres", "new_password");
            preparedStatement = connection.prepareStatement("SELECT userpassword from public.\"WriteToDataBase\" where username = ?");
            preparedStatement.setString(1, userName);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("User not found in database");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Wrong credentials");
                alert.show();
            } else {
                while (resultSet.next()) {
                    String retrievedPassword = resultSet.getString("userpassword");
//                    if (retrievedPassword.equals(encryptor.decrypt(userPassword, encryptionKey))) {
//                        m.changeScene("loggedIn.fxml");
//                    } else {
//                        System.out.println("Password did not match!");
//                        Alert alert = new Alert(Alert.AlertType.ERROR);
//                        alert.setContentText("Wrong credentials");
//                        alert.show();
//                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InvalidAlgorithmParameterException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    private String getPassword() {
//        if (passwordTextField.isVisible()) {
//            return passwordTextField.getText();
//        } else {
//            return hiddenPasswordTextField.getText();
//        }
//    }
//
//    private void updateUsernamesAndPasswords() throws IOException {
//        Scanner scanner = new Scanner(file);
//        loginInfo.clear();
//        loginInfo = new HashMap<>();
//        while (scanner.hasNext()) {
//            String[] splitInfo = scanner.nextLine().split(",");
//            loginInfo.put(splitInfo[0], splitInfo[1]);
//        }
//    }
}




//    public static void changeScene(ActionEvent event, String fxmlFile, String Title, String userName, String userEmail) {
//        Parent root = null;
//
//        if (userName != null && userEmail != null) {
//            try {
//                FXMLLoader loader = new FXMLLoader(JavaPostgreSql.class.getResource(fxmlFile));
//                root = loader.load();
//                LoggedInController loggedInController = loader.getController();
//                loggedInController.setUserInfo(userName, userEmail);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

