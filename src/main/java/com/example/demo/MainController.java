package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bouncycastle.util.Arrays;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static com.example.demo.Cryptography.decrypt;
import static com.example.demo.Cryptography.encrypt;
import static com.example.demo.CryptographyHelper.*;

public class MainController {
    @FXML
    private PasswordField hiddenPasswordTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private CheckBox showPassword;

    private static SecretKey macKey;
    private static SecretKey entranceKey;
    private static byte[] macSalt;
    private static byte[] entranceSalt;
    static String pass_file_path = "/passwordfile.aes";
    static String master_file_path = "/masterfile.aes";
    static String pass_file_writer = "passwordfile.aes";
    static String master_file_writer = "masterfile.aes";
    private static Stage stg;


    // Action Listeners
    public void createMasterPassword(ActionEvent event) throws Exception {
        setup(hiddenPasswordTextField.getText());
    }

    public void loginWithMasterPass(javafx.event.ActionEvent event) throws Exception {
        startup(hiddenPasswordTextField.getText());
    }

    // Change of scenes
    public void goBackToLogin(ActionEvent actionEvent) throws IOException {
        Main m = new Main();
        m.changeScene("LoginScene.fxml");

    }

    public void ChangeSceneToAccountCreation(ActionEvent actionEvent) throws IOException {
        Main m = new Main();
        m.changeScene("SignUpScene.fxml");
    }

    // Changes - we need to a way to say that the account does not exist, and it should be created
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

//        delete old files if they exists
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
            m.changeScene("LoggedInTableView.fxml");

        }
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
            m.changeScene("LoggedInTableView.fxml");
            getPass();
        } else if (Files.notExists(mPath)) {
            System.out.println("No Account");
        } else {
            System.out.println("INTEGRITY CHECK OF PASSWORD FILE FAILED\n");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Wrong credentials");
            alert.show();
        }
    }

    public static void AddEntriesToTableView(String domain, String username, String password) throws Exception {
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
        } else {
            // A way to not add the already registered one as it does now.
            System.out.println("Already registered");
        }
    }

    // --- HELPERS --- //

    // Creates an id based on the userDomain and userName
    // to recognize the array as unique.
    private static String accountHandler(String domain, String userName, byte[] decrypted) throws Exception {
        String dataString = new String(decrypted, StandardCharsets.UTF_8);
        String[] accounts = dataString.split("!");
        String id = domain + " " + userName;

        for (String account : accounts) {
            if (account.contains(id)) {
                return account;
            }
        }
        return null;
    }

    //It is used as a helper to decrypt the data
    // and retrieve the written entries from the file
    static byte[] getPass() throws Exception {
        String pass_file = System.getProperty("user.dir");
        pass_file += pass_file_path;
        Path path = Paths.get(pass_file);
        byte[] data = Files.readAllBytes(path);
        byte[] encrypted = Arrays.copyOfRange(data, 320, data.length);
        byte[] decrypted = decrypt(encrypted, entranceKey);

        String datastring = new String(decrypted, StandardCharsets.UTF_8);
        String[] acc = datastring.split("!");
        //   String id = domain;
        StoredEntries entries = new StoredEntries();
        LoggedInController controller = new LoggedInController();
        ArrayList<String> arrayList = new ArrayList<>();

        for (String accounts : acc) {

            //  if (accounts.contains(id)) {
            String[] accArrray = accounts.split(" ");
            arrayList.add(accArrray[0]);
            arrayList.add(accArrray[1]);
            arrayList.add(accArrray[2]);
            entries.setUserName(accArrray[0]);
            entries.setPassword(accArrray[1]);
            entries.setDomain(accArrray[2]);
            //controller.load(customer.getName(), customer.getAge(), customer.getNumber());
            controller.getList(arrayList);
            //  System.out.println(accArrray[0] + " " + accArrray[1] + " " + accArrray[2]);
            //    } else {
            //     System.out.println("no account found");
            //   }
        }
        return data;
    }

    @FXML
    void changeVisibility(ActionEvent event) {
        if (showPassword.isSelected()) {
            passwordTextField.setText(hiddenPasswordTextField.getText());
            passwordTextField.setVisible(true);
            hiddenPasswordTextField.setVisible(false);
            return;
        }
        hiddenPasswordTextField.setText(passwordTextField.getText());
        hiddenPasswordTextField.setVisible(true);
        passwordTextField.setVisible(false);
    }


    private static void deleteAccount(String domain, String username) throws Exception {
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


    // Do we use this for anything?
    private static boolean passwordCheck(String password) throws Exception {
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
}