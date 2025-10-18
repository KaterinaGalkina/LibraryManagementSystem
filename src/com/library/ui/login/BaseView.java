package com.library.ui.login;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class BaseView {
    public void start(Stage stage, GridPane currentGrid) { 
        try {
            // --- Load the image --- 
            Image image = new Image(new FileInputStream("src/assets/start_image.jpg"));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(500);   // width of the image block
            imageView.setFitHeight(500);   // width of the image block
            //imageView.setPreserveRatio(true);

            // --- White rectangle overlay for login form ---
            StackPane whitePane = new StackPane(); 
            whitePane.setStyle("-fx-background-color: white;");
            whitePane.setPrefWidth(500); // left half
            whitePane.setMaxHeight(500); 
            whitePane.getChildren().add(currentGrid);

            // --- Combine in HBox: white rectangle left, image right ---
            HBox content = new HBox();
            content.getChildren().addAll(whitePane, imageView);
            content.setAlignment(Pos.CENTER_LEFT); // align HBox content to left

            // --- Outer gray background ---
            StackPane outerPane = new StackPane();
            outerPane.setStyle("-fx-background-color: #d3d3d3;"); // gray background
            outerPane.getChildren().add(content);

            outerPane.setOnMouseClicked(e -> outerPane.requestFocus());

            Scene scene = new Scene(outerPane, 1000, 600); // adjust window size
            stage.setScene(scene);
            stage.setTitle("Library Login");
            stage.setResizable(false);
            stage.show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
    }

    public void switchToLogin(Stage stage, Connection conn) {
        LoginView loginView = new LoginView();
        GridPane loginGrid = loginView.start(stage, conn); // get the login form
        new BaseView().start(stage, loginGrid); // wrap with image
    }

    public void switchToRegister(Stage stage, Connection conn) {
        RegisterView registerView = new RegisterView();
        GridPane registerGrid = registerView.start(stage, conn); // get register form
        new BaseView().start(stage, registerGrid); // wrap with image
    }
}
