package com.example.masroofy;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * View class managing user settings configuration.
 */
class SettingsView {

    private AppManager appManager;
    private Runnable onLogout; // Added onLogout

    /**
     * Constructs a SettingsView with a specified application manager and logout handler.
     *
     * @param appManager Central logic integration point.
     * @param onLogout   Functional callback referencing external logout handling.
     */
    public SettingsView(AppManager appManager, Runnable onLogout) {
        this.appManager = appManager;
        this.onLogout = onLogout;
    }

    /**
     * Constructs a SettingsView without handling an explicit callback hook.
     *
     * @param appManager The central application manager.
     */
    public SettingsView(AppManager appManager) {
        this.appManager = appManager;
    }

    /**
     * Constructs the UI layout rendering user settings components.
     *
     * @return The complete configuration view root VBox instance.
     */
    public VBox getView() {
        VBox root = new VBox();
        root.getStyleClass().add("settings-root");

        Label pageTitle = new Label("Settings");
        pageTitle.getStyleClass().add("page-title");
        VBox.setMargin(pageTitle, new Insets(0, 0, 8, 0));

        // Card 1: Change PIN
        VBox changePinCard = createCard();
        Label changePinTitle = createCardTitle("CHANGE PIN");

        PasswordField oldPinField = createPasswordField("••••");
        PasswordField newPinField = createPasswordField("••••");

        VBox oldPinBox = new VBox(6, createFieldLabel("Old PIN"), oldPinField);
        VBox newPinBox = new VBox(6, createFieldLabel("New PIN"), newPinField);

        Button confirmBtn = new Button("✓");
        confirmBtn.getStyleClass().add("icon-button");
        confirmBtn.setPrefHeight(40);


        Label dummyLabel = createFieldLabel(" ");
        VBox btnBox = new VBox(6, dummyLabel, confirmBtn);

        // Feedback label shown after the user tries to change PIN
        Label pinMsg = new Label();

        confirmBtn.setOnAction(e -> {
            boolean ok = appManager.changePin(oldPinField.getText(), newPinField.getText());
            pinMsg.getStyleClass().setAll(ok ? "msg-success" : "msg-error");
            pinMsg.setText(ok ? "✓ PIN changed successfully." : "✗ Wrong old PIN.");
            oldPinField.clear();
            newPinField.clear();
        });


        HBox pinRow = new HBox(10, oldPinBox, newPinBox, btnBox);


        pinRow.setAlignment(Pos.CENTER_LEFT);

        pinRow.getStyleClass().add("form-row");
        HBox.setHgrow(oldPinBox, Priority.ALWAYS);
        HBox.setHgrow(newPinBox, Priority.ALWAYS);

        changePinCard.getChildren().addAll(changePinTitle, pinRow, pinMsg);

        // Card 2: Reset Cycle
        VBox resetCard = createCard();
        Label resetTitle = createCardTitle("RESET CYCLE");

        Label resetDesc = new Label("Clear all expenses and categories for the current cycle. This action cannot be undone.");
        resetDesc.getStyleClass().add("section-help");

        Button resetBtn = new Button("Reset Cycle");
        resetBtn.getStyleClass().add("danger-button");

        Label resetMsg = new Label();

        resetBtn.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure? This cannot be undone.",
                    ButtonType.YES, ButtonType.NO);
            confirm.setTitle("Reset Cycle");
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    appManager.resetCycle();
                    resetMsg.getStyleClass().setAll("msg-success");
                    resetMsg.setText("✓ Cycle reset.");
                }
            });
        });

        resetCard.getChildren().addAll(resetTitle, resetDesc, resetBtn, resetMsg);

        // Card 3: Manage Categories
        VBox categoryCard = createCard();
        Label categoryTitle = createCardTitle("MANAGE CATEGORIES");

        TextField addCatField = createTextField("Category name");
        Button addCatBtn = new Button("+ Add");
        addCatBtn.getStyleClass().add("primary-button");

        HBox addRow = new HBox(10, addCatField, addCatBtn);
        addRow.getStyleClass().add("form-row");
        HBox.setHgrow(addCatField, Priority.ALWAYS);

        Separator divider = new Separator();
        divider.getStyleClass().add("section-divider");
        VBox.setMargin(divider, new Insets(4, 0, 4, 0));

        TextField deleteCatField = createTextField("Name to delete");
        Button deleteCatBtn = new Button("Delete");
        deleteCatBtn.getStyleClass().add("danger-button");

        HBox deleteRow = new HBox(10, deleteCatField, deleteCatBtn);
        deleteRow.getStyleClass().add("form-row");
        HBox.setHgrow(deleteCatField, Priority.ALWAYS);

        Label currentCatLabel = createFieldLabel("Current categories");
        VBox.setMargin(currentCatLabel, new Insets(12, 0, 4, 0));

        FlowPane tagsPane = new FlowPane();
        tagsPane.getStyleClass().add("tag-row");

        Label catMsg = new Label();

        Runnable refreshTags = () -> {
            tagsPane.getChildren().clear();
            for (String cat : appManager.getCategories()) {
                Label tag = new Label(cat);
                tag.getStyleClass().addAll("tag", resolveTagClass(cat));
                tagsPane.getChildren().add(tag);
            }
        };

        addCatBtn.setOnAction(e -> {
            String name = addCatField.getText().trim();
            if (!name.isEmpty()) {
                appManager.addCategory(name);
                addCatField.clear();
                refreshTags.run();
                catMsg.getStyleClass().setAll("msg-success");
                catMsg.setText("✓ Category added.");
            }
        });

        deleteCatBtn.setOnAction(e -> {
            String name = deleteCatField.getText().trim();
            if (!name.isEmpty()) {
                boolean ok = appManager.deleteCategory(name);
                deleteCatField.clear();
                refreshTags.run();
                catMsg.getStyleClass().setAll(ok ? "msg-success" : "msg-error");
                catMsg.setText(ok ? "✓ Category deleted." : "✗ Category not found.");
            }
        });

        refreshTags.run();

        categoryCard.getChildren().addAll(
                categoryTitle, addRow, divider, deleteRow,
                currentCatLabel, tagsPane, catMsg
        );

        // Card 4: Delete Account
        VBox resetDbCard = createCard();
        resetDbCard.getStyleClass().add("danger-card");

        Label resetDbTitle = createCardTitle("DELETE ACCOUNT");

        Label resetDbDesc = new Label("Wipes your entire account — cycles, expenses, categories, and your user data.");
        resetDbDesc.getStyleClass().add("section-help");

        Button resetDbBtn = new Button("Delete Account");
        resetDbBtn.getStyleClass().add("danger-button");

        Label resetDbMsg = new Label();

        resetDbBtn.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "This will delete EVERYTHING including your account.\nThis cannot be undone.",
                    ButtonType.YES, ButtonType.NO);
            confirm.setTitle("Delete Account");
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    appManager.deleteCurrentAccount();
                    resetDbMsg.getStyleClass().setAll("msg-success");
                    resetDbMsg.setText("✓ Account deleted.");
                    resetDbBtn.setDisable(true);
                    if (onLogout != null) {
                        onLogout.run();
                    }
                }
            });
        });

        resetDbCard.getChildren().addAll(resetDbTitle, resetDbDesc, resetDbBtn, resetDbMsg);

        // Card 5: Logout
        VBox logoutCard = createCard();
        Button logoutBtn = new Button("Logout");
        logoutBtn.getStyleClass().add("primary-button");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);

        logoutBtn.setOnAction(e -> {
            appManager.logout();
            if (onLogout != null) {
                onLogout.run();
            }
        });
        logoutCard.getChildren().add(logoutBtn);

        VBox content = new VBox(16,
                pageTitle,
                changePinCard,
                resetCard,
                categoryCard,
                resetDbCard,
                logoutCard
        );
        content.setPadding(new Insets(28, 24, 28, 24));
        content.setAlignment(Pos.TOP_CENTER);
        content.getStyleClass().add("settings-container");

        HBox centerWrapper = new HBox(content);
        centerWrapper.setAlignment(Pos.TOP_CENTER);

        ScrollPane scroll = new ScrollPane(centerWrapper);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("settings-scroll");

        root.getChildren().add(scroll);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        return root;
    }

    /**
     * Instantiates a functional inner-card grouping box view.
     *
     * @return A styled container VBox.
     */
    private VBox createCard() {
        VBox card = new VBox(12);
        card.getStyleClass().add("settings-card");
        return card;
    }

    /**
     * Assembles standardized card headers logic formats.
     *
     * @param text Informational text binding mapping.
     * @return Instantiated structural label block.
     */
    private Label createCardTitle(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("card-title");
        return label;
    }

    /**
     * Builds uniform form label identifiers strings mapped contexts.
     *
     * @param text Output designation label sequence strings.
     * @return Output interface block references mapping string logic layouts.
     */
    private Label createFieldLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("tags-label");
        return label;
    }

    /**
     * Generates uniformly constructed hidden format string fields references handling masking formatting components blocks interfaces inputs configurations properties logic setups structures.
     *
     * @param placeholder Helper label.
     * @return Structured formatted inputs block definitions interfaces models attributes constraints.
     */
    private PasswordField createPasswordField(String placeholder) {
        PasswordField field = new PasswordField();
        field.setPromptText(placeholder);
        field.getStyleClass().add("text-input");
        return field;
    }

    /**
     * Initiates mapping interfaces rendering formats textual.
     *
     * @param placeholder Instantiation texts.
     * @return Field references attributes formats contexts settings layouts schemas inputs attributes specifications schemas bindings models outputs layouts templates formatting instances.
     */
    private TextField createTextField(String placeholder) {
        TextField field = new TextField();
        field.setPromptText(placeholder);
        field.getStyleClass().add("text-input");
        return field;
    }

    /**
     * Resolves matching dynamic text logic definitions string templates schemas bindings variables formats settings formatting arrays layouts tags contexts representations lists lists outputs attributes references.
     *
     * @param name Key lookup mapping parameters constraints templates instances formats representations logic constraints mappings constants.
     * @return Resolved output mappings attributes constraints mapping variables parameters setups formatting textual variables representations values lists objects textual constants.
     */
    private String resolveTagClass(String name) {
        return switch (name.toLowerCase()) {
            case "food"          -> "tag-food";
            case "transport"     -> "tag-transport";
            case "health"        -> "tag-health";
            case "entertainment" -> "tag-ent";
            case "utilities"     -> "tag-util";
            default              -> "tag-transport";
        };
    }
}