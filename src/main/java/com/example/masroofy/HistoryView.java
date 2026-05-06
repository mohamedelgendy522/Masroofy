package com.example.masroofy;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import java.time.*;

import java.util.*;
import java.util.ArrayList;

/**
 * Manages the UI to display user's transaction history.
 */
public class HistoryView {
    private AppManager appManager;
    private List<Transaction> transactions;

    /**
     * Constructs a HistoryView and initializes the UI components.
     *
     * @param appManager The central application manager.
     */
    public HistoryView(AppManager appManager) {
        this.appManager = appManager;

        List<Expense> expenses = appManager.getAllExpenses();
        transactions = mapExpenses(expenses);

        root = new VBox(0);
        root.getStyleClass().add("settings-root");

        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("settings-scroll");

        VBox container = new VBox(16);
        container.setPadding(new Insets(28, 24, 28, 24));

        Label title = createTitle();
        HBox filterBar = createFilterBar();
        listContainer = new VBox(10);

        renderList();

        container.getChildren().addAll(title, filterBar, listContainer);
        scroll.setContent(container);
        root.getChildren().add(scroll);
    }

    /**
     * Maps database Expense objects to the internal Transaction model for view formatting.
     *
     * @param expenses The list of raw expenses to format.
     * @return A list of Transaction objects.
     */
    private List<Transaction> mapExpenses(List<Expense> expenses) {
        List<Transaction> list = new ArrayList<>();

        for (Expense e : expenses) {

            String type = e.getType(); // "EXPENSE" أو "INCOME"
            String category;
            if ("DEPOSIT".equalsIgnoreCase(type)) {
                category = "Income";
            } else {
                category = appManager.getCategoryNameById(e.getCategoryid());
                if (category == null) category = "Unknown";
            }
            double amount = e.getAmount();
            String date = e.getDate().toString();

            list.add(new Transaction(
                    type,
                    category,
                    category,
                    amount,
                    date
            ));
        }

        return list;
    }

    /**
     * Refreshes the internal history list and view representation.
     */
    public void refresh() {
        List<Expense> expenses = appManager.getAllExpenses();
        transactions = mapExpenses(expenses);   // mapExpenses() is already private — no change needed
        renderList();                            // renderList() is already private — no change needed
    }

    // ── Filter state ────────────────────────────────────────
    private String activeFilter = "ALL";   // ALL | EXPENSE | DEPOSIT

    // ── UI containers rebuilt on filter change ──────────────
    private VBox listContainer;


    // ── Root ────────────────────────────────────────────────
    private final VBox root;

    /**
     * Retrieves the root layout of the HistoryView.
     *
     * @return The root VBox layout.
     */
    public VBox getView() { return root; }

    // ========================================================
    //  TITLE
    // ========================================================
    /**
     * Creates the view title label.
     *
     * @return A constructed title Label.
     */
    private Label createTitle() {
        Label title = new Label("History");
        title.getStyleClass().add("page-title");
        return title;
    }

    // ========================================================
    //  FILTER BAR
    // ========================================================
    /**
     * Creates a filter bar for managing displayed transactions.
     *
     * @return An HBox component comprising the filters.
     */
    private HBox createFilterBar() {
        Button allBtn     = createFilterButton("All",      "ALL");
        Button expenseBtn = createFilterButton("Expenses", "EXPENSE");
        Button depositBtn = createFilterButton("Deposits", "DEPOSIT");

        // Mark "All" active on startup
        allBtn.getStyleClass().add("filter-btn-active");

        allBtn.setOnAction(e -> {
            activeFilter = "ALL";
            resetFilterButtons(allBtn, expenseBtn, depositBtn);
            allBtn.getStyleClass().add("filter-btn-active");
            renderList();
        });

        expenseBtn.setOnAction(e -> {
            activeFilter = "EXPENSE";
            resetFilterButtons(allBtn, expenseBtn, depositBtn);
            expenseBtn.getStyleClass().add("filter-btn-active");
            renderList();
        });

        depositBtn.setOnAction(e -> {
            activeFilter = "DEPOSIT";
            resetFilterButtons(allBtn, expenseBtn, depositBtn);
            depositBtn.getStyleClass().add("filter-btn-active");
            renderList();
        });

        HBox bar = new HBox(8, allBtn, expenseBtn, depositBtn);
        bar.setAlignment(Pos.CENTER_LEFT);
        return bar;
    }

