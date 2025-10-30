package com.library.ui.menu.libraryWorkerFeatures.documentsManagement.booksManagement;

import java.sql.Connection;

import com.library.borrowingsystem.LibraryManager;
import com.library.documents.Book;
import com.library.ui.menu.libraryWorkerFeatures.documentsManagement.AddDocumentView;
import com.library.ui.menu.libraryWorkerFeatures.documentsManagement.OneDocumentView;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class AddBookView implements AddDocumentView{

    @Override
    public boolean save_document_action(Connection conn, OneDocumentView document_view){ // returns true if successful and the book was added
        // --- Define red glow effect --- for fields that have an error
        DropShadow red_glow = new DropShadow();
        red_glow.setColor(Color.RED);
        red_glow.setOffsetX(0);
        red_glow.setOffsetY(0);
        red_glow.setRadius(10);

        OneBookView book_view = (OneBookView)document_view;

        if (book_view.validity_checks(conn, red_glow, true, null)) { // If everything is still ok, we add the book
            Book new_book = new Book(book_view.document_genres, book_view.document_title, book_view.document_nb_copies, book_view.document_authors, book_view.book_isbn, book_view.book_nb_pages, book_view.book_year);
            LibraryManager.add_book(conn, new_book); // And we are making the changes permanent
            return true;
        }
        return false;
    }
}
