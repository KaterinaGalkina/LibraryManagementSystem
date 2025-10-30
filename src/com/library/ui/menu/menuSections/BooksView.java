package com.library.ui.menu.menuSections;

import java.util.ArrayList;
import java.util.Optional;
import java.sql.Connection;
import java.time.LocalDate;

import com.library.borrowingsystem.Borrowing;
import com.library.borrowingsystem.LibraryManager;
import com.library.documents.Book;
import com.library.documents.Document;
import com.library.people.Member;

import com.library.ui.ApplicationFX;
import com.library.ui.menu.libraryWorkerFeatures.documentsManagement.booksManagement.AddBookView;
import com.library.ui.menu.libraryWorkerFeatures.documentsManagement.booksManagement.EditBookView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class BooksView {
    private TextField searchField; 
    private VBox booksVBox;
    private Label statusLabel; // Number of displayed books

    public ScrollPane start(Connection conn) {
        // --- Main container ---
        VBox root = new VBox(16);
        root.setAlignment(Pos.TOP_LEFT);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: white;");

        // --- Title ---
        Label title = new Label("Books Section");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2d3436;");

        // --- Search field ---
        searchField = new TextField();
        searchField.setPromptText("Search books by title, author, ISBN, or genre...");
        searchField.setPrefWidth(400);
        searchField.setStyle("-fx-background-radius: 10; -fx-border-radius: 10;");

        // Search action    
        searchField.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ENTER || e.getText() != null)
                refreshBooksList(conn);
        });

        // --- Books list ---
        booksVBox = new VBox(10);
        booksVBox.setAlignment(Pos.TOP_LEFT);

        ScrollPane listScroll = new ScrollPane(booksVBox);
        listScroll.setFitToWidth(true);
        listScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        listScroll.setStyle("-fx-background-color: transparent;");

        // --- Status label --- (number of displayed books)
        statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill:#666; -fx-font-size: 13px;");

        // --- Assemble main layout ---
        if (ApplicationFX.getConnected_member() != null && ApplicationFX.getConnected_member().getIs_library_worker()){
            Button add_book_button = new Button("Add New Book");
            add_book_button.setStyle(
                "-fx-background-color: #2c4237; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-cursor: hand; " +
                "-fx-background-radius: 5;"
            );

            add_book_button.setOnAction(e -> {
                AddBookView addBookView = new AddBookView();
                addBookView.show_add_document_window(conn, Book.class);
                refreshBooksList(conn);
            });

            // create a spacer
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // put everything together
            HBox titleBox = new HBox(10, title, spacer, add_book_button);
            titleBox.setAlignment(Pos.CENTER_LEFT);
            titleBox.setPadding(new Insets(0, 0, 10, 0)); // some space below

            root.getChildren().addAll(titleBox, searchField, listScroll, statusLabel);
        } else {
            root.getChildren().addAll(title, searchField, listScroll, statusLabel);
        }

        // Initial load of books
        refreshBooksList(conn);

        // --- Wrap in ScrollPane for overall scrolling ---
        ScrollPane main = new ScrollPane(root);
        main.setFitToWidth(true);
        main.setFitToHeight(true);
        main.setStyle("-fx-background-color: white;");
        return main;
    }

    /* Refresh the list of displayed books based on the search field */
    private void refreshBooksList(Connection conn) {
        booksVBox.getChildren().clear();
        String keyword = (searchField.getText() == null) ? "" : searchField.getText().trim().toLowerCase();
        ArrayList<Document> docs = ApplicationFX.getDocuments();

        int count = 0;
        for (Document d : docs) {
            if (d instanceof Book b) {
                boolean match = keyword.isEmpty()
                    || (b.getTitle() != null && b.getTitle().toLowerCase().contains(keyword))
                    || (b.getIsbn() != null && b.getIsbn().toLowerCase().contains(keyword))
                    || (b.getAuthors() != null && b.getAuthors().stream().anyMatch(a ->
                        (a.getFirst_name() != null && a.getFirst_name().toLowerCase().contains(keyword)) ||
                        (a.getLast_name() != null && a.getLast_name().toLowerCase().contains(keyword))
                    ))
                    || (b.getGenre() != null && b.getGenre().stream().anyMatch(g ->
                        g.toString().toLowerCase().contains(keyword)
                    ));

                if (match) {
                    booksVBox.getChildren().add(makeBookCard(conn, b));
                    count++;
                }
            }
        }

        statusLabel.setText(count + " Displayed Book(s)");
    }


    /* Create a card UI for a single book */
    private GridPane makeBookCard(Connection conn, Book book) {
        GridPane card = new GridPane();
        card.setHgap(10);
        card.setVgap(5);
        card.setPadding(new Insets(10));
        card.setStyle(
            "-fx-background-color: #f9f9f9;" +
            "-fx-border-color: #dcdde1;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;"
        );

        Label title = new Label("📖 " + safe(book.getTitle()));
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label isbn = new Label("ISBN: " + safe(book.getIsbn()));
        Label year = new Label("Year: " + (book.getYear() == 0 ? "N/A" : book.getYear()));
        Label copies = new Label("Copies: " + book.getNb_copies());
        Label authors = new Label("Authors: " + (book.getAuthors().isEmpty() ? "N/A" : book.getAuthors().stream()
                .map(a -> safe(a.getFirst_name() + " " + a.getLast_name()))
                .reduce((a1, a2) -> a1 + ", " + a2).orElse("N/A")));
            
        Label genres = new Label("Genres: " + (book.getGenre().isEmpty() ? "N/A" : book.getGenre().stream()
                .map(g -> safe(g.toString()))
                .reduce((g1, g2) -> g1 + ", " + g2).orElse("N/A")));

        Button borrowButton = new Button("Borrow");
        borrowButton.setStyle(
            "-fx-background-color: #2c4237; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-cursor: hand; " +
            "-fx-background-radius: 5;"
        );
        borrowButton.setPrefWidth(100);
        borrowButton.setDisable(book.getNb_copies() == 0); // Disable if no copies available
        borrowButton.setOnAction(e -> handleBorrowAction(conn, book));

        card.add(title, 0, 0, 3, 1);
        card.add(isbn, 0, 1);
        card.add(year, 1, 1);
        card.add(copies, 2, 1);
        card.add(authors, 0, 2, 3, 1);
        card.add(genres, 0, 3, 3, 1);
        card.add(borrowButton, 0, 4);

        if (ApplicationFX.getConnected_member() != null && ApplicationFX.getConnected_member().getIs_library_worker()){
            Button edit_book_button = new Button("Edit");
            edit_book_button.setStyle(
                "-fx-background-color: #75619d; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-cursor: hand; " +
                "-fx-background-radius: 5;"
            );
            edit_book_button.setPrefWidth(100);

            Button delete_book_button = new Button("Delete");
            delete_book_button.setStyle(
                "-fx-background-color: #3a2d34; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-cursor: hand; " +
                "-fx-background-radius: 5;"
            );
            delete_book_button.setPrefWidth(100);

            edit_book_button.setOnAction(e -> {
                EditBookView editBookView = new EditBookView();
                editBookView.show_update_document_window(conn, book);
                refreshBooksList(conn);
            });
            delete_book_button.setOnAction(e -> handleDeleteAction(conn, book));

            card.add(edit_book_button, 1, 4);
            card.add(delete_book_button, 2, 4);
        }

        return card;
    }
    // Handle borrow button action (placeholder)
    private void handleBorrowAction(Connection conn, Book book) {
        Member member = ApplicationFX.getConnected_member();
        
        if(member == null){
            alert("Please log in to borrow books.");
            return;
        }

        LocalDate start = LocalDate.now();

        int activeBorrowings = (int) ApplicationFX.getBorrowings()
            .get(member)
            .stream()
            .filter(b -> b.getReal_return_date() == null)
            .count();

        if (activeBorrowings < 5){ // Only 5 borrowings at once
            Borrowing borrowing = new Borrowing(book, member, start);
            Boolean ok = LibraryManager.create_borrowing(conn, borrowing); // we already refreshall in the method
            if(ok){
                alert("Borrowing created successfully.");
                refreshBooksList(conn);
            }
        } else{
            alert("Failed to create borrowing.");  
        }
    }

    private void handleDeleteAction(Connection conn, Book book_to_delete){
        Alert confirmation = new Alert(AlertType.CONFIRMATION);
        confirmation.setHeaderText(null);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setContentText("Are you sure you want to delete the book: \"" + book_to_delete.getTitle() + "\" ?");

        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User confirmed deletion
            LibraryManager.remove_document(conn, book_to_delete);
            ApplicationFX.refreshAll();
            refreshBooksList(conn);
        }
    }

    // Utility to handle null or blank strings
    private String safe(String s) {
        return (s == null || s.isBlank()) ? "N/A" : s;
    }

    // Alert utility
    private void alert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
