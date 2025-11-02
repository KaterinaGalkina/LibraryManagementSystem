package com.library.ui.menu.libraryWorkerFeatures.membersManagement;

import com.library.borrowingsystem.LibraryManager;
import com.library.people.Member;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;

public class EditMemberView {
    private boolean member_is_updated = false; // Will indicate whether we need to update the list of members or not

    public boolean getMember_is_modified(){
        return this.member_is_updated;
    }

    public void setMember_is_added(boolean member_is_updated) {
        this.member_is_updated = member_is_updated;
    }

    public void showUpdateMemberWindow(Connection conn, Member member) {
        // --- Create the new stage (popup window) ---
        Stage popup_stage = new Stage();
        popup_stage.initModality(Modality.APPLICATION_MODAL); // blocks interaction with main window
        popup_stage.setTitle("Update a Book");
        popup_stage.initStyle(StageStyle.UTILITY);

        OneMemberView member_view = new OneMemberView();
        GridPane grid = member_view.get_one_member_view("👤 Modify Member", member);

        // --- Scroll wrapper ---
        ScrollPane scroll_pane = new ScrollPane(grid);
        scroll_pane.setFitToWidth(true);
        scroll_pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll_pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll_pane.setStyle("""
            -fx-background: white;
            -fx-background-color: white;
        """);

        // --- Scene setup ---
        Scene scene = new Scene(scroll_pane, 600, 400, Color.WHITE);
        popup_stage.setScene(scene);
        popup_stage.setResizable(false);

        // --- Button actions ---
        for (Node node : member_view.buttons.getChildren()) {
            if (node instanceof javafx.scene.control.Button button) {
                String text = button.getText();

                if (text.equalsIgnoreCase("Save")) {
                    button.setOnAction(e -> {
                        if (change_member_action(conn, member_view, member)){
                            popup_stage.close();
                        }
                    });
                } else if (text.equalsIgnoreCase("Cancel")) {
                    button.setOnAction(e -> popup_stage.close());
                }
            }
        }
        // --- Show the window ---
        popup_stage.showAndWait(); // Blocks until closed
    }

    public boolean change_member_action(Connection conn, OneMemberView memberView,Member member_to_change) {
        // --- Define red glow effect --- for fields that have an error
        DropShadow red_glow = new DropShadow();
        red_glow.setColor(Color.RED);
        red_glow.setOffsetX(0);
        red_glow.setOffsetY(0);
        red_glow.setRadius(10);

        // Remove all glows so that we can see only those that have currently a problem
        memberView.member_first_name_TextField.setEffect(null);
        memberView.member_last_name_TextField.setEffect(null);
        memberView.member_email_TextField.setEffect(null);
        memberView.member_phone_TextField.setEffect(null);   
        memberView.member_address_TextField.setEffect(null);
        memberView.member_birth_date_Picker.setEffect(null);

        memberView.message.setText("");
        // --- Input validation ---
        if( memberView.validity_checks(conn, red_glow, false)){
            member_to_change.setFirst_name( memberView.member_first_name);
            member_to_change.setLast_name( memberView.member_last_name);
            member_to_change.setMail( memberView.member_email);
            member_to_change.setPhone_number( memberView.member_phone);
            member_to_change.setAddress( memberView.member_address);
            member_to_change.setBirth_date( memberView.member_birth_date);

            LibraryManager.update_member(conn, member_to_change, null); // And we are making the changes permanent
            this.member_is_updated = true;
            return true;
        }

        return false; // Return true if the member was successfully updated
    }
}
