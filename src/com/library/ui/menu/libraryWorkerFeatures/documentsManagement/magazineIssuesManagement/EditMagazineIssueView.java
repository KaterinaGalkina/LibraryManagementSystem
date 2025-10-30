package com.library.ui.menu.libraryWorkerFeatures.documentsManagement.magazineIssuesManagement;

import java.sql.Connection;

import com.library.borrowingsystem.LibraryManager;
import com.library.documents.Document;
import com.library.documents.MagazineIssue;
import com.library.ui.menu.libraryWorkerFeatures.documentsManagement.EditDocumentView;
import com.library.ui.menu.libraryWorkerFeatures.documentsManagement.OneDocumentView;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class EditMagazineIssueView implements EditDocumentView{

    @Override
    public boolean change_document_action(Connection conn, OneDocumentView document_view, Document document_to_change) {

        if (document_to_change instanceof MagazineIssue magazine_issue_to_change) {
            // --- Define red glow effect --- for fields that have an error
            DropShadow red_glow = new DropShadow();
            red_glow.setColor(Color.RED);
            red_glow.setOffsetX(0);
            red_glow.setOffsetY(0);
            red_glow.setRadius(10);

            OneMagazineIssueView magazine_issue_view = (OneMagazineIssueView)document_view;

            if (magazine_issue_view.validity_checks(conn, red_glow, false, magazine_issue_to_change)) { // If everything is still ok, we add the book
                magazine_issue_to_change.setTitle(magazine_issue_view.document_title);
                magazine_issue_to_change.setIssue_date(magazine_issue_view.issue_date);
                magazine_issue_to_change.setIssue_number(magazine_issue_view.issue_number);
                magazine_issue_to_change.setGenre(magazine_issue_view.document_genres);
                magazine_issue_to_change.setAuthors(magazine_issue_view.document_authors);
                magazine_issue_to_change.setMagazine(magazine_issue_view.magazine);
                magazine_issue_to_change.setNb_copies(magazine_issue_view.document_nb_copies);
                
                LibraryManager.update_magazine_issue(conn, magazine_issue_to_change); // And we are making the changes permanent
                return true;
            }
            return false;
        } else {
            System.out.println("Bad method called !");
            return false;
        }
    }
    
}
