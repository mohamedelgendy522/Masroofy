package com.example.masroofy;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

class SettingsView {
    private final VBox root;

    SettingsView() {
        root = new VBox(0);
        root.getStyleClass().add("settings-root");

        // Scrollable inner container
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.getStyleClass().add("settings-scroll");
        scroll.setStyle("-fx-background-color: #0B0F1A; -fx-background: #0B0F1A;");

        VBox container = new VBox(16);
        container.setPadding(new Insets(28, 24, 28, 24));
        container.setMaxWidth(420);

        Label title = new Label("Settings");
        title.getStyleClass().add("page-title");

        VBox changePinCard = createChangePinCard();
        VBox resetCycleCard = createResetCycleCard();
        VBox categoriesCard = createCategoriesCard();

        container.getChildren().addAll(title, changePinCard, resetCycleCard, categoriesCard);

        scroll.setContent(container);
        root.getChildren().add(scroll);
        VBox.setVgrow(scroll, Priority.ALWAYS);
    }

    VBox getView() {
        return root;
    }

    // ── Change PIN Card ──────────────────────────────────────
    private VBox createChangePinCard() {
        VBox card = createCard("Change PIN");

        PasswordField oldPinField = new PasswordField();
        oldPinField.setPromptText("Old PIN");
        oldPinField.getStyleClass().add("text-input");
        HBox.setHgrow(oldPinField, Priority.ALWAYS);

        PasswordField newPinField = new PasswordField();
        newPinField.setPromptText("New PIN");
        newPinField.getStyleClass().add("text-input");
        HBox.setHgrow(newPinField, Priority.ALWAYS);

        Button confirmButton = new Button("✓");
        confirmButton.getStyleClass().add("icon-button");
        confirmButton.setMinWidth(40);
        confirmButton.setMinHeight(40);

        HBox row = new HBox(10, oldPinField, newPinField, confirmButton);
        row.getStyleClass().add("form-row");
        row.setAlignment(Pos.CENTER_LEFT);

        card.getChildren().add(row);
        return card;
    }

    // ── Reset Cycle Card ─────────────────────────────────────
    private VBox createResetCycleCard() {
        VBox card = createCard("Reset Cycle");

        Label info = new Label(
                "Clear all expenses and categories for the current cycle.\nThis action cannot be undone."
        );
        info.getStyleClass().add("section-help");
        info.setWrapText(true);

        Button resetButton = new Button("Reset Cycle");
        resetButton.getStyleClass().add("danger-button");

        card.getChildren().addAll(info, resetButton);
        return card;
    }

    // ── Manage Categories Card ───────────────────────────────
    private VBox createCategoriesCard() {
        VBox card = createCard("Manage Categories");

        // Add row
        TextField categoryNameField = new TextField();
        categoryNameField.setPromptText("Category name");
        categoryNameField.getStyleClass().add("text-input");
        HBox.setHgrow(categoryNameField, Priority.ALWAYS);

        Button addButton = new Button("+ Add");
        addButton.getStyleClass().add("primary-button");

        HBox addRow = new HBox(10, categoryNameField, addButton);
        addRow.getStyleClass().add("form-row");
        addRow.setAlignment(Pos.CENTER_LEFT);

        // Mid divider
        Region mid = new Region();
        mid.getStyleClass().add("section-divider");
        mid.setMaxWidth(Double.MAX_VALUE);

        // Delete row
        TextField deleteNameField = new TextField();
        deleteNameField.setPromptText("Name to delete");
        deleteNameField.getStyleClass().add("text-input");
        HBox.setHgrow(deleteNameField, Priority.ALWAYS);

        Button deleteButton = new Button("Delete");
        deleteButton.getStyleClass().add("ghost-button");

        HBox deleteRow = new HBox(10, deleteNameField, deleteButton);
        deleteRow.getStyleClass().add("form-row");
        deleteRow.setAlignment(Pos.CENTER_LEFT);

        // Tags section
        Label tagsLabel = new Label("Current categories");
        tagsLabel.getStyleClass().add("tags-label");

        FlowPane tagPane = new FlowPane(8, 8);
        tagPane.getStyleClass().add("tag-row");

        String[][] tags = {
                {"Food",          "tag-food"},
                {"Transport",     "tag-transport"},
                {"Health",        "tag-health"},
                {"Entertainment", "tag-ent"},
                {"Utilities",     "tag-util"}
        };
        for (String[] t : tags) {
            Label tag = new Label(t[0]);
            tag.getStyleClass().addAll("tag", t[1]);
            tagPane.getChildren().add(tag);
        }

        card.getChildren().addAll(addRow, mid, deleteRow, tagsLabel, tagPane);
        return card;
    }

    // ── Shared Card Builder ──────────────────────────────────
    private VBox createCard(String titleText) {
        VBox card = new VBox(14);
        card.getStyleClass().add("settings-card");
        card.setMaxWidth(Double.MAX_VALUE);

        Label title = new Label(titleText.toUpperCase());
        title.getStyleClass().add("card-title");

        Region divider = new Region();
        divider.getStyleClass().add("section-divider");
        divider.setMaxWidth(Double.MAX_VALUE);

        card.getChildren().addAll(title, divider);
        return card;
    }
}