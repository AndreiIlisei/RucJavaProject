package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable {
    @FXML
    private Button btn_logout;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setUserInfo(String userName, String email) {
//        label_welcome.setText("welcome" + userName + "!");

    }
}
