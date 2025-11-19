package com.library.ui.workspace.libraryWorkerFeatures.documentsManagement;

import java.sql.Connection;
import java.util.ArrayList;
import com.library.documents.Book;
import com.library.documents.Document;
import com.library.documents.MagazineIssue;
import com.library.ui.workspace.libraryWorkerFeatures.documentsManagement.booksManagement.OneBookView;
import com.library.ui.workspace.libraryWorkerFeatures.documentsManagement.magazineIssuesManagement.OneMagazineIssueView;

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
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Add a New Document");
        popupStage.initStyle(StageStyle.UTILITY);

        ArrayList<OneDocumentView> docs = new ArrayList<>();
        GridPane grid = null;

        if (documentClass == Book.class) {
            OneBookView documentView = new OneBookView();
            grid = documentView.get_one_book_view("Add Book", null);
            docs.add(documentView);
        } else if (documentClass == MagazineIssue.class){
            OneMagazineIssueView documentView = new OneMagazineIssueView();
            grid = documentView.get_one_magazine_issue_view("Add Magazine", null);
            docs.add(documentView);
        } 

        if (docs.size() == 0){
            System.out.println("Please add a new type in AddDocumentView.java");
            return;
        }

        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("""
            -fx-background: white;
            -fx-background-color: white;
        """);

        Scene scene = new Scene(scrollPane, 700, 500, Color.WHITE);
        popupStage.setScene(scene);
        popupStage.setResizable(false);

        // Button actions
        for (Node node : docs.get(0).buttons.getChildren()) {
            if (node instanceof Button button) {
                String text = button.getText();

                if (text.equalsIgnoreCase("Save")) {
                    button.setOnAction(e -> {
                        if (save_document_action(conn, docs.get(0))){
                            popupStage.close();
                        }
                    });
                } else if (text.equalsIgnoreCase("Cancel")) {
                    button.setOnAction(e -> popupStage.close());
                }
            }
        }
        popupStage.showAndWait();
    }

    public boolean save_document_action(Connection conn, OneDocumentView document_view);
}
