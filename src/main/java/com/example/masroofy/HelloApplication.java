package com.example.masroofy;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    private static final int PHONE_WIDTH = 390;
    private static final int PHONE_HEIGHT = 780;

    @Override
    public void start(Stage stage) {
        SettingsView settingsView = new SettingsView();

        Scene scene = new Scene(settingsView.getView(), PHONE_WIDTH, PHONE_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/com/example/masroofy/style.css").toExternalForm());

        stage.setTitle("Masroofy Settings");
        stage.setScene(scene);
        stage.setMinWidth(PHONE_WIDTH);
        stage.setMinHeight(PHONE_HEIGHT);
        stage.setMaxWidth(PHONE_WIDTH);
        stage.setMaxHeight(PHONE_HEIGHT);
        stage.show();
    }

}
