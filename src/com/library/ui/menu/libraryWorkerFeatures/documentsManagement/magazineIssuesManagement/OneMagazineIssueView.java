package com.library.ui.menu.libraryWorkerFeatures.documentsManagement.magazineIssuesManagement;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.library.documents.Document;
import com.library.documents.Magazine;
import com.library.documents.MagazineIssue;
import com.library.ui.ApplicationFX;
import com.library.ui.menu.libraryWorkerFeatures.documentsManagement.OneDocumentView;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;

public class OneMagazineIssueView extends OneDocumentView {
    // Form's fields and lables
    protected ComboBox<String> magazine_combo;
    protected Spinner<Integer> issue_number_spinner;
    protected DatePicker issue_date_picker;

    // Final values obtained
    protected Magazine magazine;
    protected int issue_number;
    protected LocalDate issue_date;

    public GridPane get_one_magazine_issue_view(String main_title_text, MagazineIssue current_magazine_issue){
        GridPane grid = get_one_document_view(main_title_text, current_magazine_issue);
        // --- Genre selection (ComboBox with multiselect simulation) ---
        
        Label magazine_label = new Label("Magazine:");
        this.magazine_combo = new ComboBox<>();

        List<String> possible_magazines = new ArrayList<>();
        for(Magazine magazine: ApplicationFX.getMagazines()){
            possible_magazines.add(magazine.getMagazine_title());
        }

        magazine_combo.getItems().addAll(possible_magazines);
        magazine_combo.setPromptText("Issue's Magazine");
        magazine_combo.setVisibleRowCount(5);

        Label issue_number_label = new Label("Issue Number:");
        this.issue_number_spinner = new Spinner<>(1, 1000, 1); // (min, max, default)
        issue_number_spinner.setEditable(true);

        Label issue_date_label = new Label("Issue Date:");
        this.issue_date_picker = new DatePicker();
        issue_date_picker.setPromptText("Select publication date");

        // --- Pre-fill values if modifying an existing magazine issue ---
        if (current_magazine_issue != null) {
            // Set current magazine if known
            Magazine currentMagazine = current_magazine_issue.getMagazine();
            if (currentMagazine != null) {
                this.magazine_combo.setValue(currentMagazine.getMagazine_title());
            }

            this.issue_number_spinner.getValueFactory().setValue(current_magazine_issue.getIssue_number());

            if (current_magazine_issue.getIssue_date() != null) {
                this.issue_date_picker.setValue(current_magazine_issue.getIssue_date());
            }
        }

        int last_row = getNextAvailableRow(grid);
        
        grid.add(magazine_label, 0, last_row);
        grid.add(this.magazine_combo, 1, last_row++);
        grid.add(issue_number_label, 0, last_row);
        grid.add(this.issue_number_spinner, 1, last_row++);
        grid.add(issue_date_label, 0, last_row);
        grid.add(this.issue_date_picker, 1, last_row++);

        this.add_buttons(grid);
        return grid;
    }

    protected boolean validity_checks(Connection conn, DropShadow red_glow, boolean adding_magazine, MagazineIssue issue_to_change){ // If we are adding a magazine we need to check that its issue number is unique for this magazine 
        String selected_magazine_title = this.magazine_combo.getValue();
        this.issue_number = this.issue_number_spinner.getValue();
        this.issue_date = this.issue_date_picker.getValue();

        this.magazine_combo.setEffect(null);
        this.issue_number_spinner.setEffect(null);
        this.issue_date_picker.setEffect(null);

        boolean error_is_present = false;
        String main_error_message = "";

        // Find magazine object from selected title
        if (selected_magazine_title != null) {
            for (Magazine mag : ApplicationFX.getMagazines()) {
                if (mag.getMagazine_title().equals(selected_magazine_title)) {
                    this.magazine = mag;
                    break;
                }
            }
        }

        error_is_present = !super.validity_checks(conn, red_glow) || error_is_present;

        if (this.magazine == null) {
            error_is_present = true;
            this.magazine_combo.setEffect(red_glow);
            main_error_message += "Please select a magazine.\n";
        } else if (adding_magazine || (issue_to_change != null && this.issue_number != issue_to_change.getIssue_number())){ // We need to check that the couple issue number and magazine_id are unique in the database
            for (Document doc: ApplicationFX.getDocuments()){
                if (doc instanceof MagazineIssue issue){
                    if (issue.getMagazine() == this.magazine && issue.getIssue_number() == this.issue_number){
                        main_error_message += "Please enter a unique issue number for this magazine!\n";
                        error_is_present = true;
                        this.issue_number_spinner.setEffect(red_glow);
                        break;
                    }
                }
            }
        }

        if (this.issue_date == null || this.issue_date.isAfter(LocalDate.now())){
            main_error_message += "Please enter a valid issue date!\n";
            error_is_present = true;
            this.issue_date_picker.setEffect(red_glow);
        }

        this.message.setText(this.message.getText() + "\n" + main_error_message);

        return !error_is_present;
    }
}
