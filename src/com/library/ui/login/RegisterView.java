package com.library.ui.login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import com.library.borrowingsystem.LibraryManager;
import com.library.people.Member;
import com.library.ui.ApplicationFX;
import com.library.ui.menu.MenuView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class RegisterView {
    public GridPane start(Stage stage, Connection conn) {
        // --- Title ---
        Label titleLabel = new Label("Register");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Label first_nameLabel = new Label("First name:");
        TextField first_nameField = new TextField();

        Label last_nameLabel = new Label("Last name:");
        TextField last_nameField = new TextField();

        Label birth_dateLabel = new Label("Birthday:");
        DatePicker birth_datePicker = new DatePicker();

        Label addressLabel = new Label("Address:");
        TextField addressField = new TextField();

        Label phoneLabel = new Label("Phone:");
        TextField phoneField = new TextField();

        Label mailLabel = new Label("Email:");
        TextField mailField = new TextField();

        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();

        Label confirmLabel = new Label("Confirm Password:");
        PasswordField confirmField = new PasswordField();

        Label message = new Label();
        Button registerButton = new Button("Register");
        Button backButton = new Button("Back to Login");

        Label infoLabel2 = new Label("Prefer to do it later?");
        infoLabel2.setStyle("-fx-text-fill: gray;");

        Button login_as_guest_button = new Button("Login as guest");

        // --- Layout setup ---
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(15);
        grid.setPadding(new Insets(20, 20, 20, 20));

        // --- Add elements to grid in logical order ---
        int row = 0;

        grid.add(titleLabel, 0, row++, 2, 1); // span across 2 columns

        grid.add(first_nameLabel, 0, row);
        grid.add(first_nameField, 1, row++);

        grid.add(last_nameLabel, 0, row);
        grid.add(last_nameField, 1, row++);

        grid.add(birth_dateLabel, 0, row);
        grid.add(birth_datePicker, 1, row++);

        grid.add(addressLabel, 0, row);
        grid.add(addressField, 1, row++);

        grid.add(phoneLabel, 0, row);
        grid.add(phoneField, 1, row++);

        grid.add(mailLabel, 0, row);
        grid.add(mailField, 1, row++);

        grid.add(passLabel, 0, row);
        grid.add(passField, 1, row++);

        grid.add(confirmLabel, 0, row);
        grid.add(confirmField, 1, row++);

        grid.add(registerButton, 1, row++);
        grid.add(message, 1, row++);
        grid.add(backButton, 1, row++);

        grid.add(infoLabel2, 0, row);
        grid.add(login_as_guest_button, 1, row++);

         // --- Center the title ---
        GridPane.setHalignment(titleLabel, javafx.geometry.HPos.CENTER);
        GridPane.setMargin(titleLabel, new Insets(0, 0, 10, 0));

        // --- Optional styling ---
        registerButton.setMaxWidth(Double.MAX_VALUE);
        backButton.setMaxWidth(Double.MAX_VALUE);

        login_as_guest_button.setOnAction(e -> {
            ApplicationFX.setConnected_member(null);
            stage.close();
            new MenuView().start(stage, conn);
        });

        // --- Register logic ---
        registerButton.setOnAction(e -> {
            String first_name = first_nameField.getText().trim();
            String last_name = last_nameField.getText().trim();
            String address = addressField.getText().trim();
            String phone = phoneField.getText().trim();
            LocalDate birthday = birth_datePicker.getValue();
            String mail = mailField.getText().trim();
            String pass = passField.getText();
            String confirm = confirmField.getText();

            if (!pass.equals(confirm)) {
                message.setText("❌ Passwords don't match!");
                return;
            }

            if (first_name.isBlank() || last_name.isBlank() || mail.isBlank() || pass.isBlank()) {
                message.setText("⚠️ Please fill in all required fields.");
                return;
            }

            if (birthday == null || birthday.isAfter(LocalDate.now())) {
                message.setText("⚠️ Please enter a valid birthday.");
                return;
            }

            try {
                // First we are checking whether this email address is already taken
                String sql = "SELECT * FROM members WHERE mail = ?";
                PreparedStatement checkStmt = conn.prepareStatement(sql);
                checkStmt.setString(1, mail);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    message.setText("❌ The user with this email address already exists!");
                    return;
                }

                Member new_member = new Member(first_name, last_name, birthday, phone, address, mail);
                if (LibraryManager.add_member(conn, new_member, LoginView.hashPassword(pass))) {
                    message.setText("✅ Registration successful!");
                } else {
                    message.setText("❌ Registration failed!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                message.setText("⚠️ Error: user may already exist.");
            }
        });

        backButton.setOnMouseClicked(e -> {
            new BaseView().start(stage, new LoginView().start(stage, conn));
        });
        
        return grid;
    }
}
