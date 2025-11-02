package com.library.ui.menu.menuSections;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Optional;

import com.library.ui.ApplicationFX;
import com.library.ui.menu.libraryWorkerFeatures.membersManagement.*;

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
        // --- Main container ---
        VBox root = new VBox(16);
        root.setAlignment(Pos.TOP_LEFT);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: white;");

        // --- Title ---
        Label titleLabel = new Label("Members");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        root.getChildren().add(titleLabel);

        // --- Search Field ---
        searchField = new TextField();
        searchField.setPromptText("Search members by name");
        searchField.setMaxWidth(400);
        searchField.setStyle("-fx-background-radius: 10; -fx-border-radius: 10;");
        searchField.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ENTER || e.getText() != null) refreshMembersList(conn);
        });

        // --- Members List Container ---
        membersVBox = new VBox(10);
        membersVBox.setAlignment(Pos.TOP_LEFT);

        ScrollPane listScroll = new ScrollPane(membersVBox);
        listScroll.setFitToWidth(true);
        listScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        listScroll.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        // --- Status label ---
        statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill:#666; -fx-font-size: 13px;");

        // --- Add Member Button ---
        Button addMemberButton = new Button("Add New Member");
        addMemberButton.setStyle(
            "-fx-background-color: #212c1b; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-cursor: hand; " +
            "-fx-background-radius: 5;"
        );
        addMemberButton.setOnAction(e -> handleAddMember(conn));

        // create a spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // put everything together
        HBox titleBox = new HBox(10, titleLabel, spacer, addMemberButton);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        titleBox.setPadding(new Insets(0, 0, 10, 0)); // some space below

        // --- Assemble main layout ---
        root.getChildren().addAll(titleBox, searchField, listScroll, statusLabel);

        // Initial load of members
        refreshMembersList(conn);

        // --- Wrap in ScrollPane ---
        ScrollPane mainScroll = new ScrollPane(root);
        mainScroll.setFitToWidth(true);
        mainScroll.setFitToHeight(true);
        mainScroll.setStyle("-fx-background-color: white;");
        return mainScroll;

    }
    private void handleAddMember(Connection conn) {
        AddMemberView addMemberView = new AddMemberView();
        addMemberView.showAddMemberWindow(conn);
        if(addMemberView.getMember_is_added()){
            refreshMembersList(conn);
            addMemberView.setMember_is_added(false);
        }
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
                boolean matches = keyword.isEmpty()
                        || ( member.getFirst_name() != null )
                           && member.getFirst_name().toLowerCase().contains(keyword)
                        || ( member.getLast_name() != null )
                            && member.getLast_name().toLowerCase().contains(keyword);
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
        toggleButton.setStyle(
            "-fx-background-color: #3498db;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 5;"
        );

        // Container that will hold the issues of this magazine
        VBox detailsBox = new VBox(6);
        detailsBox.setPadding(new Insets(6, 0, 0, 12));
        detailsBox.setVisible(false);
        detailsBox.setManaged(false);

        toggleButton.setOnAction(e -> handleShowMore(conn, member, detailsBox, toggleButton));

        // delete button
        Button deleteButton = new Button("Delete");
        deleteButton.setPrefWidth(80);
        deleteButton.setStyle(
            "-fx-background-color:#3a2d34; " +
            "-fx-text-fill:white; " +
            "-fx-font-weight:bold; " +
            "-fx-background-radius:6;"
        );

        deleteButton.setOnAction(e -> handleDeleteMember(conn, member));

        // edit button
        Button editButton = new Button("Edit");
        editButton.setPrefWidth(80);
        editButton.setStyle(
            "-fx-background-color:#75619d; " +
            "-fx-text-fill:white; " +
            "-fx-font-weight:bold; " +
            "-fx-background-radius:6;"
        );
        editButton.setOnAction(e -> handleEditMember(conn, member));
       
        HBox buttonBox = new HBox(8); // space between buttons
        buttonBox.getChildren().addAll(toggleButton, editButton, deleteButton);
        if(!member.getIs_library_worker()){ 
            // button add privileges
            Button addPrivilegesButton = new Button("Add Privileges");
            addPrivilegesButton.setStyle(
                "-fx-background-color: purple; " +
                "-fx-text-fill:white; " +
                "-fx-font-weight:bold; " +
                "-fx-background-radius:6;"
            );
            addPrivilegesButton.setOnAction(e -> handleAddPrivileges(conn, member));
            // suspend/unsuspend button
            Button suspendButton = new Button(member.getIs_suspended() ? "Unsuspend" : "Suspend");
            suspendButton.setStyle(
                "-fx-background-color: blue; " +
                "-fx-text-fill:white; " +
                "-fx-font-weight:bold; " +
                "-fx-background-radius:6;"
            );
            suspendButton.setOnAction(e -> handleSuspendMember(conn, member));

            buttonBox.getChildren().addAll(addPrivilegesButton, suspendButton);
        }

        // layout
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
        confirmation.setContentText("Are you sure you want to delete the issue: \"" + member.getFirst_name() + " " + member.getLast_name() + "\"?");

        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User confirmed deletion
            boolean success = LibraryManager.remove_member(conn, member);
            if (success) {
                alert("Member deleted successfully.");
                refreshMembersList(conn);
            } else {
                alert("Failed to delete the member. Please try again.");
            }
        }
    }

    private void handleEditMember(Connection conn, Member member) {
        EditMemberView editMemberView = new EditMemberView();
        editMemberView.showUpdateMemberWindow(conn, member);
        if(editMemberView.getMember_is_modified()){
            refreshMembersList(conn);
            editMemberView.setMember_is_added(false);
        }
    }
    private void handleSuspendMember(Connection conn, Member member){
        boolean newStatus = ! member.getIs_suspended();
        member.setIs_suspended(newStatus);

        boolean success = LibraryManager.suspend_member(conn, member);
        if (success) {
            alert("Member " + (newStatus ? "suspended" : "unsuspended") + " successfully.");
            ApplicationFX.refreshAll();
            refreshMembersList(conn);
        } else {
            alert("Failed to update the member's status. Please try again.");
        }
    }
    private void handleAddPrivileges(Connection conn, Member member){
        member.setIs_library_worker(true);

        boolean success = LibraryManager.set_privileges_to_member(conn, member);
        if (success) {
            alert("Library worker privileges granted successfully.");
            refreshMembersList(conn);
        } else {
            alert("Failed to update the member's privileges. Please try again.");
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
    private String safe(String s) { return (s == null || s.isBlank()) ? "N/A" : s; }
}
