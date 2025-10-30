package com.library.ui.menu.menuSections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import com.library.borrowingsystem.LibraryManager;
import com.library.people.Member;
import com.library.ui.ApplicationFX;
import com.library.ui.login.LoginView;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class ProfileView {
    private static Map<Object, Object> originalValues = new HashMap<>();

    private void update_original_values(Member connected_member, TextField first_nameField, TextField last_nameField, DatePicker birth_datePicker, TextField addressField, TextField phoneField, TextField mailField) {
        originalValues.put(first_nameField, connected_member.getFirst_name());
        originalValues.put(last_nameField, connected_member.getLast_name());
        originalValues.put(birth_datePicker, connected_member.getBirth_date());
        originalValues.put(addressField, connected_member.getAddress());
        originalValues.put(phoneField, connected_member.getPhone_number());
        originalValues.put(mailField, connected_member.getMail());
    }

    private boolean isAnyFieldModified() {
    for (Object fieldObj : originalValues.keySet()) {
        if (fieldObj instanceof TextField tf) {
            if (!tf.getText().equals(originalValues.get(tf))) {
                return true;
            }
        } else if (fieldObj instanceof DatePicker dp) {
            if (!dp.getValue().equals(originalValues.get(dp))) {
                return true;
            }
        }
    }
    return false;
}

    public ScrollPane start(Connection conn) { 
        // --- Create a simple grid layout for the main content ---
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(20);
        grid.setHgap(20);
        grid.setStyle("-fx-background-color: white;");
        grid.setPadding(new Insets(20, 20, 20, 20));

        grid.setStyle("""
            -fx-background-color: white;
            -fx-focus-color: lightgray;
            -fx-faint-focus-color: transparent;
        """);

        Member connected_member = ApplicationFX.getConnected_member();
        // --- Title ---
        Label titleLabel = new Label("My Profile");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Label first_nameLabel = new Label("First name:");
        TextField first_nameField = new TextField(connected_member.getFirst_name());

        Label last_nameLabel = new Label("Last name:");
        TextField last_nameField = new TextField(connected_member.getLast_name());

        Label birth_dateLabel = new Label("Birthday:");
        DatePicker birth_datePicker = new DatePicker(connected_member.getBirth_date());

        Label addressLabel = new Label("Address:");
        TextField addressField = new TextField(connected_member.getAddress());

        Label phoneLabel = new Label("Phone:");
        TextField phoneField = new TextField(connected_member.getPhone_number());

        Label mailLabel = new Label("Email:");
        TextField mailField = new TextField(connected_member.getMail());

        Separator separator = new Separator(Orientation.HORIZONTAL);
        separator.setPrefWidth(450); // adjust to control line length
        separator.setStyle("-fx-background-color: #cccccc;");

        Label password_change_Label = new Label("Change password");

        Label old_passLabel = new Label("Old password:");
        PasswordField old_passField = new PasswordField();

        Label new_passLabel = new Label("New Password:");
        PasswordField new_passField = new PasswordField();

        Label confirmLabel = new Label("Confirm new Password:");
        PasswordField confirmField = new PasswordField();

        Label message = new Label();
        Button saveButton = new Button("Save modifications");
        Button resetButton = new Button("Reset");
        resetButton.setDisable(true); // initially nothing to reset

        // --- Add elements to grid in logical order ---
        int row = 0;

        grid.add(titleLabel, 0, row++, 2, 1); // span across 2 columns

        grid.add(message, 0, row++, 2, 1); // span across 2 columns

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

        grid.add(separator, 0, row++, 2, 1); // To separate all fields from the password ones

        grid.add(password_change_Label, 0, row++, 2, 1); // span across 2 columns

        grid.add(old_passLabel, 0, row);
        grid.add(old_passField, 1, row++);

        grid.add(new_passLabel, 0, row);
        grid.add(new_passField, 1, row++);

        grid.add(confirmLabel, 0, row);
        grid.add(confirmField, 1, row++);

        grid.add(resetButton, 0, row);
        grid.add(saveButton, 1, row++);

         // --- Center the title ---
        GridPane.setHalignment(titleLabel, javafx.geometry.HPos.CENTER);
        GridPane.setMargin(titleLabel, new Insets(0, 0, 10, 0));

        // --- Optional styling ---
        resetButton.setMaxWidth(Double.MAX_VALUE);
        saveButton.setMaxWidth(Double.MAX_VALUE);

        // --- Keep track of original values ---
        update_original_values(connected_member, first_nameField, last_nameField, birth_datePicker, addressField, phoneField, mailField);

        // --- Define green glow effect --- for fields that were modified
        DropShadow green_glow = new DropShadow();
        green_glow.setColor(Color.LIMEGREEN);
        green_glow.setOffsetX(0);
        green_glow.setOffsetY(0);
        green_glow.setRadius(10);

        // --- Define red glow effect --- for fields that were modified and have an error
        DropShadow red_glow = new DropShadow();
        red_glow.setColor(Color.RED);
        red_glow.setOffsetX(0);
        red_glow.setOffsetY(0);
        red_glow.setRadius(10);

        // --- Attach listeners for modification detection ---
        ChangeListener<String> textListener = (obs, oldVal, newVal) -> {
            TextField tf = (TextField) ((javafx.beans.property.StringProperty) obs).getBean();
            if (!newVal.equals(originalValues.get(tf))) {
                tf.setEffect(green_glow);
            } else {
                tf.setEffect(null);
            }
            resetButton.setDisable(!isAnyFieldModified()); // If anything is changed we enable resseting, otherwise we are disabling it
        };

        ChangeListener<LocalDate> dateListener = (obs, oldVal, newVal) -> {
            if (!newVal.equals(originalValues.get(birth_datePicker))) {
                birth_datePicker.setEffect(green_glow);
            } else {
                birth_datePicker.setEffect(null);
            }
            resetButton.setDisable(!isAnyFieldModified()); // If anything is changed we enable resseting, otherwise we are disabling it
        };

        // Attach to fields
        first_nameField.textProperty().addListener(textListener);
        last_nameField.textProperty().addListener(textListener);
        addressField.textProperty().addListener(textListener);
        phoneField.textProperty().addListener(textListener);
        mailField.textProperty().addListener(textListener);
        birth_datePicker.valueProperty().addListener(dateListener);

        // --- Reset logic ---
        resetButton.setOnAction(e -> {
            first_nameField.setText((String) originalValues.get(first_nameField));
            last_nameField.setText((String) originalValues.get(last_nameField));
            addressField.setText((String) originalValues.get(addressField));
            phoneField.setText((String) originalValues.get(phoneField));
            mailField.setText((String) originalValues.get(mailField));
            birth_datePicker.setValue((LocalDate) originalValues.get(birth_datePicker));

            // Remove all glows
            first_nameField.setEffect(null);
            last_nameField.setEffect(null);
            addressField.setEffect(null);
            phoneField.setEffect(null);
            mailField.setEffect(null);
            birth_datePicker.setEffect(null);

            old_passField.setText("");
            new_passField.setText("");
            confirmField.setText("");

            message.setText("");
        });

        // --- Save logic ---
        saveButton.setOnAction(e -> {
            // Remove all glows so that we can see only those that have currently a problem
            first_nameField.setEffect(null);
            last_nameField.setEffect(null);
            addressField.setEffect(null);
            phoneField.setEffect(null);
            mailField.setEffect(null);
            birth_datePicker.setEffect(null);

            String error_message = ""; // To keep track of every error we may encounter

            String first_name = first_nameField.getText();
            if (!first_name.equals((String) originalValues.get(first_nameField))) {
                if (first_name.equals("")) {
                    error_message += "First name cannot be empty\n";
                    first_nameField.setEffect(red_glow);
                } else {
                    connected_member.setFirst_name(first_name);
                }
            }

            String last_name = last_nameField.getText();
            if (!last_name.equals((String) originalValues.get(last_nameField))) {
                if (last_name.equals("")) {
                    error_message += "Last name cannot be empty\n";
                    last_nameField.setEffect(red_glow);
                } else {
                    connected_member.setLast_name(last_name);
                }
            }

            LocalDate birthdate = birth_datePicker.getValue();
            if (!birthdate.equals((LocalDate) originalValues.get(birth_datePicker))) {
                if (birthdate == null || birthdate.isAfter(LocalDate.now())) {
                    error_message += "Birthday is not valid\n";
                } else {
                    connected_member.setBirth_date(birthdate);
                }
            }

            // Address can be empty
            String address = addressField.getText();
            if (!address.equals((String) originalValues.get(addressField))) {
                connected_member.setAddress(address);
            }

            // phone number can be empty
            String phone = phoneField.getText();
            if (!phone.equals((String) originalValues.get(phoneField))) {
                connected_member.setPhone_number(phone);
            }

            // Before modyfing the mail, if the user would like to change the password we have to first handle this
            String old_pass = old_passField.getText();
            String possible_new_password = null;
            if (!old_pass.equals("")) {
                if (LoginView.authenticate((String) originalValues.get(mailField), old_pass, conn) != null){
                    String new_pass = new_passField.getText();
                    String confirm_pass = confirmField.getText();
                    if (!new_pass.equals(confirm_pass)) {
                        error_message += "Passwords don't match\n";
                        new_passField.setEffect(red_glow);
                        confirmField.setEffect(red_glow);
                    } else if(new_pass.equals("")){
                        error_message += "Password cannot be empty\n";
                        new_passField.setEffect(red_glow);
                        confirmField.setEffect(red_glow);
                    } else {
                        // Everything is good we can update
                        possible_new_password = LoginView.hashPassword(new_pass);
                    }
                } else {
                    error_message += "The old password is incorrect\n";
                    old_passField.setEffect(red_glow);
                }
            }

            String mail = mailField.getText();
            if (!mail.equals((String) originalValues.get(mailField))) {
                if (mail.equals("")) {
                    error_message += "Email connot be empty\n";
                    mailField.setEffect(red_glow);
                } else { // We have to check that it is unique in the database
                    try {

                        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM members WHERE mail = ?");
                        stmt.setString(1, mail);
                        ResultSet rs = stmt.executeQuery();

                        if (rs.next()) { // There is already a user with this email address
                            error_message += "This email is already taken\n";
                            mailField.setEffect(red_glow);
                        } else {
                            connected_member.setMail(mail);
                        }
                    } catch (SQLException e2) {
                        System.out.println("Error while querrying the database");
                    }
                }
            }

            message.setText(error_message);

            // We update that we were able to update
            if (LibraryManager.update_member(conn, connected_member, possible_new_password) && error_message.equals("")) {
                message.setText("Updates were made successfully !");
                old_passField.setText("");
                new_passField.setText("");
                confirmField.setText("");
            }
            // If some entries were modified we need to update them
            update_original_values(connected_member, first_nameField, last_nameField, birth_datePicker, addressField, phoneField, mailField);
        });

        // Allow clicking on empty background to remove focus from fields
        grid.setOnMouseClicked(e -> {
            grid.requestFocus();
        });

        // --- Wrap the GridPane inside a ScrollPane ---
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(grid);
        scrollPane.setFitToWidth(true);       // make grid width match the scrollpane width
        scrollPane.setFitToHeight(false);     // height can scroll
        scrollPane.setPannable(true);         // allows dragging to scroll
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // optional: hide horizontal scrollbar
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        return scrollPane;
    }
}
