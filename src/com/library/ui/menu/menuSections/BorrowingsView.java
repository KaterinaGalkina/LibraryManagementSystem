package com.library.ui.menu.menuSections;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;

import com.library.borrowingsystem.Borrowing;
import com.library.borrowingsystem.LibraryManager;
import com.library.people.Member;
import com.library.ui.ApplicationFX;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BorrowingsView {

    private Label statusLabel;
    private VBox borrowingsBox;

    public ScrollPane start(Connection conn) {

        // --- Main container ---
        VBox root = new VBox(10);
        root.setAlignment(Pos.TOP_LEFT);
        root.setPadding(new Insets(20));

        // --- Title ---
        Label title = new Label("My Borrowings");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // --- Filter ComboBox ---
        ComboBox<String> filterBox = new ComboBox<>();
        filterBox.getItems().addAll("All", "Active", "Returned", "Late");
        filterBox.setValue("All");
        filterBox.setOnAction(e -> refreshBorrowingsList(conn, filterBox.getValue()));

        HBox topBar = new HBox(20, title, filterBox);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(20));

        // --- Borrowings list ---
        borrowingsBox = new VBox(8);
        borrowingsBox.setAlignment(Pos.TOP_LEFT);

        //--- Scroll pane for borrowings list ---
        ScrollPane scrollPane = new ScrollPane(borrowingsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // --- Status label ---
        statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill:#666; -fx-font-size: 13px;");

        // --- Assemble root ---
        root.getChildren().addAll(topBar, scrollPane, statusLabel);

        // Initial refresh
        refreshBorrowingsList(conn, "All");

        // --- Main scroll pane ---
        ScrollPane main = new ScrollPane(root);
        main.setFitToWidth(true);
        main.setFitToHeight(true);
        main.setStyle("-fx-background-color: transparent;");
        
        return main;
    }
    // Refresh borrowings list
    private void refreshBorrowingsList(Connection conn, String filter) {
        borrowingsBox.getChildren().clear();

        HashMap<Member, HashSet<Borrowing>> borrowings = ApplicationFX.getBorrowings();
        final LocalDate today = LocalDate.now();

        if (borrowings == null || borrowings.isEmpty()) {
            statusLabel.setText("Total borrowings: 0");
            return;
        }

        Member member = ApplicationFX.getConnected_member();
        HashSet<Borrowing> memberBorrowings = borrowings.get(member);

        if (memberBorrowings == null || memberBorrowings.isEmpty()) {
            statusLabel.setText("Total borrowings: 0");
            return;
        }

        int total = 0;

        for (Borrowing borrowing : memberBorrowings) {
            total++;

            // --- filtre par statut ---
            boolean isReturned = (borrowing.getReal_return_date() != null);
            boolean isLate = !isReturned
                    && borrowing.getReturn_date() != null
                    && borrowing.getReturn_date().isBefore(today);
            boolean isActive = !isReturned && !isLate;

            boolean matches =
                "All".equals(filter)
                || ("Active".equals(filter)   && isActive)
                || ("Returned".equals(filter) && isReturned)
                || ("Late".equals(filter)     && isLate);

            if (!matches) continue;
            
            GridPane itemBox = createBorrowingCard(conn, borrowing);
            borrowingsBox.getChildren().add(itemBox);
        }

        statusLabel.setText("Total borrowings: " + total);
    }
    // create borrowing item UI
    private GridPane createBorrowingCard(Connection conn, Borrowing borrowing) {
        // Grid
        GridPane card = new GridPane();
        card.setHgap(10);
        card.setVgap(6);
        card.setPadding(new Insets(10));
        card.setStyle(
            "-fx-background-color: #f9f9f9;" +
            "-fx-border-color: #dcdde1;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;"
        );


        String startTxt = (borrowing.getBorrowing_date() == null) ? "N/A" : borrowing.getBorrowing_date().toString();
        String expTxt = (borrowing.getReturn_date() == null) ? "N/A" : borrowing.getReturn_date().toString();
        String realTxt = (borrowing.getReal_return_date() == null) ? null : borrowing.getReal_return_date().toString();
        // title
        Label borrowingTitle = new Label("• " + safe(borrowing.getDocument().getTitle()));
        borrowingTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // start date
        Label startDateLabel = new Label("  - Borrowed on: " + startTxt);
        startDateLabel.setStyle("-fx-font-size: 13px;");

        // expected return date
        Label expDateLabel = new Label("  - Expected return date: " + expTxt);
        expDateLabel.setStyle("-fx-font-size: 13px;");

        // real return date
        Label realDateLabel = new Label("  - Actual return date: " + (realTxt == null ? "Not returned yet" : realTxt));
        realDateLabel.setStyle("-fx-font-size: 13px;");

        card.add(borrowingTitle, 0, 0);
        card.add(startDateLabel, 0, 1);
        card.add(expDateLabel, 0, 2);
        card.add(realDateLabel, 0, 3);

        // Return button if not yet returned

        if (borrowing.getReal_return_date() == null) {
            Button returnBtn = new Button("Return");
            returnBtn.setStyle(
                "-fx-background-color: #d63031; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-cursor: hand; " +
                "-fx-background-radius: 5;"
            );
            returnBtn.setOnAction(e -> handleReturnAction(conn, borrowing));
            card.add(returnBtn, 0, 4);
        }

        return card ;
    }
    // Handle return action
    private void handleReturnAction(Connection conn, Borrowing borrowing) {
        borrowing.setReal_return_date(LocalDate.now());
        boolean ok = LibraryManager.finish_borrowing(conn, borrowing);
        if (ok) {
            alert("✅ Successfully returned.");
            refreshBorrowingsList(conn, "All");
        } else {
            alert("⚠️ Failed to return. Please try again.");
        }
    }

    // Alert utility
    private void alert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Utility to handle null or blank strings
    private String safe(String s) {
        return (s == null || s.isBlank()) ? "N/A" : s;
    }
}
