package com.example.masroofy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

class StatsView {

    private final AppManager appManager;
    private final Runnable onViewHistory;

    StatsView(AppManager appManager, Runnable onViewHistory) {
        this.appManager = appManager;
        this.onViewHistory = onViewHistory;
    }

    VBox getView() {
        VBox root = new VBox(16);
        root.getStyleClass().add("stats-root");
        root.setPadding(new Insets(24, 20, 24, 20));

        Label title = new Label("Weekly Stats");
        title.getStyleClass().add("stats-title");

        VBox weeklyCard = new VBox(10);
        weeklyCard.getStyleClass().add("stats-card");
        weeklyCard.setAlignment(Pos.CENTER);

        Label totalLabel = new Label("Total Spent This Week");
        totalLabel.getStyleClass().add("stats-subtitle");

        double weeklyTotal = appManager.getWeeklyTotalSpent();
        Label amountLabel = new Label(formatAmount(weeklyTotal));
        amountLabel.getStyleClass().add("stats-amount");

        Label currencyLabel = new Label("EGP");
        currencyLabel.getStyleClass().add("stats-currency");

        Button historyButton = new Button("View History");
        historyButton.getStyleClass().add("stats-ghost-button");
        historyButton.setMaxWidth(Double.MAX_VALUE);
        historyButton.setOnAction(event -> {
            if (onViewHistory != null) {
                onViewHistory.run();
            }
        });

        weeklyCard.getChildren().addAll(totalLabel, amountLabel, currencyLabel, historyButton);

        VBox breakdownCard = new VBox(12);
        breakdownCard.getStyleClass().add("stats-card");

        Label breakdownTitle = new Label("Spending by Category");
        breakdownTitle.getStyleClass().add("stats-section-title");

        VBox rows = new VBox(10);
        rows.getChildren().addAll(buildCategoryRows());

        breakdownCard.getChildren().addAll(breakdownTitle, rows);

        root.getChildren().addAll(title, weeklyCard, breakdownCard);
        return root;
    }

    private List<Node> buildCategoryRows() {
        Map<String, Double> totals = appManager.getCategoryTotals();
        List<Node> rows = new ArrayList<>();

        double total = totals.values().stream().mapToDouble(Double::doubleValue).sum();
        if (total <= 0) {
            Label empty = new Label("No data yet");
            empty.getStyleClass().add("stats-empty");
            rows.add(empty);
            return rows;
        }

        totals.entrySet().stream()
                .sorted(Comparator.comparingDouble((Map.Entry<String, Double> e) -> e.getValue()).reversed())
                .forEach(entry -> rows.add(buildCategoryRow(entry.getKey(), entry.getValue(), total)));

        return rows;
    }

    private HBox buildCategoryRow(String name, double amount, double total) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        Region dot = new Region();
        dot.getStyleClass().add("stats-dot");

        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("stats-row-label");

        double percent = total == 0 ? 0 : (amount / total) * 100.0;
        Label percentLabel = new Label(String.format(Locale.US, "%.0f%%", percent));
        percentLabel.getStyleClass().add("stats-row-value");

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        row.getChildren().addAll(dot, nameLabel, spacer, percentLabel);
        return row;
    }

    private String formatAmount(double value) {
        if (value == (long) value) {
            return String.format(Locale.US, "%d", (long) value);
        }
        return String.format(Locale.US, "%.2f", value);
    }
}
