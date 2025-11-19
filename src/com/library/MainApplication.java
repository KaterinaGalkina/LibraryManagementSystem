package com.library;
import com.library.ui.ApplicationFX;

public class MainApplication {
    public static void main(String[] args) {
        try {
            ApplicationFX.ui(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
