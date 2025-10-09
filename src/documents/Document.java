package documents;
import java.util.Set;

import people.Author;

public abstract class Document {
    private int id;
    private Set<Type> type;
    private String title;
    private int nb_copies;
    private static int nb_documents = 0;
    private Set<Author> authors;

    Document(Set<Type> type, String title, int nb_copies, Set<Author> authors){
        this.type = type;
        this.title = title;
        this.nb_copies = nb_copies;
        this.authors = authors;
        this.id = nb_documents;
        nb_documents++;
        for(Author author: authors){ // We are checking consistency: every author in the set has this document in its list of writings
            author.addDocument(this);
        }
    }

    // When we are retriving the information from the database, we want to set the same id, without incrementing number of documents
    Document(Set<Type> type, String title, int nb_copies, int id, Set<Author> authors){
        this.type = type;
        this.title = title;
        this.nb_copies = nb_copies;
        this.authors = authors;
        this.id = id;
        // The consistency is assumed since we are not really creating a new document but retrieving the existing one, where everything is correct
    }

    public Set<Type> getType() {
        return this.type;
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

    public static int getNb_documents() {
        return nb_documents;
    }

    public void setType(Set<Type> type) {
        this.type = type;
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
