package com.library.ui.menu.libraryWorkerFeatures.membersManagement;

import com.library.people.Member;
import com.library.ui.login.LoginView;
import com.library.borrowingsystem.LibraryManager;

import java.sql.Connection;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AddMemberView {
    private boolean member_is_added = false; 
    
    public boolean getMember_is_added(){
        return this.member_is_added;
    }

    public void setMember_is_added(boolean member_is_added) {
        this.member_is_added = member_is_added;
    }

    public void showAddMemberWindow(Connection conn) {
        // --- Create the new stage (popup window) ---
        Stage popup_stage = new Stage();
        popup_stage.initModality(Modality.APPLICATION_MODAL); // blocks interaction with main window
        popup_stage.setTitle("Add a New Mgazine");
        popup_stage.initStyle(StageStyle.UTILITY);

        OneMemberView member_view = new OneMemberView();
        GridPane grid = member_view.get_one_member_view("👤 Add New Member", null);

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
            if (node instanceof Button button) {
                String text = button.getText();

                if (text.equalsIgnoreCase("Save")) {
                    button.setOnAction(e -> {
                        if (save_member_action(conn, member_view)){
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

    public boolean save_member_action(Connection conn, OneMemberView member_view) {
        // --- Define red glow effect --- for fields that have an error
        DropShadow red_glow = new DropShadow();
        red_glow.setColor(Color.RED);
        red_glow.setOffsetX(0);
        red_glow.setOffsetY(0);
        red_glow.setRadius(10);

        // Remove all glows so that we can see only those that have currently a problem
        member_view.member_first_name_TextField.setEffect(null);
        member_view.member_last_name_TextField.setEffect(null);
        member_view.member_email_TextField.setEffect(null);
        member_view.member_phone_TextField.setEffect(null);
        member_view.member_birth_date_Picker.setEffect(null);
        member_view.member_address_TextField.setEffect(null);
        member_view.member_password_Field.setEffect(null);
        member_view.message.setText("");


        if (member_view.validity_checks(conn, red_glow, true)) {
            Member new_member = new Member(
                member_view.member_first_name,
                member_view.member_last_name,
                member_view.member_birth_date,
                member_view.member_phone,
                member_view.member_address,
                member_view.member_email
            );
            LibraryManager.add_member(conn, new_member, LoginView.hashPassword(member_view.member_password)); // And we are making the changes permanent
            this.member_is_added = true;
            return true;
        }
        return false;
    }
}
