package com.library.ui.workspace.libraryWorkerFeatures.magazineManagement;

import java.sql.Connection;
import com.library.borrowingsystem.LibraryManager;
import com.library.documents.Magazine;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EditMagazineView {
    public void showUpdateMagazineWindow(Connection conn, Magazine magazine_to_change) {
        Stage popup_stage = new Stage();
        popup_stage.initModality(Modality.APPLICATION_MODAL); // blocks interaction with main window
        popup_stage.setTitle("Update a Magazine");
        popup_stage.initStyle(StageStyle.UTILITY);

        OneMagazineView magazine_view = new OneMagazineView();
        GridPane grid = magazine_view.get_one_magazine_view("Modify Magazine", magazine_to_change);

        ScrollPane scroll_pane = new ScrollPane(grid);
        scroll_pane.setFitToWidth(true);
        scroll_pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll_pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll_pane.setStyle("""
            -fx-background: white;
            -fx-background-color: white;
        """);

        Scene scene = new Scene(scroll_pane, 700, 250, Color.WHITE);
        popup_stage.setScene(scene);
        popup_stage.setResizable(false);

        // Button actions
        for (Node node : magazine_view.buttons.getChildren()) {
            if (node instanceof Button button) {
                String text = button.getText();

                if (text.equalsIgnoreCase("Save")) {
                    button.setOnAction(e -> {
                        if (change_magazine_action(conn, magazine_view, magazine_to_change)){
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

    private boolean change_magazine_action(Connection conn, OneMagazineView magazine_view, Magazine magazine_to_change){ // returns true if successful and the magazine was modified
        if (magazine_view.validity_checks(false)) { // If everything is still ok, we add the book
            magazine_to_change.setMagazine_title(magazine_view.magazine_title);
            magazine_to_change.setPeriodicity(magazine_view.magazine_periodicity);
            LibraryManager.update_magazine(conn, magazine_to_change); // And we are making the changes permanent
            return true;
        }
        return false;
    }
}
