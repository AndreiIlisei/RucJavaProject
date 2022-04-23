package com.example.demo;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;
import java.security.Security;

public class Cryptography {

    public static byte[] decrypt(byte[] input, SecretKey key) throws Exception {

        Security.addProvider(new BouncyCastleProvider());

        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding", "BC");

        byte[] iv = new byte[cipher.getBlockSize()];
        iv = Arrays.copyOf(input, iv.length);
        IvParameterSpec ivParam = new IvParameterSpec(iv);

        byte[] encrypted = Arrays.copyOfRange(input, iv.length, input.length);

        cipher.init(Cipher.DECRYPT_MODE, key, ivParam);
        byte[] decrypt = cipher.doFinal(encrypted);
        return decrypt;

    }


    public static byte[] encrypt(byte[] input, SecretKey entranceKey) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding", "BC");

        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[cipher.getBlockSize()];
        random.nextBytes(iv);
        IvParameterSpec ivParam = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, entranceKey, ivParam);
        byte[] encrypted = cipher.doFinal(input);

        return Arrays.concatenate(iv, encrypted);
    }
}
