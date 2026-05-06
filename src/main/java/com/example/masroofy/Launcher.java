package com.example.masroofy;

import javafx.application.Application;

/**
 * Main launcher entry point class to execute the JavaFX application process.
 */
public class Launcher {
    /**
     * Dispatches the main method arguments to the application launcher.
     *
     * @param args The input arguments.
     */
    public static void main(String[] args) {
        Application.launch(HelloApplication.class, args);
    }
}