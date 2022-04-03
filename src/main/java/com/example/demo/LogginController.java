package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import java.io.IOException;

public class LogginController {

    @FXML
    private Button btn_login;
    @FXML
    private TextField tf_username;
//  @FXML
//  private Label wrongLogIn;
    @FXML
    private PasswordField tf_password;


    public void userLogin(ActionEvent event) throws IOException {
        checkLogin();
    }

    private void checkLogin() throws IOException {
        Main m = new Main();

        if (tf_username.getText().isEmpty() && tf_password.getText().isEmpty()) {
            System.out.println("No data.");
//          wrongLogIn.setText("Please enter your data.");
        }
        else if (tf_username.getText().toString().equals("javacoding") && tf_password.getText().toString().equals("123")) {
//          wrongLogIn.setText("Success!");
            System.out.println("Sucessfully.");
            m.changeScene("loggedIn.fxml");
        }
        else {
            System.out.println("Wrong credentials.");
//          wrongLogIn.setText("Wrong username or password!");
        }
    }

    public void createAccount(ActionEvent actionEvent) throws IOException {
        Main m = new Main();
        m.changeScene("sign-up.fxml");
    }
}

