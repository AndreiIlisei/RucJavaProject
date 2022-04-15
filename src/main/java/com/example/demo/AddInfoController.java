package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddInfoController implements Initializable {

    //Text input
    @FXML
    public TextField userNameFx;

    @FXML
    public TextField emailFx;

    @FXML
    public TextField passwordFx;

    @FXML
    public TextField websiteFx;

    @FXML
    public TextField notesFx;


    String query = null;
    Connection connection = null;
    ResultSet resultSet = null;
    PreparedStatement preparedStatement;
    StoredInfo storedInfo = null;
    private boolean update;
    int userid;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void save(javafx.scene.input.MouseEvent mouseEvent) {

        DatabaseConnection dbCon = new DatabaseConnection();
        connection = dbCon.DatabaseConnection();
        String username = userNameFx.getText();
        String email = emailFx.getText();
        String website = websiteFx.getText();
        String password = passwordFx.getText();
        String notes = notesFx.getText();


        if (username.isEmpty() || email.isEmpty() || website.isEmpty() || password.isEmpty() || notes.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Fill All DATA");
            alert.showAndWait();

        } else {
            getQuery();
            insert();
            clean();
        }
    }

    @FXML
    private void clean() {
        userNameFx.setText(null);
        emailFx.setText(null);
        websiteFx.setText(null);
        passwordFx.setText(null);
        notesFx.setText(null);
    }

    private void getQuery() {

        if (update == false) {
            query = "INSERT INTO public.\"StoredInfo\" (username, email, website, password, notes) VALUES (?,?,?,?,?)";

        } else {
            query = "UPDATE public.\"StoredInfo\" "
                    + "SET username=?, email=?, website=?, password=?, notes=?"
                    + "WHERE userid = '" + userid + "'";

//                    + "username=?,"
//                    + "email=?,"
//                    + "website=?,"
//                    + "password=?,"
//                    + "notes=? WHERE userid = '"+userid+"'";
        }

    }

    private void insert() {
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userNameFx.getText());
            preparedStatement.setString(2, emailFx.getText());
            preparedStatement.setString(3, websiteFx.getText());
            preparedStatement.setString(4, passwordFx.getText());
            preparedStatement.setString(5, notesFx.getText());

            preparedStatement.execute();

        } catch (SQLException ex) {
            Logger.getLogger(AddInfoController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    void setTextField(int id, String userName, String email, String website, String password, String notes) {
        userid = id;
        userNameFx.setText(userName);
        emailFx.setText(email);
        websiteFx.setText(website);
        passwordFx.setText(password);
        notesFx.setText(notes);
    }

    void setUpdate(boolean b) {
        this.update = b;

    }


//
//    public void saveDialog(javafx.scene.input.MouseEvent mouseEvent){
//        AddInfoController.save(userNameFx.getText(), emailFx.getText(), passwordFx.getText(), websiteFx.getText(), notesFx.getText());
//        clean();
////        TableViewController.refreshTable();
//    }
//
//    public static void save(String userName, String Email, String Website, String Password, String Notes ) {
//        String url = "jdbc:postgresql://localhost:5432/PFSExam?currentSchema=PFSExam";
//        Properties prop = new Properties();
//        prop.setProperty("user", "postgres");
//        prop.setProperty("password", Global.ACCOUNT_SID);
//
//        String username = userName;
//        String email = Email;
//        String website = Website;
//        String password = Password;
//        String notes = Notes;
//        DatabaseConnection dbCon = new DatabaseConnection();
//        dbCon.DatabaseConnection();
//
//        // query
//        String query = "INSERT INTO public.\"StoredInfo\" (username, email, website, password, notes) VALUES(?, ?, ?, ?, ?)";
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection con = DriverManager.getConnection(url, prop);
//            PreparedStatement stmt = con.prepareStatement(query);
//
//            stmt.setString(1, username);
//            stmt.setString(2, email);
//            stmt.setString(3, website);
//            stmt.setString(4, password);
//            stmt.setString(5, notes);
//            stmt.executeUpdate();
//            System.out.println("Sucessfully created.");
//
//        } catch (Exception e) {
//            System.out.println("Sucessfully not.");
//            Logger lgr = Logger.getLogger(AddInfoController.class.getName());
//            lgr.log(Level.SEVERE, e.getMessage(), e);
//        }
//    }
//
//        @FXML
//    private void clean() {
//        userNameFx.setText(null);
//        emailFx.setText(null);
//        websiteFx.setText(null);
//        passwordFx.setText(null);
//        notesFx.setText(null);
//    }
//
//        void setTextField(String userName, String email, String website, String password, String notes) {
//        userNameFx.setText(userName);
//        emailFx.setText(email);
//        websiteFx.setText(website);
//        passwordFx.setText(password);
//        passwordFx.setText(notes);
//    }
//
//    void setUpdate(boolean b) {
//        this.update = b;
//
//    }


}

