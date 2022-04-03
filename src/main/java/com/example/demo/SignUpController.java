package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import java.io.IOException;

public class SignUpController {

    @FXML
    private Button btn_sign_up;
    @FXML
    private TextField create_name;
    @FXML
    private TextField create_email;
    @FXML
    private PasswordField create_pass;


    public void getData(ActionEvent actionEvent) {
        System.out.println(create_name.getText());
        System.out.println(create_email.getText());
        System.out.println(create_pass.getText());

        JavaPostgreSql.writeToDatabase(create_name.getText(), create_email.getText(), create_pass.getText());



    }
}