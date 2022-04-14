package com.example.demo;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;

public class Encryptor {
    private static SecretKey entranceKey;

    //Advanced Encryption Standard
    //128 bit
    byte[] IV = {0x01, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
    private static byte[] iv;
    private static SecretKey key;
    private static final int keyLength = 32;
    private static final SecureRandom random = new SecureRandom();
    static String dir = "C:\\Users\\Steffen Giessing\\Desktop\\PFSExam\\RucJavaProject\\src\\main\\java\\com\\encryption";
    static String fileName = dir + "passwordFile.txt";
//    public String encrypt(String input, byte[] secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException,
//            InvalidAlgorithmParameterException, InvalidKeyException,
//            BadPaddingException, IllegalBlockSizeException {
//
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        SecretKeySpec key = new SecretKeySpec(secretKey, "AES");
//        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV));
//        byte[] cipherText = cipher.doFinal(input.getBytes());
//        return Base64.getEncoder()
//                .encodeToString(cipherText);
//    }
//
//
//    public String decrypt(String cipherText, byte[] secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException,
//            InvalidAlgorithmParameterException, InvalidKeyException,
//            BadPaddingException, IllegalBlockSizeException {
//
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        SecretKeySpec key = new SecretKeySpec(secretKey, "AES");
//        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV));
//        byte[] plainText = cipher.doFinal(Base64.getDecoder()
//                .decode(cipherText));
//        return new String(plainText);
//    }
//    private static IvParameterSpec generateIV(Cipher cipher) throws Exception {
//        byte [] ivBytes = new byte[cipher.getBlockSize()];
//        random.nextBytes(ivBytes);
//        return new IvParameterSpec(ivBytes);
//    }

//    public byte[] stringToByteArray(String keyString) {
//        String[] keyFragments = keyString.split(" ");
//
//        byte[] key = new byte[16];
//        for (int i = 0; i < keyFragments.length; i++) {
//            key[i] = Byte.parseByte(keyFragments[i]);
//        }
//        return key;
//    }
    private static SecretKey generateKey() throws Exception {
        byte[] keyBytes = new byte[keyLength];
        random.nextBytes(keyBytes);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        return keySpec;
    }

    public static byte[] encrypt(String file) throws Exception {
        byte[] IV = {0x01, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
        key = generateKey();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5padding", "BC");
        SecureRandom csprng = new SecureRandom();
        byte[] randomBytes = new byte[16];
        csprng.nextBytes(randomBytes);
        String ivstr = Arrays.toString(randomBytes);
        iv = ivstr.getBytes(Charset.defaultCharset());

        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV));

        System.out.println(Base64.getEncoder().withoutPadding().encodeToString(key.getEncoded()));
        printByteArr(iv);

        return cipher.doFinal(file.getBytes());
    }

    private static void printByteArr(byte[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.printf(i == 0 ? "%d" : ",%d", (arr[i] & 0xFF));
        }
        System.out.println("]");
    }

    public static String  /*byte[]*/ decrypt(byte[] cipherText /*, SecretKey key*/) throws Exception {
        byte[] IV = {0x01, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
        printByteArr(cipherText);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV));


       // byte[] decrypted = cipher.doFinal(cipherText);
     //   return decrypted;
        return new String(cipher.doFinal(cipherText));

    }
    public void createAccount () throws Exception {
        Path path = Paths.get(fileName);
        byte[] data = Files.readAllBytes(path);

        byte[] encryptedData = Arrays.copyOfRange(data, 320, data.length);
        //byte[] decrypt = decrypt(encryptedData, entranceKey);

    }


    public static void main(String[] args) throws Exception {
        //Encryptor encryptor = new Encryptor();


        Security.addProvider(new BouncyCastleProvider());
        byte[] keybytes = Hex.decode("000102030405060708090a0b0c0d0e0f");

        byte[] cipherText = encrypt(fileName);
        String getText = decrypt(cipherText);
        System.out.println(getText);
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