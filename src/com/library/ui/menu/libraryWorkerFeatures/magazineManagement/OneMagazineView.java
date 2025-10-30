package com.library.ui.menu.libraryWorkerFeatures.magazineManagement;

import java.sql.Connection;
import com.library.documents.Magazine;
import com.library.documents.Periodicity;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class OneMagazineView {
    // Form's fields and lables
    protected Label main_title;
    protected TextField magazine_title_field;
    protected ToggleGroup periodicity_group;
    protected HBox buttons;
    protected Label message;

    // Final values obtained;
    protected String magazine_title;
    protected Periodicity magazine_periodicity;

    public GridPane get_one_magazine_view(String main_title_text, Magazine current_magazine){
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
        Label title_label = new Label("Title:");
        this.magazine_title_field = new TextField();

        Label periodicity_label = new Label("Periodicity:");

        this.periodicity_group = new ToggleGroup();

        HBox periodicity_box = new HBox(10);
        periodicity_box.setAlignment(Pos.CENTER_LEFT);

        for (Periodicity periodicity: Periodicity.values()){
            RadioButton rb = new RadioButton(Periodicity.convertPeriodicityToString(periodicity));
            rb.setToggleGroup(this.periodicity_group);
            periodicity_box.getChildren().add(rb);
        }

        // --- Buttons ---
        Button save_button = new Button("Save");
        save_button.setStyle("-fx-background-color: #253721; -fx-text-fill: white; -fx-background-radius: 8;");

        Button cancel_button = new Button("Cancel");
        cancel_button.setStyle("-fx-background-color: #7b1d26; -fx-text-fill: white; -fx-background-radius: 8;");

        this.buttons = new HBox(10, save_button, cancel_button);
        buttons.setAlignment(Pos.CENTER);

        this.message = new Label(); // For errors

        // --- Setting the values of the current book, if we are modifying ---
        if (current_magazine != null){
            this.magazine_title_field.setText(current_magazine.getMagazine_title());
            for (Toggle toggle : this.periodicity_group.getToggles()) {
                RadioButton rb = (RadioButton) toggle;
                if (rb.getText().equalsIgnoreCase(Periodicity.convertPeriodicityToString(current_magazine.getPeriodicity()))) {
                    rb.setSelected(true);
                    break;
                }
            }
        }
        
        // --- Add everything to grid ---
        int row = 0;
        grid.add(this.main_title, 0, row++, 2, 1);
        grid.add(title_label, 0, row);
        grid.add(this.magazine_title_field, 1, row++);
        grid.add(periodicity_label, 0, row);
        grid.add(periodicity_box, 1, row++);
        grid.add(this.buttons, 0, row++, 2, 1);
        grid.add(this.message, 0, row, 2, 1);

        return grid;
    }

    protected boolean validity_checks(Connection conn, DropShadow red_glow, boolean adding_magazine){ // adding_book = true if we are adding a book, in this case the isbn should be unique
        this.magazine_title = this.magazine_title_field.getText().trim();
        RadioButton selected = (RadioButton) this.periodicity_group.getSelectedToggle();
        this.magazine_periodicity = selected != null ? Periodicity.convertStringToPeriodicity(selected.getText()) : null;
        boolean error_is_present = false; // Will indicate if so far everything is ok
        String main_error_message = "";

        if (magazine_title.equals("")){
            error_is_present = true;
            this.magazine_title_field.setEffect(red_glow);
            main_error_message += "The magazine's title cannot be empty\n";
        }
        
        if (this.magazine_periodicity == null) {
            error_is_present = true;
            main_error_message += "Please select a periodicity type.\n";
        }

        this.message.setText(main_error_message);
        if (error_is_present){
            return false;
        } else {
            return true;
        }
    }
}
