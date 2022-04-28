package com.example.demo;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class CryptographyHelper {

    public static byte[] hash(byte[] message) throws NoSuchAlgorithmException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());

        MessageDigest mda = MessageDigest.getInstance("SHA-512", "BC");
        return mda.digest(message);
    }

    public static byte[] randomNumberGenerator(int size) throws NoSuchAlgorithmException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());

        SecureRandom rand = new SecureRandom();
        byte[] data = new byte[size];
        rand.nextBytes(data);
        return data;
    }

    public static byte[] hmac(byte[] input, SecretKey key) throws Exception {

        Security.addProvider(new BouncyCastleProvider());
        Mac mac = Mac.getInstance("HmacSHA512", "BC");
        mac.init(key);

        return mac.doFinal(input);
    }

    public static SecretKey generateKey(String password, byte[] salt) throws FileNotFoundException, IOException, NoSuchProviderException, InvalidKeySpecException, NoSuchAlgorithmException {
        Security.addProvider(new BouncyCastleProvider());
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256", "BC");

        //generate key
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 1000000, 256);
        SecretKey temporaryKey = factory.generateSecret(spec);
        SecretKey generatedKey = new SecretKeySpec(temporaryKey.getEncoded(), "AES");
        return generatedKey;
    }
}
