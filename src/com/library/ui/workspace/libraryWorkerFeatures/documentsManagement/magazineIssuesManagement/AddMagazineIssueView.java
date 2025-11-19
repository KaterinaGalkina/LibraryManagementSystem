package com.library.ui.workspace.libraryWorkerFeatures.documentsManagement.magazineIssuesManagement;

import java.sql.Connection;
import com.library.borrowingsystem.LibraryManager;
import com.library.documents.MagazineIssue;
import com.library.ui.ApplicationFX;
import com.library.ui.workspace.libraryWorkerFeatures.documentsManagement.AddDocumentView;
import com.library.ui.workspace.libraryWorkerFeatures.documentsManagement.OneDocumentView;

public class AddMagazineIssueView implements AddDocumentView {

    @Override
    public boolean save_document_action(Connection conn, OneDocumentView document_view) {
        if (!(document_view instanceof OneMagazineIssueView)) {
                System.out.println("Wrong class in AddMagazineIssueView !");
                return false;
            }
        OneMagazineIssueView magazine_issue_view = (OneMagazineIssueView)document_view;

        if (magazine_issue_view.magazine_issue_validity_checks(conn, true, null)) { // If everything is still ok, we add the book
            MagazineIssue new_issue = new MagazineIssue(magazine_issue_view.document_genres, magazine_issue_view.document_title, magazine_issue_view.document_nb_copies, magazine_issue_view.document_authors, magazine_issue_view.issue_number, magazine_issue_view.issue_date, magazine_issue_view.magazine);
            LibraryManager.add_magazine_issue(conn, new_issue);// And we are making the changes permanent
            ApplicationFX.addDocument(new_issue);
            return true;
        }

        return false;
    }
    
}