    /**
     * Instantiates a single filter button.
     *
     * @param text   The visible text on the button.
     * @param filter The underlying filter string value.
     * @return The configured Button.
     */
    private Button createFilterButton(String text, String filter) {
        Button btn = new Button(text);
        btn.getStyleClass().add("filter-btn");
        return btn;
    }

    /**
     * Resets visual states of specific filter buttons.
     *
     * @param buttons A variable argument array of Buttons to reset.
     */
    private void resetFilterButtons(Button... buttons) {
        for (Button b : buttons) {
            b.getStyleClass().remove("filter-btn-active");
        }
    }

    // ========================================================
    //  TRANSACTION LIST  (rebuilt on every filter change)
    // ========================================================
    /**
     * Re-renders the transaction list elements into the UI, based on filter.
     */
    private void renderList() {
        listContainer.getChildren().clear();

        // Filter transactions
        List<Transaction> filtered = new ArrayList<>();
        for (Transaction t : transactions) {
            if (activeFilter.equals("ALL") || t.type.equals(activeFilter)) {
                filtered.add(t);
            }
        }

        if (filtered.isEmpty()) {
            listContainer.getChildren().add(createEmptyState());
            return;
        }

        // Group by date
        String lastDateHeader = "";
        for (Transaction t : filtered) {
            String currentHeader = formatDate(t.date);

            if (!currentHeader.equals(lastDateHeader)) {
                lastDateHeader = currentHeader;
                listContainer.getChildren().add(createDateHeader(currentHeader));
            }
            listContainer.getChildren().add(createTransactionRow(t));
        }
    }

    // ========================================================
    //  DATE HEADER
    // ========================================================
    /**
     * Creates a date group header label.
     *
     * @param dateText The formatted date string.
     * @return The Label containing the text header.
     */
    private Label createDateHeader(String dateText) {
        Label lbl = new Label(dateText.toUpperCase());
        lbl.getStyleClass().add("history-date-header");
        lbl.setPadding(new Insets(8, 0, 2, 0));
        return lbl;
    }

    // ========================================================
    //  SINGLE TRANSACTION ROW
    // ========================================================
    /**
     * Creates the view row for a specific transaction layout.
     *
     * @param t The transaction item.
     * @return The resulting HBox containing the transaction UI elements.
     */
    private HBox createTransactionRow(Transaction t) {

        Label icon = new Label(categoryEmoji(t.category));
        icon.getStyleClass().addAll("tx-icon", "tx-icon-" + t.type.toLowerCase());


        Label nameLbl = new Label(t.name);
        nameLbl.getStyleClass().add("tx-name");

        Label catTag = new Label(t.category);
        catTag.getStyleClass().addAll("tag", categoryTagClass(t.category));

        VBox middle = new VBox(4, nameLbl, catTag);
        middle.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(middle, Priority.ALWAYS);


        boolean isExpense = t.type.equals("EXPENSE");
        String  sign      = isExpense ? "-" : "+";
        Label   amtLbl    = new Label(sign + "EGP " + String.format("%,.2f", t.amount));
        amtLbl.getStyleClass().addAll("tx-amount",
                isExpense ? "tx-amount-expense" : "tx-amount-deposit");

        Label typeBadge = new Label(t.type);
        typeBadge.getStyleClass().addAll("tx-badge",
                isExpense ? "tx-badge-expense" : "tx-badge-deposit");

        VBox right = new VBox(4, amtLbl, typeBadge);
        right.setAlignment(Pos.CENTER_RIGHT);

        // ── Assemble row ─────────────────────────────────────
        HBox row = new HBox(12, icon, middle, right);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("tx-row");
        row.setPadding(new Insets(14, 16, 14, 16));


        return row;
    }

