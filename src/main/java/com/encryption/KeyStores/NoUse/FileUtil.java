package com.encryption.KeyStores.NoUse;

import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {
    public static byte[] readAllBytes(String plaintextFileName) {
        byte[] bytesRead = {};
        try {
            bytesRead = Files.readAllBytes(Paths.get(plaintextFileName));
        } catch (Exception e) {}
        return bytesRead; // returns {} if file does not exist
    }

    public static void write(String transformation,
                             String plaintextFileName, byte[] output, String ivString) {
        String outFile = "";
        String[] parts = transformation.split("/");
        if (parts.length == 3 && parts[0].equals("AES")) {
            outFile = plaintextFileName + ".aes";
        } else { }
        try {
            Files.write(Paths.get(outFile), output);
        } catch (Exception e) { e.printStackTrace(); }
    }
}