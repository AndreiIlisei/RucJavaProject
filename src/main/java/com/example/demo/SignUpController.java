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

    public SignUpController() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
    }

    public void getData2(ActionEvent actionEvent) throws IOException , NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        System.out.println(create_name.getText());
        System.out.println(create_email.getText());
        System.out.println(create_pass.getText());

        JavaPostgresSql.writeToFile(create_name.getText(), create_email.getText(), create_pass.getText());
    }


    public void goBackToLogin(ActionEvent actionEvent) throws IOException {
        Main m = new Main();
        m.changeScene("loggin.fxml");
    }
}