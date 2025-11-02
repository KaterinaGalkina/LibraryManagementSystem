package com.library.ui.menu.libraryWorkerFeatures.membersManagement;

import com.library.people.Member;

import java.sql.Connection;
import java.time.LocalDate;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.control.DatePicker;

public class OneMemberView {
    // Form's fields and lables
    protected Label main_title;
    protected TextField member_first_name_TextField;
    protected TextField member_last_name_TextField;
    protected TextField member_email_TextField;
    protected TextField member_phone_TextField;
    protected TextField member_address_TextField;
    protected DatePicker member_birth_date_Picker;
    protected PasswordField member_password_Field;
    protected PasswordField confirm_password_Field;
    
    protected HBox buttons;
    protected Label message;

    // Final values obtained
    protected String member_first_name;
    protected String member_last_name;
    protected String member_email;
    protected String member_phone;
    protected LocalDate member_birth_date;
    protected String member_address;
    protected String member_password;
    protected String member_confirm_password;

    public GridPane get_one_member_view(String main_title_text, Member current_member){
        // --- Grid layout ---
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(15);
        grid.setHgap(15);
        grid.setPadding(new Insets(25));
        grid.setStyle("-fx-background-color: white; -fx-border-radius: 10; -fx-background-radius: 10;");

        this.main_title = new Label(main_title_text);
        this.main_title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2d3436;");

        // --- Fields ---
        Label first_name_label = new Label("First Name:");
        this.member_first_name_TextField = new TextField();

        Label last_name_label = new Label("Last Name:");
        this.member_last_name_TextField = new TextField();

        Label email_label = new Label("Email:");
        this.member_email_TextField = new TextField();

        Label phone_label = new Label("Phone:");
        this.member_phone_TextField = new TextField();

        Label address_label = new Label("Address:");
        this.member_address_TextField = new TextField();

        Label birth_date_label = new Label("Birth Date:");
        this.member_birth_date_Picker = new DatePicker();

        Label password_label = new Label("Password:");
        this.member_password_Field = new PasswordField();

        Label confirm_password_label = new Label("Confirm Password:");
        this.confirm_password_Field = new PasswordField();

        boolean adding = (current_member == null); 
        if (!adding){
            // --- Modifying existing member: hide password fields ---
            this.member_password_Field.setVisible(false);
            this.member_password_Field.setManaged(false);
            this.confirm_password_Field.setVisible(false);
            this.confirm_password_Field.setManaged(false);

            // Hide labels too
            password_label.setVisible(false);
            password_label.setManaged(false);
            confirm_password_label.setVisible(false);
            confirm_password_label.setManaged(false);
        }

        // --- Buttons ---
        Button save_button = new Button("Save");
        save_button.setStyle("-fx-background-color: #253721; -fx-text-fill: white; -fx-background-radius: 8;");

        Button cancel_button = new Button("Cancel");
        cancel_button.setStyle("-fx-background-color: #7b1d26; -fx-text-fill: white; -fx-background-radius: 8;");

        this.buttons = new HBox(10, save_button, cancel_button);
        buttons.setAlignment(Pos.CENTER);

        this.message = new Label(); // For errors

        // --- Setting the values of the current member, if we are modifying ---
        if(current_member != null){
            this.member_first_name_TextField.setText(current_member.getFirst_name());
            this.member_last_name_TextField.setText(current_member.getLast_name());
            this.member_email_TextField.setText(current_member.getMail());
            this.member_phone_TextField.setText(current_member.getPhone_number());
            this.member_address_TextField.setText(current_member.getAddress());
            this.member_birth_date_Picker.setValue(current_member.getBirth_date());
        }

        // --- Add everything to grid ---
        int row = 0;
        grid.add(this.main_title, 0, row++, 2, 1);
        grid.add(first_name_label, 0, row);
        grid.add(this.member_first_name_TextField, 1, row++);
        grid.add(last_name_label, 0, row);
        grid.add(this.member_last_name_TextField, 1, row++);
        grid.add(email_label, 0, row);
        grid.add(this.member_email_TextField, 1, row++);
        grid.add(phone_label, 0, row);
        grid.add(this.member_phone_TextField, 1, row++);
        grid.add(address_label, 0, row);
        grid.add(this.member_address_TextField, 1, row++);
        grid.add(birth_date_label, 0, row);
        grid.add(this.member_birth_date_Picker, 1, row++);
        grid.add(password_label, 0, row);
        grid.add(this.member_password_Field, 1, row++);
        grid.add(confirm_password_label, 0, row);
        grid.add(this.confirm_password_Field, 1, row++);
        grid.add(this.buttons, 0, row++, 2, 1);
        grid.add(this.message, 0, row, 2, 1);
        
        return grid;
    }

    protected boolean validity_checks(Connection conn, DropShadow red_glow, boolean adding_member) {
        this.member_first_name = this.member_first_name_TextField.getText().trim();
        this.member_last_name = this.member_last_name_TextField.getText().trim();
        this.member_email = this.member_email_TextField.getText().trim();
        this.member_phone = this.member_phone_TextField.getText().trim();
        this.member_address = this.member_address_TextField.getText().trim();
        this.member_birth_date = this.member_birth_date_Picker.getValue();
        this.member_password = this.member_password_Field.getText().trim();
        this.member_confirm_password = this.confirm_password_Field.getText().trim();

        boolean error_is_present = false; // Will indicate if so far everything is ok
        String main_error_message = "";

        if (this.member_first_name.equals("")){
            error_is_present = true;
            this.member_first_name_TextField.setEffect(red_glow);
            main_error_message += "First name cannot be empty.\n";
        }

        if (this.member_last_name.equals("")){
            error_is_present = true;
            this.member_last_name_TextField.setEffect(red_glow);
            main_error_message += "Last name cannot be empty.\n";
        }

        if (this.member_email.equals("")){
            error_is_present = true;
            this.member_email_TextField.setEffect(red_glow);
            main_error_message += "Email cannot be empty.\n";
        }

        if (this.member_birth_date == null){   
            error_is_present = true;
            main_error_message += "Please select a birth date.\n";
        }

        if (adding_member) { // <-- add
            if (this.member_password.equals("")){
                error_is_present = true;
                this.member_password_Field.setEffect(red_glow);
                main_error_message += "Password cannot be empty.\n";
            }
            if (!this.member_password.equals(this.member_confirm_password)){
                error_is_present = true;
                this.confirm_password_Field.setEffect(red_glow);
                main_error_message += "Passwords do not match.\n";
            }
        } // <-- add
        if (!this.member_password.equals(this.member_confirm_password)){
            error_is_present = true;
            this.confirm_password_Field.setEffect(red_glow);
            main_error_message += "Passwords do not match.\n";
        }


        this.message.setText(main_error_message);
        if (error_is_present){
            return false;
        } else {
            return true;
        }
    }
}
