package com.library.ui.menu;

import java.sql.Connection;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class BorrowingsView {
    public GridPane start(Connection conn) { // TODO
        // --- Create a simple grid layout for the main content ---
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(20);
        grid.setHgap(20);
        grid.setStyle("-fx-background-color: white;");

        // --- Add UI components (parameterized for flexibility) ---
        Label label = new Label("📚 Borrowings Section");
        label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        grid.add(label, 0, 0);

        return grid;
    }
}
