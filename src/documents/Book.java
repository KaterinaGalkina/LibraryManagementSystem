package documents;

import java.util.Set;

import people.Author;

public class Book extends Document {
    private int isbn;
    private int nb_pages;
    private int year;

    Book(Set<Type> type, String title, int nb_copies, Set<Author> authors, int isbn, int nb_pages, int year) {
        super(type, title, nb_copies, authors);
        this.isbn = isbn;
        this.nb_pages = nb_pages;
        this.year = year;
    }

    public int getIsbn() {
        return isbn;
    }

    public int getNb_pages() {
        return nb_pages;
    }

    public int getYear() {
        return year;
    }
    
}
