package com.library.ui.menu.libraryWorkerFeatures.documentsManagement;

import java.sql.Connection;
import java.util.ArrayList;

import com.library.documents.Book;
import com.library.documents.Document;
import com.library.documents.MagazineIssue;
import com.library.ui.menu.libraryWorkerFeatures.documentsManagement.booksManagement.OneBookView;
import com.library.ui.menu.libraryWorkerFeatures.documentsManagement.magazineIssuesManagement.OneMagazineIssueView;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public interface AddDocumentView {
    public default void show_add_document_window(Connection conn, Class<? extends Document> documentClass) {
        // --- Create the new stage (popup window) ---
        Stage popup_stage = new Stage();
        popup_stage.initModality(Modality.APPLICATION_MODAL); // blocks interaction with main window
        popup_stage.setTitle("Add a New Document");
        popup_stage.initStyle(StageStyle.UTILITY);

        ArrayList<OneDocumentView> docs = new ArrayList<>();
        GridPane grid = null;

        if (documentClass == Book.class) {
            OneBookView document_view = new OneBookView();
            grid = document_view.get_one_book_view("📖 Add Book", null);
            docs.add(document_view);
        } else if (documentClass == MagazineIssue.class){
            OneMagazineIssueView document_view = new OneMagazineIssueView();
            grid = document_view.get_one_magazine_issue_view("📖 Add Magazine", null);
            docs.add(document_view);
        } 

        if (docs.size() == 0){
            System.out.println("Please add a new type in AddDocumentView.java");
            return;
        }

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
        Scene scene = new Scene(scroll_pane, 700, 500, Color.WHITE);
        popup_stage.setScene(scene);
        popup_stage.setResizable(false);

        // --- Button actions ---
        for (Node node : docs.get(0).buttons.getChildren()) {
            if (node instanceof Button button) {
                String text = button.getText();

                if (text.equalsIgnoreCase("Save")) {
                    button.setOnAction(e -> {
                        if (save_document_action(conn, docs.get(0))){
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

    public boolean save_document_action(Connection conn, OneDocumentView document_view);
}
