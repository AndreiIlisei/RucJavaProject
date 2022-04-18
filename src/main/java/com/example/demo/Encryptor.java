package com.example.demo;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
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
    static String dir = "D:\\RUC";
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

    public static byte[] encrypt(String file, SecretKey entranceKey) throws Exception {
        byte[] IV = {0x01, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
        key = generateKey();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5padding", "BC");
        SecureRandom csprng = new SecureRandom();
        byte[] randomBytes = new byte[cipher.getBlockSize()];
        csprng.nextBytes(randomBytes);
        //String ivstr = Arrays.toString(randomBytes);
        //iv = ivstr.getBytes(Charset.defaultCharset());
        IvParameterSpec ivParam = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, key, ivParam);
        byte[] encrypted = cipher.doFinal(file.getBytes());
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
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");

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

        Path path = Paths.get("user.dir\\passfile.txt");
        byte[] data = Files.readAllBytes(path);
        byte[] encryptedData = Arrays.copyOfRange(data, 320, data.length);
        byte[] decrypt = decrypt(encryptedData, entranceKey);

        if(accountHandler(username, useremail, decrypt) == null) {

            byte[] encrypted = encrypt(password, entranceKey);

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


    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        String master_password = "masterPassword";
        Path path = Paths.get("user.dir\\masterfile.txt");
        byte[] master_pass = Files.readAllBytes(path);

        entranceSalt = Arrays.copyOf(master_pass, 256);

        entranceKey = generateKey(master_password, entranceSalt);


        createAccount("test");
    }
}