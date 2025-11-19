package com.library.ui.workspace.libraryWorkerFeatures.membersManagement;

import com.library.borrowingsystem.LibraryManager;
import com.library.people.Member;
import com.library.ui.generalStyling.UIStyling;
import java.sql.Connection;
import java.time.LocalDate;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
    protected CheckBox worker_checkbox;
    
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
    protected boolean member_is_library_worker;

    public GridPane get_one_member_view(String main_title_text, Member current_member){
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(15);
        grid.setHgap(15);
        grid.setPadding(new Insets(25));
        grid.setStyle("-fx-background-color: white; -fx-border-radius: 10; -fx-background-radius: 10;");

        this.main_title = new Label(main_title_text);
        this.main_title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2d3436;");

        // Fields
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

        Label worker_label = new Label("Is Library Worker:");
        this.worker_checkbox = new CheckBox();

        Label password_label = new Label("Password:");
        this.member_password_Field = new PasswordField();

        Label confirm_password_label = new Label("Confirm Password:");
        this.confirm_password_Field = new PasswordField();

        // Buttons
        Button save_button = new Button("Save");
        UIStyling.button_styling(save_button, "#253721", "white");

        Button cancel_button = new Button("Cancel");
        UIStyling.button_styling(cancel_button, "#7b1d26", "white");

        this.buttons = new HBox(10, save_button, cancel_button);
        buttons.setAlignment(Pos.CENTER);

        this.message = new Label(); // For errors

        // Setting the values of the current member, if we are modifying
        if(current_member != null){
            this.member_first_name_TextField.setText(current_member.getFirst_name());
            this.member_last_name_TextField.setText(current_member.getLast_name());
            this.member_email_TextField.setText(current_member.getMail());
            this.member_phone_TextField.setText(current_member.getPhone_number());
            this.member_address_TextField.setText(current_member.getAddress());
            this.member_birth_date_Picker.setValue(current_member.getBirth_date());
            this.worker_checkbox.setSelected(current_member.getIs_library_worker());
        }

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
        grid.add(worker_label, 0, row);
        grid.add(this.worker_checkbox, 1, row++);
        if (current_member == null){
            grid.add(password_label, 0, row);
            grid.add(this.member_password_Field, 1, row++);
            grid.add(confirm_password_label, 0, row);
            grid.add(this.confirm_password_Field, 1, row++);
        }
        grid.add(this.buttons, 0, row++, 2, 1);
        grid.add(this.message, 0, row, 2, 1);
        return grid;
    }

    protected boolean validity_checks(Connection conn, boolean adding_member) {
        // Remove all glows so that we can see only those that have currently a problem
        this.member_first_name_TextField.setEffect(null);
        this.member_last_name_TextField.setEffect(null);
        this.member_email_TextField.setEffect(null);
        this.member_phone_TextField.setEffect(null);
        this.member_birth_date_Picker.setEffect(null);
        this.member_address_TextField.setEffect(null);
        this.member_password_Field.setEffect(null);
        this.confirm_password_Field.setEffect(null);
        this.message.setText("");

        this.member_first_name = this.member_first_name_TextField.getText().trim();
        this.member_last_name = this.member_last_name_TextField.getText().trim();
        this.member_email = this.member_email_TextField.getText().trim();
        this.member_phone = this.member_phone_TextField.getText().trim();
        this.member_address = this.member_address_TextField.getText().trim();
        this.member_birth_date = this.member_birth_date_Picker.getValue();
        this.member_password = this.member_password_Field.getText().trim();
        this.member_confirm_password = this.confirm_password_Field.getText().trim();
        this.member_is_library_worker = this.worker_checkbox.isSelected();

        boolean error_is_present = false; // Will indicate if so far everything is ok
        String main_error_message = "";

        if (this.member_first_name.equals("")){
            error_is_present = true;
            this.member_first_name_TextField.setEffect(UIStyling.red_glow);
            main_error_message += "First name cannot be empty.\n";
        }

        if (this.member_last_name.equals("")){
            error_is_present = true;
            this.member_last_name_TextField.setEffect(UIStyling.red_glow);
            main_error_message += "Last name cannot be empty.\n";
        }

        if (this.member_email.equals("")){
            error_is_present = true;
            this.member_email_TextField.setEffect(UIStyling.red_glow);
            main_error_message += "Email cannot be empty.\n";
        }

        if (adding_member){
            if (LibraryManager.exists(conn, "members", "mail", this.member_email)) {
                error_is_present = true;
                this.member_email_TextField.setEffect(UIStyling.red_glow);
                main_error_message += "The user with this email address already exists.\n";
            }
        }

        if (this.member_birth_date == null){   
            error_is_present = true;
            main_error_message += "Please select a birth date.\n";
        }

        if (adding_member) {
            if (this.member_password.equals("")){
                error_is_present = true;
                this.member_password_Field.setEffect(UIStyling.red_glow);
                main_error_message += "Password cannot be empty.\n";
            }
        }
        
        if (adding_member || !this.member_password.isEmpty()) {
            if (!this.member_password.equals(this.member_confirm_password)){
                error_is_present = true;
                this.member_password_Field.setEffect(UIStyling.red_glow);
                this.confirm_password_Field.setEffect(UIStyling.red_glow);
                main_error_message += "Passwords do not match.\n";
            }
        }

        this.message.setText(main_error_message);
        if (error_is_present){
            return false;
        } else {
            return true;
        }
    }
}
