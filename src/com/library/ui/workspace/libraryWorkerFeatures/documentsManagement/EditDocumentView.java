package com.library.ui.workspace.libraryWorkerFeatures.documentsManagement;

import java.sql.Connection;
import java.util.ArrayList;
import com.library.documents.Book;
import com.library.documents.Document;
import com.library.documents.MagazineIssue;
import com.library.ui.workspace.libraryWorkerFeatures.documentsManagement.booksManagement.OneBookView;
import com.library.ui.workspace.libraryWorkerFeatures.documentsManagement.magazineIssuesManagement.*;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.layout.GridPane;

public interface EditDocumentView {
    public default void show_update_document_window(Connection conn, Document document_to_change) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Update a Document");
        popupStage.initStyle(StageStyle.UTILITY);

        ArrayList<OneDocumentView> docs = new ArrayList<>();
        GridPane grid = null;

        if (document_to_change instanceof Book book_to_change) {
            OneBookView document_view = new OneBookView();
            grid = document_view.get_one_book_view("Modify Book", book_to_change);
            docs.add(document_view);
        } else if (document_to_change instanceof MagazineIssue magazine_issue_to_change) {
            OneMagazineIssueView document_view = new OneMagazineIssueView();
            grid = document_view.get_one_magazine_issue_view("Modify Magazine Issue", magazine_issue_to_change);
            docs.add(document_view);
        }

        if (docs.size() == 0){
            System.out.println("Please add a new type in EditDocumentView.java");
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
                        if (change_document_action(conn, docs.get(0), document_to_change)){
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

    public boolean change_document_action(Connection conn, OneDocumentView document_view, Document document_to_change);
}
