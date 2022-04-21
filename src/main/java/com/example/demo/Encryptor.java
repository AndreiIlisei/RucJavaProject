package com.example.demo;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.Arrays;

import java.nio.file.NoSuchFileException;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
public class Encryptor {


    //Advanced Encryption Standard
    //128 bit
    byte[] IV = {0x01, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
    private static byte[] iv;
    private static SecretKey key;
    private static SecretKey macKey;
    private static SecretKey entranceKey;
    //entranceKey = generateKey(master_passwd, encSalt);

    private static byte[] macSalt;

    private static final int keyLength = 32;
    private static final SecureRandom random = new SecureRandom();
    static String dir = "C:\\Users\\Steffen Giessing\\Desktop\\PFSExam\\RucJavaProject\\src\\main\\java\\com\\encryption";
    static String fileName = dir + "\\passfile.txt";
    private static byte[] entranceSalt;

    public static SecretKey generateKey(String password, byte[] salt) throws
            FileNotFoundException,
            IOException,
            NoSuchProviderException,
            InvalidKeySpecException,
            NoSuchAlgorithmException {
        Security.addProvider(new BouncyCastleProvider());
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256", "BC");

        //generate key
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKey tmpKey = factory.generateSecret(spec);
        SecretKey key = new SecretKeySpec(tmpKey.getEncoded(), "AES");
        return key;
    }

    private static SecretKey generateKey() throws Exception {
        byte[] keyBytes = new byte[keyLength];
        random.nextBytes(keyBytes);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        return keySpec;
    }

    public static byte[] encrypt(byte[] file, SecretKey entranceKey) throws Exception {
        key = generateKey();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5padding", "BC");
        SecureRandom csprng = new SecureRandom();
        byte[] randomBytes = new byte[cipher.getBlockSize()];
        csprng.nextBytes(randomBytes);
        //String ivstr = Arrays.toString(randomBytes);
        //iv = ivstr.getBytes(Charset.defaultCharset());
        IvParameterSpec ivParam = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, key, ivParam);
        byte[] encrypted = cipher.doFinal(file);
        byte[] iv_encrypted = Arrays.concatenate(randomBytes, encrypted);
        System.out.println(Base64.getEncoder().withoutPadding().encodeToString(key.getEncoded()));
        printByteArr(iv);

        return iv_encrypted;
    }

    private static void printByteArr(byte[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.printf(i == 0 ? "%d" : ",%d", (arr[i] & 0xFF));
        }
        System.out.println("]");
    }

    public static byte[] decrypt(byte[] cipherText, SecretKey key) throws Exception {

        printByteArr(cipherText);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5padding", "BC");

        byte[] iv = new byte[cipher.getBlockSize()];
        iv = Arrays.copyOf(cipherText, iv.length);
        IvParameterSpec ivParam = new IvParameterSpec(iv);

        byte[] encrypted = Arrays.copyOfRange(cipherText, iv.length, cipherText.length);

        cipher.init(Cipher.DECRYPT_MODE, key, ivParam);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;

    }
    public static void createAccount(String password) throws Exception {
        String username = "test username";
        String useremail = "test@email.test";
        byte[] placeholder = {};
        //Windows path
        //C:\Users\Steffen Giessing\Desktop\PFSExam\RucJavaProject\src\main\java\com\example\demo\dir\passfile.txt
        //Mac path
        ///Users/steffensiltamiesgiessing/Desktop/SecurityExam/RucJavaProject/src/main/java/com/example/demo/dir/masterfile.aes
        Path path = Paths.get("/Users/steffensiltamiesgiessing/Desktop/SecurityExam/RucJavaProject/src/main/java/com/example/demo/dir/masterfile.aes");
        byte[] data = Files.readAllBytes(path);
        byte[] encryptedData = Arrays.copyOfRange(data, 320, data.length);
        byte[] decrypt = decrypt(encryptedData, entranceKey);

        if(accountHandler(username, useremail, decrypt) == null) {

            byte[] encrypted = encrypt(placeholder, entranceKey);

            byte[] hmac = hmac(encrypted, macKey);
            byte[] salt_mac_encrypt = Arrays.concatenate(macSalt, hmac, encrypted);

            try(FileOutputStream output = new FileOutputStream("passwordFile")){
                output.write(salt_mac_encrypt);
                output.close();
                System.out.println("Registered");
            }
        } else {
            System.out.println("Already registered");
        }
    }
    private static String accountHandler(String username, String useremail, byte[] decrypted) throws Exception{
        String dataString = new String(decrypted, "UTF-8");
        String[] accounts = dataString.split("!");
        String id = username + " " + useremail;

        for (String account : accounts){
            if(account.contains(id)){
                return account;
            }
        }

        return null;
    }


