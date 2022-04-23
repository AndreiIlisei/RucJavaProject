package com.example.demo;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.security.KeyStore;
import java.security.Security;

public class NewKeyStore {
    private static final String UNICODE_FORMAT = "UTF-8";
    private static final String keyName = "MainKey";

    public static void main(String[] args) {

        try {
            // First
            Security.addProvider(new BouncyCastleProvider());
            SecretKey key = generateKey("AES");
            storeToKeyStore(key, getUserPassword(), getText(), "D:\\RUC\\keystore");
            Cipher cipher;
            cipher = Cipher.getInstance("AES");

           byte[] encryptedData = encryptEntries("random", key, cipher);
            String encryptedString = new String(encryptedData);
            System.out.println(encryptedString);

            String decrypted = decryptEntries(encryptedData, key, cipher);
            System.out.println(decrypted);

            // Second
//            SecretKey key = LoadFromKeyStore("D:\\RUC\\keystore", getUserPassword());

//            byte[] encryptedContent = Files.readAllBytes(Path.of("D:\\RUC\\keystore"));
//            System.out.println(encryptedContent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getText()
            throws IOException
    {
        // Enter data using BufferReader
        System.out.println("Enter some random text" );

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // Reading data using readLine
        String text = reader.readLine();

        // Printing the read line
        System.out.println("The text is:" + text);
        return text;
    }

//
//    public static String getText()
//            throws IOException
//    {
//        // Enter data using BufferReader
//        System.out.println("Enter some random text" );
//
//        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//
//        // Reading data using readLine
//        String text = reader.readLine();
//
//        // Printing the read line
//        System.out.println("The text is:" + text);
//        return text;
//    }

    public static String getUserPassword()
            throws IOException
    {
        // Enter data using BufferReader
        System.out.println("Enter password" );

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // Reading data using readLine
        String password = reader.readLine();

        // Printing the read line
        System.out.println("Password is:" + password);
        return password;
    }


    public static SecretKey generateKey(String encryptionType) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(encryptionType);
            SecretKey myKey = keyGenerator.generateKey();
            return myKey;
        } catch (Exception e) {
            return null;
        }
    }

    public static void storeToKeyStoreWithoutKey(String password, String filepath) throws Exception {
        FileWriter file = new FileWriter(filepath);
        file.write(password);
    }

    public class WriteToFile {
        public static void main(String[] args) {
            try {
                FileWriter myWriter = new FileWriter("filename.txt");
                myWriter.write("Files in Java might be tricky, but it is fun enough!");
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }


        public static void storeToKeyStore(SecretKey keyToKeyStore, String password, String data, String filepath) throws Exception {
        File file = new File(filepath);
        KeyStore store = KeyStore.getInstance("BKS", "BC");
        if (!file.exists()) {
            store.load(null, null);
        }

        store.setKeyEntry(keyName, keyToKeyStore, password.toCharArray(), null);
        OutputStream writeStream = new FileOutputStream(filepath);
        store.store(writeStream, password.toCharArray());
    }

    public static SecretKey LoadFromKeyStore(String filepath, String password) {
        try {
            KeyStore store = KeyStore.getInstance("BKS", "BC");
            InputStream readStream = new FileInputStream(filepath);
            store.load(readStream, password.toCharArray());
            SecretKey key = (SecretKey) store.getKey(keyName, password.toCharArray());
            return key;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] encryptEntries(String dataToEncrypt, SecretKey myKey, Cipher cipher) {
        try {
            byte[] text = dataToEncrypt.getBytes(UNICODE_FORMAT);
            cipher.init(Cipher.ENCRYPT_MODE, myKey);
            byte[] textEncrypted = cipher.doFinal(text);
            return textEncrypted;

        } catch (Exception e) {
            return null;
        }
    }

    public static String decryptEntries(byte[] dataToDecrypt, SecretKey myKey, Cipher cipher)
    {
        try{
            cipher.init(Cipher.DECRYPT_MODE, myKey);
            byte[] textDecrypted = cipher.doFinal(dataToDecrypt);
            String result = new String(textDecrypted);

            return result;
        }
        catch (Exception e){
            System.out.println(e);
            return null;
        }
    }
}