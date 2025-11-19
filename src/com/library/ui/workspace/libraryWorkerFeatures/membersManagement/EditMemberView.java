package com.library.ui.workspace.libraryWorkerFeatures.membersManagement;

import com.library.borrowingsystem.LibraryManager;
import com.library.people.Member;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;

public class EditMemberView {
    public void showUpdateMemberWindow(Connection conn, Member member) {
        Stage popup_stage = new Stage();
        popup_stage.initModality(Modality.APPLICATION_MODAL); // To block the interaction with main window
        popup_stage.setTitle("Update a Member");
        popup_stage.initStyle(StageStyle.UTILITY);

        OneMemberView member_view = new OneMemberView();
        GridPane grid = member_view.get_one_member_view("Modify Member", member);

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
        // Showing window
        popup_stage.showAndWait();
    }

    public boolean change_member_action(Connection conn, OneMemberView memberView, Member member_to_change) {
        if(memberView.validity_checks(conn, false)){
            member_to_change.setFirst_name(memberView.member_first_name);
            member_to_change.setLast_name(memberView.member_last_name);
            member_to_change.setMail(memberView.member_email);
            member_to_change.setPhone_number(memberView.member_phone);
            member_to_change.setAddress(memberView.member_address);
            member_to_change.setBirth_date(memberView.member_birth_date);
            member_to_change.setIs_library_worker(memberView.member_is_library_worker);

            LibraryManager.update_member(conn, member_to_change, null); // And we are making the changes permanent
            LibraryManager.set_privileges_to_member(conn, member_to_change);
            return true;
        }
        return false;
    }
}
