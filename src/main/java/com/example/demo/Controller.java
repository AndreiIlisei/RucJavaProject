package com.example.demo;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    //Table
    @FXML
    private TableView<Customer> tableView;

    //Columns
    @FXML
    private TableColumn<Customer, String> nameColumn;

    @FXML
    private TableColumn<Customer, String> ageColumn;

    @FXML
    private TableColumn<Customer, String> numberColumn;

    //Text input
    @FXML
    private TextField nameInput;

    @FXML
    private TextField passwordInput;

    @FXML
    private TextField websiteInput;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("age"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("number"));
    }

    //Submit button
    @FXML
    void submit(ActionEvent event) throws Exception {
        Customer customer = new Customer(nameInput.getText(), passwordInput.getText(), websiteInput.getText());
        ObservableList<Customer> customers = tableView.getItems();
        customers.add(customer);
        tableView.setItems(customers);
        Encryptor.createAccount(nameInput.getText(), passwordInput.getText(), websiteInput.getText());
    }


    public void load(String domain) throws Exception {
        Encryptor.getPass(domain);
    }

    public void loadData(ActionEvent event) throws Exception {
        Encryptor.getPass();
    }


    @FXML
    void removeCustomer(ActionEvent event) {
        int selectedID = tableView.getSelectionModel().getSelectedIndex();
        tableView.getItems().remove(selectedID);
    }
}