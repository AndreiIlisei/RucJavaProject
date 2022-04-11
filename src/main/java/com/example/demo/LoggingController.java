package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class LoggingController {
    @FXML
    private Button btn_login;
    @FXML
    private TextField tf_username;
    @FXML
    private PasswordField tf_password;

    // Action even Caller
//    public void userLogin(ActionEvent event) throws IOException {
//        checkLogin();
//    }

    public void loginPerson(ActionEvent event) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        JavaPostgreSql.loginUser(event, tf_username.getText(), tf_password.getText());
    }


    // Check Login Method
//    private void checkLogin() throws IOException {
//        Main m = new Main();
//
//        if (tf_username.getText().isEmpty() && tf_password.getText().isEmpty()) {
//            System.out.println("No data.");
////          wrongLogIn.setText("Please enter your data.");
//        }
//        else if (tf_username.getText().toString() && tf_password.getText().toString().equals("123")) {
////          wrongLogIn.setText("Success!");
//            System.out.println("Sucessfully.");
//            m.changeScene("loggedIn.fxml");
//        }
//        else {
//            System.out.println("Wrong credentials.");
////          wrongLogIn.setText("Wrong username or password!");
//        }
//    }

    public void createAccount(ActionEvent actionEvent) throws IOException {
        Main m = new Main();
        m.changeScene("sign-up.fxml");
    }


}


//    private static final String ALG = "AES";
////    private static final String CIPH = ("AES/ECB/NoPadding", "BC");
//    private static final String KEYFAC = "PBKDF2WithHmacSHA1";

//    private void encrypt(File file, String password) throws Exception {
//        FileOutputStream outFile;
//        try (
//                // file to be encrypted as input stream
//                FileInputStream inFile = new FileInputStream(file)) {
//            // output file stream
//            outFile = new FileOutputStream(file + ".aes");
//
//
//            //create salt
//            byte[] salt = new byte[8];
//            SecureRandom secureRandom = new SecureRandom();
//            secureRandom.nextBytes(salt);
//
//            // Write the salt to output file stream
//            outFile.write(salt);
//
//            //Generate Secret Key
//            SecretKeyFactory factory = SecretKeyFactory
//                    .getInstance(KEYFAC);
//            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536,
//                    128);
//            SecretKey secretKey = factory.generateSecret(keySpec);
//            SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), ALG);
//
//            // Initialize the cipher
//            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding", "BC");
//            cipher.init(Cipher.ENCRYPT_MODE, secret);
//            AlgorithmParameters params = cipher.getParameters();
//            byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
//
//            //output the iv to the file output stream
//            outFile.write(iv);
//
//            //encrypt the input file
//            byte[] input = new byte[64];
//            int bytesRead;
//            while ((bytesRead = inFile.read(input)) != -1) {
//                byte[] output = cipher.update(input, 0, bytesRead);
//                if (output != null) {
//                    outFile.write(output);
//                }
//            }
//            byte[] output = cipher.doFinal();
//            if (output != null) {
//                outFile.write(output);
//            }
//        }
//
//        outFile.flush();
//        outFile.close();
//
//
//    }
//
//
//    public void EncryptFile(ActionEvent event) throws Exception {
//        encrypt(new File(""), "");
//    }

//
//    public void FileOpener(ActionEvent action) throws IOException {
//        FileChooser fileChooser = new FileChooser();
//        File selectedFile = fileChooser.showOpenDialog(null);
//
//        if(selectedFile != null) {
//        listview.getItems().add(selectedFile.getName());
//        } else {
//        System.out.println("file is not valid");
//        }
//    }