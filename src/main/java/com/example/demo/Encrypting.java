package com.example.demo;//package com.example.demo;
//
//public class Encrypting {
//
//    public class FileUtil {
//        public static byte[] readAllBytes(String plaintextFileName) {
//            byte[] bytesRead = {};
//            try {
//                bytesRead = Files.readAllBytes(Paths.get(plaintextFileName));
//            } catch (Exception e) {}
//            return bytesRead; // returns {} if file does not exist
//        }
//
//}
//
//    public class DecryptFileCBC {
//        public static void main(String[] args) {.. }
//        String dir = "/Users/nielsj/Desktop/dev/gpg";
//        String mr = "MedicalRecordNielsJ";
//        String plaintextFileName = dir + "/" + mr + "." + "pdf",
//                testFile = dir + "/" + mr + "." + "test" + "." + "pdf";
//        byte[] keyBytes = Hex.decode("000102030405060708090a0b0c0d0e0f");
//        void decrypt() {
//            try {
//// Reading
//                String ivString = library.FileUtil.getIV("AES/CBC/PKCS5Padding",
//                        plaintextFileName);
//                IvParameterSpec iv = new IvParameterSpec(Hex.decode(ivString));
//                byte[] input = FileUtil.readAllBytes("AES/CBC/PKCS5Padding",
//                        plaintextFileName + "." + ivString);
//// Decrypting
//                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
//                SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
//                cipher.init(Cipher.DECRYPT_MODE, key, iv);
//                byte[] output = cipher.doFinal(input);
//// Writing
//                library.FileUtil.write(testFile, output);
//            } catch (Exception e) { e.printStackTrace();
//            }
//        }
//    }
//
//// Create
//    public static KeyStore createKeyStore() {
//        KeyStore store = null;
//        try {
//            store = KeyStore.getInstance("BKS", "BC");
//            store.load(null, null);
//        } catch (Exception e) { ..}
//        return store;
//    }
//
//// Load
//    String storeFileName = ...;
//    char[] storePW = "burger".toCharArray();
//    KeyStore store = KeyStore.getInstance("BKS", "BC");
//    FileInputStream fis = new FileInputStream(storeFileName);
//store.load(fis, storePW);
//fis.close();
//

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.KeyStore;
import java.security.Security;


public class Encrypting {

    static String dir = "D:\\RUC";
    static String storeFileName = dir + "\\keystore.bks";
    static char[] storePW = "burger".toCharArray(),
            secretKeyPW = "pizza".toCharArray();

    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());
        KeyStore ks = createKeyStore();
//        generateAndAddKey(ks);
//        store(ks);
    }

    // First time when a user runs the app a empty key store gets created.
    //  I guess a if statement should be made here to check if there is a keystore
    // already created and if not to create one.
    //TO DO
    public static KeyStore createKeyStore() {
        KeyStore store = null;
        try {
            store = KeyStore.getInstance("BKS", "BC");
            store.load(null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return store;
    }

//
//    public static KeyStore load() {
//        KeyStore ks = null;
//        try {
//            // step (1)
//            ks = KeyStore.getInstance("BKS", "BC");
//            // step (2)
//            FileInputStream fis = new FileInputStream(storeFileName);
//            ks.load(fis, storePW);
//            fis.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return ks;
//    }
//
//    public static SecretKeySpec getKey() {
//        SecretKeySpec key = null;
//        try {
//            KeyStore ks = load();
//            ks = load();
//            System.out.print("MedicalKS: please type password: ");
//            Scanner scanner = new Scanner(System.in);
//            char[] pw = scanner.nextLine().toCharArray();
//            key = (SecretKeySpec) ks.getKey("key", pw);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return key;
//
//    }
//
//    public static void store(KeyStore store) {
//        try {
//            FileOutputStream fOut = new FileOutputStream(storeFileName);
//            store.store(fOut, storePW);
//            fOut.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//    public static void generateAndAddKey(KeyStore store) {
//        try {
//// generating random bytes
//            SecureRandom secureRandom = SecureRandom.getInstance("DEFAULT", "BC");
//            byte[] keyBytes = new byte[16];
//            secureRandom.nextBytes(keyBytes);
//// generating key
//            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
//// adding key to keystore
//            KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry(key);
//            KeyStore.ProtectionParameter protection = new KeyStore.PasswordProtection(secretKeyPW);
//            store.setEntry("key", entry, protection);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//

//
//    public static SecretKey generatePBKey() {
//        SecretKey key = null;
//        try {
//// parameters for the key generation
////            char[] password = getUserPassword();
//            byte[] salt = {0, 1, 2, 3, 4, 5,  }; // 32 bytes = 256 bits
//// generating the key
//            SecretKeyFactory factory
//                    = SecretKeyFactory.getInstance("PBKDF2WITHHMACSHA256", "BC");
//            key
//                    = factory.generateSecret(new PBEKeySpec(password, salt, 1000000, 128));
//        } catch (Exception e) { }
//        return key;
//    }
}
