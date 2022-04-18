package com.encryption.KeyStores.NoUse;

import org.apache.commons.lang3.SerializationUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Security;

public class PasswordProtector {

    PasswordProtector(String passwordFileName) {
        Security.insertProviderAt(new BouncyCastleProvider(), 1);

        this.passwordFileName = passwordFileName;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        } catch (Exception e) { e.printStackTrace(); }
    }


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

    // Generate Secret Key Function( Password based Key Derivation)

//    public static SecretKey generatePBKey() {
//        SecretKey key = null;
//        try {
//// parameters for the key generation
//            char[] password = getUserPassword();
//            byte[] salt = {0, 1, 2, 3, 4, 5, 6, 7, 8 }; // 32 bytes = 256 bits
//// generating the key
//            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WITHHMACSHA256", "BC");
//            key = factory.generateSecret(new PBEKeySpec(password, salt, 1000000, 128));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return key;
//    }


    // instance variables

    byte[] keyBytes = generatePBKey();

    private byte[] generatePBKey() {
        return new byte[0];
    }

    SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
    String ivString = "9f741fdb5d8845bdb48a94394e84f8a3";
    byte[] iv = Hex.decode(ivString);

    String passwordFileName;
    Cipher cipher;

    // instance methods

    // load() and decrypt()

    PasswordTable load() {
        PasswordTable pt;
        byte[] encrypted
                = FileUtil.readAllBytes("AES/CBC/PKCS5Padding");
        if (encrypted.length == 0) {
            pt = new PasswordTable();
        }
        else {
            byte[] serialized = decrypt(encrypted);
            pt = (PasswordTable) SerializationUtils.deserialize(serialized);
        }
        return pt;
    }

    byte[] decrypt(byte[] input) {
        byte[] decrypted = {};
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            decrypted = cipher.doFinal(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decrypted;
    }

    // store() and encrypt()

    void store(PasswordTable pt) {
        byte[] serialized = SerializationUtils.serialize(pt);
        byte[] encrypted = encrypt(serialized);
        FileUtil.write("AES/CBC/PKCS5Padding", passwordFileName, encrypted, ivString);
    }

    byte[] encrypt(byte[] input) {
        byte[] encrypted = {};
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            encrypted = cipher.doFinal(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypted;
    }

}