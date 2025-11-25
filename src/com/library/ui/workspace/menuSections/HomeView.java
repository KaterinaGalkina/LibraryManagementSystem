package com.library.ui.workspace.menuSections;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import com.library.borrowingsystem.Borrowing;
import com.library.borrowingsystem.LibraryManager;
import com.library.documents.Book;
import com.library.documents.Document;
import com.library.documents.Genre;
import com.library.documents.MagazineIssue;
import com.library.people.Author;
import com.library.people.Member;
import com.library.ui.ApplicationFX;
import com.library.ui.generalStyling.UIStyling;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class HomeView {
    private static ArrayList<Book> top_books;
    private static ArrayList<MagazineIssue> top_magazine_issues;
    private static ArrayList<Genre> top_genre;
    private static ArrayList<Author> top_author;

    private String formatList(String title, ArrayList<?> items) {
        StringBuilder sb = new StringBuilder(title + "\n");
        int rank = 1;
        for (Object item : items) {
            sb.append("    ").append(rank++).append(". ").append(item.toString()).append("\n");
        }
        return sb.toString();
    }

    private void updateStatistics() {
        HashMap<Member, HashSet<Borrowing>> borrowings = ApplicationFX.getBorrowings();
        HashMap<Book, Integer> books_popularity = new HashMap<>();
        HashMap<MagazineIssue, Integer> magazine_issues_popularity = new HashMap<>();
        HashMap<Genre, Integer> genre_popularity = new HashMap<>();
        HashMap<Author, Integer> author_popularity = new HashMap<>();

        for (Member member : borrowings.keySet()) {
            for (Borrowing borrowing : borrowings.get(member)) {
                Document doc = borrowing.getDocument();
                // Books
                if (doc instanceof Book book) {
                    books_popularity.put(book, books_popularity.getOrDefault(book, 0) + 1);
                }
                // Magazine issues
                else if (doc instanceof MagazineIssue magazine_issue) {
                    magazine_issues_popularity.put(magazine_issue, magazine_issues_popularity.getOrDefault(magazine_issue, 0) + 1);
                }
                // Genres
                if (doc.getGenre() != null) {
                    for (Genre genre : doc.getGenre()) {
                        genre_popularity.put(genre, genre_popularity.getOrDefault(genre, 0) + 1);
                    }
                }
                // Authors
                if (doc.getAuthors() != null) {
                    for (Author author : doc.getAuthors()) {
                        author_popularity.put(author, author_popularity.getOrDefault(author, 0) + 1);
                    }
                }
            }
        }

        top_books = new ArrayList<>();
        top_magazine_issues = new ArrayList<>();
        top_genre = new ArrayList<>();
        top_author = new ArrayList<>();

        // We are extracting only top 5
        top_books.addAll(
            books_popularity.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue())) // The most popular first
                .limit(5)
                .map(Map.Entry::getKey)
                .toList()
        );

        top_magazine_issues.addAll(
            magazine_issues_popularity.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .limit(5)
                .map(Map.Entry::getKey)
                .toList()
        );

        top_genre.addAll(
            genre_popularity.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .limit(5)
                .map(Map.Entry::getKey)
                .toList()
        );

        top_author.addAll(
            author_popularity.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .limit(5)
                .map(Map.Entry::getKey)
                .toList()
        );
    }

    public ScrollPane start(Connection conn) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(10);
        grid.setHgap(20);
        grid.setPadding(new Insets(30));
        grid.setStyle("-fx-background-color: white;");

        Member connectedMember = ApplicationFX.getConnected_member();

        // Welcome message
        String welcome_message = "Welcome to the library management system !";
        if (connectedMember != null){
            welcome_message = "Welcome back, " + connectedMember.getFirst_name() + "!";
        } 
        Label main_label = new Label(welcome_message);
        main_label.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");

        int row = 0;
        grid.add(main_label, 0, row++, 2, 1);

        if (connectedMember != null){
            // Current fines info
            Label fine_label = new Label("Currently you owe the library:");
            fine_label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            // Borrowing statistics
            int nb_overdue = 0;
            int nb_soon_expiring = 0;
            int nb_regular = 0;
            LocalDate today = LocalDate.now();

            HashSet<Borrowing> allBorrowings = !ApplicationFX.getBorrowings().containsKey(connectedMember) || ApplicationFX.getBorrowings().get(connectedMember) == null ? new HashSet<>() : ApplicationFX.getBorrowings().get(connectedMember);

            for (Borrowing b : allBorrowings) {
                if (b.getReal_return_date() == null) { // active borrowing
                    if (b.getReturn_date().isBefore(today)) {
                        nb_overdue++;
                    } else if (ChronoUnit.DAYS.between(today, b.getReturn_date()) <= 5) {
                        nb_soon_expiring++;
                    } else {
                        nb_regular++;
                    }
                }
            }

            int total_borrowings = allBorrowings.size();
            int active_borrowings = nb_overdue + nb_soon_expiring + nb_regular;
            int finished_borrowings = total_borrowings - active_borrowings;

            // Summary labels
            Label borrowings_summary_label = new Label("My Borrowings summary:");
            borrowings_summary_label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            Label active_borrowings_label = new Label(" - Active borrowings: " + active_borrowings);
            Label overdue_label = new Label("    • Overdue borrowings: " + nb_overdue);
            Label soon_label = new Label("    • Soon expiring borrowings (≤5 days left): " + nb_soon_expiring);
            Label regular_label = new Label("    • Regular borrowings: " + nb_regular);
            Label finished_borrowings_label = new Label(" - Finished borrowings: " + finished_borrowings);

            grid.add(fine_label, 0, row++, 2, 1);

            VBox fines_box = get_fines_details_box(conn, connectedMember, allBorrowings);

            GridPane.setMargin(fine_label, new Insets(20, 0, 20, 0));
            grid.add(fines_box, 0, row++, 2, 1);
            GridPane.setMargin(fines_box, new Insets(0, 0, 20, 0));

            grid.add(borrowings_summary_label, 0, row++, 2, 1);
            GridPane.setMargin(borrowings_summary_label, new Insets(0, 0, 20, 0));
            grid.add(active_borrowings_label, 0, row++);
            grid.add(overdue_label, 0, row++);
            grid.add(soon_label, 0, row++);
            grid.add(regular_label, 0, row++);
            grid.add(finished_borrowings_label, 0, row++);
        }

        updateStatistics();

        Label general_summary_label = new Label("Library's general statistics of the current month:"); 
        general_summary_label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        VBox top_stats_box = new VBox(15);
        top_stats_box.setAlignment(Pos.TOP_LEFT);

        Label topBooksLabel = new Label(formatList("Top Books:", top_books));
        Label topMagazinesLabel = new Label(formatList("Top Magazine Issues:", top_magazine_issues));
        Label topGenresLabel = new Label(formatList("Top Genres:", top_genre));
        Label topAuthorsLabel = new Label(formatList("Top Authors:", top_author));

        top_stats_box.getChildren().addAll(topBooksLabel, topMagazinesLabel, topGenresLabel, topAuthorsLabel);

        grid.add(general_summary_label, 0, row++, 2, 1);
        grid.add(top_stats_box, 0, row++, 2, 1);

        GridPane.setMargin(main_label, new Insets(0, 0, 30, 0));
        GridPane.setMargin(general_summary_label, new Insets(20, 0, 20, 0));
        GridPane.setMargin(top_stats_box, new Insets(0, 0, 20, 0));

        // Scroll wrapper
        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        scrollPane.setStyle("-fx-background: white; -fx-background-color: white;");
        return scrollPane;
    }

    private VBox get_fines_details_box(Connection conn, Member connectedMember, HashSet<Borrowing> allBorrowings){
        double nb_active_overdues = 0.;
        double nb_past_overdues = 0.;

        ArrayList<Label> active_borriwings = new ArrayList<>();
        ArrayList<Label> past_borriwings = new ArrayList<>();
        ArrayList<Borrowing> unpaid_active_borrwings = new ArrayList<>();
        ArrayList<Borrowing> unpaid_past_borrwings = new ArrayList<>();

        boolean prev_status = connectedMember.getPenalty_status();
        // We are updating penalty status of the member
        connectedMember.setPenalty_status(allBorrowings);
        if (prev_status != connectedMember.getPenalty_status()){
            // update the DB accordingly
            LibraryManager.suspend_member(conn, connectedMember);
        }

        for (Borrowing b: allBorrowings) {
            double penalty = b.getPenalty();
            // We have to check whether the user has already paid the fine or not
            if (!b.getFine_paid() && penalty != 0 && b.getReal_return_date() != null){
                nb_past_overdues += penalty;
                Long daysLate = ChronoUnit.DAYS.between(b.getReturn_date(), b.getReal_return_date());
                past_borriwings.add(new Label("    • \"" + b.getDocument().getTitle() + "\", fine: " + penalty + "$\n      You were supposed to give the document back on " + b.getReturn_date() + " while you did on " + b.getReal_return_date() + " (" + daysLate + " days overdue)"));
                unpaid_past_borrwings.add(b);
            } else if (!b.getFine_paid() && penalty != 0){
                nb_active_overdues += penalty;
                LocalDate today = LocalDate.now();
                Long daysLate = ChronoUnit.DAYS.between(b.getReturn_date(), today);
                active_borriwings.add(new Label("    • \"" + b.getDocument().getTitle() + "\", fine: " + penalty + "$\n      You were supposed to give the document back on " + b.getReturn_date() + " (" + daysLate + " days overdue)"));
                unpaid_active_borrwings.add(b);
            }
        }

        Label active_overdues_label = new Label(" - Active overdues: Total fine: " + nb_active_overdues + "$");
        Label past_overdues_label = new Label(" - Past overdues: Total fine: " + nb_past_overdues + "$");

        Button pay_fine_button = new Button("Pay your fine");
        UIStyling.button_styling(pay_fine_button, "#2e2179ff", "white");

        // If there is nothing to pay, we are disabling the button
        pay_fine_button.setDisable(nb_active_overdues + nb_past_overdues == 0);

        VBox fines_box = new VBox(15);
        fines_box.setAlignment(Pos.TOP_LEFT);

        fines_box.getChildren().add(active_overdues_label);
        fines_box.getChildren().addAll(active_borriwings);
        fines_box.getChildren().add(past_overdues_label);
        fines_box.getChildren().addAll(past_borriwings);
        fines_box.getChildren().add(pay_fine_button);

        pay_fine_button.setOnAction(e -> {
            String info_message = "";
            if (unpaid_active_borrwings.size() != 0){
                info_message += "In order to pay for your active overdues, you have to first give them back !";
            }
            if (unpaid_past_borrwings.size() != 0){
                info_message = "Thank you for paying your fine !\n" + info_message;
            }

            UIStyling.alert(info_message);
            for (Borrowing b: unpaid_past_borrwings){
                b.setFine_paid(true);
                LibraryManager.update_fine_paid_borrowing(conn, b);
            }
            fines_box.getChildren().removeAll(past_borriwings);
            past_overdues_label.setText(" - Past overdues: Total fine: 0.0$");
            pay_fine_button.setDisable(unpaid_active_borrwings.size() == 0);

            boolean prev_penaltyStatus = connectedMember.getPenalty_status();
            // We are updating penalty status of the member
            connectedMember.setPenalty_status(allBorrowings);
            if (prev_penaltyStatus != connectedMember.getPenalty_status()){
                // update the DB accordingly
                LibraryManager.suspend_member(conn, connectedMember);
            }
        });

        return fines_box;
    }
}