package com.example.demo;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.bouncycastle.util.Arrays;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.demo.Cryptography.decrypt;
import static com.example.demo.Cryptography.encrypt;
import static com.example.demo.CryptographyHelper.*;

public class Encryptor implements Initializable {
    @FXML
    private PasswordField tf_password;
    @FXML
    private PasswordField create_pass;

    @FXML
    private TableView<TableViewModel> mainTableView;

    @FXML
    private TableColumn<TableViewModel, String> userNameColumn;

    @FXML
    private TableColumn<TableViewModel, String> emailColumn;

    @FXML
    private TableColumn<TableViewModel, String> password;

    @FXML
    private TableColumn<TableViewModel, String> website;

    @FXML
    private TableColumn<TableViewModel, String> notes;

    @FXML
    public TextField userNameFx;

    @FXML
    public TextField emailFx;

    @FXML
    public TextField passwordFx;

    @FXML
    public TextField websiteFx;

    @FXML
    public TextField notesFx;

    private static SecretKey macKey;
    private static SecretKey entranceKey;
    private static byte[] macSalt;
    private static byte[] entranceSalt;
    static String pass_file_path = "/passwordfile.aes";
    static String master_file_path = "/masterfile.aes";
    static String pass_file_writer = "passwordfile.aes";
    static String master_file_writer = "masterfile.aes";




    // Change of scenes
    public void goBackToLogin(ActionEvent actionEvent) throws IOException {
        Main m = new Main();
        m.changeScene("loggin.fxml");
    }

    public void ChangeSceneToAccountCreation(ActionEvent actionEvent) throws IOException {
        Main m = new Main();
        m.changeScene("sign-up.fxml");
    }



    // Functions
    private static void deleteAccount(String domain, String username) throws Exception{
        //get data from passwd_file
        String pass_file = System.getProperty("user.dir");
        pass_file += pass_file_path;
        Path path = Paths.get(pass_file);
        byte[] data = Files.readAllBytes(path);

        //strip hmac and salt
        byte[] encrypted_data = Arrays.copyOfRange(data, 320, data.length);
        byte[] decrypted = decrypt(encrypted_data, entranceKey);

        //check if account exists
        if (accountHandler(domain, username, decrypted) != null) {
            String dataString = new String(decrypted, StandardCharsets.UTF_8);
            String[] accounts = dataString.split("!");
            String account = domain + " " + username;
            //search through accounts and delete account
            for (int i = 0; i < accounts.length; i++) {
                if (accounts[i].contains(account)) {
                    accounts[i] = null;
                    break;
                }
            }

            //rebuild list of accounts by filling the removed
            String newAccList = "";
            for (int i = 0; i < accounts.length; i++) {
                if (accounts[i] != null) {
                    newAccList += accounts[i] + "!";
                }
            }

            //turn accounts list back into bytes
            byte[] bytesData = newAccList.getBytes(StandardCharsets.UTF_8);

            //encrypt data
            byte[] encrypted = encrypt(bytesData, entranceKey);

            //generate salt, hmac, and append data
            byte[] hmac = hmac(encrypted, macKey);
            byte[] salt_hmac_and_encrypted = Arrays.concatenate(macSalt, hmac, encrypted);

            //write to file
            try (FileOutputStream output = new FileOutputStream(pass_file_writer)) {
                output.write(salt_hmac_and_encrypted);
                output.close();
                System.out.println("USER ACCOUNT REMOVED!\n");
            }
        } else {//account not found
            System.out.println("USER ACCOUNT DOES NOT EXIST!\n");
        }
    }



