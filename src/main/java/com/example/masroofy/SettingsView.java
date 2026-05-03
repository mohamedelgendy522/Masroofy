package com.example.masroofy;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

class SettingsView {
    private final VBox root;

    SettingsView() {
        root = new VBox(16);
        root.setPadding(new Insets(24));
        root.setAlignment(Pos.TOP_CENTER);
        root.getStyleClass().add("settings-root");

        VBox container = new VBox(16);
        container.getStyleClass().add("settings-container");

        Label title = new Label("Settings");
        title.getStyleClass().add("page-title");

        VBox changePinCard = createChangePinCard();
        VBox resetCycleCard = createResetCycleCard();
        VBox categoriesCard = createCategoriesCard();

        container.getChildren().addAll(title, changePinCard, resetCycleCard, categoriesCard);
        root.getChildren().add(container);
    }

    VBox getView() {
        return root;
    }

    private VBox createChangePinCard() {
        VBox card = createCard("Change PIN");

        TextField oldPinField = new TextField();
        oldPinField.setPromptText("Old PIN");
        oldPinField.getStyleClass().add("text-input");

        TextField newPinField = new TextField();
        newPinField.setPromptText("New PIN");
        newPinField.getStyleClass().add("text-input");

        Button changeButton = new Button("Change");
        changeButton.getStyleClass().add("primary-button");

        HBox row = new HBox(12, oldPinField, newPinField, changeButton);
        row.getStyleClass().add("form-row");

        card.getChildren().add(row);
        return card;
    }

    private VBox createResetCycleCard() {
        VBox card = createCard("Reset Cycle");

        Label info = new Label("Clear all expenses and categories for the current cycle.");
        info.getStyleClass().add("section-help");

        Button resetButton = new Button("Reset Cycle");
        resetButton.getStyleClass().add("danger-button");

        card.getChildren().addAll(info, resetButton);
        return card;
    }

    private VBox createCategoriesCard() {
        VBox card = createCard("Manage Categories");

        TextField categoryNameField = new TextField();
        categoryNameField.setPromptText("Category name");
        categoryNameField.getStyleClass().add("text-input");

        Button addButton = new Button("Add");
        addButton.getStyleClass().add("primary-button");

        HBox addRow = new HBox(12, categoryNameField, addButton);
        addRow.getStyleClass().add("form-row");

        TextField deleteNameField = new TextField();
        deleteNameField.setPromptText("Name to delete");
        deleteNameField.getStyleClass().add("text-input");

        Button deleteButton = new Button("Delete");
        deleteButton.getStyleClass().add("danger-button");

        HBox deleteRow = new HBox(12, deleteNameField, deleteButton);
        deleteRow.getStyleClass().add("form-row");

        card.getChildren().addAll(addRow, deleteRow);
        return card;
    }

    private VBox createCard(String titleText) {
        VBox card = new VBox(12);
        card.getStyleClass().add("settings-card");

        Label title = new Label(titleText);
        title.getStyleClass().add("section-title");

        Region divider = new Region();
        divider.getStyleClass().add("section-divider");

        card.getChildren().addAll(title, divider);
        return card;
    }
}
