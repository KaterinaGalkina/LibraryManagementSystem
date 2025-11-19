package com.library.ui.workspace.libraryWorkerFeatures.magazineManagement;

import java.sql.Connection;

import com.library.borrowingsystem.LibraryManager;
import com.library.documents.Magazine;
import com.library.ui.ApplicationFX;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AddMagazineView {
    public void showAddMagazineWindow(Connection conn) {
        Stage popup_stage = new Stage();
        popup_stage.initModality(Modality.APPLICATION_MODAL); // blocks interaction with main window
        popup_stage.setTitle("Add a New Mgazine");
        popup_stage.initStyle(StageStyle.UTILITY);

        OneMagazineView magazine_view = new OneMagazineView();
        GridPane grid = magazine_view.get_one_magazine_view("Add New Magazine", null);

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
                        if (save_magazine_action(conn, magazine_view)){
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

    private boolean save_magazine_action(Connection conn, OneMagazineView magazine_view){ // returns true if successful and the book was added
        if (magazine_view.validity_checks(true)) { // If everything is still ok, we add the book
            Magazine new_magazine = new Magazine(magazine_view.magazine_title, magazine_view.magazine_periodicity);
            LibraryManager.add_magazine(conn, new_magazine); // And we are making the changes permanent
            ApplicationFX.addMagazine(new_magazine);
            return true;
        }
        return false;
    }
}