    private static void changeAccount(String domain, String username, String password) throws Exception {

        String pass_file = System.getProperty("user.dir");
        pass_file += pass_file_path;
        Path path = Paths.get(pass_file);
        byte[] data = Files.readAllBytes(path);

        //strip hmac
        byte[] encrypted_data = Arrays.copyOfRange(data, 320, data.length);
        byte[] decrypted = decrypt(encrypted_data, entranceKey);

        //perform account change
        if (accountHandler(domain, username, decrypted) != null) {
            String dataString = new String(decrypted, StandardCharsets.UTF_8);
            String[] accounts = dataString.split("!");
            String account = domain + " " + username;
            String updated = domain + " " + username + " " + password;
            //search through accounts and delete account
            for (int i = 0; i < accounts.length; i++) {
                if (accounts[i].contains(account)) {
                    accounts[i] = updated;
                    break;
                }
            }
            //rebuild list of accounts and change to byte[]
            String newAccList = "";
            for (String acc : accounts) {
                newAccList += acc + "!";
            }
            byte[] bytesData = newAccList.getBytes(StandardCharsets.UTF_8);

            //encrypt new data
            byte[] encrypted = encrypt(bytesData, entranceKey);

            //generate new hmac and append
            byte[] hmac = hmac(encrypted, macKey);
            byte[] salt_hmac_and_encrypted = Arrays.concatenate(macSalt, hmac, encrypted);

            //write to file
            try (FileOutputStream output = new FileOutputStream(pass_file_writer)) {
                output.write(salt_hmac_and_encrypted);
                output.close();
                System.out.println("USER ACCOUNT UPDATED!\n");
            }
        } else {//account not found
            System.out.println("USER ACCOUNT DOES NOT EXIST!\n");
        }
    }
    public void getAddInformation(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("addInformation.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Encryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    public void save(javafx.scene.input.MouseEvent mouseEvent) throws Exception{
        Main m = new Main();

        if (userNameFx.getText().isEmpty() || emailFx.getText().isEmpty() || website.getText().isEmpty() || password.getText().isEmpty() || notes.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Fill All DATA");
            alert.showAndWait();

        } else {
            //getQuery();
            createAccount(userNameFx.getText(), emailFx.getText(), passwordFx.getText());
        }
    }

    public static void createAccount(String domain, String username, String password) throws Exception {
        String pass_file = System.getProperty("user.dir");
        pass_file += pass_file_path;
        Path path = Paths.get(pass_file);

        byte[] data = Files.readAllBytes(path);
        byte[] encryptedData = Arrays.copyOfRange(data, 320, data.length);
        byte[] decrypt = decrypt(encryptedData, entranceKey);

        if (accountHandler(domain, username, decrypt) == null) {

            String account = domain + " " + username + " " + password + "!";
            byte[] accountBytes = account.getBytes(StandardCharsets.UTF_8);
            byte[] encrypted = encrypt(accountBytes, entranceKey);

            byte[] hmac = hmac(encrypted, macKey);
            byte[] salt_mac_encrypt = Arrays.concatenate(macSalt, hmac, encrypted);

            try (FileOutputStream output = new FileOutputStream(pass_file_writer)) {
                output.write(salt_mac_encrypt);
                output.close();
                System.out.println("Registered");
            }
            getPass(domain);
        } else {
            System.out.println("Already registered");
        }
    }

    private static String accountHandler(String username, String useremail, byte[] decrypted) throws Exception {
        String dataString = new String(decrypted, StandardCharsets.UTF_8);
        String[] accounts = dataString.split("!");
        String id = username + " " + useremail;

        for (String account : accounts) {
            if (account.contains(id)) {
                return account;
            }
        }
        return null;
    }

    private static boolean passwordCheck(String password) throws Exception{
        //get contents
        String master_passwd_path = System.getProperty("user.dir");
        master_passwd_path += master_file_path;
        Path path = Paths.get(master_passwd_path);
        byte[] contents = Files.readAllBytes(path);

        //get salt and password as bytes for comparison
        byte[] salt = Arrays.copyOf(contents, 256);
        byte[] password_bytes = password.getBytes();

        //concatenate the salt and the password then hash it
        byte[] salted_password = Arrays.concatenate(salt, password_bytes);
        byte[] hashed = hash(salted_password);

        return (Arrays.areEqual(contents, Arrays.concatenate(salt, hashed)));
    }

    private static void getPass(String domain) throws Exception {
        String pass_file = System.getProperty("user.dir");
        pass_file += pass_file_path;
        Path path = Paths.get(pass_file);
        byte[] data = Files.readAllBytes(path);

        byte[] encrypted = Arrays.copyOfRange(data, 320, data.length);
        byte[] decrypted = decrypt(encrypted, entranceKey);

        String datastring = new String(decrypted, StandardCharsets.UTF_8);
        System.out.println(datastring);
        String[] acc = datastring.split("!");
     //   String id = domain;
        for (String accounts : acc) {
          //  if (accounts.contains(id)) {
                String[] accArrray = accounts.split(" ");
                System.out.println(accArrray[0]);
        //    } else {
           //     System.out.println("no account found");
         //   }
        }
    }

    private static boolean fileCheck() {
        //Used for passwd_file path
        String passwd_file_path = System.getProperty("user.dir");
        passwd_file_path += pass_file_path;

        //Used for master_passwd path
        String master_passwd_path = System.getProperty("user.dir");
        master_passwd_path += master_file_path;

        File passwd_file = new File(passwd_file_path);
        File master_passwd = new File(master_passwd_path);

        return (passwd_file.exists() && master_passwd.exists());
    }

    public void createMasterPassword(ActionEvent event) throws Exception {
        setup(create_pass.getText());
    }
    public static void setup(String master_passwd) throws Exception {
        Main m = new Main();
        String passwd_file_string = System.getProperty("user.dir");
        passwd_file_string += pass_file_path;
        //Used for master_passwd path
        String master_passwd_string = System.getProperty("user.dir");
        master_passwd_string += master_file_path;

        //initialize file paths
        Path passwd_file_path = Paths.get(passwd_file_string);
        Path master_passwd_path = Paths.get(master_passwd_string);

        //initialize files
        File master_passwd_file = new File(master_passwd_string);
        File passwd_file = new File(passwd_file_string);

        //delete old files if they exists
        Files.deleteIfExists(passwd_file_path);
        Files.deleteIfExists(master_passwd_path);

        //create files
        passwd_file.createNewFile();
        master_passwd_file.createNewFile();

        //get master password
//         master_passwd = "DetHerErMasterPassword";
        byte[] password = master_passwd.getBytes();

        //get salts and combine with master password
        entranceSalt = randomNumberGenerator(256);
        macSalt = randomNumberGenerator(256);
        byte[] salted_password = Arrays.concatenate(entranceSalt, password);

        //setup master_passwd file
        byte[] hash = hash(salted_password);
        byte[] salt_and_hash = Arrays.concatenate(entranceSalt, hash);

        //write data to master_passwd file
        try (FileOutputStream output = new FileOutputStream(master_file_writer)) {
            output.write(salt_and_hash);
            output.close();
        }

        entranceKey = generateKey(master_passwd, entranceSalt);
        macKey = generateKey(master_passwd, macSalt);


        //get hash for passwd_file and append to file
        byte[] passwd_file_data = Files.readAllBytes(passwd_file_path);
        byte[] encrypted = encrypt(passwd_file_data, entranceKey);
        byte[] hmac = hmac(encrypted, macKey);
        byte[] salt_hmac_and_encrypted = Arrays.concatenate(macSalt, hmac, encrypted);


        //write data to passwd_file
        try (FileOutputStream output = new FileOutputStream(pass_file_writer)) {
            output.write(salt_hmac_and_encrypted);
            output.close();
            System.out.println("This worked");
            m.changeScene("loggedIn.fxml");

        }
    }

    // Action buttons
    public void loginFromMasterPass(javafx.event.ActionEvent event) throws Exception{
        startup(tf_password.getText());
    }
    private static void startup(String master_passwd) throws Exception {
        Main m = new Main();

        String passwd_file_string = System.getProperty("user.dir");
        passwd_file_string += pass_file_path;
        Path pPath = Paths.get(passwd_file_string);
        byte[] passwd_file_data = Files.readAllBytes(pPath);

        String master_passwd_string = System.getProperty("user.dir");
        master_passwd_string += master_file_path;
        Path mPath = Paths.get(master_passwd_string);
        byte[] master_passwd_data = Files.readAllBytes(mPath);

        macSalt = Arrays.copyOf(passwd_file_data, 256);
        entranceSalt = Arrays.copyOf(master_passwd_data, 256);

        entranceKey = generateKey(master_passwd, entranceSalt);
        macKey = generateKey(master_passwd, macSalt);

        byte[] lastHmac = Arrays.copyOfRange(passwd_file_data, 256, 320);
        byte[] encrypted = Arrays.copyOfRange(passwd_file_data, 320, passwd_file_data.length);
        byte[] currentHmac = hmac(encrypted, macKey);

        if (Arrays.areEqual(lastHmac, currentHmac)) {
            System.out.println("INTEGRITY CHECK OF PASSWORD FILE SUCCESS");
                                    m.changeScene("loggedIn.fxml");

        } else {
            System.out.println("INTEGRITY CHECK OF PASSWORD FILE FAILED\n");
            Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Wrong credentials");
                        alert.show();
        }
    }


    public static void main(String[] args) throws Exception {

        if (!fileCheck()) {
            setup("DetHerErMasterPassword");
        } else {
            boolean diditpass = passwordCheck("DetHerErMasterPassword");
            if (!diditpass) {
                System.out.println("false");
            } else {
                System.out.println("true");
                startup("DetHerErMasterPassword");
                createAccount("stef", "Steffen", "DetHerErMasterPassword");
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new TableViewModel("stef","stef","stef","stef","stef");

        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userNameColumn"));
        mainTableView.setItems(tableView);
    }
    private ObservableList<TableViewModel> tableView = FXCollections.observableArrayList(
            new TableViewModel("stef","stef","stef","stef","stef")


    );
}