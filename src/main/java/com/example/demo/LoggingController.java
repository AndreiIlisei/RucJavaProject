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


    public void loginPerson2(ActionEvent event) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        JavaPostgresSql.loginHandler(event, tf_username.getText(), tf_password.getText());
    }

    public void createAccount(ActionEvent actionEvent) throws IOException {
        Main m = new Main();
        m.changeScene("sign-up.fxml");
    }


}
