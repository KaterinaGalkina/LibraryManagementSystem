package com.library.documents;

import java.time.LocalDate;
import java.util.Set;

import com.library.people.Author;

public class MagazineIssue extends Document{
    private int issue_number;
    private LocalDate issue_date;
    private Magazine magazine;

    // If we are creating a fresh new
    public MagazineIssue(Set<Genre> genre, String title, int nb_copies, Set<Author> authors, int issue_number, LocalDate issue_date, Magazine magazine) {
        super(genre, title, nb_copies, authors);
        this.issue_number = issue_number; // We are checking that it is positive
        this.issue_date = issue_date; // We are checking whether the date is not empty
        this.magazine = magazine;
    }

    // If we are only retrieving it from a database (there is already an id)
    public MagazineIssue(Set<Genre> genre, String title, int nb_copies, int id, Set<Author> authors, int issue_number, LocalDate issue_date, Magazine magazine) {
        super(genre, title, nb_copies, id, authors);
        this.issue_number = issue_number;
        this.issue_date = issue_date;
        this.magazine = magazine;
    }

    public int getIssue_number() {
        return this.issue_number;
    }

    public LocalDate getIssue_date() {
        return this.issue_date;
    }

    public Magazine getMagazine() {
        return this.magazine;
    }

    public void setIssue_number(int issue_number) {
        this.issue_number = issue_number;
    }

    public void setIssue_date(LocalDate issue_date) {
        this.issue_date = issue_date;
    }

    public void setMagazine(Magazine magazine) {
        this.magazine = magazine;
    }
}
