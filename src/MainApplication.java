import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import borrowingsystem.Borrowing;
import borrowingsystem.LibraryManager;
import documents.Book;
import documents.Document;
import documents.Genre;
import documents.Magazine;
import documents.MagazineIssue;
import documents.Periodicity;
import people.Author;
import people.Member;

public class MainApplication {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:database/library_info.db"; 

        try {
            Connection conn = DriverManager.getConnection(url);
            ArrayList<Author> authors = LibraryManager.get_authors(conn);
            ArrayList<Member> members = LibraryManager.get_members(conn);
            ArrayList<Magazine> magazines = LibraryManager.get_magazines(conn);
            ArrayList<Document> documents = LibraryManager.get_documents(conn, authors, magazines);
            HashMap<Member,HashSet<Borrowing>> borrowings = LibraryManager.get_borrowings(conn, members, documents);

            System.out.println("Existing members:");
            for(Member m: members){
                System.out.println("\t" + m.toString());
            }

            System.out.println("Existing authors:");
            for(Author a: authors){
                System.out.println("\t" + a.toString());
            }

            System.out.println("Existing magazines:");
            for(Magazine m: magazines){
                System.out.println("\t" + m.toString());
            }

            System.out.println("Existing documents:");
            for(Document d: documents){
                System.out.println("\t" + d.toString());
            }

            System.out.println("Existing borrowings:");
            for(Member m: borrowings.keySet()){
                System.out.println("For " + m.toString() + " : ");
                for(Borrowing b: borrowings.get(m)){
                    System.out.println("\t- " + b.toString() + "");
                }
            }

            // Member m1 = new Member("Kate", "Galkina", LocalDate.of(2004, 10, 10), "+330611879072", "121, rue de Bellevue", "ekaterina.galkina@outlook.com");
            // Author a1 = new Author("George", "Orwell", LocalDate.of(1903, 6, 25));
            // Book b1 = new Book(new HashSet<>(Arrays.asList(Genre.FICTION)), "Animal Farm", 2, new HashSet<>(Arrays.asList(a1)), "9780194267533", 176, 1945);
            // Magazine magazine1 = new Magazine("New Scientist", Periodicity.WEEKLY);
            // Author a2 = new Author("Alison", "George", LocalDate.of(1988, 1, 3));
            // authors.add(a2);
            // MagazineIssue magazineIssue1 = new MagazineIssue(new HashSet<>(Arrays.asList(Genre.SCIENCE_FICTION)), "Science fiction special: The future of a genre", 3, new HashSet<>(Arrays.asList(authors.get(1))), 875, LocalDate.of(2008, 11, 12), magazines.get(0));
            // Borrowing b1 = new Borrowing(documents.get(0), members.get(0), LocalDate.now());

            // LibraryManager.add_magazine(conn, magazine1);
            // LibraryManager.create_member(conn, m1);
            // LibraryManager.add_document(conn, b1);
            // LibraryManager.add_document(conn, magazineIssue1);
            // LibraryManager.create_borrowing(conn, b1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