    // ========================================================
    //  EMPTY STATE
    // ========================================================
    /**
     * Creates an empty state UI when no transactions match.
     *
     * @return A VBox encapsulating the empty state.
     */
    private VBox createEmptyState() {
        Label icon = new Label("📭");
        icon.setStyle("-fx-font-size: 36px;");

        Label msg = new Label("No transactions yet");
        msg.getStyleClass().add("empty-state-title");

        Label sub = new Label("Add an expense or deposit from the Dashboard.");
        sub.getStyleClass().add("empty-state-sub");
        sub.setWrapText(true);

        VBox box = new VBox(10, icon, msg, sub);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(48, 24, 48, 24));
        box.getStyleClass().add("settings-card");
        box.setMaxWidth(Double.MAX_VALUE);
        return box;
    }


    /**
     * Formats a raw database date string into human-readable text.
     *
     * @param raw The raw date string to evaluate.
     * @return The mapped friendly date string format.
     */
    private String formatDate(String raw) {

        String dateOnly = raw.split("T")[0];

        String[] parts = dateOnly.split("-");
        if (parts.length != 3) return dateOnly;

        int year  = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day   = Integer.parseInt(parts[2]);

        String[] months = {
                "", "January","February","March","April","May","June",
                "July","August","September","October","November","December"
        };

        // Date matching format for Today and Yesterday logic
        String today     = LocalDate.now().toString();
        String yesterday = LocalDate.now().minusDays(1).toString();

        if (dateOnly.equals(today))     return "Today, " + months[month] + " " + day;
        if (dateOnly.equals(yesterday)) return "Yesterday, " + months[month] + " " + day;

        return months[month] + " " + day + ", " + year;
    }

    /**
     * Resolves the CSS style class specific to a category mapping.
     *
     * @param category The name of the category.
     * @return A CSS class specific string for styling.
     */
    private String categoryTagClass(String category) {
        return switch (category) {
            case "Food"          -> "tag-food";
            case "Transport"     -> "tag-transport";
            case "Health"        -> "tag-health";
            case "Entertainment" -> "tag-ent";
            case "Utilities"     -> "tag-util";
            default              -> "tag-income";   // Income / Other
        };
    }

    /**
     * Maps a category string to an emoji.
     *
     * @param category The string category representation.
     * @return The emoji corresponding to the category.
     */
    private String categoryEmoji(String category) {
        return switch (category) {
            case "Food"          -> "🍕";
            case "Transport"     -> "🚇";
            case "Health"        -> "💊";
            case "Entertainment" -> "🎬";
            case "Utilities"     -> "⚡";
            case "Income"        -> "💸";
            default              -> "📦";
        };
    }

    // ========================================================
    // TRANSACTION MODEL
    // ========================================================
    /**
     * Represents an internal data model structure for the history view component.
     */
    static class Transaction {
        final String type;       // "EXPENSE" | "DEPOSIT"
        final String name;       // display name (e.g. "Pizza Palace")
        final String category;   // (e.g. "Food")
        final double amount;
        final String date;       // "yyyy-MM-dd"

        /**
         * Initializes a structured internal transaction model.
         *
         * @param type     The transaction type.
         * @param name     The display name for reference.
         * @param category The string mapped category value.
         * @param amount   The specific operation amount.
         * @param date     The string formatted transaction date.
         */
        Transaction(String type, String name, String category,
                    double amount, String date) {
            this.type     = type;
            this.name     = name;
            this.category = category;
            this.amount   = amount;
            this.date     = date;
        }
    }
}