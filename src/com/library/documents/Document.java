package com.library.documents;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.library.people.Author;

public abstract class Document {
    private int id;
    private Set<Genre> genre;
    private String title;
    private int nb_copies;
    private static int next_id = 0;
    private Set<Author> authors;

    Document(Set<Genre> genre, String title, int nb_copies, Set<Author> authors){
        this.genre = genre != null ? genre : new HashSet<>();
        this.title = title != null ? title : "Default Title";
        this.nb_copies = nb_copies > 0 ? nb_copies : 0;
        this.authors = authors != null ? authors : new HashSet<>();
        this.id = next_id;
        next_id++;
        for(Author author: this.authors){ // We are checking consistency: every author in the set has this document in its list of writings
            author.addDocument(this);
        }
    }

    // When we are retriving the information from the database, we want to set the same id, without incrementing number of documents
    Document(Set<Genre> genre, String title, int nb_copies, int id, Set<Author> authors){
        this.genre = genre != null ? genre : new HashSet<>() ;
        this.title = title != null ? title : "Default Title";
        this.nb_copies = nb_copies > 0 ? nb_copies : 0;
        this.authors = authors != null ? authors : new HashSet<>();
        this.id = id;
        if (next_id <= id) {
            next_id = id + 1;
        }
        for(Author author: this.authors){ // We are checking consistency: every author in the set has this document in its list of writings
            author.addDocument(this);
        }    
    }

    @Override
    public String toString() {
        return "\"" + this.title + "\" \n\t\tby " + String.join("\n\t\t   ", this.authors.stream().map(author -> author.toString()).collect(Collectors.toList()));
    }

    public Set<Genre> getGenre() {
        return this.genre;
    }

    public String getTitle() {
        return this.title;
    }

    public int getNb_copies() {
        return this.nb_copies;
    }

    public int getId() {
        return this.id;
    }

    public Set<Author> getAuthors() {
        return this.authors;
    }

    public void setGenre(Set<Genre> genre) {
        this.genre = genre != null ? genre : new HashSet<>();
    }

    public void setTitle(String title) {
        this.title = title != null ? title : "Default Title";
    }

    public void setNb_copies(int nb_copies) {
        this.nb_copies = nb_copies > 0 ? nb_copies : 0;
    }

    // To maintain consistency
    public void setAuthors(Set<Author> new_authors) {
        Set<Author> oldAuthors = new HashSet<>(this.authors); 
        for (Author author : oldAuthors) {
            this.deleteAuthor(author);
        }
        this.authors = new_authors != null ? new_authors : new HashSet<>();
        for (Author author : this.authors) {
            this.addAuthor(author);
        }
    }

    public boolean deleteAuthor(Author author_to_delete){
        if (this.authors.contains(author_to_delete)){ // Ok comparaison on reference, because we won't create the same author 2 times
            this.authors.remove(author_to_delete);
            author_to_delete.deleteDocument(this);
            return true;
        }
        return false;
    }

    // Adds an author in the list of authors of this document, and returns true if it succeds
    public boolean addAuthor(Author new_author){
        if (new_author != null && !authors.contains(new_author)){
            authors.add(new_author);
            new_author.addDocument(this);
            return true;
        }
        return false;
    }
}
