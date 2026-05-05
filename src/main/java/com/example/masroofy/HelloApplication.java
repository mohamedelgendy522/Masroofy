package com.example.masroofy;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    private static final int PHONE_WIDTH = 480;
    private static final int PHONE_HEIGHT = 800;

    private final DataBaseManager dbManager = new DataBaseManager();
    private AppManager appManager;
    private BorderPane mainLayout;

    @Override
    public void start(Stage stage) {
        dbManager.initDB();
        appManager = new AppManager(dbManager);

        Scene scene = new Scene(new VBox(), PHONE_WIDTH, PHONE_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/com/example/masroofy/style.css").toExternalForm());

        // Starts directly into main layout with Dashboard to test the UI quickly
        showMainApp(scene, appManager);

        stage.setTitle("Masroofy App");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void showMainApp(Scene scene, AppManager appManager) {
        mainLayout = new BorderPane();
        mainLayout.getStyleClass().add("settings-root");

        // Top Header
        HBox topHeader = new HBox();
        topHeader.getStyleClass().add("settings-card");
        topHeader.setPadding(new Insets(15, 20, 15, 20));
        topHeader.setAlignment(Pos.CENTER_LEFT);
        // Remove lower corners rounding slightly or keep it standard card:
        topHeader.setStyle("-fx-background-radius: 0 0 16 16; -fx-border-radius: 0 0 16 16; -fx-border-width: 0 0 1 0;");

        String userName = appManager.getCurrentUserName();
        if (userName == null || userName.isEmpty()) userName = "User";

        Label welcomeLabel = new Label("Welcome, " + userName);
        welcomeLabel.setStyle("-fx-text-fill: #F0F2FF; -fx-font-size: 18px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button settingsBtn = new Button("⚙");
        settingsBtn.getStyleClass().add("icon-button");
        settingsBtn.setStyle("-fx-font-size: 18px; -fx-padding: 0; -fx-min-width: 38px; -fx-min-height: 38px; -fx-pref-width: 38px; -fx-pref-height: 38px;");
        settingsBtn.setOnAction(e -> {
            mainLayout.setCenter(new SettingsView(appManager).getView());
        });

        topHeader.getChildren().addAll(welcomeLabel, spacer, settingsBtn);
        mainLayout.setTop(topHeader);

        HBox bottomNav = new HBox(5);
        bottomNav.getStyleClass().add("settings-card");
        bottomNav.setPadding(new Insets(10, 10, 10, 10));
        bottomNav.setAlignment(Pos.CENTER);

        Button dashBtn = createNavButton("Dashboard");
        Button statsBtn = createNavButton("Stats");
        Button histBtn = createNavButton("History");
        Button addBtn = createNavButton("Add");

        dashBtn.setOnAction(e -> mainLayout.setCenter(new DashboardView(appManager).getView()));
        statsBtn.setOnAction(e -> mainLayout.setCenter(new StatsView(appManager, () -> mainLayout.setCenter(new HistoryView(appManager).getView())).getView()));
        histBtn.setOnAction(e -> mainLayout.setCenter(new HistoryView(appManager).getView()));
        addBtn.setOnAction(e -> {
            DashboardView dv = new DashboardView(appManager);
            dv.setHistoryView(new HistoryView(appManager));
            mainLayout.setCenter(dv.getAddOptionsView(() -> mainLayout.setCenter(new DashboardView(appManager).getView())));
        });

        bottomNav.getChildren().addAll(dashBtn, statsBtn, histBtn, addBtn);

        mainLayout.setBottom(bottomNav);
        mainLayout.setCenter(new DashboardView(appManager).getView());

        scene.setRoot(mainLayout);
    }

    private Button createNavButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #7C3AED; -fx-font-size: 14px; -fx-font-weight: bold;");
        btn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btn, Priority.ALWAYS);
        return btn;
    }

    private VBox buildStatsRoot(Scene scene, AppManager appManager) {
        return new StatsView(appManager, () -> scene.setRoot(buildHistoryRoot(scene, appManager))).getView();
    }

    private VBox buildHistoryRoot(Scene scene, AppManager appManager) {
        return new HistoryView(appManager).getView();
    }

    private VBox buildLoginRoot(Scene scene, AppManager appManager)     {
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
            if (loggedIn) {
                showMainApp(scene, appManager);
            } else {
                statusLabel.setText("Invalid ID or PIN.");
            }
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