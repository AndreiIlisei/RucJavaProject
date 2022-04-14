package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SignUpController {

    @FXML
    private TextField create_name;
    @FXML
    private TextField create_email;
    @FXML
    private PasswordField create_pass;
    @FXML
    private TextField passwordTextField;
    @FXML
    private CheckBox showPassword;

    Encryptor encryptor = new Encryptor();

    byte[] encryptionKey = {65, 12, 12, 12, 12, 12, 12, 12, 12,
            12, 12, 12, 12, 12, 12, 12 };

  //  String encryptedPass = encryptor.encrypt(String.valueOf(create_pass), encryptionKey);

    public SignUpController() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
    }

    @FXML
    void changeVisibility(ActionEvent event) {
        if (showPassword.isSelected()) {
            passwordTextField.setText(create_pass.getText());
            passwordTextField.setVisible(true);
            create_pass.setVisible(false);
            return;
        }
        create_pass.setText(passwordTextField.getText());
        create_pass.setVisible(true);
        passwordTextField.setVisible(false);
    }

    public void getData(ActionEvent actionEvent) throws IOException , NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        System.out.println(create_name.getText());
        System.out.println(create_email.getText());
        System.out.println(create_pass.getText());

   //     JavaPostgreSql.writeToDatabase(create_name.getText(), create_email.getText(), String.valueOf(encryptedPass.getBytes()));
    }

    public void getData2(ActionEvent actionEvent) throws IOException , NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        System.out.println(create_name.getText());
        System.out.println(create_email.getText());
        System.out.println(create_pass.getText());

       // JavaPostgreSql.writeToFile(create_name.getText(), create_email.getText(), String.valueOf(encryptedPass.getBytes()));
    }


    public void goBackToLogin(ActionEvent actionEvent) throws IOException {
        Main m = new Main();
        m.changeScene("loggin.fxml");
    }
}