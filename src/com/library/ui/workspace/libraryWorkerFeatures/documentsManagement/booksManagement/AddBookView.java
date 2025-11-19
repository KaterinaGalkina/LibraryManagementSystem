package com.library.ui.workspace.libraryWorkerFeatures.documentsManagement.booksManagement;

import java.sql.Connection;
import com.library.borrowingsystem.LibraryManager;
import com.library.documents.Book;
import com.library.ui.ApplicationFX;
import com.library.ui.workspace.libraryWorkerFeatures.documentsManagement.AddDocumentView;
import com.library.ui.workspace.libraryWorkerFeatures.documentsManagement.OneDocumentView;

public class AddBookView implements AddDocumentView{

    @Override
    public boolean save_document_action(Connection conn, OneDocumentView document_view){ // returns true if successful and the book was added
        if (!(document_view instanceof OneBookView)) {
            System.out.println("Wrong class in AddBookView !");
            return false;
        }
        OneBookView book_view = (OneBookView)document_view;

        if (book_view.book_validity_checks(conn, true, null)) { // If everything is still ok, we add the book
            Book new_book = new Book(book_view.document_genres, book_view.document_title, book_view.document_nb_copies, book_view.document_authors, book_view.book_isbn, book_view.book_nb_pages, book_view.book_year);
            LibraryManager.add_book(conn, new_book); // And we are making the changes permanent
            ApplicationFX.addDocument(new_book); // We are adding it in our list
            return true;
        }
        return false;
    }
}
