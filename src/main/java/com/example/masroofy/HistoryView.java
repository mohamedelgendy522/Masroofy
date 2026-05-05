package com.example.masroofy;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

class HistoryView {

    private final AppManager appManager;
    private final Runnable onBack;

    HistoryView(AppManager appManager, Runnable onBack) {
        this.appManager = appManager;
        this.onBack = onBack;
    }

    VBox getView() {
        VBox root = new VBox(16);
        root.getStyleClass().add("stats-root");
        root.setPadding(new Insets(24, 20, 24, 20));

        Label title = new Label("History");
        title.getStyleClass().add("stats-title");

        VBox card = new VBox(10);
        card.getStyleClass().add("stats-card");
        card.setAlignment(Pos.CENTER_LEFT);

        Label placeholder = new Label("History screen coming soon.");
        placeholder.getStyleClass().add("stats-empty");

        Button backButton = new Button("Back to Stats");
        backButton.getStyleClass().add("stats-ghost-button");
        backButton.setOnAction(event -> {
            if (onBack != null) {
                onBack.run();
            }
        });

        Region spacer = new Region();
        spacer.setPrefHeight(6);

        card.getChildren().addAll(placeholder, spacer, backButton);

        root.getChildren().addAll(title, card);
        return root;
    }
}
