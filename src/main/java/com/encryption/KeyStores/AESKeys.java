package com.encryption.KeyStores;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AESKeys {
    public static SecretKey getNewAESKey() throws NoSuchAlgorithmException {
        SecureRandom rand = new SecureRandom();
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256, rand);
        return keyGen.generateKey();
    }
}