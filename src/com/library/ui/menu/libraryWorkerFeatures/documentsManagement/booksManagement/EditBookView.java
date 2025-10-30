package com.library.ui.menu.libraryWorkerFeatures.documentsManagement.booksManagement;

import java.sql.Connection;

import com.library.borrowingsystem.LibraryManager;
import com.library.documents.Book;
import com.library.documents.Document;
import com.library.ui.menu.libraryWorkerFeatures.documentsManagement.EditDocumentView;
import com.library.ui.menu.libraryWorkerFeatures.documentsManagement.OneDocumentView;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class EditBookView implements EditDocumentView{

    @Override
    public boolean change_document_action(Connection conn, OneDocumentView document_view, Document document_to_change) {
        if (document_to_change instanceof Book book_to_change) {
            // --- Define red glow effect --- for fields that have an error
            DropShadow red_glow = new DropShadow();
            red_glow.setColor(Color.RED);
            red_glow.setOffsetX(0);
            red_glow.setOffsetY(0);
            red_glow.setRadius(10);

            OneBookView book_view = (OneBookView)document_view;

            if (book_view.validity_checks(conn, red_glow, false, book_to_change)) { // If everything is still ok, we add the book
                book_to_change.setGenre(book_view.document_genres);
                book_to_change.setAuthors(book_view.document_authors);
                book_to_change.setTitle(book_view.document_title);
                book_to_change.setNb_copies(book_view.document_nb_copies);
                book_to_change.setIsbn(book_view.book_isbn);
                book_to_change.setNb_pages(book_view.book_nb_pages);
                book_to_change.setYear(book_view.book_year);
                LibraryManager.update_book(conn, book_to_change); // And we are making the changes permanent
                return true;
            }
            return false;
        } else {
            System.out.println("Bad method called !");
            return false;
        }
    }
}
