package com.example.masroofy;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


public class DashboardView {

    // ── Dependencies ────────────────────────────────────────────────────────
    private final AppManager appManager;
    private HistoryView historyView;   // optional; set via setHistoryView()

    // ── Root container ───────────────────────────────────────────────────────
    private final VBox root;

    // ── Live labels (updated on refresh) ────────────────────────────────────
    private Label balanceAmountLbl;
    private Label balanceCycleLbl;
    private Label balanceSubLbl;
    private Label weeklyValueLbl;
    private Label dailyValueLbl;
    private Label budgetSpentLbl;
    private StackPane budgetFillPane;
    private VBox categoryRows;

    // ── Category bar colours (cycle through) ────────────────────────────────
    private static final String[] CAT_COLORS = {
            "#7C3AED", "#4ADE80", "#38BDF8", "#FBBF24",
            "#F87171", "#C084FC", "#FB923C", "#818CF8"
    };

    // ════════════════════════════════════════════════════════════════════════
    //  CONSTRUCTORS
    // ════════════════════════════════════════════════════════════════════════

    public DashboardView(AppManager appManager) {
        this(appManager, null);
    }

    public DashboardView(AppManager appManager, HistoryView historyView) {
        this.appManager  = appManager;
        this.historyView = historyView;

        root = new VBox(0);
        root.getStyleClass().add("settings-root");

        buildUI();
        refresh();
    }

    // ── Accessors ────────────────────────────────────────────────────────────

    public VBox getView() { return root; }

    public void setHistoryView(HistoryView historyView) {
        this.historyView = historyView;
    }

    public VBox getAddOptionsView(Runnable onDone) {
        VBox container = new VBox(20);
        container.getStyleClass().add("settings-root");
        container.setPadding(new Insets(40, 24, 40, 24));
        container.setAlignment(Pos.CENTER);

        Button addDepositBtn = new Button("Add Deposit");
        addDepositBtn.getStyleClass().add("success-button");
        addDepositBtn.setMaxWidth(Double.MAX_VALUE);
        addDepositBtn.setOnAction(e -> showAddDepositPage(container, onDone));

        Button addExpenseBtn = new Button("Add Expense");
        addExpenseBtn.getStyleClass().add("primary-button");
        addExpenseBtn.setMaxWidth(Double.MAX_VALUE);
        addExpenseBtn.setOnAction(e -> showAddExpensePage(container, onDone));

        container.getChildren().addAll(addDepositBtn, addExpenseBtn);
        return container;
    }

    private void showAddDepositPage(VBox parentContainer, Runnable onDone) {
        VBox page = new VBox(16);
        page.getStyleClass().add("settings-card");
        page.setMaxWidth(Double.MAX_VALUE);

        Label title = new Label("ADD DEPOSIT");
        title.getStyleClass().add("card-title");

        Label amtLabel = new Label("Salary Amount (EGP)");
        amtLabel.getStyleClass().add("field-label");
        TextField amtField = new TextField();
        amtField.setPromptText("0.00");
        amtField.getStyleClass().add("text-input");

        Label startLabel = new Label("Start Date");
        startLabel.getStyleClass().add("field-label");
        DatePicker startDate = new DatePicker(LocalDate.now());
        startDate.setMaxWidth(Double.MAX_VALUE);

        Label endLabel = new Label("End Date");
        endLabel.getStyleClass().add("field-label");
        DatePicker endDate = new DatePicker(LocalDate.now().plusMonths(1));
        endDate.setMaxWidth(Double.MAX_VALUE);

        Label errorLbl = new Label();
        errorLbl.getStyleClass().add("error-label");
        errorLbl.setVisible(false);
        errorLbl.setManaged(false);

        Button submitBtn = new Button("Confirm Deposit");
        submitBtn.getStyleClass().add("success-button");
        submitBtn.setMaxWidth(Double.MAX_VALUE);

        submitBtn.setOnAction(e -> {
            errorLbl.setVisible(false);
            errorLbl.setManaged(false);

            try {
                double amount = Double.parseDouble(amtField.getText().trim());
                if (amount <= 0) throw new NumberFormatException();

                LocalDate start = startDate.getValue();
                LocalDate end = endDate.getValue();

                if (start == null || end == null || end.isBefore(start)) {
                    showError(errorLbl, "Invalid dates.");
                    return;
                }

                // Set up cycle internally uses the deposit valuesappManager.startNewCycle(amount, start, end);

                refresh();
                if (onDone != null) onDone.run();

            } catch (NumberFormatException ex) {
                showError(errorLbl, "Enter a valid positive amount.");
            }
        });

        page.getChildren().addAll(title, amtLabel, amtField, startLabel, startDate, endLabel, endDate, errorLbl, submitBtn);

        parentContainer.getChildren().setAll(page);
    }

