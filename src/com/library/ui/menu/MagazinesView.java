package com.library.ui.menu;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.library.borrowingsystem.Borrowing;
import com.library.borrowingsystem.LibraryManager;
import com.library.documents.Document;
import com.library.documents.Magazine;
import com.library.documents.MagazineIssue;
import com.library.people.Member;
import com.library.ui.ApplicationFX;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class MagazinesView {

    private TextField searchField;
    private VBox magazinesVBox;
    private Label statusLabel; // Number of displayed magazines

    public ScrollPane start(Connection conn) {
        // --- Main container ---
        VBox root = new VBox(16);
        root.setAlignment(Pos.TOP_LEFT);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: white;");

        // --- Title ---
        Label title = new Label("📰 Magazines");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2d3436;");

        // --- Search field ---
        searchField = new TextField();
        searchField.setPromptText("Search magazines by title or periodicity...");
        searchField.setPrefWidth(400);
        searchField.setStyle("-fx-background-radius: 10; -fx-border-radius: 10;");
        searchField.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ENTER || e.getText() != null) refreshMagazinesList(conn);
        });

        // --- Magazines list ---
        magazinesVBox = new VBox(10);
        magazinesVBox.setAlignment(Pos.TOP_LEFT);

        ScrollPane listScroll = new ScrollPane(magazinesVBox);
        listScroll.setFitToWidth(true);
        listScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        listScroll.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        // --- Status label ---
        statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill:#666; -fx-font-size: 13px;");

        // --- Assemble main layout ---
        root.getChildren().addAll(title, searchField, listScroll, statusLabel);

        // --- Initial load ---
        refreshMagazinesList(conn);

        // --- Wrap in ScrollPane ---
        ScrollPane mainScroll = new ScrollPane(root);
        mainScroll.setFitToWidth(true);
        mainScroll.setFitToHeight(true);
        mainScroll.setStyle("-fx-background-color: white;");
        return mainScroll;
    }

    // Refresh magazines list based on search
    private void refreshMagazinesList(Connection conn) {
        magazinesVBox.getChildren().clear();
        String keyword = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        ArrayList<Magazine> magazines = ApplicationFX.getMagazines();

        int count = 0;
        if (magazines != null) {
            for (Magazine mag : magazines) {
                boolean matches = keyword.isEmpty()
                        || (mag.getMagazine_title() != null
                            && mag.getMagazine_title().toLowerCase().contains(keyword))
                        || (mag.getPeriodicity() != null
                            && mag.getPeriodicity().toString().toLowerCase().contains(keyword));

                if (matches) {
                    magazinesVBox.getChildren().add(createMagazineCard(conn, mag));
                    count++;
                }
            }
        }
        statusLabel.setText("Displaying " + count + " magazine(s).");
    }

    // Create a card for a magazine (with toggle button to show/hide issues)
    private GridPane createMagazineCard(Connection conn,Magazine mag) {
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

        Label title = new Label("Title: " + safe(mag.getMagazine_title()));
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label periodicity = new Label("Periodicity: " + safe(String.valueOf(mag.getPeriodicity())));
        periodicity.setStyle("-fx-font-size: 14px;");

        Button toggleBtn = new Button("Show issues");
        toggleBtn.setStyle("-fx-background-color:#129459ff; -fx-text-fill:white; -fx-font-weight:bold; -fx-background-radius:6;");

        // Container that will hold the issues of this magazine
        VBox issuesBox = new VBox(6);
        issuesBox.setPadding(new Insets(6, 0, 0, 12));
        issuesBox.setVisible(false);
        issuesBox.setManaged(false);

        toggleBtn.setOnAction(e -> handleToggleIssues(conn, mag, issuesBox, toggleBtn));

        // Layout
        card.add(title,      0, 0);
        card.add(periodicity,0, 1);
        card.add(toggleBtn,  1, 0, 1, 2); // button on the right side
        card.add(issuesBox,  0, 2, 2, 1); // issues below, spanning two columns

        return card;
    }

    // Handle show/hide issues for a magazine
    private void handleToggleIssues(Connection conn, Magazine mag, VBox issuesBox, Button toggleBtn){
            boolean show = !issuesBox.isVisible();

            if (show && issuesBox.getChildren().isEmpty()) {
                // Load issues the first time we open
                ArrayList<MagazineIssue> issues = findIssuesFor(mag);

                if (issues.isEmpty()) {
                    issuesBox.getChildren().add(new Label("No issues found for this magazine."));
                } else {
                    for (MagazineIssue issue : issues) {
                        GridPane issueCard = createIssueCard(conn, issue); 
                        issuesBox.getChildren().add(issueCard);
                    }
                }
            }
            
            issuesBox.setVisible(show);
            issuesBox.setManaged(show);
            toggleBtn.setText(show ? "Hide issues" : "Show issues");
    }

    // issue card with borrow button
    private GridPane createIssueCard(Connection conn, MagazineIssue issue) {
        GridPane issueCard = new GridPane();
        issueCard.setHgap(10);
        issueCard.setVgap(5);
        issueCard.setPadding(new Insets(8));
        issueCard.setStyle(
            "-fx-background-color: #f0f0f0;" +
            "-fx-border-color: #dcdde1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );

        Label issueLabel = new Label("• Title: " + safe(issue.getTitle())
                +" Issue #" + issue.getIssue_number()
                + " | Date: " + (issue.getIssue_date() != null
                    ? issue.getIssue_date().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    : "N/A")
                + " | Copies: " + issue.getNb_copies());
        issueLabel.setStyle("-fx-font-size: 14px;");

        Button borrowBtn = new Button("Borrow");
        borrowBtn.setStyle(
            "-fx-background-color: #129459ff; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-cursor: hand; " +
            "-fx-background-radius: 5;"
        );
        borrowBtn.setDisable(issue.getNb_copies() == 0); // Disable if no copies available
        borrowBtn.setOnAction(e -> handleBorrowAction(conn, issue));    

        issueCard.add(issueLabel, 0, 0);
        issueCard.add(borrowBtn, 1, 0);

        return issueCard;
    }
    
    // Handle borrow button action (placeholder)
    private void handleBorrowAction(Connection conn, MagazineIssue issue) {
        Member member = ApplicationFX.getConnected_member();
        
        if(member == null){
            alert("Please log in to borrow books.");
            return;
        }

        LocalDate start = LocalDate.now();

        Borrowing borrowing = new Borrowing(issue, member, start);
        Boolean ok = LibraryManager.create_borrowing(conn, borrowing); // we already refreshall in the method
        if(ok){
            alert("Borrowing created successfully.");
            refreshMagazinesList(conn);
        }else{
            alert("Failed to create borrowing.");  
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

    /** Find all MagazineIssue belonging to this magazine, from documents in memory. */
    private ArrayList<MagazineIssue> findIssuesFor(Magazine mag) {
        ArrayList<MagazineIssue> magazineIssues = new ArrayList<>();
        ArrayList<Document> docs = ApplicationFX.getDocuments();
        if (docs == null) return magazineIssues;

        for (Document d : docs) {
            if (d instanceof MagazineIssue magazineIssue) {

                if (magazineIssue.getMagazine() != null && (magazineIssue.getMagazine() == mag || 
                    safe(magazineIssue.getMagazine().getMagazine_title()).equalsIgnoreCase(safe(mag.getMagazine_title())))) 
                {
                    magazineIssues.add(magazineIssue);
                }
            }
        }
        return magazineIssues;
    }

    private String safe(String s) { return (s == null || s.isBlank()) ? "N/A" : s; }
}
