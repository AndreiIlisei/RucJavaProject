package com.encryption.KeyStores.NoUse;

import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

public class ECBShortExample {
    public static void main(String[] args) throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        byte[] keyBytes = Hex.decode("000102030405060708090a0b0c0d0e0f");
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding", "BC");
        byte[] input = Hex.decode("a0a1a2a3a4a5a6a7a0a1a2a3a4a5a6a7a0a1a2a3a4a5a6a7a0a1a2a3a4a5a6a7");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        System.out.println("input : " + Hex.toHexString(input));
        byte[] output = cipher.doFinal(input); // encryption
        System.out.println("encrypted: " + Hex.toHexString(output));
        cipher.init(Cipher.DECRYPT_MODE, key);
        System.out.println("decrypted: " + Hex.toHexString(cipher.doFinal(output))); // decryption
    }
}