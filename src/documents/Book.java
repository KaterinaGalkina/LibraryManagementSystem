package documents;

import java.util.Set;

import people.Author;

public class Book extends Document {
    private String isbn;
    private int nb_pages;
    private int year;

    public Book(Set<Genre> genre, String title, int nb_copies, Set<Author> authors, String isbn, int nb_pages, int year) {
        super(genre, title, nb_copies, authors);
        this.isbn = isbn;
        this.nb_pages = nb_pages;
        this.year = year;
    }

    public Book(Set<Genre> genre, String title, int id, int nb_copies, Set<Author> authors, String isbn, int nb_pages, int year) {
        super(genre, title, nb_copies, id, authors);
        this.isbn = isbn;
        this.nb_pages = nb_pages;
        this.year = year;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getNb_pages() {
        return nb_pages;
    }

    public int getYear() {
        return year;
    }
    
}
