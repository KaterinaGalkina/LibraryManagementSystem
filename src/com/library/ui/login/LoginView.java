package com.library.ui.login;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.library.people.Member;
import com.library.ui.ApplicationFX;
import com.library.ui.generalStyling.UIStyling;
import com.library.ui.workspace.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;

public class LoginView {

    public GridPane start(Stage stage, Connection conn) {
        Label titleLabel = new Label("Log in");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Label userLabel = new Label("Username:");
        TextField usernameField = new TextField();

        Label passLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Label message = new Label();
        Button loginButton = new Button("Login");

        UIStyling.button_styling(loginButton, "#2c4237", "white");

        Label infoLabel = new Label("Don't have an account?");
        infoLabel.setStyle("-fx-text-fill: gray;");

        Label registerLink = new Label("Register");
        registerLink.setStyle("-fx-text-fill: #000000; -fx-underline: true; -fx-cursor: hand;");

        Label infoLabel2 = new Label("Prefer to do it later?");
        infoLabel2.setStyle("-fx-text-fill: gray;");

        Button login_as_guest_button = new Button("Login as guest");

        UIStyling.button_styling(login_as_guest_button, "#024d60", "white");

        registerLink.setOnMouseClicked(e -> {
            new BaseView().start(stage, new RegisterView().start(stage, conn));
        });

        GridPane loginGrid = new GridPane();
        loginGrid.setAlignment(Pos.CENTER);
        loginGrid.setVgap(10);
        loginGrid.setHgap(10);

        int row = 0;
        loginGrid.add(titleLabel, 0, row++, 2, 1); // span across 2 columns
        loginGrid.add(userLabel, 0, row);
        loginGrid.add(usernameField, 1, row++);
        loginGrid.add(passLabel, 0, row);
        loginGrid.add(passwordField, 1, row++);
        loginGrid.add(loginButton, 1, row++);
        loginGrid.add(message, 1, row++);
        loginGrid.add(infoLabel, 0, row);
        loginGrid.add(registerLink, 1, row++);
        loginGrid.add(infoLabel2, 0, row);
        loginGrid.add(login_as_guest_button, 1, row++);

        GridPane.setHalignment(titleLabel, javafx.geometry.HPos.CENTER);
        GridPane.setMargin(titleLabel, new Insets(0, 0, 10, 0));
        
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            Member connected_member = authenticate(username, password, conn); 
            if (connected_member != null) {
                message.setText("Login successful!");

                ApplicationFX.setConnected_member(connected_member);
                stage.close();

                new MenuView().start(stage, conn);
            } else {
                message.setText("Invalid credentials");
            }
        });

        login_as_guest_button.setOnAction(e -> {
            ApplicationFX.setConnected_member(null);
            stage.close();
            new MenuView().start(stage, conn);
        });
        return loginGrid;
    }

    public static Member authenticate(String username, String password, Connection conn) {
        // email is unique so we can find the member by it 
        try {
            String sql = "SELECT * FROM members WHERE mail = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, hashPassword(password));
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                System.out.println("Error: Member not found!");
                return null;
            } 
            int member_id = rs.getInt("id");
            if (member_id != -1){
                Member member = ApplicationFX.getMembers().stream()
                        .filter(a -> a.getId() == member_id)
                        .findFirst()
                        .orElse(null);
                return member;
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Error while connecting to the database.");
            return null;
        }
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            // Convert bytes to hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}