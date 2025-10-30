package com.library.ui.menu.libraryWorkerFeatures.documentsManagement.booksManagement;

import java.sql.Connection;
import java.time.LocalDate;
import com.library.documents.Book;
import com.library.documents.Document;
import com.library.ui.ApplicationFX;
import com.library.ui.menu.libraryWorkerFeatures.documentsManagement.OneDocumentView;

import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class OneBookView extends OneDocumentView{
    // Form's fields and lables
    protected TextField isbn_field;
    protected Spinner<Integer> nb_pages_spinner;
    protected Spinner<Integer> year_spinner;
    protected HBox buttons;

    // Final values obtained
    protected String book_isbn;
    protected int book_nb_pages;
    protected int book_year;

    public GridPane get_one_book_view(String main_title_text, Book current_book){
        GridPane grid = get_one_document_view(main_title_text, current_book);

        Label isbn_label = new Label("ISBN:");
        this.isbn_field = new TextField();

        Label nb_pages_label = new Label("Number of Pages:");
        this.nb_pages_spinner = new Spinner<>(1, 10000, 200);
        nb_pages_spinner.setEditable(true);

        Label year_label = new Label("Year:");
        this.year_spinner = new Spinner<>(1500, LocalDate.now().getYear(), LocalDate.now().getYear());
        year_spinner.setEditable(true);

        // --- Setting the values of the current book, if we are modifying ---
        if (current_book != null){
            this.isbn_field.setText(current_book.getIsbn());
            this.nb_pages_spinner.getValueFactory().setValue(current_book.getNb_pages());
            this.year_spinner.getValueFactory().setValue(current_book.getYear());
        }  

        int last_row = getNextAvailableRow(grid);
        
        grid.add(isbn_label, 0, last_row);
        grid.add(this.isbn_field, 1, last_row++);
        grid.add(nb_pages_label, 0, last_row);
        grid.add(this.nb_pages_spinner, 1, last_row++);
        grid.add(year_label, 0, last_row);
        grid.add(this.year_spinner, 1, last_row);

        this.add_buttons(grid);

        return grid;
    }

    protected boolean validity_checks(Connection conn, DropShadow red_glow, boolean adding_book, Book book_to_change){ // adding_book = true if we are adding a book, in this case the isbn should be unique
        this.book_isbn = this.isbn_field.getText().trim();
        this.book_nb_pages = this.nb_pages_spinner.getValue();
        this.book_year = this.year_spinner.getValue();

        this.isbn_field.setEffect(null);
        this.nb_pages_spinner.setEffect(null);
        this.year_spinner.setEffect(null);

        boolean error_is_present = false; // Will indicate if so far everything is ok
        String main_error_message = "";

        error_is_present = !super.validity_checks(conn, red_glow) || error_is_present;
        
        if (book_isbn.length() != 13 || !book_isbn.matches("\\d*")) {
            error_is_present = true;
            this.isbn_field.setEffect(red_glow);
            main_error_message += "The book's isbn is not in correct format! (The correct format is 13 digits)\n";
        } else if (adding_book || (book_to_change != null && this.book_isbn != book_to_change.getIsbn())) { // We need to verify that the isbn is unique
            for( Document doc: ApplicationFX.getDocuments()){
                if (doc instanceof Book book){
                    if (book.getIsbn().equals(book_isbn)){
                        error_is_present = true;
                        this.isbn_field.setEffect(red_glow);
                        main_error_message += "The book's isbn should be unique, there exist another book with this isbn\n";
                        break;
                    }
                }
            }
        }

        this.message.setText(this.message.getText() + "\n" + main_error_message);
        return !error_is_present;
    }
}
