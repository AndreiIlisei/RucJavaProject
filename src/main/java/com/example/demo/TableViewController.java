//package com.example.demo;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.stage.Stage;
//import javafx.stage.StageStyle;
//
//import java.io.IOException;
//import java.net.URL;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ResourceBundle;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class TableViewController implements Initializable {
//
//    //Table
//    @FXML
//    private TableView<StoredInfo> mainTableView;
//
//    //Columns
//    @FXML
//    private TableColumn<StoredInfo, String> idColumn;
//
//    @FXML
//    private TableColumn<StoredInfo, String> userNameColumn;
//
//    @FXML
//    private TableColumn<StoredInfo, String> emailColumn;
//
//    @FXML
//    private TableColumn<StoredInfo, String> websiteColumn;
//
//    @FXML
//    private TableColumn<StoredInfo, String> passwordColumn;
//
//    @FXML
//    private TableColumn<StoredInfo, String> notesColumn;
//
//    String query = null;
//    Connection connection = null;
//    PreparedStatement preparedStatement = null;
//    ResultSet resultSet = null;
//    StoredInfo storedInfo = null;
//    ObservableList<StoredInfo> StoredInfoList = FXCollections.observableArrayList();
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        loadData();
//    }
//
//    private void loadData() {
//        DatabaseConnection dbCon = new DatabaseConnection();
//        connection = dbCon.DatabaseConnection();
//
//        refreshTable();
//        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
//        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
//        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
//        websiteColumn.setCellValueFactory(new PropertyValueFactory<>("website"));
//        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
//        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
//    }
//
//    @FXML
//    private void deleteInfo(javafx.scene.input.MouseEvent mouseEvent) {
//        try {
//            storedInfo = mainTableView.getSelectionModel().getSelectedItem();
//            query = "DELETE FROM public.\"StoredInfo\" WHERE userid = " + storedInfo.getId();
//            DatabaseConnection dbCon = new DatabaseConnection();
//            connection = dbCon.DatabaseConnection();
//
//            preparedStatement = connection.prepareStatement(query);
//            preparedStatement.execute();
//            refreshTable();
//
//        } catch (SQLException ex) {
//            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    @FXML
//    private void editInfo(javafx.scene.input.MouseEvent mouseEvent) {
//
//        storedInfo = mainTableView.getSelectionModel().getSelectedItem();
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource("addInformation.fxml"));
//        try {
//            loader.load();
//        } catch (IOException ex) {
//            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        AddInfoController addInfoController = loader.getController();
//        addInfoController.setUpdate(true);
//        addInfoController.setTextField(storedInfo.getId(), storedInfo.getUserName(), storedInfo.getEmail(),
//                storedInfo.getWebsite(), storedInfo.getPassword(), storedInfo.getNotes());
//        Parent parent = loader.getRoot();
//        Stage stage = new Stage();
//        stage.setScene(new Scene(parent));
//        stage.initStyle(StageStyle.UTILITY);
//        stage.show();
//    }
//
//    @FXML
//    private void refreshTable() {
//        try {
//            StoredInfoList.clear();
//
//            query = "SELECT * FROM public.\"StoredInfo\"";
//            preparedStatement = connection.prepareStatement(query);
//            resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                StoredInfoList.add(new StoredInfo(
//                        resultSet.getInt("userid"),
//                        resultSet.getString("username"),
//                        resultSet.getString("email"),
//                        resultSet.getString("website"),
//                        resultSet.getString("password"),
//                        resultSet.getString("notes")));
//                mainTableView.setItems(StoredInfoList);
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public void getAddInformation(javafx.scene.input.MouseEvent mouseEvent) {
//        try {
//            Parent parent = FXMLLoader.load(getClass().getResource("addInformation.fxml"));
//            Scene scene = new Scene(parent);
//            Stage stage = new Stage();
//            stage.setScene(scene);
//            stage.initStyle(StageStyle.UTILITY);
//            stage.show();
//        } catch (IOException ex) {
//            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
////    @FXML
////    private void close(MouseEvent event) {
////        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
////        stage.close();
////    }
//}