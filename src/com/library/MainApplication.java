package com.library;
import com.library.ui.ApplicationFX;

public class MainApplication {
    public static void main(String[] args) {
        
        // String url = "jdbc:sqlite:database/library_info.db"; 
        

        try {
            ApplicationFX.ui(args);
            // Connection conn = DriverManager.getConnection(url);
            
            // if (conn != null) {
            //     System.out.println("Connected to the database.");
            // } else {
            //     System.out.println("Failed to connect to the database.");
            //     return;
            // }
            // // Activation des FK de la database
            // var st = conn.createStatement();
            // st.execute("PRAGMA foreign_keys = ON");
            // st.close();

            
            // // Member m1 = new Member("Kate", "Galkina", LocalDate.of(2004, 10, 10), "+330611879072", "121, rue de Bellevue", "ekaterina.galkina@outlook.com");
            // // LibraryManager.add_member(conn, m1, LoginView.hashPassword("SuperStrongPassword"));
            // // Member m2 = new Member("John", "Doe", LocalDate.of(1990, 5, 15), "+330612345678", "123, rue de Paris", "john@gmail.com");

            // // Author a1 = new Author("George", "Orwell", LocalDate.of(1903, 6, 25));
            // // Author a2 = new Author("Alison", "George", LocalDate.of(1988, 1, 3));
            // Author a3 = new Author("Isaac", "Asimov", LocalDate.of(1920, 1, 2));

            // //Book b1 = new Book(new HashSet<>(Arrays.asList(Genre.SCIENCE_FICTION)), "Foundation", 1, new HashSet<>(Arrays.asList(a3)), "9780553293357", 255, 1951);
            //Book b2 = new Book(new HashSet<>(Arrays.asList(Genre.FICTION)), "Animal Farm", 2, new HashSet<>(Arrays.asList(a1)), "9780194267533", 176, 1945);
            //Book b3 = new Book(new HashSet<>(Arrays.asList(Genre.ROMANCE, Genre.LITERATURE)), "The Hunger Games", 3, new HashSet<>(Arrays.asList(a2)), "9780439023528", 374, 2008);

            //  Magazine magazine1 = new Magazine("New Scientist", Periodicity.WEEKLY);
            //  Magazine magazine2 = new Magazine("Time", Periodicity.WEEKLY);
            //  Magazine magazine3 = new Magazine("National Geographic", Periodicity.MONTHLY);

            // MagazineIssue magazineIssue1 = new MagazineIssue(new HashSet<>(Arrays.asList(Genre.SCIENCE_FICTION)), "Science fiction special: The future of a genre", 3,3, new HashSet<>(Arrays.asList(a2)), 875, LocalDate.of(2008, 11, 12), magazine1);
            // MagazineIssue magazineIssue2 = new MagazineIssue(new HashSet<>(Arrays.asList(Genre.SCIENCE)), "The science of climate change", 5,4, new HashSet<>(Arrays.asList(a3)), 1023, LocalDate.of(2020, 6, 1), magazine2);
            // MagazineIssue magazineIssue3 = new MagazineIssue(new HashSet<>(Arrays.asList(Genre.ENVIRONMENT)), "Exploring the Amazon rainforest", 2,5, new HashSet<>(Arrays.asList(a1)), 678, LocalDate.of(2019, 9, 15), magazine3);

            // Borrowing borrowing1 = new Borrowing(b1,m1,LocalDate.of(2023, 10, 1), LocalDate.of(2023, 10, 15));
            // Borrowing borrowing2 = new Borrowing(magazineIssue1,m2,LocalDate.of(2023, 9, 20));
            // Borrowing borrowing3 = new Borrowing(b3,m1,LocalDate.of(2023, 8, 5), LocalDate.of(2023, 8, 26));
            // Borrowing borrowing4 = new Borrowing(b2,m1,LocalDate.of(2023, 10, 20), LocalDate.of(2023, 11, 10));

            // ArrayList<Author> authors = new ArrayList<>(); 
            //ArrayList<Member> members = new ArrayList<>();
            // ArrayList<Magazine> magazines =new ArrayList<>(); 
            // ArrayList<Document> documents = new ArrayList<>(); 
            // HashMap<Member,HashSet<Borrowing>> borrowings = new HashMap<>();

            // magazines.add(magazine1);
            // magazines.add(magazine2);
            // magazines.add(magazine3);

            // members.add(m1);
            // members.add(m2);
            
            // authors.add(a1);
            //authors.add(a2);
            //authors.add(a3);

            //  documents.add(magazineIssue1);
            //  documents.add(magazineIssue2);
            //  documents.add(magazineIssue3);
            //documents.add(b2);
            //documents.add(b3);
            //documents.add(b1);
            // borrowings.put(m1, new HashSet<>(Arrays.asList(borrowing1, borrowing3, borrowing4)));
            // borrowings.put(m2, new HashSet<>(Arrays.asList(borrowing2)));
            
            // for(Member m: members){
            //     LibraryManager.add_member(conn, m);
            // }
            // for(Magazine m: magazines){
            //     LibraryManager.add_magazine(conn, m);
            // }
            // for(Document d: documents){ 
            //    LibraryManager.add_document(conn, d);
            // }
            
            
            // for(Member m: borrowings.keySet()){
            //     for(Borrowing b: borrowings.get(m)){
            //         LibraryManager.create_borrowing(conn, b);
            //     }
            // }


            // authors = LibraryManager.get_authors(conn);
            // members =  LibraryManager.get_members(conn);
            // magazines = LibraryManager.get_magazines(conn);
            // documents = LibraryManager.get_documents(conn, authors, magazines);
            // borrowings = LibraryManager.get_borrowings(conn, members, documents);

            // HashMap<Integer, HashSet<Borrowing>> borrowingsByMemberId = new HashMap<>();
            // for (Member m : borrowings.keySet()) {
            //     borrowingsByMemberId.put(m.getId(), borrowings.get(m));
            // }
            
            // System.out.println("Existing members:");
            // for(Member m: members){
            //     System.out.println("\t" + m.toString());
            // }

            // System.out.println("Existing authors:");
            // for(Author a: authors){
            //     System.out.println("\t" + a.toString());
            // }

            // System.out.println("Existing magazines:");
            // for(Magazine m: magazines){
            //     System.out.println("\t" + m.toString());
            // }

            // System.out.println("Existing documents:");
            // for(Document d: documents){
            //     System.out.println("\t" + d.toString());
            // }


            // System.out.println("Existing borrowings:");
            // for (Member m : members) {
            //     HashSet<Borrowing> set = borrowingsByMemberId.get(m.getId());
            //     if (set == null || set.isEmpty()) continue;

            //     System.out.println("For memberId=" + m.getId() + " (" + m.toString() + ") : ");
            //     for (Borrowing b : set) {
            //         System.out.println("\t- " + b.toString());
            //     }
            // }

            /* 
            System.out.println("Existing borrowings:");
            for(Member m: borrowings.keySet()){
                System.out.println("For " + m.toString() + " : ");
                for(Borrowing b: borrowings.get(m)){
                    System.out.println("\t- " + b.toString() + "");
                }
            }*/

            // Example: finishing a borrowing
            // System.out.println("\nFinishing borrowing of " + borrowing2.toString() );
            // borrowing2.setReal_return_date(LocalDate.of(2023, 10, 5));
            // LibraryManager.finish_borrowing(conn, borrowing2);


        //     borrowings = LibraryManager.get_borrowings(conn, members, documents);
        //    // ---> NOUVEAU : reconstruire la map par id
        //     HashMap<Integer, HashSet<Borrowing>> borrowingsByMemberId2 = new HashMap<>();
        //     for (Member m : borrowings.keySet()) {
        //         borrowingsByMemberId2.put(m.getId(), borrowings.get(m));
        //     }

        //     // Afficher par id (au lieu d'utiliser l'objet Member initial)
        //     int memberId = borrowing2.getMember().getId();
        //     System.out.println("Updated borrowings for memberId=" + memberId + " : ");
        //     HashSet<Borrowing> updatedSet = borrowingsByMemberId2.get(memberId);
        //     if (updatedSet != null && !updatedSet.isEmpty()) {
        //         for (Borrowing b : updatedSet) {
        //             System.out.println("\t- " + b.toString());
        //         }
        //     } else {
        //         System.out.println("\t(no borrowings)");
        //     }
        //     // Example: removing a member
        //     System.out.println("\nRemoving member " + m2.toString());
        //     LibraryManager.remove_member(conn, m2);
        //     members =  LibraryManager.get_members(conn);
        //     System.out.println("Updated members:");
        //     for(Member m: members){
        //         System.out.println("\t" + m.toString());
        //     }
        //     // Example: removing a book
        //     System.out.println("\nRemoving book " + b2.toString());     
        //     LibraryManager.remove_book(conn, b2);
        //     documents = LibraryManager.get_documents(conn, authors, magazines);
        //     System.out.println("Updated documents:");
        //     for(Document d: documents){
        //         System.out.println("\t" + d.toString());
        //     }

        //     // Example: removing a magazine issue
        //     System.out.println("\nRemoving magazine issue " + magazineIssue1.toString());
        //     LibraryManager.remove_magazine_issue(conn, magazineIssue1);
        //     documents = LibraryManager.get_documents(conn, authors, magazines);
        //     System.out.println("Updated documents:");
        //     for(Document d: documents){
        //         System.out.println("\t" + d.toString());
        //     }

        //     // Example: removing an author
        //     System.out.println("\nRemoving author " + a2.toString());
        //     LibraryManager.remove_author(conn, a2);
        //     authors = LibraryManager.get_authors(conn);
        //     documents = LibraryManager.get_documents(conn, authors, magazines);
        //     System.out.println("Updated authors:");
        //     for(Author a: authors){
        //         System.out.println("\t" + a.toString());
        //     }
           
        //     // Also check documents
        //     System.out.println("Updated documents:");   
        //     for(Document d: documents){
        //         System.out.println("\t" + d.toString());
        //     }

            // conn.close();
            // System.out.println("Disconnected from the database.");  
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
