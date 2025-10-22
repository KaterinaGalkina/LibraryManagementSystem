package com.library.ui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.library.borrowingsystem.Borrowing;
import com.library.borrowingsystem.LibraryManager;
import com.library.documents.*;
import com.library.people.*;
import com.library.ui.login.*;
import com.library.ui.menu.*;

import javafx.application.Application;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ApplicationFX extends Application {
    private static Connection conn; 
    private static ArrayList<Author> authors;
    private static ArrayList<Member> members;
    private static ArrayList<Magazine> magazines;
    private static ArrayList<Document> documents;
    private static HashMap<Member,HashSet<Borrowing>> borrowings;
    private static Member connected_member;

    @Override
    public void start(Stage primaryStage) {
        try {
            String url = "jdbc:sqlite:database/library_info.db";
            conn = DriverManager.getConnection(url);

            if (conn != null) {
                System.out.println("Connected to the database.");
            } else {
                System.out.println("Failed to connect to the database.");
                return;
            }

            var st = conn.createStatement();
            st.execute("PRAGMA foreign_keys = ON");
            st.close();

            // We are retrieving all the information from the database
            refreshAll();

            LoginView loginView = new LoginView();
            GridPane loginGrid = loginView.start(primaryStage, conn); // returns the form grid

            BaseView baseView = new BaseView();
            baseView.start(primaryStage, loginGrid); // wraps the form with image and shows stage

            // When the JavaFX app closes, we close DB connection
            primaryStage.setOnCloseRequest(e -> {
                try {
                    if (conn != null && !conn.isClosed()) {
                        conn.close();
                        System.out.println("Disconnected from the database.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void refreshAll(){
        try{
            // We are retrieving all the information from the database
            ApplicationFX.authors = LibraryManager.get_authors(conn);
            ApplicationFX.members = LibraryManager.get_members(conn);
            ApplicationFX.magazines = LibraryManager.get_magazines(conn);
            ApplicationFX.documents = LibraryManager.get_documents(conn, authors, magazines); 
            ApplicationFX.borrowings = LibraryManager.get_borrowings(conn, members, documents);

        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    public static ArrayList<Author> getAuthors() {
        return ApplicationFX.authors;
    }

    public static ArrayList<Member> getMembers() {
        return ApplicationFX.members;
    }

    public static ArrayList<Magazine> getMagazines() {
        return ApplicationFX.magazines;
    }

    public static ArrayList<Document> getDocuments() {
        return ApplicationFX.documents;
    }

    public static HashMap<Member, HashSet<Borrowing>> getBorrowings() {
        return ApplicationFX.borrowings;
    }

    public static Member getConnected_member() {
        return ApplicationFX.connected_member;
    }

    public static void setConnected_member(Member connected_member) {
        ApplicationFX.connected_member = connected_member;
    }

    public static void ui(String[] args) {
        launch(args);
    }
}
