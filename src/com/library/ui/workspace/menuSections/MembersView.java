package com.library.ui.workspace.menuSections;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Optional;
import com.library.ui.ApplicationFX;
import com.library.ui.generalStyling.UIStyling;
import com.library.ui.workspace.libraryWorkerFeatures.membersManagement.*;
import com.library.people.Member;
import com.library.borrowingsystem.LibraryManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class MembersView {
    private TextField searchField;
    private VBox membersVBox;
    private Label statusLabel; // Number of displayed members

    public ScrollPane start(Connection conn) { 
        VBox root = new VBox(16);
        root.setAlignment(Pos.TOP_LEFT);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: white;");

        // Title
        Label titleLabel = new Label("Members");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        root.getChildren().add(titleLabel);

        // Search Field 
        searchField = new TextField();
        searchField.setPromptText("Search members by name");
        searchField.setMaxWidth(400);
        searchField.setStyle("-fx-background-radius: 10; -fx-border-radius: 10;");
        searchField.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ENTER || e.getText() != null) refreshMembersList(conn);
        });

        // Members List Container
        membersVBox = new VBox(10);
        membersVBox.setAlignment(Pos.TOP_LEFT);

        ScrollPane listScroll = new ScrollPane(membersVBox);
        listScroll.setFitToWidth(true);
        listScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        listScroll.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        // Status label
        statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill:#666; -fx-font-size: 13px;");

        // Add Member Button
        Button addMemberButton = new Button("Add New Member");
        UIStyling.button_styling(addMemberButton, "#212c1b", "white");
        addMemberButton.setOnAction(e -> handleAddMember(conn));

        // create a spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // put everything together
        HBox titleBox = new HBox(10, titleLabel, spacer, addMemberButton);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        titleBox.setPadding(new Insets(0, 0, 10, 0)); // some space below

        // Main layout
        root.getChildren().addAll(titleBox, searchField, listScroll, statusLabel);

        // Initial load of members
        refreshMembersList(conn);

        ScrollPane mainScroll = new ScrollPane(root);
        mainScroll.setFitToWidth(true);
        mainScroll.setFitToHeight(true);
        mainScroll.setStyle("-fx-background-color: white;");
        return mainScroll;

    }

    private void handleAddMember(Connection conn) {
        AddMemberView addMemberView = new AddMemberView();
        addMemberView.showAddMemberWindow(conn);
        refreshMembersList(conn);
    }

    // Refresh the members list based on the search field
    private void refreshMembersList(Connection conn) {
        membersVBox.getChildren().clear();
        String keyword = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        ArrayList<Member> members = ApplicationFX.getMembers();

        int displayedCount = 0;
        if(members != null){
            for (Member member : members) {
                if(member.getId() == ApplicationFX.getConnected_member().getId()){
                    continue; // skip current user
                }
                boolean matches = keyword.isEmpty() || (
                    (member.getFirst_name() != null && member.getFirst_name().toLowerCase().contains(keyword)) || 
                    (member.getLast_name() != null && member.getLast_name().toLowerCase().contains(keyword)));
                if (matches) {
                    membersVBox.getChildren().add(createMemberCard(conn, member));
                    displayedCount++;
                }
            }
        }
        statusLabel.setText("Displayed members: " + displayedCount);
    }

    // Member card UI component
    private GridPane createMemberCard(Connection conn, Member member) {
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

        Label first_name = new Label("First Name: " + member.getFirst_name());
        first_name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label last_name = new Label("Last Name: " + member.getLast_name());
        last_name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button toggleButton = new Button("Show More");
        UIStyling.button_styling(toggleButton, "#3498db", "white");

        // Container that will hold the issues of this magazine
        VBox detailsBox = new VBox(6);
        detailsBox.setPadding(new Insets(6, 0, 0, 12));
        detailsBox.setVisible(false);
        detailsBox.setManaged(false);

        toggleButton.setOnAction(e -> handleShowMore(conn, member, detailsBox, toggleButton));

        // delete button
        Button deleteButton = new Button("Delete");
        deleteButton.setPrefWidth(80);
        UIStyling.button_styling(deleteButton, "#3a2d34", "white");

        deleteButton.setOnAction(e -> handleDeleteMember(conn, member));

        // edit button
        Button editButton = new Button("Edit");
        editButton.setPrefWidth(80);
        UIStyling.button_styling(editButton, "#75619d", "white");
        editButton.setOnAction(e -> handleEditMember(conn, member));
       
        HBox buttonBox = new HBox(8); // space between buttons
        buttonBox.getChildren().addAll(toggleButton, editButton, deleteButton);

        // button add privileges
        String priviledges = "Add Privileges";
        if (member.getIs_library_worker()){
            priviledges = "Remove Privileges";
        }
        Button privilegesButton = new Button(priviledges);
        UIStyling.button_styling(privilegesButton, "purple", "white");
        if (member.getIs_library_worker()){
            privilegesButton.setOnAction(e -> handle_privileges(conn, member, false));
        } else {
            privilegesButton.setOnAction(e -> handle_privileges(conn, member, true));
        }

        card.add(first_name, 0, 0);
        card.add(last_name, 1, 0);
        card.add(buttonBox, 2, 0);
        card.add(detailsBox, 0, 1, 3, 1); 

        return card;
    }

    private void handleShowMore(Connection conn, Member member, VBox detailsBox, Button toggleButton) {
        boolean show = ! detailsBox.isVisible();

        if(show && detailsBox.getChildren().isEmpty()) {
    
            detailsBox.getChildren().addAll(
                new Label("Address: " + safe(member.getAddress())),
                new Label("Email: " + safe(member.getMail())),
                new Label("Phone: " + safe(member.getPhone_number())),
                new Label("Penalty Status: " + (member.getPenalty_status() ? "Yes" : "No")),
                new Label("Library Worker: " + (member.getIs_library_worker() ? "Yes" : "No"))
            );
        }

        detailsBox.setVisible(show);
        detailsBox.setManaged(show);
        toggleButton.setText(show ? "Show Less" : "Show More");

    }

    private void handleDeleteMember(Connection conn, Member member) {
        Alert confirmation = new Alert(AlertType.CONFIRMATION);
        confirmation.setHeaderText(null);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setContentText("Are you sure you want to delete the member: \"" + member.getFirst_name() + " " + member.getLast_name() + "\"?");

        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User confirmed deletion
            boolean success = LibraryManager.remove_member(conn, member);
            ApplicationFX.refreshAll();
            if (success) {
                UIStyling.alert("Member deleted successfully.");
                refreshMembersList(conn);
            } else {
                UIStyling.alert("Failed to delete the member. Please try again.");
            }
        }
    }

    private void handleEditMember(Connection conn, Member member) {
        EditMemberView editMemberView = new EditMemberView();
        editMemberView.showUpdateMemberWindow(conn, member);
        refreshMembersList(conn);
    }

    private void handle_privileges(Connection conn, Member member, boolean add){ // if add = true then we are adding priviledges, otherwise we are removing it
        member.setIs_library_worker(add);

        boolean success = LibraryManager.set_privileges_to_member(conn, member);
        if (success) {
            UIStyling.alert("Library worker privileges changed successfully.");
            refreshMembersList(conn);
        } else {
            UIStyling.alert("Failed to update the member's privileges. Please try again.");
        }
    }

    private String safe(String s) { return (s == null || s.isBlank()) ? "N/A" : s; }
}
