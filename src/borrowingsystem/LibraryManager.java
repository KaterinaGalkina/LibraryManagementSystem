package borrowingsystem;

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

import documents.Book;
import documents.Document;
import documents.Magazine;
import documents.MagazineIssue;
import documents.Periodicity;
import documents.Genre;
import people.Author;
import people.Member;

public class LibraryManager {

    public static ArrayList<Author> get_authors(Connection conn){
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM people p WHERE NOT EXISTS (SELECT 1 FROM members m WHERE m.id = p.id);");
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

    public static ArrayList<Document> get_documents(Connection conn, ArrayList<Author> authors, ArrayList<Magazine> magazines){ 
        try {
            Statement stmt = conn.createStatement();

            // First, we are retrieving all books
            ResultSet rs_books = stmt.executeQuery("SELECT * FROM documents INNER JOIN books ON documents.id = books.id;");
            ArrayList<Document> documents = new ArrayList<>();

            while (rs_books.next()) {
                int book_id = rs_books.getInt("id");
                String book_title = rs_books.getString("title");
                int book_nb_copies = rs_books.getInt("nb_copies");
                String book_isbn = rs_books.getString("isbn");
                int book_pages_number = rs_books.getInt("pages_number");
                int book_year = rs_books.getInt("year");

                stmt = conn.createStatement();
                ResultSet rs_genres = stmt.executeQuery("SELECT * FROM document_genres INNER JOIN documents ON documents.id = document_genres.document_id AND documents.id = " + book_id + ";");

                Set<Genre> genre_of_of_the_current_book = new HashSet<>();
                while (rs_genres.next()) {
                    genre_of_of_the_current_book.add(Genre.convertStringToGenre(rs_genres.getString("genre")));
                }

                stmt = conn.createStatement();
                ResultSet rs_authors = stmt.executeQuery("SELECT * FROM documents_authors INNER JOIN documents ON documents.id = documents_authors.document_id AND documents.id = " + book_id + ";");

                Set<Author> authors_of_the_current_book = new HashSet<>();

                while (rs_authors.next()) {
                    int id =rs_authors.getInt("author_id");
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

                documents.add(
                    new Book(
                        genre_of_of_the_current_book,
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

            // Then, we are taking care of magazines
            stmt = conn.createStatement();
            // First, we are retrieving periodicity of each magazine:
            ResultSet rs_magazines_periodicity = stmt.executeQuery("SELECT * FROM magazines INNER JOIN magazine_numbers ON magazines.magazine_id = magazine_numbers.magazine_id;");
            
            // Association of each magazine_id with the corresponding periodicity
            HashMap<Integer, Periodicity> periodicidy_by_magazine = new HashMap<>();
            while (rs_magazines_periodicity.next()) {
                periodicidy_by_magazine.put(rs_magazines_periodicity.getInt("magazine_id"), Periodicity.convertStringToPeriodicity(rs_magazines_periodicity.getString("periodicity")));
            }

            stmt = conn.createStatement();
            ResultSet rs_magazines = stmt.executeQuery("SELECT * FROM documents INNER JOIN magazine_numbers ON documents.id = magazine_numbers.id;");

            while (rs_magazines.next()) {
                int magazineIssue_id = rs_magazines.getInt("id");
                int magazine_id = rs_magazines.getInt("magazine_id");

                Statement stmt2 = conn.createStatement();
                ResultSet rs_genres = stmt2.executeQuery("SELECT * FROM document_genres INNER JOIN documents ON documents.id = document_genres.document_id AND documents.id = " + magazineIssue_id + ";");

                Set<Genre> genre_of_of_the_current_magazine = new HashSet<>();
                while (rs_genres.next()) {
                    genre_of_of_the_current_magazine.add(Genre.convertStringToGenre(rs_genres.getString("genre")));
                }

                Statement stmt3 = conn.createStatement();
                ResultSet rs_authors = stmt3.executeQuery("SELECT * FROM documents_authors INNER JOIN documents ON documents.id = documents_authors.document_id AND documents.id = " + magazineIssue_id + ";");

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

                Magazine magazine = magazines.stream()
                        .filter(a -> a.getMagazine_id() == magazine_id)
                        .findFirst()
                        .orElse(null);

                documents.add(
                    new MagazineIssue(
                        genre_of_of_the_current_magazine,
                        rs_magazines.getString("title"), 
                        rs_magazines.getInt("nb_copies"), 
                        magazineIssue_id,
                        authors_of_the_current_magazine,
                        rs_magazines.getInt("issue_number"),
                        LocalDate.parse(rs_magazines.getString("issue_date")),
                        magazine
                    )
                );
            }

            return documents;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HashMap<Member, HashSet<Borrowing>> get_borrowings(Connection conn, ArrayList<Member> members, ArrayList<Document> documents){ 
        try {
            Statement stmt = conn.createStatement();
            HashMap<Member, HashSet<Borrowing>> borrowings_per_member = new HashMap<>();
            Document document;
            for(Member member: members){
                borrowings_per_member.put(member, new HashSet<>());
                stmt = conn.createStatement();

                // ResultSet rs = stmt.executeQuery("SELECT * FROM borrowings INNER JOIN documents ON documents.id = borrowings.document_id WHERE borrowings.person_id = " + member.getId() + ";");
                ResultSet rs = stmt.executeQuery("SELECT borrowings.id as borrowing_id, borrowings.document_id, borrowings.person_id, borrowings.start_date, borrowings.expected_end_date, borrowings.real_end_date FROM borrowings INNER JOIN documents ON documents.id = borrowings.document_id WHERE borrowings.person_id = " + member.getId() + ";");
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
                            new Borrowing(rs.getInt("borrowing_id"), 
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
        String sql = "INSERT INTO borrowings (id, document_id, person_id, start_date, expected_end_date, real_end_date) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
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
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean finish_borrowing(Borrowing borrowing){ // TODO
        return false;
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
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean remove_magazine(Connection conn, Magazine magazine_to_remove){ //TODO
        return false;
    }

    public static boolean add_document(Connection conn, Document new_document){ 
        String sql = "INSERT INTO documents (id, title, nb_copies) VALUES (?, ?, ?)";

        try {
            Statement stmt = conn.createStatement();

            // Check whether a Document with this id already exists
            ResultSet rs = stmt.executeQuery("SELECT * FROM documents WHERE id = " + new_document.getId() + ";");

            if (rs.next()) {
                // rs.next() returns true if there is at least one row
                System.out.println("Error: Impossible to add the document, it already exists!");
                return false;
            }

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, new_document.getId());
            pstmt.setString(2, new_document.getTitle());
            pstmt.setInt(3, new_document.getNb_copies());
            pstmt.executeUpdate();

            for (Author author: new_document.getAuthors()){
                Statement stmt_existance_of_author = conn.createStatement();

                // Check whether an author exists in the people table, to be able to make association in the table documents_authors
                ResultSet rs_author = stmt_existance_of_author.executeQuery("SELECT * FROM people WHERE id = " + author.getId() + ";");

                if (!rs_author.next()) { // We need to add it
                    String sql_author_insertion = "INSERT INTO people (id, first_name, last_name, birth_date) VALUES (?, ?, ?, ?)";

                    PreparedStatement pstmt_author_insertion = conn.prepareStatement(sql_author_insertion);
                    pstmt_author_insertion.setInt(1, author.getId());
                    pstmt_author_insertion.setString(2, author.getFirst_name());
                    pstmt_author_insertion.setString(3, author.getLast_name());
                    pstmt_author_insertion.setString(4, author.getBirth_date().toString());
                    pstmt_author_insertion.executeUpdate();
                }

                // Once the author is added, we can insert a line indicating that he wrote a document we are inserting
                String sql_author_insertion = "INSERT INTO documents_authors (document_id, author_id) VALUES (?, ?)";

                PreparedStatement pstmt_author_document_insertion = conn.prepareStatement(sql_author_insertion);
                pstmt_author_document_insertion.setInt(1, new_document.getId());
                pstmt_author_document_insertion.setInt(2, author.getId());
                pstmt_author_document_insertion.executeUpdate();
            }

            for (Genre genre: new_document.getGenre()){
                String sql_genre_insertion = "INSERT INTO document_genres (document_id, genre) VALUES (?, ?)";
                PreparedStatement pstmt2 = conn.prepareStatement(sql_genre_insertion);
                pstmt2.setInt(1, new_document.getId());
                pstmt2.setString(2, Genre.convertGenreToString(genre));
                pstmt2.executeUpdate();
            }

            if (new_document instanceof Book new_book) {
                String sql2 = "INSERT INTO books (id, isbn, pages_number, year) VALUES (?, ?, ?, ?)";

                PreparedStatement pstmt2 = conn.prepareStatement(sql2);
                pstmt2.setInt(1, new_book.getId());
                pstmt2.setString(2, new_book.getIsbn());
                pstmt2.setInt(3, new_book.getNb_pages());
                pstmt2.setInt(4, new_book.getYear());
                pstmt2.executeUpdate();

            } else if (new_document instanceof MagazineIssue new_magazine_issue) {
                // Check whether a Magazine already exists oif not, then we create it
                stmt = conn.createStatement();
                ResultSet rs2 = stmt.executeQuery("SELECT * FROM magazines WHERE magazine_id = " + new_magazine_issue.getMagazine().getMagazine_id() + ";");

                if (!rs2.next()) {
                    // rs2.next() returns false then there is no such magazine in the system, so we have to add it
                    String sql2 = "INSERT INTO magazine (magazine_id, periodicity) VALUES (?, ?)";
                    PreparedStatement pstmt2 = conn.prepareStatement(sql2);
                    pstmt2.setInt(1, new_magazine_issue.getMagazine().getMagazine_id());
                    pstmt2.setString(2, Periodicity.convertPeriodicityToString(new_magazine_issue.getMagazine().getPeriodicity()));
                    pstmt2.executeUpdate();
                }

                // We know that it doesn't exist since we aleardy checked the document id in the documents table which is primary key
                String sql3 = "INSERT INTO magazine_numbers (id, magazine_id, issue_number, issue_date) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt3 = conn.prepareStatement(sql3);
                pstmt3.setInt(1, new_magazine_issue.getId());
                pstmt3.setInt(2, new_magazine_issue.getMagazine().getMagazine_id());
                pstmt3.setInt(3, new_magazine_issue.getIssue_number());
                pstmt3.setString(4, new_magazine_issue.getIssue_date().toString());
                pstmt3.executeUpdate();
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean remove_document(Document document_to_remove){ // TODO
        return false;
    }

    public static boolean create_member(Connection conn, Member new_member){
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
            String sql_members = "INSERT INTO members (id, penalty_status, phone_number, address, mail) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql_members);
            pstmt.setInt(1, new_member.getId());
            pstmt.setInt(2, new_member.getPenalty_status() ? 1 : 0);
            pstmt.setString(3, new_member.getPhone_number());
            pstmt.setString(4, new_member.getAddress());
            pstmt.setString(5, new_member.getMail());
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean remove_member(Member member_to_remove){ // TODO
        return false;
    }
}
