package com.library.ui.generalStyling;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class UIStyling {

    // Red glow for incorrectly filled fields
    public static DropShadow red_glow;
    static {
        red_glow = new DropShadow();
        red_glow.setColor(Color.RED);
        red_glow.setOffsetX(0);
        red_glow.setOffsetY(0);
        red_glow.setRadius(10);
    }

    // Alert message styling
    public static void alert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Button styling
    public static void button_styling(Button button, String background_color, String text_color){
        button.setStyle(
            "-fx-background-color: " + background_color + "; " +
            "-fx-text-fill:" + text_color + "; " +
            "-fx-font-weight:bold; " +
            "-fx-background-radius:6;"
        );
    }
}
