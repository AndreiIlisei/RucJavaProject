//package com.example.demo;
//
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.layout.Pane;
//import javafx.stage.Stage;
//import javafx.scene.image.Image; import javafx.scene.image.ImageView;
//
//import java.io.IOException;
//
//public class HelloApplication extends Application {
//    @Override
//    public void start(Stage stage) throws IOException {
////        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
////        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        Scene scene = new Scene(new Pane(), 300, 100);
//        Image anImage = new Image(getClass().getResourceAsStream("hello.gif"));
//        Button b = new Button();
//        b.setGraphic(new ImageView(anImage));
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public static void main(String[] args) {
//        launch();
//    }
//}

package com.example.demo;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    public void start(Stage primaryStage) {
        Pane  aPane = new Pane();

        Button addButton = new Button("Add");
        addButton.relocate(175, 10);
        addButton.setPrefSize(100, 25);


        Button removeButton = new Button("Remove");
        removeButton.relocate(250, 10);
        removeButton.setPrefSize(100, 25);


        aPane.getChildren().addAll(addButton, removeButton);
        primaryStage.setTitle("Password Manager"); // Set title of window
        primaryStage.setScene(new Scene(aPane, 700,300)); // Set size of window
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
