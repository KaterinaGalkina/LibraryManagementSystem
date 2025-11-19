package com.library.ui.workspace.libraryWorkerFeatures.membersManagement;

import com.library.people.Member;
import com.library.ui.ApplicationFX;
import com.library.ui.login.LoginView;
import com.library.borrowingsystem.LibraryManager;
import java.sql.Connection;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AddMemberView {
    public void showAddMemberWindow(Connection conn) {
        Stage popup_stage = new Stage();
        popup_stage.initModality(Modality.APPLICATION_MODAL); // To block the interaction with the main window
        popup_stage.setTitle("Add a New Member");
        popup_stage.initStyle(StageStyle.UTILITY);

        OneMemberView member_view = new OneMemberView();
        GridPane grid = member_view.get_one_member_view("Add New Member", null);

        ScrollPane scroll_pane = new ScrollPane(grid);
        scroll_pane.setFitToWidth(true);
        scroll_pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll_pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll_pane.setStyle("""
            -fx-background: white;
            -fx-background-color: white;
        """);

        Scene scene = new Scene(scroll_pane, 600, 400, Color.WHITE);
        popup_stage.setScene(scene);
        popup_stage.setResizable(false);

        // Buttons
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
        
        // Showing the window
        popup_stage.showAndWait();
    }

    public boolean save_member_action(Connection conn, OneMemberView member_view) {
        if (member_view.validity_checks(conn, true)) {
            Member new_member = new Member(
                member_view.member_first_name,
                member_view.member_last_name,
                member_view.member_birth_date,
                member_view.member_phone,
                member_view.member_address,
                member_view.member_email
            );
            new_member.setIs_library_worker(member_view.member_is_library_worker);
            LibraryManager.add_member(conn, new_member, LoginView.hashPassword(member_view.member_password)); // And we are making the changes permanent
            LibraryManager.set_privileges_to_member(conn, new_member);
            ApplicationFX.addMember(new_member);
            return true;
        }
        return false;
    }
}
