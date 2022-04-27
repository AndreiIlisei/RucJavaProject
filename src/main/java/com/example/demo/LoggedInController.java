package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.example.demo.MainController.getPass;

public class LoggedInController implements Initializable {

    //Table
    @FXML
    private TableView<StoredEntries> tableView;

    //Columns
    @FXML
    private TableColumn<StoredEntries, String> domainColumn;
    @FXML
    private TableColumn<StoredEntries, String> userNameColumn;
    @FXML
    private TableColumn<StoredEntries, String> passwordColumn;

    //Text input
    @FXML
    private TextField domainInput;
    @FXML
    private TextField userNameInput;
    @FXML
    private TextField passwordInput;

    // A bit unsure what this is
    private static String domain;
    private static String userName;
    private static String password;

    StoredEntries userEntries = new StoredEntries();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        domainColumn.setCellValueFactory(new PropertyValueFactory<StoredEntries, String>("domain"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<StoredEntries, String>("userName"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<StoredEntries, String>("password"));

        tableView.setItems(getData());
    }

    //Submit button
    @FXML
    void submit(ActionEvent event) throws Exception {
        StoredEntries userEntries = new StoredEntries(domainInput.getText(), userNameInput.getText(), passwordInput.getText());
        ObservableList<StoredEntries> entries = tableView.getItems();
        entries.add(userEntries);
        tableView.setItems(entries);
        MainController.AddEntriesToTableView(domainInput.getText(), userNameInput.getText(), passwordInput.getText());
    }

    @FXML
    public void getList(ArrayList list) throws Exception {
        domain = list.get(0).toString();
        userName = list.get(1).toString();
        password = list.get(2).toString();

        userEntries.setDomain(list.get(0).toString());
        userEntries.setUserName(list.get(1).toString());
        userEntries.setPassword(list.get(2).toString());

    }

    private ObservableList<StoredEntries> getData() {

        ObservableList<StoredEntries> newAccount = FXCollections.observableArrayList(
                new StoredEntries(domain, userName, password)
        );
        return newAccount;
    }


    @FXML
    void refreshTable(ActionEvent event) {
        try {
            getPass();
        } catch (Exception e) {
            e.printStackTrace();
        }
        StoredEntries storedEntries = new StoredEntries(domain, userName, password);
        ObservableList<StoredEntries> entries = tableView.getItems();
        entries.add(storedEntries);
        tableView.setItems(entries);
    }

    @FXML
    void removeCustomer(ActionEvent event) {
        int selectedID = tableView.getSelectionModel().getSelectedIndex();
        tableView.getItems().remove(selectedID);
    }

    @FXML
    void logOut(ActionEvent event) throws IOException {
        Main m = new Main();
        m.changeScene("LoginScene.fxml");
    }
}