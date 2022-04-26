package com.example.demo;

import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.demo.Encryptor.getPass;

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


    private static String name;

    private static String age;

    private static String number;

    Customer customer = new Customer();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("age"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("number"));

        tableView.setItems(getData());
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

    public void loadData(ActionEvent event) throws Exception {
        getPass();
    }

    @FXML
    public void getList(ArrayList list) throws Exception {
//        name = "null";
//        age = "null";
//        number = "null";


        name = list.get(0).toString();
        age = list.get(1).toString();
        number = list.get(2).toString();
        customer.setName(list.get(0).toString());
        customer.setAge(list.get(1).toString());
        customer.setNumber(list.get(2).toString());

    }

    private ObservableList<Customer> getData (){

            ObservableList<Customer> newAccount = FXCollections.observableArrayList(
            new Customer(name, age, number)
            );
            return newAccount;
    }


    public void loadData(ActionEvent actionEvent, String name, String age, String number) throws Exception {
        Customer customer = new Customer(name, age, number);
        ObservableList<Customer> customers = tableView.getItems();
        customers.add(customer);
        tableView.setItems(customers);

    }

    @FXML
    void removeCustomer(ActionEvent event) {
        try {
            getPass();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Customer customeres = new Customer(name, age, number);
        ObservableList<Customer> customers = tableView.getItems();
        customers.add(customeres);
        tableView.setItems(customers);
    }
}