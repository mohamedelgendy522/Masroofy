package com.example.masroofy;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    private static final int PHONE_WIDTH = 390;
    private static final int PHONE_HEIGHT = 780;

    @Override
    public void start(Stage stage) {
        AppManager appManager = new AppManager(new DataBaseManager());

        // Tabs

        Tab historyTab   = new Tab("History",   new HistoryView(appManager).getView());
        historyTab.setClosable(false);

        Tab settingsTab = new Tab("Settings", new SettingsView().getView());
        settingsTab.setClosable(false);

        TabPane tabPane = new TabPane( historyTab ,settingsTab );


        Scene scene = new Scene(tabPane, PHONE_WIDTH, PHONE_HEIGHT);

        scene.getStylesheets().add(
                getClass().getResource("/com/example/masroofy/style.css").toExternalForm()
        );

        stage.setTitle("Masroofy");
        stage.setScene(scene);
        stage.setMinWidth(PHONE_WIDTH);
        stage.setMinHeight(PHONE_HEIGHT);
        stage.setMaxWidth(PHONE_WIDTH);
        stage.setMaxHeight(PHONE_HEIGHT);
        stage.show();
    }
}