    private void showAddExpensePage(VBox parentContainer, Runnable onDone) {
        VBox page = new VBox(16);
        page.getStyleClass().add("settings-card");
        page.setMaxWidth(Double.MAX_VALUE);

        Label title = new Label("ADD EXPENSE");
        title.getStyleClass().add("card-title");

        Label amtLabel = new Label("Amount (EGP)");
        amtLabel.getStyleClass().add("field-label");
        TextField amtField = new TextField();
        amtField.setPromptText("0.00");
        amtField.getStyleClass().add("text-input");

        Label catLabel = new Label("Category");
        catLabel.getStyleClass().add("field-label");
        ComboBox<String> catCombo = new ComboBox<>();
        catCombo.getStyleClass().add("combo-input");
        catCombo.setMaxWidth(Double.MAX_VALUE);
        catCombo.setPromptText("Select category…");

        List<String> cats = appManager.getCategories();
        catCombo.getItems().addAll(cats);
        if (!cats.isEmpty()) catCombo.getSelectionModel().selectFirst();

        Label errorLbl = new Label();
        errorLbl.getStyleClass().add("error-label");
        errorLbl.setVisible(false);
        errorLbl.setManaged(false);

        Button submitBtn = new Button("Confirm Expense");
        submitBtn.getStyleClass().add("primary-button");
        submitBtn.setMaxWidth(Double.MAX_VALUE);

        submitBtn.setOnAction(e -> {
            errorLbl.setVisible(false);
            errorLbl.setManaged(false);

            try {
                double amount = Double.parseDouble(amtField.getText().trim());
                if (amount <= 0) throw new NumberFormatException();

                String selectedCat = catCombo.getSelectionModel().getSelectedItem();
                if (selectedCat == null || selectedCat.isBlank()) {
                    showError(errorLbl, "Please select a category.");
                    return;
                }

                int catId = appManager.getCategoryIdByName(selectedCat);
                appManager.addExpense(amount, catId, "EXPENSE");

                refresh();
                if (historyView != null) historyView.refresh();
                if (onDone != null) onDone.run();

            } catch (NumberFormatException ex) {
                showError(errorLbl, "Enter a valid positive amount.");
            }
        });

        page.getChildren().addAll(title, amtLabel, amtField, catLabel, catCombo, errorLbl, submitBtn);

        parentContainer.getChildren().setAll(page);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  BUILD  (called once — creates the skeleton)
    // ════════════════════════════════════════════════════════════════════════

    private void buildUI() {
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("settings-scroll");
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox container = new VBox(16);
        container.setPadding(new Insets(28, 24, 28, 24));

        container.getChildren().addAll(
                buildBalanceCard(),
                buildStatRow(),
                buildBudgetCard(),
                buildCategoryCard()
        );

        scroll.setContent(container);
        root.getChildren().add(scroll);
    }

    // ── Balance card ─────────────────────────────────────────────────────────

    private VBox buildBalanceCard() {
        balanceCycleLbl = new Label();
        balanceCycleLbl.getStyleClass().add("balance-cycle-label");

        balanceAmountLbl = new Label();
        balanceAmountLbl.getStyleClass().add("balance-amount");

        balanceSubLbl = new Label();
        balanceSubLbl.getStyleClass().add("balance-sub");

        VBox card = new VBox(6, balanceCycleLbl, balanceAmountLbl, balanceSubLbl);
        card.getStyleClass().add("balance-card");
        card.setMaxWidth(Double.MAX_VALUE);
        return card;
    }

    // ── Stat row (weekly / daily) ─────────────────────────────────────────────

    private HBox buildStatRow() {
        VBox weeklyTile = buildStatTile("WEEKLY SPEND");
        weeklyValueLbl  = (Label) ((VBox) weeklyTile).getChildren().get(1);

        VBox dailyTile  = buildStatTile("DAILY LIMIT");
        dailyValueLbl   = (Label) ((VBox) dailyTile).getChildren().get(1);

        HBox row = new HBox(12, weeklyTile, dailyTile);
        HBox.setHgrow(weeklyTile, Priority.ALWAYS);
        HBox.setHgrow(dailyTile,  Priority.ALWAYS);
        weeklyTile.setMaxWidth(Double.MAX_VALUE);
        dailyTile.setMaxWidth(Double.MAX_VALUE);
        return row;
    }

    private VBox buildStatTile(String labelText) {
        Label lbl = new Label(labelText);
        lbl.getStyleClass().add("stat-label");

        Label val = new Label("—");
        val.getStyleClass().addAll("stat-value", "stat-safe");

        VBox tile = new VBox(4, lbl, val);
        tile.getStyleClass().add("settings-card");
        tile.setPadding(new Insets(16));
        return tile;
    }

    // ── Budget progress card ──────────────────────────────────────────────────

    private VBox buildBudgetCard() {
        Label title = new Label("BUDGET PROGRESS");
        title.getStyleClass().add("card-title");

        budgetSpentLbl = new Label("EGP 0");
        budgetSpentLbl.getStyleClass().add("budget-spent");

        // track + fill
        budgetFillPane = new StackPane();
        budgetFillPane.getStyleClass().add("budget-fill");
        budgetFillPane.setMaxWidth(0);   // will be updated in refresh()

        StackPane track = new StackPane(budgetFillPane);
        track.getStyleClass().add("budget-track");
        StackPane.setAlignment(budgetFillPane, Pos.CENTER_LEFT);

        VBox card = new VBox(10, title, budgetSpentLbl, track);
        card.getStyleClass().add("settings-card");
        return card;
    }

    // ── Category breakdown card ───────────────────────────────────────────────

    private VBox buildCategoryCard() {
        Label title = new Label("SPENDING BY CATEGORY");
        title.getStyleClass().add("card-title");

        categoryRows = new VBox(10);

        VBox card = new VBox(12, title, categoryRows);
        card.getStyleClass().add("settings-card");
        return card;
    }


    // ════════════════════════════════════════════════════════════════════════
    //  REFRESH  (re-reads data from AppManager and updates all live labels)
    // ════════════════════════════════════════════════════════════════════════

    public void refresh() {
        refreshBalanceCard();
        refreshStatTiles();
        refreshBudgetBar();
        refreshCategoryRows();
    }

    // ── Balance ───────────────────────────────────────────────────────────────

    private void refreshBalanceCard() {
        Cycle cycle = appManager.getCurrentCycle();

        if (cycle == null) {
            balanceCycleLbl.setText("NO ACTIVE CYCLE");
            balanceAmountLbl.setText("EGP 0.00");
            balanceSubLbl.setText("Set up a cycle to start tracking");
            return;
        }

        // Header label: "TOTAL BALANCE — MAY 2026"
        String monthYear = cycle.getStartDate()
                .format(DateTimeFormatter.ofPattern("MMMM yyyy")).toUpperCase();
        balanceCycleLbl.setText("TOTAL BALANCE — " + monthYear);

        // Main amount
        double remaining = appManager.getRemainingBalance();
        balanceAmountLbl.setText(String.format("EGP %,.2f", remaining));

        // Sub label
        String start = cycle.getStartDate().format(DateTimeFormatter.ofPattern("MMM d"));
        String end   = cycle.getEndDate().format(DateTimeFormatter.ofPattern("MMM d"));
        balanceSubLbl.setText("Cycle: " + start + " → " + end);
    }

    // ── Stat tiles ────────────────────────────────────────────────────────────

    private void refreshStatTiles() {
        // Weekly spend
        double weekly = appManager.getWeeklyTotalSpent();
        weeklyValueLbl.setText(String.format("EGP %,.0f", weekly));
        weeklyValueLbl.getStyleClass().removeAll("stat-safe", "stat-warn", "stat-danger");
        weeklyValueLbl.getStyleClass().add("stat-warn");

        // Daily limit
        double daily = appManager.getDailyLimit();
        dailyValueLbl.setText(String.format("EGP %,.0f", daily));
        dailyValueLbl.getStyleClass().removeAll("stat-safe", "stat-warn", "stat-danger");
        dailyValueLbl.getStyleClass().add(daily > 0 ? "stat-safe" : "stat-danger");
    }

    // ── Budget bar ────────────────────────────────────────────────────────────

    private void refreshBudgetBar() {
        Cycle cycle = appManager.getCurrentCycle();
        double spent = appManager.getTotalSpent();

        budgetSpentLbl.setText(String.format("EGP %,.2f spent", spent));

        if (cycle == null || cycle.getTotalBudget() <= 0) {
            budgetFillPane.setMaxWidth(0);
            return;
        }

        // We update the bar width after the scene has been laid out.
        // Using a listener on the track's width ensures we always get a valid px value.
        // The fill pane sits inside a StackPane (track); we bind its maxWidth to a fraction.
        double ratio = Math.min(spent / cycle.getTotalBudget(), 1.0);

        // We can't know the track pixel width at build time, so we use a
        // post-layout callback via the parent's widthProperty.
        StackPane track = (StackPane) budgetFillPane.getParent();
        if (track != null) {
            double trackW = track.getWidth();
            if (trackW > 0) {
                budgetFillPane.setMaxWidth(trackW * ratio);
                budgetFillPane.setPrefWidth(trackW * ratio);
            } else {
                // Hook into width once it's known
                track.widthProperty().addListener((obs, o, n) -> {
                    double w = n.doubleValue() * ratio;
                    budgetFillPane.setMaxWidth(w);
                    budgetFillPane.setPrefWidth(w);
                });
            }
        }
    }

    // ── Category rows ─────────────────────────────────────────────────────────

    private void refreshCategoryRows() {
        categoryRows.getChildren().clear();

        Map<String, Double> totals = appManager.getCategoryTotals();
        if (totals == null || totals.isEmpty()) {
            Label empty = new Label("No spending data yet.");
            empty.getStyleClass().add("section-help");
            categoryRows.getChildren().add(empty);
            return;
        }

        double max = totals.values().stream().mapToDouble(Double::doubleValue).max().orElse(1);
        int colorIdx = 0;

        for (Map.Entry<String, Double> entry : totals.entrySet()) {
            String cat    = entry.getKey();
            double amount = entry.getValue();
            String color  = CAT_COLORS[colorIdx % CAT_COLORS.length];
            colorIdx++;

            categoryRows.getChildren().add(buildCategoryRow(cat, amount, max, color));
        }
    }

    private VBox buildCategoryRow(String name, double amount, double max, String color) {
        Label nameLbl = new Label(name);
        nameLbl.getStyleClass().add("cat-name");

        Label amtLbl = new Label(String.format("EGP %,.0f", amount));
        amtLbl.getStyleClass().add("cat-amount");

        HBox header = new HBox(nameLbl, amtLbl);
        HBox.setHgrow(nameLbl, Priority.ALWAYS);
        header.setAlignment(Pos.CENTER_LEFT);

        // Mini bar
        Pane fill = new Pane();
        fill.getStyleClass().add("cat-bar-fill");
        fill.setStyle("-fx-background-color: " + color + ";");

        StackPane track = new StackPane(fill);
        track.getStyleClass().add("cat-bar-track");
        StackPane.setAlignment(fill, Pos.CENTER_LEFT);

        double ratio = max > 0 ? Math.min(amount / max, 1.0) : 0;
        track.widthProperty().addListener((obs, o, n) -> {
            double w = n.doubleValue() * ratio;
            fill.setMaxWidth(w);
            fill.setPrefWidth(w);
        });

        return new VBox(5, header, track);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  DIALOGS
    // ════════════════════════════════════════════════════════════════════════

    // ── Add Expense ───────────────────────────────────────────────────────────

    private void showAddExpenseDialog() {
        Stage dialog = createDialogStage("Add Expense");

        // Amount
        Label amtLabel = new Label("Amount (EGP)");
        amtLabel.getStyleClass().add("field-label");
        TextField amtField = new TextField();
        amtField.setPromptText("0.00");
        amtField.getStyleClass().add("text-input");
        amtField.setMaxWidth(Double.MAX_VALUE);

        // Category
        Label catLabel = new Label("Category");
        catLabel.getStyleClass().add("field-label");
        ComboBox<String> catCombo = new ComboBox<>();
        catCombo.getStyleClass().add("combo-input");
        catCombo.setMaxWidth(Double.MAX_VALUE);
        catCombo.setPromptText("Select category…");

        List<String> cats = appManager.getCategories();
        catCombo.getItems().addAll(cats);
        if (!cats.isEmpty()) catCombo.getSelectionModel().selectFirst();

        // Error
        Label errorLbl = new Label();
        errorLbl.getStyleClass().add("error-label");
        errorLbl.setVisible(false);
        errorLbl.setManaged(false);

        // Submit
        Button submitBtn = new Button("Add Expense");
        submitBtn.getStyleClass().add("primary-button");
        submitBtn.setMaxWidth(Double.MAX_VALUE);

        submitBtn.setOnAction(e -> {
            errorLbl.setVisible(false);
            errorLbl.setManaged(false);

            double amount;
            try {
                amount = Double.parseDouble(amtField.getText().trim());
                if (amount <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                showError(errorLbl, "Please enter a valid positive amount.");
                return;
            }

            String selectedCat = catCombo.getSelectionModel().getSelectedItem();
            if (selectedCat == null || selectedCat.isBlank()) {
                showError(errorLbl, "Please select a category.");
                return;
            }

            int catId = appManager.getCategoryIdByName(selectedCat);
            appManager.addExpense(amount, catId, "EXPENSE");

            dialog.close();
            refresh();
            if (historyView != null) historyView.refresh();
        });

        VBox content = buildDialogContent("Add Expense", dialog,
                amtLabel, amtField, catLabel, catCombo, errorLbl, submitBtn);

        showDialog(dialog, content);
    }

    // ── Add Deposit ───────────────────────────────────────────────────────────

    private void showAddDepositDialog() {
        Stage dialog = createDialogStage("Add Deposit");

        Label amtLabel = new Label("Amount (EGP)");
        amtLabel.getStyleClass().add("field-label");
        TextField amtField = new TextField();
        amtField.setPromptText("0.00");
        amtField.getStyleClass().add("text-input");
        amtField.setMaxWidth(Double.MAX_VALUE);

        Label errorLbl = new Label();
        errorLbl.getStyleClass().add("error-label");
        errorLbl.setVisible(false);
        errorLbl.setManaged(false);

        Button submitBtn = new Button("Add Deposit");
        submitBtn.getStyleClass().add("success-button");
        submitBtn.setMaxWidth(Double.MAX_VALUE);

        submitBtn.setOnAction(e -> {
            errorLbl.setVisible(false);
            errorLbl.setManaged(false);

            double amount;
            try {
                amount = Double.parseDouble(amtField.getText().trim());
                if (amount <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                showError(errorLbl, "Please enter a valid positive amount.");
                return;
            }

            appManager.addIncome(amount);

            dialog.close();
            refresh();
            if (historyView != null) historyView.refresh();
        });

        VBox content = buildDialogContent("Add Deposit", dialog,
                amtLabel, amtField, errorLbl, submitBtn);

        showDialog(dialog, content);
    }

    // ── Dialog helpers ────────────────────────────────────────────────────────

    private Stage createDialogStage(String title) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle(title);
        return stage;
    }

    /**
     * Wraps dialog body nodes in a styled VBox with a header row (title + close button).
     */
    private VBox buildDialogContent(String titleText, Stage dialog, javafx.scene.Node... bodyNodes) {
        // Header
        Label titleLbl = new Label(titleText);
        titleLbl.getStyleClass().add("dialog-title");

        Button closeBtn = new Button("✕");
        closeBtn.getStyleClass().add("dialog-close-btn");
        closeBtn.setOnAction(e -> dialog.close());

        HBox header = new HBox(titleLbl, closeBtn);
        HBox.setHgrow(titleLbl, Priority.ALWAYS);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox content = new VBox(14);
        content.getStyleClass().add("dialog-content");
        content.setPadding(new Insets(24));
        content.setMinWidth(320);
        content.setMaxWidth(360);

        content.getChildren().add(header);

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #1E2B45; -fx-pref-height: 1;");
        content.getChildren().add(sep);

        content.getChildren().addAll(bodyNodes);
        return content;
    }

    private void showDialog(Stage dialog, VBox content) {
        StackPane root = new StackPane(content);
        root.setStyle("-fx-background-color: rgba(0,0,0,0.6);");
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root);
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);

        // Inherit stylesheet from owner window if available
        if (this.root.getScene() != null) {
            scene.getStylesheets().addAll(this.root.getScene().getStylesheets());
        }

        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void showError(Label lbl, String msg) {
        lbl.setText(msg);
        lbl.setVisible(true);
        lbl.setManaged(true);
    }
}

