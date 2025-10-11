package people;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import documents.Document;

public class Author extends Person {
    Set<Document> written_documents;

    // We don't have to add list of writings right now 
    public Author(String first_name, String last_name, LocalDate birth_date) {
        super(first_name, last_name, birth_date);
        written_documents = new HashSet<Document>();
    }

    public Author(String first_name, String last_name, LocalDate birth_date, Set<Document> written_documents) {
        super(first_name, last_name, birth_date);
        this.written_documents = written_documents;
        for (Document document: written_documents){ // We are checking consistency: every document in the set has this author in its list of authors
            document.addAuthor(this);
        } 
    }

    public Author(String first_name, String last_name, LocalDate birth_date, int id) {
        super(id, first_name, last_name, birth_date);
        written_documents = new HashSet<Document>();
    }

    public void setWritten_documents(Set<Document> written_documents) {
        this.written_documents = written_documents;
    }
    
    // Adds a document in the list of writings of this author, and returns true if it succeds
    public boolean addDocument(Document new_document){
        if (new_document != null && !written_documents.contains(new_document)){
            written_documents.add(new_document);
            return true;
        }
        return false;
    }

    public boolean deleteDocument(Document document_to_delete){
        if(this.written_documents.contains(document_to_delete)){
            this.written_documents.remove(document_to_delete);
            return true;
        }
        return false;
    }
}
