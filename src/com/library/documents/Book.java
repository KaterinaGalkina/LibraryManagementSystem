package com.library.documents;

import java.util.Set;

import com.library.people.Author;

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

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setNb_pages(int nb_pages) {
        this.nb_pages = nb_pages;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
