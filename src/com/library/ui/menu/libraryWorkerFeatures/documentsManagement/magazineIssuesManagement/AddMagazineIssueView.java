package com.library.ui.menu.libraryWorkerFeatures.documentsManagement.magazineIssuesManagement;

import java.sql.Connection;

import com.library.borrowingsystem.LibraryManager;
import com.library.documents.MagazineIssue;
import com.library.ui.menu.libraryWorkerFeatures.documentsManagement.AddDocumentView;
import com.library.ui.menu.libraryWorkerFeatures.documentsManagement.OneDocumentView;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class AddMagazineIssueView implements AddDocumentView {

    @Override
    public boolean save_document_action(Connection conn, OneDocumentView document_view) {
        // --- Define red glow effect --- for fields that have an error
        DropShadow red_glow = new DropShadow();
        red_glow.setColor(Color.RED);
        red_glow.setOffsetX(0);
        red_glow.setOffsetY(0);
        red_glow.setRadius(10);

        OneMagazineIssueView magazine_issue_view = (OneMagazineIssueView)document_view;

        if (magazine_issue_view.validity_checks(conn, red_glow, true, null)) { // If everything is still ok, we add the book
            MagazineIssue new_issue = new MagazineIssue(magazine_issue_view.document_genres, magazine_issue_view.document_title, magazine_issue_view.document_nb_copies, magazine_issue_view.document_authors, magazine_issue_view.issue_number, magazine_issue_view.issue_date, magazine_issue_view.magazine);
            LibraryManager.add_magazine_issue(conn, new_issue);// And we are making the changes permanent
            return true;
        }

        return false;
    }
    
}
