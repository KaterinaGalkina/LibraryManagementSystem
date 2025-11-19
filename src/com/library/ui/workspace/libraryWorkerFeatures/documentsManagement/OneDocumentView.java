package com.library.ui.workspace.libraryWorkerFeatures.documentsManagement;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import com.library.borrowingsystem.LibraryManager;
import com.library.documents.Document;
import com.library.documents.Genre;
import com.library.people.Author;
import com.library.ui.ApplicationFX;
import com.library.ui.generalStyling.UIStyling;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public abstract class OneDocumentView {
    // Form's fields and lables
    protected Label main_title;
    protected TextField title_field;
    protected Spinner<Integer> nb_copies_spinner;
    protected ComboBox<String> genre_combo;
    protected FlowPane selected_genres_pane;
    protected VBox authors_container;
    protected VBox authors_section;
    protected HBox buttons;
    protected Label message;

    // Final values obtained
    public String document_title;
    public int document_nb_copies;
    public HashSet<Genre> document_genres;
    public HashSet<Author> document_authors;

    public GridPane get_one_document_view(String main_title_text, Document current_document){
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(15);
        grid.setHgap(15);
        grid.setPadding(new Insets(25));
        grid.setStyle("-fx-background-color: white; -fx-border-radius: 10; -fx-background-radius: 10;");

        this.main_title = new Label(main_title_text);
        this.main_title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2d3436;");

        // Fields
        Label title_label = new Label("Title:");
        this.title_field = new TextField();

        Label nb_copies_label = new Label("Number of Copies:");
        this.nb_copies_spinner = new Spinner<>(1, 1000, 1);
        nb_copies_spinner.setEditable(true);

        Label genre_label = new Label("Genres:");
        this.genre_combo = new ComboBox<>();

        List<String> possible_genres = new ArrayList<>();
        for(Genre g: Genre.values()){
            possible_genres.add(g.toString());
        }

        genre_combo.getItems().addAll(possible_genres);

        genre_combo.setPromptText("Document's genre");

        // Limiting how many rows are visible before scrollbars appear
        genre_combo.setVisibleRowCount(5);

        // To display selected genres dynamically
        this.selected_genres_pane = new FlowPane(10, 5);
        selected_genres_pane.setPadding(new Insets(5, 0, 0, 0));

        genre_combo.setOnAction(e -> {
            String selected = genre_combo.getValue();
            if (selected != null && selected_genres_pane.getChildren().stream()
                    .noneMatch(n -> ((Label) n).getText().equals(selected + " ✕"))) {

                Label genre_chip = new Label(selected + " ✕");
                genre_chip.setStyle("-fx-background-color: #dfe6e9; -fx-padding: 5 10; -fx-background-radius: 10; -fx-cursor: hand;");
                
                genre_chip.setOnMouseClicked(ev -> selected_genres_pane.getChildren().remove(genre_chip));
                selected_genres_pane.getChildren().add(genre_chip);
            }

            // Reset the ComboBox selection
            Platform.runLater(() -> genre_combo.setValue(null));
        });

        // Custom button cell to always show the prompt when value is null
        genre_combo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(genre_combo.getPromptText());
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: black;");
                }
            }
        });

        // Authors section
        Label authors_label = new Label("Authors:");
        this.authors_container = new VBox(10);
        authors_container.setAlignment(Pos.TOP_LEFT);

        // Buttons
        Button add_author_button = new Button("+ Add Author");
        UIStyling.button_styling(add_author_button, "#024d60", "white");

        Button find_author_button = new Button("Find existing Author");
        UIStyling.button_styling(find_author_button, "#024d60", "white");

        // ComboBox to select existing authors
        ComboBox<String> author_combo = new ComboBox<>();
        List<String> existing_authors = ApplicationFX.getAuthors().stream()
            .map(el -> el.getFirst_name() + " " + el.getLast_name())
            .toList();

        author_combo.getItems().addAll(existing_authors);
        author_combo.setPromptText("Select existing author");

        // Button actions
        add_author_button.setOnAction(ev -> addAuthorForm(null));

        // Custom button cell to always show the prompt when value is null
        author_combo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(author_combo.getPromptText());
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: black;");
                }
            }
        });

        // Limit how many rows are visible before scrollbars appear
        author_combo.setVisibleRowCount(5);

        // ComboBox action (add an existing author)
        author_combo.setOnAction(e -> {
            String selected = author_combo.getValue();
            if (selected != null && !selected.isBlank()) {

                // Optional: check if already added
                boolean already_added = authors_container.getChildren().stream().anyMatch(node -> {
                    if (node instanceof GridPane gp) {
                        for (var child : gp.getChildren()) {
                            if (child instanceof TextField tf && tf.getText().equalsIgnoreCase(selected.split(" ")[0])) {
                                return true;
                            }
                        }
                    }
                    return false;
                });

                if (!already_added) {
                    Author found_author = null;
                    for (Author author: ApplicationFX.getAuthors()){
                        if (author.getFirst_name().equals(selected.split(" ")[0]) && author.getLast_name().equals(selected.split(" ")[1])){
                            found_author = author;
                        }
                    }
                    if (found_author != null){
                        addAuthorForm(found_author);
                    }
                }
            }

            // Reset selection
            Platform.runLater(() -> {
                author_combo.getSelectionModel().clearSelection();
                author_combo.setValue(null);
                author_combo.setPromptText("Select existing author");
            });
        });

        HBox authors_buttons = new HBox(10, add_author_button, author_combo);
        this.authors_section = new VBox(10, authors_label, authors_container, authors_buttons);
        authors_section.setAlignment(Pos.TOP_LEFT);

        // --- Setting the values of the current book, if we are modifying ---
        if (current_document != null){
            this.title_field.setText(current_document.getTitle());
            this.nb_copies_spinner.getValueFactory().setValue(current_document.getNb_copies());

            List<Label> book_genres = new ArrayList<>();
            
            for(Genre genre: current_document.getGenre()){
                Label genre_chip = new Label(genre.toString() + " ✕");
                genre_chip.setStyle("-fx-background-color: #dfe6e9; -fx-padding: 5 10; -fx-background-radius: 10; -fx-cursor: hand;");
                genre_chip.setOnMouseClicked(ev -> selected_genres_pane.getChildren().remove(genre_chip));
                book_genres.add(genre_chip);
            }
            selected_genres_pane.getChildren().addAll(book_genres);

            for(Author author: current_document.getAuthors()){
                addAuthorForm(author);
                
            }
        }

        // Adding everything to grid except buttons as they will come at the end of everything
        int row = 0;
        grid.add(this.main_title, 0, row++, 2, 1);
        grid.add(title_label, 0, row);
        grid.add(this.title_field, 1, row++);
        grid.add(nb_copies_label, 0, row);
        grid.add(this.nb_copies_spinner, 1, row++);
        grid.add(genre_label, 0, row);
        grid.add(genre_combo, 1, row++);
        grid.add(this.selected_genres_pane, 1, row++);
        grid.add(authors_label, 0, row);
        grid.add(this.authors_section, 1, row++);
        
        return grid;
    }

    public int getNextAvailableRow(GridPane grid) {
        int maxRow = -1;
        for (Node node : grid.getChildren()) {
            Integer rowIndex = GridPane.getRowIndex(node);
            if (rowIndex != null && rowIndex > maxRow) {
                maxRow = rowIndex;
            }
        }
        return maxRow + 1; // next empty row
    }

    // Single author grid
    private void addAuthorForm(Author author){
        GridPane grid_author = new GridPane();
        grid_author.setAlignment(Pos.CENTER_LEFT);
        grid_author.setVgap(10);
        grid_author.setHgap(10);
        grid_author.setStyle("-fx-background-color: #f9f9f9; -fx-padding: 10; -fx-border-radius: 8; -fx-background-radius: 8;");

        Label first_name_label = new Label("First name:");
        TextField first_name_field = new TextField(author != null ? author.getFirst_name() : "");
        
        Label last_name_label = new Label("Last name:");
        TextField last_name_field = new TextField(author != null ? author.getLast_name() : "");

        Label birth_date_label = new Label("Birthday:");
        DatePicker birth_date_picker = new DatePicker(author != null ? author.getBirth_date() : null);

        Label message_author = new Label("");

        if (author != null) { // If a user added an author he can't modify the info about this author, only to remove it 
            first_name_field.setEditable(false);
            last_name_field.setEditable(false);
            birth_date_picker.setDisable(true);
            // Visually also, we indicate that it is read only
            first_name_field.setStyle("-fx-text-fill: #7e8182ff;");
            last_name_field.setStyle("-fx-text-fill: #7e8182ff;");
            birth_date_picker.setStyle("-fx-opacity: 1; -fx-text-fill: #7e8182ff;");
        }

        Button remove_author_button = new Button("✕");
        remove_author_button.setStyle(
            "-fx-background-color: #7b1d26; -fx-text-fill: white; -fx-background-radius: 50%; -fx-min-width: 25; -fx-min-height: 25;"
        );
        remove_author_button.setOnAction(ev -> authors_container.getChildren().remove(grid_author));

        int row = 0;
        grid_author.add(first_name_label, 0, row);
        grid_author.add(first_name_field, 1, row++);
        grid_author.add(last_name_label, 0, row);
        grid_author.add(last_name_field, 1, row++);
        grid_author.add(birth_date_label, 0, row);
        grid_author.add(birth_date_picker, 1, row++);
        grid_author.add(remove_author_button, 2, 0);
        grid_author.add(message_author, 0, row, 2, 1);

        authors_container.getChildren().add(grid_author);
    }

    // Method to add buttons "save", "cancel" and the message at the end
    public GridPane add_buttons(GridPane grid){
        if (grid == null){
            System.out.println("Grid is empty");
            return null;
        }
        // Buttons
        Button save_button = new Button("Save");
        UIStyling.button_styling(save_button, "#253721", "white");

        Button cancel_button = new Button("Cancel");
        UIStyling.button_styling(cancel_button, "#7b1d26", "white");

        this.buttons = new HBox(10, save_button, cancel_button);
        buttons.setAlignment(Pos.CENTER);

        this.message = new Label(); // For errors

        int last_row = getNextAvailableRow(grid);

        grid.add(this.buttons, 0, last_row++, 2, 1);
        grid.add(this.message, 0, last_row, 2, 1);

        return grid;
    }

    protected boolean validity_checks(Connection conn){ 
        this.document_title = this.title_field.getText().trim();
        this.document_nb_copies = this.nb_copies_spinner.getValue();
        this.document_genres = new HashSet<>();
        this.selected_genres_pane.getChildren().forEach(node -> document_genres.add(Genre.convertStringToGenre(((Label) node).getText().replace(" ✕", ""))));
        this.document_authors = new HashSet<>();

        // Remove all glows so that we can see only those that have currently a problem
        this.title_field.setEffect(null);
        this.nb_copies_spinner.setEffect(null);
        this.genre_combo.setEffect(null);
        this.message.setText("");

        for (Node node : this.authors_container.getChildren()) {
            if (node instanceof GridPane gp) {
                for (Node child : gp.getChildren()) {
                    if (child instanceof TextField || child instanceof DatePicker) {
                        child.setEffect(null); // remove any red_glow or other effect
                    } else if (child instanceof Label lb && lb.getText().startsWith("Please")) { // error's text
                        lb.setText("");
                    }
                }
            }
        }

        boolean error_is_present = false; // Will indicate if so far everything is ok
        for (Node node : this.authors_container.getChildren()) {
            if (node instanceof GridPane gp) {
                String first_name = null;
                String last_name = null;
                LocalDate birth_date = null;

                String error_message = "";
                for (Node child : gp.getChildren()) {
                    Integer col = GridPane.getColumnIndex(child);
                    Integer row2 = GridPane.getRowIndex(child);

                    // Find TextFields and DatePicker by their position in the grid
                    if (child instanceof TextField tf) {
                        if (row2 == 0 && col == 1) { // first_name field
                            first_name = tf.getText().trim();
                            if (first_name.equals("")) {
                                error_message += "Please set the correct author's first name\n";
                                tf.setEffect(UIStyling.red_glow);
                                error_is_present = true;
                            }
                        } else if (row2 == 1 && col == 1) { // last_name field
                            last_name = tf.getText().trim();
                            if (last_name.equals("")) {
                                error_message += "Please set the correct author's last name\n";
                                tf.setEffect(UIStyling.red_glow);
                                error_is_present = true;
                            }
                        }
                    } else if (child instanceof DatePicker dp && row2 == 2 && col == 1) { // birth_date field
                        birth_date = dp.getValue();
                        if (birth_date == null || birth_date.isAfter(LocalDate.now())) {
                            error_message += "Please enter a valid birthday\n";
                            dp.setEffect(UIStyling.red_glow);
                            error_is_present = true;
                        }
                    } else if (child instanceof Label lb && row2 == 3) { // It is at the end so the message will be complete
                        lb.setText(error_message);
                        error_message = ""; // And we reset it for next authors
                    }
                }

                // We are trying to find an author that exists in a database even if the user entered it himself, in case he didn't noticed
                Author author = null;

                if (!error_is_present) { // If there is no error we are searching for the author and add him if he does not exist yet
                    for(Author a: ApplicationFX.getAuthors()){
                        if (first_name.equals(a.getFirst_name()) && last_name.equals(a.getLast_name()) && birth_date.equals(a.getBirth_date())){
                            author = a;
                        }
                    }

                    if (author!= null && !document_authors.contains(author)){
                        document_authors.add(author);
                    } else {
                        Author new_author = new Author(first_name, last_name, birth_date);
                        LibraryManager.add_author(conn, new_author); // We are making the changes permanent by adding this author to database
                        ApplicationFX.addAuthor(new_author);
                        document_authors.add(new_author);
                    }
                }
            }
        }

        String main_error_message = "";
        // We are checking that everything is correct
        if (!error_is_present){ // If all authors were correctly added, we continue checks
            if (document_title.equals("")){
                error_is_present = true;
                this.title_field.setEffect(UIStyling.red_glow);
                main_error_message += "The document's title cannot be empty!\n";
            }

            if (document_genres.size() == 0){
                error_is_present = true;
                this.genre_combo.setEffect(UIStyling.red_glow);
                main_error_message += "You have to pick at least one genre for this document !\n";
            }

            if (document_authors.size() == 0){
                error_is_present = true;
                main_error_message += "You have to mention at least one author for this document !\n";
            }
        }
        this.message.setText(main_error_message);
        return !error_is_present;
    }
}
