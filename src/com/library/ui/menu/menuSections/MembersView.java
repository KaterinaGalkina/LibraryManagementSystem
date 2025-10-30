package com.library.ui.menu.menuSections;

import java.sql.Connection;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

public class MembersView {
    public ScrollPane start(Connection conn) { // TODO
        // --- Create a simple grid layout for the main content ---
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(20);
        grid.setHgap(20);
        grid.setStyle("-fx-background-color: white;");

        // --- Add UI components (parameterized for flexibility) ---
        Label label = new Label("You can manage other users here !");
        label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        grid.add(label, 0, 0);

        // --- Scroll wrapper ---
        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        return scrollPane;
    }
}
