package com.example.demo;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class AddInfoController implements Initializable {


    @FXML
    private TableView<StoredInfo> tableView;

    @FXML
    private TableColumn<StoredInfo, String> userNameColumn;

    @FXML
    private TableColumn<StoredInfo, String> emailColumn;

    @FXML
    private TableColumn<StoredInfo, String> password;

    @FXML
    private TableColumn<StoredInfo, String> website;

    @FXML
    private TableColumn<StoredInfo, String> notes;


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

    private boolean update;
    int userid;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userNameColumn.setCellValueFactory(new PropertyValueFactory<StoredInfo, String>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<StoredInfo, String>("email"));
        password.setCellValueFactory(new PropertyValueFactory<StoredInfo, String>("password"));
        website.setCellValueFactory(new PropertyValueFactory<StoredInfo, String>("website"));
        notes.setCellValueFactory(new PropertyValueFactory<StoredInfo, String>("notes"));
    }


    @FXML
    public void save(javafx.scene.input.MouseEvent mouseEvent) throws Exception {
        StoredInfo info = new StoredInfo(userNameFx.getText(), emailFx.getText(), websiteFx.getText(), passwordFx.getText(), notesFx.getText());
        ObservableList<StoredInfo> information = tableView.getItems();
        information.add(info);
        tableView.setItems(information);
    }

//        String username = userNameFx.getText();
//        String email = emailFx.getText();
//        String website = websiteFx.getText();
//        String password = passwordFx.getText();
//        String notes = notesFx.getText();


//        if (username.isEmpty() || email.isEmpty() || website.isEmpty() || password.isEmpty() || notes.isEmpty()) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setHeaderText(null);
//            alert.setContentText("Please Fill All DATA");
//            alert.showAndWait();
//
//        } else {


            //getQuery();
//            System.out.println("it has smth");
//            new TableViewModel(username, email, password ,website,notes);
//            createAccount(username, email, password);
//            new TableViewModel(userNameFx.getText(),emailFx.getText(),"stef","stef","stef");
//            userNameFx.setText("username");
//        emailFx.setText("email");
//        websiteFx.setText("website");
//        passwordFx.setText("password");
//        passwordFx.setText("notes");
//        }
//    }

    @FXML
    private void clean() {
        userNameFx.setText(null);
        emailFx.setText(null);
        websiteFx.setText(null);
        passwordFx.setText(null);
        notesFx.setText(null);
    }

//    private void getQuery() {
//
//        if (update == false) {
//            query = "INSERT INTO public.\"StoredInfo\" (username, email, website, password, notes) VALUES (?,?,?,?,?)";
//
//        } else {
//            query = "UPDATE public.\"StoredInfo\" "
//                    + "SET username=?, email=?, website=?, password=?, notes=?"
//                    + "WHERE userid = '" + userid + "'";
//
////                    + "username=?,"
////                    + "email=?,"
////                    + "website=?,"
////                    + "password=?,"
////                    + "notes=? WHERE userid = '"+userid+"'";
//        }
//
//    }

    private void insert() {

//        try {
//
////            preparedStatement = connection.prepareStatement(query);
////            preparedStatement.setString(1, userNameFx.getText());
////            preparedStatement.setString(2, emailFx.getText());
////            preparedStatement.setString(3, websiteFx.getText());
////            preparedStatement.setString(4, passwordFx.getText());
////            preparedStatement.setString(5, notesFx.getText());
////
////            preparedStatement.execute();
//
//        } catch (SQLException ex) {
//            Logger.getLogger(AddInfoController.class.getName()).log(Level.SEVERE, null, ex);
//        }

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