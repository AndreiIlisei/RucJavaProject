package com.example.demo;

import org.bouncycastle.util.Arrays;

import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import static com.example.demo.Cryptography.decrypt;
import static com.example.demo.Cryptography.encrypt;
import static com.example.demo.CryptographyHelper.*;

public class Encryptor {

    private static SecretKey key;
    private static SecretKey macKey;
    private static SecretKey entranceKey;
    private static byte[] macSalt;
    private static byte[] entranceSalt;
    static String pass_file_path = "/passwordfile.aes";
    static String master_file_path ="/masterfile.aes";
    static String pass_file_writer = "passwordfile.aes";
    static String master_file_writer = "masterfile.aes";
    static Cryptography cryptography = new Cryptography();

    private static void printByteArr(byte[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.printf(i == 0 ? "%d" : ",%d", (arr[i] & 0xFF));
        }
        System.out.println("]");
    }

    public static void createAccount(String password) throws Exception {
        String username = "test username";
        String useremail = "test@email.test";
        byte[] placeholder = {};

        String master_passwd_path = System.getProperty("user.dir");
        master_passwd_path += pass_file_path;
        Path path = Paths.get(master_passwd_path);

        byte[] data = Files.readAllBytes(path);
        byte[] encryptedData = Arrays.copyOfRange(data, 320, data.length);
        byte[] decrypt = decrypt(encryptedData, entranceKey);

        if (accountHandler(username, useremail, decrypt) == null) {

            String account = username + " " + useremail + " " + password + "!";
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

    private static boolean passwordCheck(String entry) throws
            FileNotFoundException,
            IOException,
            NoSuchAlgorithmException,
            NoSuchProviderException {
        //get contents
        String master_passwd_path = System.getProperty("user.dir");
        master_passwd_path += master_file_path;
        Path path = Paths.get(master_passwd_path);
        byte[] contents = Files.readAllBytes(path);

        //get salt and password as bytes for comparison
        byte[] salt = Arrays.copyOf(contents, 256);
        byte[] password = entry.getBytes();

        //concatenate the salt and the password then hash it
        byte[] salted_password = Arrays.concatenate(salt, password);
        byte[] hashed = hash(salted_password);

        return (Arrays.areEqual(contents, Arrays.concatenate(salt, hashed)));
    }

    private static void getPass() throws Exception {
        String username = "test username";
        String pass_file = System.getProperty("user.dir");
        pass_file += pass_file_path;
        Path path = Paths.get(pass_file);
        byte[] data = Files.readAllBytes(path);

        byte[] encrypted = Arrays.copyOfRange(data, 320, data.length);
        byte[] decrypted = decrypt(encrypted, entranceKey);

        String datastring = new String(decrypted, StandardCharsets.UTF_8);
        System.out.println(datastring);
//        String[] acc = datastring.split("!");
//        String id = username;
//        for (String accounts : acc) {
//            //  if (accounts.contains(id)) {
//            String[] accArrray = accounts.split(" ");
//
//            System.out.println("Username: " + accArrray[0] + " " + accArrray[1]);
////            }else {
////                System.out.println("no account found");
////            }
//        }
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

    public static void setup() throws Exception {

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
        String master_passwd = "DetHerErMasterPassword";
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
        }
    }

    private static void startup() throws Exception {

        String passwd_file_string = System.getProperty("user.dir");
        passwd_file_string += pass_file_path;
        Path pPath = Paths.get(passwd_file_string);
        byte[] passwd_file_data = Files.readAllBytes(pPath);

        String master_passwd_string = System.getProperty("user.dir");
        master_passwd_string += master_file_path;
        Path mPath = Paths.get(master_passwd_string);
        byte[] master_passwd_data = Files.readAllBytes(mPath);

        String master_passwd = "DetHerErMasterPassword";

        macSalt = Arrays.copyOf(passwd_file_data, 256);
        entranceSalt = Arrays.copyOf(master_passwd_data, 256);

        entranceKey = generateKey(master_passwd, entranceSalt);
        macKey = generateKey(master_passwd, macSalt);

        byte[] lastHmac = Arrays.copyOfRange(passwd_file_data, 256, 320);
        byte[] encrypted = Arrays.copyOfRange(passwd_file_data, 320, passwd_file_data.length);
        byte[] currentHmac = hmac(encrypted, macKey);

        if (Arrays.areEqual(lastHmac, currentHmac)) {
        } else {
            System.out.println("INTEGRITY CHECK OF PASSWORD FILE FAILED\n");
        }
    }

    public static void main(String[] args) throws Exception {

        if (!fileCheck()) {
            setup();
        } else {
            boolean diditpass = passwordCheck("DetHerErMasterPassword");
            if (!diditpass) {
                System.out.println("false");
            } else {
                System.out.println("true");
                startup();
                createAccount("stef");
            }
        }
        getPass();
    }
}