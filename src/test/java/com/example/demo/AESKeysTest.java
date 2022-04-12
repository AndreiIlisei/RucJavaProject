package com.example.demo;

import com.encryption.KeyStores.AESKeys;
import org.testng.annotations.Test;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Unit test for AESKeys class.
 */
public class AESKeysTest
{

    @Test
    public void verifyAESKey() {
        try {
            SecretKey key = AESKeys.getNewAESKey();
            System.out.println("Key algorithm is: " + key.getAlgorithm());
            String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
            System.out.println("Base64 encoded key is: " + encodedKey);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}