    public static byte[] hmac(byte[] input,SecretKey key) throws Exception{

        Security.addProvider(new BouncyCastleProvider());

        Mac mac = Mac.getInstance("HmacSHA512", "BC");

        mac.init(key);

        return mac.doFinal(input);
    }
    public static byte[] randomNumberGenerator(int size) throws NoSuchAlgorithmException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());

        SecureRandom rand = new SecureRandom();
        byte[] data = new byte[size];
        rand.nextBytes(data);
        return data;
    }
    public static byte[] hash(byte[] message) throws
            NoSuchAlgorithmException,
            NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());

        MessageDigest mda = MessageDigest.getInstance("SHA-512", "BC");
        return mda.digest(message);
    }

    private static boolean passwordCheck(String entry) throws
            FileNotFoundException,
            IOException,
            NoSuchAlgorithmException,
            NoSuchProviderException {
        //get contents
        String master_passwd_path = System.getProperty("user.dir");
        master_passwd_path += "/masterfile.aes";
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

    public static void main(String[] args) throws Exception {

        if (!fileCheck()) {
            setup();
        } else {
            boolean diditpass = passwordCheck("DetHerErMasterPassword");
            if (!diditpass) {
                System.out.println("false");
            } else {
                System.out.println("true");
            }

            // passwordCheck("steffen");

//        byte[] master_passwd_data = Files.readAllBytes(master_passwd_path);

            //entranceSalt = Arrays.copyOf(master_passwd_data, 256);


/*
        Security.addProvider(new BouncyCastleProvider());
        byte[] keybytes = Hex.decode("000102030405060708090a0b0c0d0e0f");

        String master_password = "masterPassword";
        Path path = Paths.get("/Users/steffensiltamiesgiessing/Desktop/SecurityExam/RucJavaProject/src/main/java/com/example/demo/dir/masterfile.aes");

        //Steffen Windows path
        //C:\Users\Steffen Giessing\Desktop\PFSExam\RucJavaProject\src\main\java\com\example\demo\dir\masterfile.aes
        //STEFFEN MAC's PATH
        ///Users/steffensiltamiesgiessing/Desktop/SecurityExam/RucJavaProject/src/main/java/com/example/demo/dir/masterfile.aes

        byte[] master_pass = Files.readAllBytes(path);

        entranceSalt = Arrays.copyOf(master_pass, 256);

        entranceKey = generateKey(master_password, entranceSalt);

*/


            //  createAccount("test");
            //  byte[] cipherText = encrypt(fileName);
            //String getText = decrypt(cipherText);
            //   System.out.println(getText);
            //128 bit
//        byte[] encryptionKey = {65, 12, 12, 12, 12, 12, 12, 12, 12,
//                12, 12, 12, 12, 12, 12, 12 };
//
//        String stringKey = "65 12 12 12 12 12 12 12 12 12 12 12 12 12 12 12";
//
//
//        byte[] key = encryptor.stringToByteArray(stringKey);
//
//        String input = "Secret";
//
//        System.out.println(encryptor.encrypt(input,key));
//        //output: VyEcl0pLeqQLemGONcik0w==
//
//        System.out.println(encryptor.decrypt("VyEcl0pLeqQLemGONcik0w==",key));
        }
    }
    private static boolean fileCheck() {
        //Used for passwd_file path
        String passwd_file_path = System.getProperty("user.dir");
        passwd_file_path += "/passwordfile.aes";

        //Used for master_passwd path
        String master_passwd_path = System.getProperty("user.dir");
        master_passwd_path += "/masterfile.aes";

        File passwd_file = new File(passwd_file_path);
        File master_passwd = new File(master_passwd_path);

        return (passwd_file.exists() && master_passwd.exists());
    }



    public static void setup () throws Exception, NoSuchFileException {

        String passwd_file_string = System.getProperty("user.dir");
        passwd_file_string += "/passwordfile.aes";
        //Used for master_passwd path
        String master_passwd_string = System.getProperty("user.dir");
        master_passwd_string += "/masterfile.aes";

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
        try (FileOutputStream output = new FileOutputStream("masterfile.aes")) {
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
        try (FileOutputStream output = new FileOutputStream("passwordfile.aes")) {
            output.write(salt_hmac_and_encrypted);
            output.close();
        }


    }
}