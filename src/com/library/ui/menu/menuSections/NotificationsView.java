package com.library.ui.menu.menuSections;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;

import com.library.borrowingsystem.Borrowing;
import com.library.ui.ApplicationFX;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class NotificationsView {

    private HBox createNotificationCard(String message, String type) {
        HBox box = new HBox(10);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Circle icon = new Circle(6);
        switch (type) {
            case "overdue":
                icon.setFill(Color.RED);
                break;
            case "dueSoon":
                icon.setFill(Color.ORANGE);
                break;
            default:
                icon.setFill(Color.GRAY);
                break;
        }

        Text msg = new Text(message);
        msg.setStyle("-fx-font-size: 14px;");

        // HBox.setHgrow(msg, Priority.ALWAYS);
        box.setMinWidth(500);
        box.getChildren().addAll(icon, msg);
        return box;
    }

    public ScrollPane start(Connection conn) {
        // --- Header bar with filter ---
        Label label = new Label("📚 Notifications Section");
        label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ComboBox<String> filterBox = new ComboBox<>();
        filterBox.getItems().addAll("All", "Due soon", "Overdue");
        filterBox.setValue("All");

        HBox topBar = new HBox(20, label, filterBox);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(20));

        // --- Fetch borrowings once ---
        HashSet<Borrowing> borrowings = ApplicationFX.getBorrowings().get(ApplicationFX.getConnected_member());

        // --- Grid for notifications ---
        GridPane gridNotifications = new GridPane();
        gridNotifications.setVgap(15);
        gridNotifications.setPadding(new Insets(20));
        gridNotifications.setAlignment(Pos.TOP_CENTER);

        // --- StackPane for overlay (to center empty message) ---
        StackPane notificationsContainer = new StackPane(gridNotifications);

        // --- Helper function to populate notifications dynamically ---
        Runnable refreshNotifications = () -> {
            // Remove old cards
            gridNotifications.getChildren().clear();

            int row = 0;
            LocalDate today = LocalDate.now();

            for (Borrowing b : borrowings) {
                LocalDate returnDate = b.getReturn_date();
                LocalDate realReturnDate = b.getReal_return_date();

                String type = null;
                String message = null;

                // Logic for determining notification type
                if (realReturnDate != null) {
                    continue; // skip returned items
                } else if (returnDate.isBefore(today)) {
                    type = "overdue";
                    long daysLate = ChronoUnit.DAYS.between(returnDate, today);
                    message = "\"" + b.getDocument().getTitle() + "\" is overdue by " + daysLate + " days.";
                    message += "\nCurrent fine: " + daysLate * 0.5 + "$";
                } else if (ChronoUnit.DAYS.between(today, returnDate) <= 5) {
                    type = "dueSoon";
                    long daysLeft = ChronoUnit.DAYS.between(today, returnDate);
                    message = "\"" + b.getDocument().getTitle() + "\" is due in " + daysLeft + " days.";
                }

                // Apply filter and add to UI
                if (type != null && message != null) {
                    String selected = filterBox.getValue();
                    if (selected.equals("All")
                            || (selected.equals("Overdue") && type.equals("overdue"))
                            || (selected.equals("Due soon") && type.equals("dueSoon"))) {
                        gridNotifications.add(createNotificationCard(message, type), 0, row++);
                    }
                }
            }

            // --- Show message if none ---
            notificationsContainer.getChildren().removeIf(node -> node instanceof Label);
            if (gridNotifications.getChildren().isEmpty()) {
                Label empty = new Label("✅ No current notifications.");
                empty.setStyle("-fx-text-fill: #555; -fx-font-style: italic; -fx-font-size: 16px;");
                notificationsContainer.getChildren().add(empty);
            }
        };

        // Initial population
        refreshNotifications.run();

        // Refresh on filter change
        filterBox.valueProperty().addListener((obs, oldVal, newVal) -> refreshNotifications.run());

        // --- Combine header + notifications in vertical layout ---
        VBox mainLayout = new VBox(10);
        mainLayout.getChildren().addAll(topBar, notificationsContainer);
        VBox.setVgrow(notificationsContainer, Priority.ALWAYS);

        // --- Scroll wrapper ---
        ScrollPane scrollPane = new ScrollPane(mainLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        return scrollPane;
    }
}