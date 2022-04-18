package com.encryption.KeyStores.NoUse;

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
        String text = "Trying encryption";

        try {
            Security.addProvider(new BouncyCastleProvider());
            SecretKey key = generateKey("AES");
            storeToKeyStore(key, "randomPassword", "D:\\RUC\\keystore");

            Cipher cipher;
            cipher = Cipher.getInstance("AES");

            byte[] encryptedData = encryptEntries(text, key, cipher);
            String encryptedString = new String(encryptedData);
            System.out.println(encryptedString);

            String decrypted = decryptEntries(encryptedData, key, cipher);
            System.out.println(decrypted);

        } catch (Exception e) {
                e.printStackTrace();
        }
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

    public static void storeToKeyStore(SecretKey keyToKeyStore, String password, String filepath) throws Exception {
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
