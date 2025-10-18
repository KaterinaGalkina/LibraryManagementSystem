package com.library.ui.menu;

import java.sql.Connection;

import com.library.ui.login.BaseView;
import com.library.ui.login.LoginView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuView {
    public void start(Stage stage, Connection conn) {
        // --- Menu container with BorderPane (for fixed bottom section) ---
        BorderPane menuContainer = new BorderPane();
        menuContainer.setStyle("-fx-background-color: #06402B;");

        // --- VBox for menu items (center of the menu) ---
        VBox menuItemsBox = new VBox(20);
        menuItemsBox.setAlignment(Pos.TOP_CENTER);
        menuItemsBox.setPadding(new Insets(30));

        // --- Example static menu sections ---
        Label title = new Label("📚 Library Menu");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label home = new Label("Home");
        Label profile = new Label("Profile");
        Label books = new Label("Books");
        Label magazines = new Label("Magazines");
        Label borrowings = new Label("Borrowings");
        Label notifications = new Label("Notifications");

        Label[] labels = {home, profile, books, magazines, borrowings, notifications};
        for (Label label : labels) {
            label.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-cursor: hand;");
        }

        menuItemsBox.getChildren().addAll(title, home, profile, books, magazines, borrowings, notifications);

        // --- "Log out" button (fixed at bottom) ---
        Button logoutButton = new Button("Log out");
        logoutButton.setStyle(
            "-fx-background-color: #ffffff; " +
            "-fx-text-fill: #06402B; " +
            "-fx-font-weight: bold; " +
            "-fx-cursor: hand; " +
            "-fx-background-radius: 5;"
        );
        logoutButton.setOnAction(e -> {
            stage.close();

            Stage new_stage = new Stage();
            BaseView baseView = new BaseView();
            LoginView loginView = new LoginView();
            GridPane loginGrid = loginView.start(new_stage, conn);
            baseView.start(new_stage, loginGrid);
        });

        BorderPane.setMargin(logoutButton, new Insets(20));
        BorderPane.setAlignment(logoutButton, Pos.BOTTOM_CENTER);

        // --- Assemble the menu ---
        menuContainer.setCenter(menuItemsBox);
        menuContainer.setBottom(logoutButton);

        // By default we are in home 
        GridPane homeGrid = new HomeView().start(conn);

        // --- Right content area (3/4 width) ---
        StackPane contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color: white;");
        contentArea.setAlignment(Pos.CENTER);
        contentArea.getChildren().add(homeGrid);

        // Default: show home
        contentArea.getChildren().setAll(new HomeView().start(conn));

        // --- Click behavior for each menu item ---
        home.setOnMouseClicked(e -> contentArea.getChildren().setAll(new HomeView().start(conn)));
        profile.setOnMouseClicked(e -> contentArea.getChildren().setAll(new ProfileView().start(conn)));
        books.setOnMouseClicked(e -> contentArea.getChildren().setAll(new BooksView().start(conn)));
        magazines.setOnMouseClicked(e -> contentArea.getChildren().setAll(new MagazinesView().start(conn)));
        borrowings.setOnMouseClicked(e -> contentArea.getChildren().setAll(new BorrowingsView().start(conn)));
        notifications.setOnMouseClicked(e -> contentArea.getChildren().setAll(new NotificationsView().start(conn)));

        // --- Split layout (menu left, content right) ---
        HBox layout = new HBox();
        layout.getChildren().addAll(menuContainer, contentArea);
        HBox.setHgrow(contentArea, Priority.ALWAYS);

        // So that the user can deselect a field
        layout.setOnMouseClicked(e -> layout.requestFocus());

        // --- Scene setup ---
        Scene scene = new Scene(layout, 1000, 600);
        stage.setScene(scene);
        stage.setTitle("Library Home");
        stage.setResizable(false);
        stage.show();

        // --- Responsive ratio (menu = 1/4 width, content = 3/4) ---
        layout.widthProperty().addListener((obs, oldVal, newVal) -> {
            double totalWidth = newVal.doubleValue();
            menuContainer.setPrefWidth(totalWidth * 0.25);
            contentArea.setPrefWidth(totalWidth * 0.75);
        });
    }
}
