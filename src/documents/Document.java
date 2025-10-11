package documents;
import java.util.Set;

import people.Author;

public abstract class Document {
    private int id;
    private Set<Genre> genre;
    private String title;
    private int nb_copies;
    private static int next_id = 0;
    private Set<Author> authors;

    Document(Set<Genre> genre, String title, int nb_copies, Set<Author> authors){
        this.genre = genre;
        this.title = title;
        this.nb_copies = nb_copies;
        this.authors = authors;
        this.id = next_id;
        next_id++;
        for(Author author: authors){ // We are checking consistency: every author in the set has this document in its list of writings
            author.addDocument(this);
        }
    }

    // When we are retriving the information from the database, we want to set the same id, without incrementing number of documents
    Document(Set<Genre> genre, String title, int nb_copies, int id, Set<Author> authors){
        this.genre = genre;
        this.title = title;
        this.nb_copies = nb_copies;
        this.authors = authors;
        this.id = id;
        if (next_id <= id) {
            next_id = id + 1;
        }
        for(Author author: authors){ // We are checking consistency: every author in the set has this document in its list of writings
            author.addDocument(this);
        }    
    }

    @Override
    public String toString() {
        return "\"" + this.title + "\" by " + this.authors.toString();
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
        this.genre = genre;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNb_copies(int nb_copies) {
        this.nb_copies = nb_copies;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public boolean deleteAuthor(Author author_to_delete){
        if (this.authors.contains(author_to_delete)){ // Ok comparaison on reference, because we won't create the same author 2 times
            this.authors.remove(author_to_delete);
            return true;
        }
        return false;
    }

    // Adds an author in the list of authors of this document, and returns true if it succeds
    public boolean addAuthor(Author new_author){
        if (new_author != null && !authors.contains(new_author)){
            authors.add(new_author);
            return true;
        }
        return false;
    }
}
