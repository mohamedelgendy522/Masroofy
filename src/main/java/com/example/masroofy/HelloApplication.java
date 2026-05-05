package com.example.masroofy;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    private static final int PHONE_WIDTH = 360;
    private static final int PHONE_HEIGHT = 720;

    private final DataBaseManager dbManager = new DataBaseManager();
    private AppManager appManager;

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
        dbManager.initDB();
        appManager = new AppManager(dbManager);

        Scene scene = new Scene(new VBox(), PHONE_WIDTH, PHONE_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/com/example/masroofy/style.css").toExternalForm());
        scene.setRoot(buildStatsRoot(scene, appManager));

        stage.setTitle("Masroofy App");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}

    private VBox buildStatsRoot(Scene scene, AppManager appManager) {
        return new StatsView(appManager, () -> scene.setRoot(buildHistoryRoot(scene, appManager))).getView();
    }

    private VBox buildHistoryRoot(Scene scene, AppManager appManager) {
        return new HistoryView(appManager, () -> scene.setRoot(buildStatsRoot(scene, appManager))).getView();
    }

    private VBox buildLoginRoot(Scene scene, AppManager appManager) {
        VBox root = new VBox(16);
        root.getStyleClass().add("settings-root");
        root.setPadding(new Insets(40, 28, 40, 28));
        root.setAlignment(Pos.TOP_CENTER);

        Region topSpacer = new Region();
        VBox.setVgrow(topSpacer, Priority.ALWAYS);

        VBox card = new VBox(14);
        card.getStyleClass().add("settings-card");
        card.setMaxWidth(Double.MAX_VALUE);
        card.setAlignment(Pos.TOP_CENTER);

        Label brand = new Label("Masroofy");
        brand.setStyle("-fx-text-fill: #7C3AED; -fx-font-size: 24px; -fx-font-weight: bold;");

        Region brandLine = new Region();
        brandLine.setPrefHeight(3);
        brandLine.setPrefWidth(48);
        brandLine.setMinHeight(3);
        brandLine.setMinWidth(48);
        brandLine.setMaxWidth(48);
        brandLine.setStyle("-fx-background-color: #7C3AED; -fx-background-radius: 2;");

        VBox brandBox = new VBox(6, brand, brandLine);
        brandBox.setAlignment(Pos.CENTER);

        Region spacer = new Region();
        spacer.setPrefHeight(8);

        Label idLabel = new Label("ID");
        idLabel.setStyle("-fx-text-fill: #7C3AED; -fx-font-size: 12px; -fx-font-weight: bold;");
        idLabel.setMaxWidth(Double.MAX_VALUE);
        idLabel.setAlignment(Pos.CENTER_LEFT);

        TextField userIdField = new TextField();
        userIdField.setPromptText("User ID");
        userIdField.getStyleClass().add("text-input");
        userIdField.setMaxWidth(Double.MAX_VALUE);

        Label pinLabel = new Label("PIN");
        pinLabel.setStyle("-fx-text-fill: #7C3AED; -fx-font-size: 12px; -fx-font-weight: bold;");
        pinLabel.setMaxWidth(Double.MAX_VALUE);
        pinLabel.setAlignment(Pos.CENTER_LEFT);

        PasswordField pinField = new PasswordField();
        pinField.setPromptText("PIN");
        pinField.getStyleClass().add("text-input");
        pinField.setMaxWidth(Double.MAX_VALUE);

        Button loginButton = new Button("Log In");
        loginButton.getStyleClass().add("primary-button");
        loginButton.setMaxWidth(Double.MAX_VALUE);

        Label statusLabel = new Label("");
        statusLabel.getStyleClass().add("section-help");
        statusLabel.setMaxWidth(Double.MAX_VALUE);
        statusLabel.setAlignment(Pos.CENTER_LEFT);

        Label registerHint = new Label("Don't have an account?");
        registerHint.getStyleClass().add("section-help");

        Button registerButton = new Button("Register");
        registerButton.getStyleClass().add("section-help");
        registerButton.setStyle("-fx-background-color: transparent; -fx-underline: true; -fx-text-fill: #7C3AED;");
        registerButton.setOnAction(event -> scene.setRoot(buildRegisterRoot(scene, appManager)));

        HBox registerRow = new HBox(6, registerHint, registerButton);
        registerRow.setAlignment(Pos.CENTER);

        loginButton.setOnAction(event -> {
            String userIdText = userIdField.getText() == null ? "" : userIdField.getText().trim();
            String pinText = pinField.getText() == null ? "" : pinField.getText().trim();

            if (userIdText.isEmpty() || pinText.isEmpty() || !userIdText.matches("\\d+")) {
                statusLabel.setText("Enter a valid ID and PIN.");
                return;
            }

            int userId = Integer.parseInt(userIdText);
            boolean loggedIn = appManager.login(userId, pinText);
            statusLabel.setText(loggedIn ? "Logged in. ID: " + userId : "Invalid ID or PIN.");
        });

        card.getChildren().addAll(
                brandBox,
                spacer,
                idLabel,
                userIdField,
                pinLabel,
                pinField,
                loginButton,
                registerRow,
                statusLabel
        );

        Region bottomSpacer = new Region();
        VBox.setVgrow(bottomSpacer, Priority.ALWAYS);

        root.getChildren().addAll(
                topSpacer,
                card,
                bottomSpacer
        );

        return root;
    }

    private VBox buildRegisterRoot(Scene scene, AppManager appManager) {
        VBox root = new VBox(16);
        root.getStyleClass().add("settings-root");
        root.setPadding(new Insets(40, 28, 40, 28));
        root.setAlignment(Pos.TOP_CENTER);

        Region topSpacer = new Region();
        VBox.setVgrow(topSpacer, Priority.ALWAYS);

        VBox card = new VBox(14);
        card.getStyleClass().add("settings-card");
        card.setMaxWidth(Double.MAX_VALUE);
        card.setAlignment(Pos.TOP_CENTER);

        Label brand = new Label("Masroofy");
        brand.setStyle("-fx-text-fill: #7C3AED; -fx-font-size: 24px; -fx-font-weight: bold;");

        Region brandLine = new Region();
        brandLine.setPrefHeight(3);
        brandLine.setPrefWidth(48);
        brandLine.setMinHeight(3);
        brandLine.setMinWidth(48);
        brandLine.setMaxWidth(48);
        brandLine.setStyle("-fx-background-color: #7C3AED; -fx-background-radius: 2;");

        VBox brandBox = new VBox(6, brand, brandLine);
        brandBox.setAlignment(Pos.CENTER);

        Region spacer = new Region();
        spacer.setPrefHeight(14);

        Label nameLabel = new Label("Name");
        nameLabel.setStyle("-fx-text-fill: #7C3AED; -fx-font-size: 12px; -fx-font-weight: bold;");
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        nameLabel.setAlignment(Pos.CENTER_LEFT);

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        nameField.getStyleClass().add("text-input");
        nameField.setMaxWidth(Double.MAX_VALUE);

        Label pinLabel = new Label("PIN");
        pinLabel.setStyle("-fx-text-fill: #7C3AED; -fx-font-size: 12px; -fx-font-weight: bold;");
        pinLabel.setMaxWidth(Double.MAX_VALUE);
        pinLabel.setAlignment(Pos.CENTER_LEFT);

        PasswordField pinField = new PasswordField();
        pinField.setPromptText("PIN (4 digits)");
        pinField.getStyleClass().add("text-input");
        pinField.setMaxWidth(Double.MAX_VALUE);

        Label confirmPinLabel = new Label("Confirm PIN");
        confirmPinLabel.setStyle("-fx-text-fill: #7C3AED; -fx-font-size: 12px; -fx-font-weight: bold;");
        confirmPinLabel.setMaxWidth(Double.MAX_VALUE);
        confirmPinLabel.setAlignment(Pos.CENTER_LEFT);

        PasswordField confirmPinField = new PasswordField();
        confirmPinField.setPromptText("Confirm PIN");
        confirmPinField.getStyleClass().add("text-input");
        confirmPinField.setMaxWidth(Double.MAX_VALUE);

        Button registerButton = new Button("Register");
        registerButton.getStyleClass().add("primary-button");
        registerButton.setMaxWidth(Double.MAX_VALUE);

        Label statusLabel = new Label("");
        statusLabel.getStyleClass().add("section-help");
        statusLabel.setMaxWidth(Double.MAX_VALUE);
        statusLabel.setAlignment(Pos.CENTER_LEFT);

        registerButton.setOnAction(event -> {
            String name = nameField.getText() == null ? "" : nameField.getText().trim();
            String pin = pinField.getText() == null ? "" : pinField.getText().trim();
            String confirmPin = confirmPinField.getText() == null ? "" : confirmPinField.getText().trim();

            if (name.isEmpty()) {
                statusLabel.setText("Enter your name.");
                return;
            }
            if (!pin.matches("\\d{4}")) {
                statusLabel.setText("PIN must be 4 digits.");
                return;
            }
            if (!pin.equals(confirmPin)) {
                statusLabel.setText("PINs do not match.");
                return;
            }

            int userId = appManager.registerUser(pin, name);
            if (userId == -1) {
                statusLabel.setText("Registration failed.");
            } else {
                statusLabel.setText("Registered. Your ID: " + userId);
            }
        });

        Button backButton = new Button("Back to login");
        backButton.getStyleClass().add("section-help");
        backButton.setStyle("-fx-background-color: transparent;");
        backButton.setOnAction(event -> scene.setRoot(buildLoginRoot(scene, appManager)));

        card.getChildren().addAll(
                brandBox,
                spacer,
                nameLabel,
                nameField,
                pinLabel,
                pinField,
                confirmPinLabel,
                confirmPinField,
                registerButton,
                backButton,
                statusLabel
        );

        Region bottomSpacer = new Region();
        VBox.setVgrow(bottomSpacer, Priority.ALWAYS);

        root.getChildren().addAll(
                topSpacer,
                card,
                bottomSpacer
        );

        return root;
    }

}
