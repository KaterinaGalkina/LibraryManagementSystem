package documents;
import java.util.Set;

import people.Author;

public abstract class Document {
    private Set<Type> type;
    private String title;
    private int nb_copies;
    private int id;
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
        return type;
    }

    public String getTitle() {
        return title;
    }

    public int getNb_copies() {
        return nb_copies;
    }

    public int getId() {
        return id;
    }

    public static int getNb_documents() {
        return nb_documents;
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
