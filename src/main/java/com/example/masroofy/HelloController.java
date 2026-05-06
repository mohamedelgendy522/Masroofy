package com.example.masroofy;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller for the default Hello view logic.
 */
public class HelloController {
    @FXML
    private Label welcomeText;

    /**
     * Handles the click event for the Hello button.
     */
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}