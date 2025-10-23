package com.library.borrowingsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.library.documents.Book;
import com.library.documents.Document;
import com.library.documents.Magazine;
import com.library.documents.MagazineIssue;
import com.library.documents.Periodicity;
import com.library.documents.Genre;
import com.library.people.Author;
import com.library.people.Member;
import com.library.ui.ApplicationFX;

public class LibraryManager {


    public static ArrayList<Author> get_authors(Connection conn){
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Distinct people.id, people.first_name, people.last_name, people.birth_date FROM documents_authors INNER JOIN people on documents_authors.author_id = people.id ;"); // dans tous les cas on crée deux fois author et member si un auteur est un member
            ArrayList<Author> authors = new ArrayList<>();

            while (rs.next()) {
                authors.add(
                    new Author(rs.getString("first_name"), 
                            rs.getString("last_name"), 
                            LocalDate.parse(rs.getString("birth_date")),
                            rs.getInt("id")
                    )
                );
            }
            return authors;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Member> get_members(Connection conn){ 
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM people NATURAL JOIN members;");

            ArrayList<Member> members = new ArrayList<>();

            while (rs.next()) {
                String member_first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                LocalDate birth_date = LocalDate.parse(rs.getString("birth_date"));
                int id = rs.getInt("id");
                boolean penalty_status = rs.getBoolean("penalty_status");
                String phone_number = rs.getString("phone_number");
                String address = rs.getString("address");
                String mail = rs.getString("mail");

                Statement stmt2 = conn.createStatement();
                ResultSet rs2 = stmt2.executeQuery("SELECT COUNT(*) FROM borrowings WHERE person_id = " + rs.getInt("id") + " AND expected_end_date IS NULL;");

                int nb_borrowings = rs2.getInt("COUNT(*)");

                members.add(
                    new Member(member_first_name, 
                    last_name, 
                    birth_date,
                    id, 
                    penalty_status, 
                    phone_number, 
                    address, 
                    mail, 
                    nb_borrowings)
                );
            }
            return members;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Magazine> get_magazines(Connection conn){
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM magazines;");
            ArrayList<Magazine> magazines = new ArrayList<>();

            while (rs.next()) {
                magazines.add(
                    new Magazine(rs.getInt("magazine_id"),
                        rs.getString("magazine_title"), 
                        Periodicity.convertStringToPeriodicity(rs.getString("periodicity"))
                    )
                );
            }
            return magazines;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static ArrayList<Book> get_books(Connection conn, ArrayList<Author> authors){ 
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs_books = stmt.executeQuery("SELECT * FROM documents INNER JOIN books ON documents.id = books.id;");
            
            ArrayList<Book> books = new ArrayList<>();

            while (rs_books.next()) {
                int book_id = rs_books.getInt("id");
                String book_title = rs_books.getString("title");
                int book_nb_copies = rs_books.getInt("nb_copies");
                String book_isbn = rs_books.getString("isbn");
                int book_pages_number = rs_books.getInt("pages_number");
                int book_year = rs_books.getInt("year");

                stmt = conn.createStatement();
                ResultSet rs_genres = stmt.executeQuery("SELECT * FROM document_genres Where document_genres.document_id  = " + book_id + ";");

                Set<Genre> genres_of_the_current_book = new HashSet<>();
                while (rs_genres.next()) {
                    genres_of_the_current_book.add(Genre.convertStringToGenre(rs_genres.getString("genre"))); 
                }

                stmt = conn.createStatement();
                ResultSet rs_authors = stmt.executeQuery("SELECT * FROM documents_authors WHERE documents_authors.document_id = " + book_id + ";");

                Set<Author> authors_of_the_current_book = new HashSet<>();

                while (rs_authors.next()) {
                    int id = rs_authors.getInt("author_id");
                    Author author = authors.stream()
                        .filter(a -> a.getId() == id)
                        .findFirst()
                        .orElse(null);
                    if (author != null){
                        authors_of_the_current_book.add(author);
                    } else {
                        System.out.println("Error: An author is absent !");
                    }
                }

                books.add(
                    new Book(
                        genres_of_the_current_book,
                        book_title, 
                        book_id, 
                        book_nb_copies, 
                        authors_of_the_current_book,
                        book_isbn,
                        book_pages_number,
                        book_year
                    )
                );
            }
            return books;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static ArrayList<MagazineIssue> get_magazine_issues(Connection conn, ArrayList<Author> authors, ArrayList<Magazine> magazines){ 
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM documents INNER JOIN magazine_numbers ON documents.id = magazine_numbers.id;");

            ArrayList<MagazineIssue> magazine_issues = new ArrayList<>();

            while (rs.next()) {
                int magazineIssue_id = rs.getInt("id");
                int magazine_id = rs.getInt("magazine_id");

                Statement stmt2 = conn.createStatement();
                ResultSet rs_genres = stmt2.executeQuery("SELECT * FROM document_genres WHERE document_genres.document_id = " + magazineIssue_id + ";");

                Set<Genre> genre_of_of_the_current_magazine = new HashSet<>();
                while (rs_genres.next()) {
                    genre_of_of_the_current_magazine.add(Genre.convertStringToGenre(rs_genres.getString("genre")));
                }

                Statement stmt3 = conn.createStatement();
                ResultSet rs_authors = stmt3.executeQuery("SELECT * FROM documents_authors WHERE documents_authors.document_id = " + magazineIssue_id + ";");

                Set<Author> authors_of_the_current_magazine = new HashSet<>();

                while (rs_authors.next()) {
                    int id = rs_authors.getInt("author_id");
                    Author author = authors.stream()
                        .filter(a -> a.getId() == id)
                        .findFirst()
                        .orElse(null);
                    if (author != null){
                        authors_of_the_current_magazine.add(author);
                    } else {
                        System.out.println("Error: An author is abscent !");
                    }
                }
                // We find the magazine corresponding to this magazine issue
                Magazine magazine = magazines.stream()
                        .filter(a -> a.getMagazine_id() == magazine_id)
                        .findFirst()
                        .orElse(null);
                magazine_issues.add(
                    new MagazineIssue(
                        genre_of_of_the_current_magazine,
                        rs.getString("title"), 
                        rs.getInt("nb_copies"), 
                        magazineIssue_id,
                        authors_of_the_current_magazine,
                        rs.getInt("issue_number"),
                        LocalDate.parse(rs.getString("issue_date")),
                        magazine
                    )
                );
            }
            return magazine_issues;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static ArrayList<Document> get_documents(Connection conn, ArrayList<Author> authors, ArrayList<Magazine> magazines){ 
        ArrayList<Document> documents = new ArrayList<>();
        ArrayList<Book> books = get_books(conn, authors);
        ArrayList<MagazineIssue> magazine_issues = get_magazine_issues(conn, authors, magazines);   
        documents.addAll(books);
        documents.addAll(magazine_issues);     
        return documents;
    }

    public static HashMap<Member, HashSet<Borrowing>> get_borrowings(Connection conn, ArrayList<Member> members, ArrayList<Document> documents){ 
        try {
            Statement stmt;
            HashMap<Member, HashSet<Borrowing>> borrowings_per_member = new HashMap<>();
            Document document;
            for(Member member: members){
                borrowings_per_member.put(member, new HashSet<Borrowing>());
                stmt = conn.createStatement();
                
                ResultSet rs = stmt.executeQuery("SELECT * FROM borrowings WHERE borrowings.person_id = " + member.getId() + ";");
                while (rs.next()) {
                    int document_id = rs.getInt("document_id");
                    document = documents.stream()
                        .filter(a -> a.getId() == document_id)
                        .findFirst()
                        .orElse(null);

                    String realEndDateStr = rs.getString("real_end_date");
                    LocalDate real_end_date = null;
                    if (realEndDateStr != null) {
                        real_end_date = LocalDate.parse(realEndDateStr);
                    } 

                    borrowings_per_member.get(member).add(
                        new Borrowing(rs.getInt("id"), 
                                        document, 
                                        member, 
                                        LocalDate.parse(rs.getString("start_date")), 
                                        LocalDate.parse(rs.getString("expected_end_date")), 
                                        real_end_date
                        )
                    );
                }
            }
            return borrowings_per_member;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean create_borrowing(Connection conn, Borrowing new_borrowing) {
        String checkCopies = "SELECT nb_copies FROM documents WHERE id = ?";
        String insertBorrowing = "INSERT INTO borrowings (id, document_id, person_id, start_date, expected_end_date, real_end_date) VALUES (?, ?, ?, ?, ?, ?)";
        String updateCopies = "UPDATE documents SET nb_copies = nb_copies - 1 WHERE id = ?";

        try {
            //  Vérifier s’il reste des exemplaires disponibles
            PreparedStatement checkStmt = conn.prepareStatement(checkCopies);
            checkStmt.setInt(1, new_borrowing.getDocument().getId());
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next() || rs.getInt("nb_copies") <= 0) {
                System.out.println("No copies available for this document.");
                return false;
            }

            //  Insérer le nouvel emprunt
            PreparedStatement pstmt = conn.prepareStatement(insertBorrowing);
            pstmt.setInt(1, new_borrowing.getId_borrowing());
            pstmt.setInt(2, new_borrowing.getDocument().getId());
            pstmt.setInt(3, new_borrowing.getMember().getId());
            pstmt.setString(4, new_borrowing.getBorrowing_date().toString());
            pstmt.setString(5, new_borrowing.getReturn_date().toString());
            if (new_borrowing.getReal_return_date() != null) {
                pstmt.setString(6, new_borrowing.getReal_return_date().toString());
            } else {
                pstmt.setNull(6, java.sql.Types.DATE);
            }
            pstmt.executeUpdate();

            //  Décrémenter le nombre de copies
            PreparedStatement psCopies = conn.prepareStatement(updateCopies);
            psCopies.setInt(1, new_borrowing.getDocument().getId());
            psCopies.executeUpdate();

            //  Recharger les données en mémoire
            ApplicationFX.refreshAll();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean finish_borrowing(Connection conn,Borrowing borrowing){ 
        String sql = "UPDATE borrowings SET real_end_date = ? WHERE id = ?";
        String updateCopies = "UPDATE documents SET nb_copies = nb_copies + 1 WHERE id = ?";

        try {
            String sql_borrowing = "SELECT * FROM borrowings WHERE id = " + borrowing.getId_borrowing() + ";";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql_borrowing);

            if (!rs.next()) {
                System.out.println("Error: Impossible to finish the borrowing, it doesn't exist!");
                return false;
            }

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, borrowing.getReal_return_date().toString());
            pstmt.setInt(2, borrowing.getId_borrowing());

            pstmt.executeUpdate();

            // nb_copies increment
            PreparedStatement psCopies = conn.prepareStatement(updateCopies);
            psCopies.setInt(1, borrowing.getDocument().getId());
            psCopies.executeUpdate();

            ApplicationFX.refreshAll();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean add_magazine(Connection conn, Magazine new_magazine){
        String sql = "INSERT INTO magazines (magazine_id, magazine_title, periodicity) VALUES (?, ?, ?)";

        try {
            // First we are checking whether we are not adding an already existing magazine
            Statement stmt = conn.createStatement();

            // Check whether a magazine with this magazine_id already exists
            ResultSet rs = stmt.executeQuery("SELECT * FROM magazines WHERE magazine_id = " + new_magazine.getMagazine_id() + ";");

            if (rs.next()) {
                // rs.next() returns true if there is at least one row
                System.out.println("Error: Impossible to add the magazine, it already exists!");
                return false;
            }

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, new_magazine.getMagazine_id());
            pstmt.setString(2, new_magazine.getMagazine_title());
            pstmt.setString(3, Periodicity.convertPeriodicityToString(new_magazine.getPeriodicity()));
            pstmt.executeUpdate();

            ApplicationFX.refreshAll();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean remove_magazine(Connection conn, Magazine magazine_to_remove){ 
        try {
            String sql_magazine = "SELECT * FROM magazines WHERE magazines.magazine_id = " + magazine_to_remove.getMagazine_id() + ";";
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql_magazine);
            if(!rs.next()){
                System.out.println("Error: Impossible to remove the magazine, it doesn't exist!");
                return false;
            }
            String sql_documents_deletion =  "DELETE FROM documents WHERE id IN (SELECT id FROM magazine_numbers WHERE magazine_id = "+magazine_to_remove.getMagazine_id()+");";
            
            Statement stmt2 = conn.createStatement();
            stmt2.executeUpdate(sql_documents_deletion); 
            
            String sql_magazine_deletion = "DELETE FROM magazines WHERE magazines.magazine_id = " + magazine_to_remove.getMagazine_id() + ";";
            
            stmt2 = conn.createStatement();
            stmt2.executeUpdate(sql_magazine_deletion); 

            ApplicationFX.refreshAll();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();  
            return false;
        }
    }

    // depend de add_document
    public static boolean add_book(Connection conn, Book new_book){
        String sql = "INSERT INTO documents (id, title, nb_copies) VALUES (?, ?, ?)";

        try {
            Statement stmt = conn.createStatement();

            // Check whether a Document with this id already exists
            ResultSet rs = stmt.executeQuery("SELECT * FROM documents WHERE id = " + new_book.getId() + ";");

            if (rs.next()) {
                // rs.next() returns true if there is at least one row
                System.out.println("Error: Impossible to add the document, it already exists!");
                return false;
            }

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, new_book.getId());
            pstmt.setString(2, new_book.getTitle());
            pstmt.setInt(3, new_book.getNb_copies());
            pstmt.executeUpdate();

            for (Author author: new_book.getAuthors()){
                add_author(conn, author) ; // We add the author if he doesn't exist, if he exists it will just print an error message but it won't stop the execution
                // Once the author is added, we can insert a line indicating that he wrote a document we are inserting
                String sql_author_insertion = "INSERT INTO documents_authors (document_id, author_id) VALUES (?, ?)";

                PreparedStatement pstmt_author_document_insertion = conn.prepareStatement(sql_author_insertion);
                pstmt_author_document_insertion.setInt(1, new_book.getId());
                pstmt_author_document_insertion.setInt(2, author.getId());
                pstmt_author_document_insertion.executeUpdate();
            }

            for (Genre genre: new_book.getGenre()){
                String sql_genre_insertion = "INSERT INTO document_genres (document_id, genre) VALUES (?, ?)";
                PreparedStatement pstmt2 = conn.prepareStatement(sql_genre_insertion);
                pstmt2.setInt(1, new_book.getId());
                pstmt2.setString(2, Genre.convertGenreToString(genre));
                pstmt2.executeUpdate();
            }

            String sql2 = "INSERT INTO books (id, isbn, pages_number, year) VALUES (?, ?, ?, ?)";

            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            pstmt2.setInt(1, new_book.getId());
            pstmt2.setString(2, new_book.getIsbn());
            pstmt2.setInt(3, new_book.getNb_pages());
            pstmt2.setInt(4, new_book.getYear());
            pstmt2.executeUpdate();

            ApplicationFX.refreshAll();
            return true;    
        } catch (SQLException e) {
            e.printStackTrace();        
            return false;
        }
        
    }
    public static boolean remove_book(Connection conn, Book book_to_remove){ 

        try{
            String sql_magazine = "SELECT * FROM books WHERE id = " + book_to_remove.getId() + ";";
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql_magazine);
            if(!rs.next()){
                System.out.println("Error: Impossible to remove the book, it doesn't exist!");
                return false;
            }
            String sql_document_deletion =  "DELETE FROM documents WHERE id  = "+ book_to_remove.getId()+";";
            
            Statement stmt2 = conn.createStatement();
            stmt2.executeUpdate(sql_document_deletion); 
           
            ApplicationFX.refreshAll();
            return true;  
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    // depend de add_document
    public static boolean add_magazine_issue(Connection conn, MagazineIssue new_magazine_issue){
        String sql = "INSERT INTO documents (id, title, nb_copies) VALUES (?, ?, ?)";

        try {
            Statement stmt = conn.createStatement();

            // Check whether a Document with this id already exists
            ResultSet rs = stmt.executeQuery("SELECT * FROM documents WHERE id = " + new_magazine_issue.getId() + ";");

            if (rs.next()) {
                // rs.next() returns true if there is at least one row
                System.out.println("Error: Impossible to add the document, it already exists!");
                return false;
            }

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, new_magazine_issue.getId());
            pstmt.setString(2, new_magazine_issue.getTitle());
            pstmt.setInt(3, new_magazine_issue.getNb_copies());
            pstmt.executeUpdate();

            for (Author author: new_magazine_issue.getAuthors()){
                add_author(conn, author) ; // We add the author if he doesn't exist, if he exists it will just print an error message but it won't stop the execution
                // Once the author is added, we can insert a line indicating that he wrote a document we are inserting
                String sql_author_insertion = "INSERT INTO documents_authors (document_id, author_id) VALUES (?, ?)";

                PreparedStatement pstmt_author_document_insertion = conn.prepareStatement(sql_author_insertion);
                pstmt_author_document_insertion.setInt(1, new_magazine_issue.getId());
                pstmt_author_document_insertion.setInt(2, author.getId());
                pstmt_author_document_insertion.executeUpdate();
            }

            for (Genre genre: new_magazine_issue.getGenre()){
                String sql_genre_insertion = "INSERT INTO document_genres (document_id, genre) VALUES (?, ?)";
                PreparedStatement pstmt2 = conn.prepareStatement(sql_genre_insertion);
                pstmt2.setInt(1, new_magazine_issue.getId());
                pstmt2.setString(2, Genre.convertGenreToString(genre));
                pstmt2.executeUpdate();
            }

            String sql2 =  "INSERT INTO magazine_numbers (id, magazine_id, issue_number, issue_date) VALUES (?, ?, ?, ?)";

            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            pstmt2.setInt(1, new_magazine_issue.getId());
            pstmt2.setInt(2, new_magazine_issue.getMagazine().getMagazine_id());
            pstmt2.setInt(3, new_magazine_issue.getIssue_number());
            pstmt2.setString(4, new_magazine_issue.getIssue_date().toString());
            pstmt2.executeUpdate();

            ApplicationFX.refreshAll();
            return true;    
        } catch (SQLException e) {
            e.printStackTrace();        
            return false;
        }
    }

    public static boolean remove_magazine_issue(Connection conn, MagazineIssue magazine_issue_to_remove){ 
        try {
            String sql_magazine = "SELECT * FROM magazine_numbers WHERE id = " + magazine_issue_to_remove.getId() + ";";
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql_magazine);
            if(!rs.next()){
                System.out.println("Error: Impossible to remove the magazine issue, it doesn't exist!");
                return false;
            }

            String sql_documents_deletion =  "DELETE FROM documents WHERE id = "+magazine_issue_to_remove.getId()+";";
            
            Statement stmt2 = conn.createStatement();
            stmt2.executeUpdate(sql_documents_deletion); 
            
            Magazine magazine =  magazine_issue_to_remove.getMagazine();
            String sql = "SELECT * FROM magazine_numbers WHERE magazine_id = " + magazine.getMagazine_id() + ";";

            stmt2 = conn.createStatement();
            ResultSet rs2 = stmt2.executeQuery(sql);
            if(!rs2.next()){
                String sql_magazine_deletion = "DELETE FROM magazines WHERE magazines.magazine_id = " + magazine.getMagazine_id() + ";";

                
                stmt2 = conn.createStatement();
                stmt2.executeUpdate(sql_magazine_deletion);
            
            }

            ApplicationFX.refreshAll();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();  
            return false;
        }
    }

    
    public static boolean add_document(Connection conn, Document new_document){ 
        if (new_document instanceof Book new_book) {
            return add_book(conn, new_book);

        } else if (new_document instanceof MagazineIssue new_magazine_issue) {
            return add_magazine_issue(conn, new_magazine_issue);
        }
        return false;
    }

    public static boolean remove_document(Connection conn, Document document_to_remove){ // 
        if (document_to_remove instanceof Book book_to_remove) {
            return remove_book(conn, book_to_remove);

        } else if (document_to_remove instanceof MagazineIssue magazine_issue_to_remove) {
            return remove_magazine_issue(conn, magazine_issue_to_remove);
        }
        return false;
    }

    public static boolean update_member(Connection conn, Member member, String new_password){ // If we don't want to change the password: new_password = null otherwise it is already hashed
        try {
            // First we are checking whether this member exists
            String checkQuery = "SELECT id FROM members WHERE id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, member.getId());
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {
                System.out.println("Error: member not found in the database");
                return false;
            }

            String update_people = "UPDATE people SET first_name = ?, last_name = ?, birth_date = ? WHERE id = ?";
            String update_members = new_password == null
                ? "UPDATE members SET penalty_status = ?, phone_number = ?, address = ?, mail = ? WHERE id = ?"
                : "UPDATE members SET penalty_status = ?, phone_number = ?, address = ?, mail = ?, password = ? WHERE id = ?";

            PreparedStatement pstmt_people = conn.prepareStatement(update_people);
            pstmt_people.setString(1, member.getFirst_name());
            pstmt_people.setString(2, member.getLast_name());
            pstmt_people.setString(3, member.getBirth_date().toString());
            pstmt_people.setInt(4, member.getId());
            pstmt_people.executeUpdate();

            PreparedStatement pstmt_members = conn.prepareStatement(update_members);
            pstmt_members.setInt(1, member.getPenalty_status() ? 1 : 0);
            pstmt_members.setString(2, member.getPhone_number());
            pstmt_members.setString(3, member.getAddress());
            pstmt_members.setString(4, member.getMail());

            if (new_password == null) {
                pstmt_members.setInt(5, member.getId());
            } else {
                pstmt_members.setString(5, new_password);
                pstmt_members.setInt(6, member.getId());
            }

            pstmt_members.executeUpdate();

            ApplicationFX.refreshAll();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean add_member(Connection conn, Member new_member, String password){
        try {
            // First we are checking whether this member aleady exists
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM people WHERE id = " + new_member.getId() + ";");
            if (rs.next()) {
                // rs.next() returns true if there is at least one row
                System.out.println("Error: Impossible to add the member, it already exists!");
                return false;
            }

            // Insertion into people table
            String sql_people = "INSERT INTO people (id, first_name, last_name, birth_date) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt_people_insertion = conn.prepareStatement(sql_people);
            pstmt_people_insertion.setInt(1, new_member.getId());
            pstmt_people_insertion.setString(2, new_member.getFirst_name());
            pstmt_people_insertion.setString(3, new_member.getLast_name());
            pstmt_people_insertion.setString(4, new_member.getBirth_date().toString());
            pstmt_people_insertion.executeUpdate();

            // Insertion into members table
            String sql_members = "INSERT INTO members (id, penalty_status, phone_number, address, mail, password) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql_members);
            pstmt.setInt(1, new_member.getId());
            pstmt.setInt(2, new_member.getPenalty_status() ? 1 : 0);
            pstmt.setString(3, new_member.getPhone_number());
            pstmt.setString(4, new_member.getAddress());
            pstmt.setString(5, new_member.getMail());
            pstmt.setString(6, password);
            pstmt.executeUpdate();

            ApplicationFX.refreshAll();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean remove_member(Connection conn,Member member_to_remove){ 
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM members WHERE id = "+ member_to_remove.getId()+";");
 
            if(!rs.next()){
                System.out.println("Error: Impossible to remove this member, it doesn't exist!");
                return false;
            }

            Statement stmt2 = conn.createStatement();
            stmt2.executeUpdate("DELETE FROM people WHERE id = " + member_to_remove.getId() + ";");
            
            ApplicationFX.refreshAll();
            return true;

        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean add_author(Connection conn, Author new_author){
        try {
            // First we are checking whether this author aleady exists
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM people WHERE id = " + new_author.getId() + ";");
            if (rs.next()) {
                // rs.next() returns true if there is at least one row
                System.out.println("Error: Impossible to add the author, it already exists!");
                return false;
            }

            String sql = "INSERT INTO people (id, first_name, last_name, birth_date) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, new_author.getId());
            pstmt.setString(2, new_author.getFirst_name());
            pstmt.setString(3, new_author.getLast_name());
            pstmt.setString(4, new_author.getBirth_date().toString());
            pstmt.executeUpdate();

            ApplicationFX.refreshAll();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean remove_author(Connection conn, Author author_to_remove){ 
        try{

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM people WHERE id = "+ author_to_remove.getId()+";");
 
            if(!rs.next()){
                System.out.println("Error: Impossible to remove this author, it doesn't exist!");
                return false;
            }

            Statement stmt2 = conn.createStatement();
            stmt2.executeUpdate("DELETE FROM people WHERE id = " + author_to_remove.getId() + ";");
            
            ApplicationFX.refreshAll();
            return true;


        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

}
