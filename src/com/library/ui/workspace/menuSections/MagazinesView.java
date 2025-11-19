package com.library.ui.workspace.menuSections;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import com.library.borrowingsystem.Borrowing;
import com.library.borrowingsystem.LibraryManager;
import com.library.documents.Document;
import com.library.documents.Magazine;
import com.library.documents.MagazineIssue;
import com.library.people.Member;
import com.library.ui.ApplicationFX;
import com.library.ui.generalStyling.UIStyling;
import com.library.ui.workspace.libraryWorkerFeatures.documentsManagement.magazineIssuesManagement.AddMagazineIssueView;
import com.library.ui.workspace.libraryWorkerFeatures.documentsManagement.magazineIssuesManagement.EditMagazineIssueView;
import com.library.ui.workspace.libraryWorkerFeatures.magazineManagement.AddMagazineView;
import com.library.ui.workspace.libraryWorkerFeatures.magazineManagement.EditMagazineView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class MagazinesView {

    private TextField searchField;
    private ComboBox<String> filterBox;
    private VBox magazinesVBox;
    private Label statusLabel; // Number of displayed magazines

    public ScrollPane start(Connection conn) {
        VBox root = new VBox(16);
        root.setAlignment(Pos.TOP_LEFT);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: white;");

        Label title = new Label("Magazines");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2d3436;");
        
        filterBox = new ComboBox<>();
        filterBox.getItems().addAll("Genre","Author","Magazine Title","Issue Title","Periodicity", "All");
        filterBox.setValue("All");
        filterBox.setOnAction(e -> refreshMagazinesList(conn));

        searchField = new TextField();
        searchField.setPromptText("Search magazines by title or periodicity...");
        searchField.setPrefWidth(400);
        searchField.setStyle("-fx-background-radius: 10; -fx-border-radius: 10;");
        searchField.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ENTER || e.getText() != null) refreshMagazinesList(conn);
        });
        
        HBox topBar = new HBox(20, searchField, filterBox);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(20));

        // Magazines list
        magazinesVBox = new VBox(10);
        magazinesVBox.setAlignment(Pos.TOP_LEFT);

        ScrollPane listScroll = new ScrollPane(magazinesVBox);
        listScroll.setFitToWidth(true);
        listScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        listScroll.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        // Status label
        statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill:#666; -fx-font-size: 13px;");

        if (ApplicationFX.getConnected_member() != null && ApplicationFX.getConnected_member().getIs_library_worker()){
            Button add_magazine_button = new Button("Add New Magazine");
            UIStyling.button_styling(add_magazine_button, "#212c1b", "white");

            add_magazine_button.setOnAction(e -> {
                AddMagazineView addMagazineView = new AddMagazineView();
                addMagazineView.showAddMagazineWindow(conn);
                refreshMagazinesList(conn);
            });

            Button add_magazine_issue_button = new Button("Add New Issue");
            UIStyling.button_styling(add_magazine_issue_button, "#17506a", "white");

            add_magazine_issue_button.setOnAction(e -> {
                AddMagazineIssueView addMagazineIssueView = new AddMagazineIssueView();
                addMagazineIssueView.show_add_document_window(conn, MagazineIssue.class);
                refreshMagazinesList(conn);
            });

            // create a spacer
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // put everything together
            HBox titleBox = new HBox(10, title, spacer, add_magazine_button, add_magazine_issue_button);
            titleBox.setAlignment(Pos.CENTER_LEFT);
            titleBox.setPadding(new Insets(0, 0, 10, 0)); // some space below

            root.getChildren().addAll(titleBox, topBar, listScroll, statusLabel);
        } else {
            root.getChildren().addAll(title, topBar, listScroll, statusLabel);
        }

        // Initial load
        refreshMagazinesList(conn);

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
        String filter = filterBox.getValue();

        int count = 0;
        if (magazines != null) {
            for (Magazine mag : magazines) {
                ArrayList<MagazineIssue> issues = findIssuesFor(mag);
                if(filter != null && !filter.equals("All")) {
                    boolean matchesFilter = false;
                    switch (filter) {
                        case "Genre":
                            matchesFilter =(issues != null && issues.stream().anyMatch(issue -> 
                                issue.getGenre() != null &&
                                issue.getGenre().stream().anyMatch(g ->
                                    g.toString().toLowerCase().contains(keyword)
                                )
                            ));
                            break;
                        case "Author":
                            matchesFilter = issues != null && issues.stream().anyMatch(issue ->
                                issue.getAuthors() != null &&
                                issue.getAuthors().stream().anyMatch(a -> a.getFirst_name().toLowerCase().contains(keyword) || a.getLast_name().toLowerCase().contains(keyword))
                            );
                            
                            break;
                        case "Issue Title":
                            matchesFilter = issues != null && issues.stream().anyMatch(issue ->
                                issue.getTitle() != null &&
                                issue.getTitle().toLowerCase().contains(keyword)
                            );
                            break;
                        case "Magazine Title":
                            matchesFilter = mag.getMagazine_title() != null && mag.getMagazine_title().toLowerCase().contains(keyword);
                            break;
                        case "Periodicity":
                            matchesFilter = mag.getPeriodicity() != null && mag.getPeriodicity().toString().toLowerCase().contains(keyword);
                            break;      
                        default:
                            matchesFilter = true; // should not reach here
                    }   
                    if (!matchesFilter) continue; // Skip if it doesn't match the filter
                }

                boolean matches = keyword.isEmpty()
                    || (mag.getMagazine_title() != null && mag.getMagazine_title().toLowerCase().contains(keyword))
                    || (mag.getPeriodicity() != null && mag.getPeriodicity().toString().toLowerCase().contains(keyword))
                    || (issues != null && issues.stream().anyMatch(issue -> 
                        (issue.getTitle() != null && issue.getTitle().toLowerCase().contains(keyword)) ||
                        (issue.getAuthors() != null && issue.getAuthors().stream().anyMatch(a -> 
                            a.getFirst_name().toLowerCase().contains(keyword) || a.getLast_name().toLowerCase().contains(keyword)
                        )) ||
                        (issue.getGenre() != null && issue.getGenre().stream().anyMatch(g -> 
                            g.toString().toLowerCase().contains(keyword)
                        ))
                    ));

                if (matches) {
                    magazinesVBox.getChildren().add(createMagazineCard(conn, mag));
                    count++;
                }
            }
        }
        statusLabel.setText("Displaying " + count + " magazine(s).");
    }

    // Create a card for a magazine (with toggle button to show/hide issues)
    private GridPane createMagazineCard(Connection conn, Magazine mag) {
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
        UIStyling.button_styling(toggleBtn, "#2c4237", "white");
        
        HBox buttonBox = new HBox(8); // space between buttons
        buttonBox.getChildren().add(toggleBtn);

        if (ApplicationFX.getConnected_member() != null &&
            ApplicationFX.getConnected_member().getIs_library_worker()) {

            Button edit_magazine_button = new Button("Edit");
            edit_magazine_button.setPrefWidth(80);
            UIStyling.button_styling(edit_magazine_button, "#75619d", "white");

            Button delete_magazine_button = new Button("Delete");
            delete_magazine_button.setPrefWidth(80);
            UIStyling.button_styling(delete_magazine_button, "#3a2d34", "white");

            edit_magazine_button.setOnAction(e -> {
                EditMagazineView editMagazineView = new EditMagazineView();
                editMagazineView.showUpdateMagazineWindow(conn, mag);
                refreshMagazinesList(conn);
                ApplicationFX.refreshAll();
            });

            delete_magazine_button.setOnAction(e -> {
                handle_delete_magazine_action(conn, mag);
            });

            buttonBox.getChildren().addAll(edit_magazine_button, delete_magazine_button);
        }

        // Container that will hold the issues of this magazine
        VBox issuesBox = new VBox(6);
        issuesBox.setPadding(new Insets(6, 0, 0, 12));
        issuesBox.setVisible(false);
        issuesBox.setManaged(false);

        toggleBtn.setOnAction(e -> handleToggleIssues(conn, mag, issuesBox, toggleBtn));

        card.add(title, 0, 0);
        card.add(periodicity, 0, 1);
        card.add(buttonBox, 1, 0, 1, 2); // all buttons in one place
        card.add(issuesBox, 0, 2, 2, 1); // issues below, spanning two columns

        return card;
    }

    private void handle_delete_magazine_action(Connection conn, Magazine magazine_to_delete){
        Alert confirmation = new Alert(AlertType.CONFIRMATION);
        confirmation.setHeaderText(null);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setContentText("Are you sure you want to delete the magazine: \"" + magazine_to_delete.getMagazine_title() + "\" ?\n" + 
            "Important: All issues of this magazine will be automatically deleted !");

        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User confirmed deletion
            LibraryManager.remove_magazine(conn, magazine_to_delete);
            // We have to refresh all because by removing one magazine, we may remove some magazine issues (of this deleted magazine) -> Documents list to update
            // but also some people may have borriwed these documents -> Borrowings list to update
            // And some authors can have these magazine issues in their written documents -> Authors to update
            ApplicationFX.refreshAll();
            refreshMagazinesList(conn);
        }
    }

    private void handle_delete_magazine_issue_action(Connection conn, MagazineIssue magazine_issue_to_delete){
        Alert confirmation = new Alert(AlertType.CONFIRMATION);
        confirmation.setHeaderText(null);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setContentText("Are you sure you want to delete the issue: \"" + magazine_issue_to_delete.getTitle() + "\" \nof the magazine \"" + magazine_issue_to_delete.getMagazine().getMagazine_title() + "\"?");

        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User confirmed deletion
            LibraryManager.remove_document(conn, magazine_issue_to_delete);
            // In this case, as when we are removing a magazine, we have to update everything
            ApplicationFX.refreshAll();
            refreshMagazinesList(conn);
        }
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
        issueLabel.setWrapText(true);

        Button borrowBtn = new Button("Borrow");
        UIStyling.button_styling(borrowBtn, "#2c4237", "white");
        borrowBtn.setPrefWidth(100);

        borrowBtn.setDisable(issue.getNb_copies() == 0); // Disable if no copies available
        borrowBtn.setOnAction(e -> handleBorrowAction(conn, issue));    

        issueCard.add(issueLabel, 0, 0, 3, 1);
        issueCard.add(borrowBtn, 0, 1);

        if (ApplicationFX.getConnected_member() != null && ApplicationFX.getConnected_member().getIs_library_worker()){
            Button edit_button = new Button("Edit");
            UIStyling.button_styling(edit_button, "#75619d", "white");
            
            edit_button.setPrefWidth(100);

            Button delete_button = new Button("Delete");
            UIStyling.button_styling(delete_button, "#3a2d34", "white");
            delete_button.setPrefWidth(100);

            edit_button.setOnAction(e -> {
                EditMagazineIssueView editMagazineIssueView = new EditMagazineIssueView();
                editMagazineIssueView.show_update_document_window(conn, issue);
                refreshMagazinesList(conn);
                ApplicationFX.refreshAll();
            });

            delete_button.setOnAction(e -> {
                handle_delete_magazine_issue_action(conn, issue);
            });

            issueCard.add(edit_button, 1, 1);
            issueCard.add(delete_button, 2, 1);
        }
        return issueCard;
    }
    
    // Handle borrow button action (placeholder)
    private void handleBorrowAction(Connection conn, MagazineIssue issue) {
        Member member = ApplicationFX.getConnected_member();
        
        if(member == null){
            UIStyling.alert("Please log in to borrow magazine issues !");
            return;
        }

        if (member.getPenalty_status()) {
            UIStyling.alert("Your account is suspended. Please contact a librarian.");
            return;
        }

        LocalDate start = LocalDate.now();

        if (!ApplicationFX.getBorrowings().containsKey(member)){
            ApplicationFX.getBorrowings().put(member, new HashSet<>());
        }

        int activeBorrowings = (int) ApplicationFX.getBorrowings()
            .get(member)
            .stream()
            .filter(b -> b.getReal_return_date() == null)
            .count();

        if (activeBorrowings < 5){ // Only 5 borrowings at once
            Borrowing borrowing = new Borrowing(issue, member, start);
            Boolean ok = LibraryManager.create_borrowing(conn, borrowing); 
            ApplicationFX.refreshAll();
            if(ok){
                UIStyling.alert("Borrowing created successfull!");
                refreshMagazinesList(conn);
            }
        } else{
            UIStyling.alert("You can only borrow 5 items at once!");  
        }
    }

    // Find all MagazineIssue belonging to this magazine, from documents in memory
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
