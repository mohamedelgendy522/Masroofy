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
import javafx.scene.chart.PieChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;

/**
 * Handles the display of visual statistics and graphical representations of the user's cycle.
 */
class StatsView {

    private final AppManager appManager;
    private final Runnable onViewHistory;

    /**
     * Constructs a StatsView with the provided dependencies.
     *
     * @param appManager    The central application manager.
     * @param onViewHistory Runnable callback defining history view transitions.
     */
    StatsView(AppManager appManager, Runnable onViewHistory) {
        this.appManager = appManager;
        this.onViewHistory = onViewHistory;
    }

    /**
     * Builds the statistics application view components and graphical contexts logic contexts configurations templates settings logic.
     *
     * @return Formatted outputs constraints outputs templates formats mapping schemas definitions mapping inputs instances lists variables references mappings contexts attributes parameters.
     */
    VBox getView() {
        VBox root = new VBox();
        root.getStyleClass().add("settings-root");

        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("settings-scroll");
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox container = new VBox(20);
        container.setPadding(new Insets(28, 24, 28, 24));

        Label title = new Label("Weekly Stats");
        title.getStyleClass().add("page-title");

        VBox weeklyCard = new VBox(10);
        weeklyCard.getStyleClass().add("settings-card");
        weeklyCard.setAlignment(Pos.CENTER);

        Label totalLabel = new Label("Total Spent This Week");
        totalLabel.getStyleClass().add("card-title");

        double weeklyTotal = appManager.getWeeklyTotalSpent();
        Label amountLabel = new Label(formatAmount(weeklyTotal));
        amountLabel.setStyle("-fx-text-fill: #EDE9FF; -fx-font-size: 34px; -fx-font-weight: bold;");

        Label currencyLabel = new Label("EGP");
        currencyLabel.getStyleClass().add("section-help");

        Button historyButton = new Button("View History");
        historyButton.getStyleClass().add("ghost-button");
        historyButton.setMaxWidth(Double.MAX_VALUE);
        historyButton.setOnAction(event -> {
            if (onViewHistory != null) {
                onViewHistory.run();
            }
        });

        weeklyCard.getChildren().addAll(totalLabel, amountLabel, currencyLabel, historyButton);

        VBox breakdownCard = new VBox(12);
        breakdownCard.getStyleClass().add("settings-card");

        Label breakdownTitle = new Label("SPENDING BY CATEGORY");
        breakdownTitle.getStyleClass().add("card-title");

        PieChart pieChart = new PieChart();
        pieChart.setLegendVisible(false);
        pieChart.setLabelsVisible(false);
        pieChart.setMinHeight(200);
        pieChart.setPrefHeight(200);

        VBox rows = new VBox(10);
        rows.getChildren().addAll(buildCategoryRows(pieChart));

        breakdownCard.getChildren().addAll(breakdownTitle, pieChart, rows);

        container.getChildren().addAll(title, weeklyCard, breakdownCard);
        scroll.setContent(container);
        root.getChildren().add(scroll);

        return root;
    }

    /**
     * Extrapolates chart mappings processing values logic textual objects list items lists settings.
     *
     * @param pieChart Target mapping instances attributes templates configurations arrays representations outputs components structures.
     * @return Generated sequences variables objects references contexts instances schemas.
     */
    private List<Node> buildCategoryRows(PieChart pieChart) {
        Map<String, Double> totals = appManager.getCategoryTotals();
        List<Node> rows = new ArrayList<>();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        double total = totals.values().stream().mapToDouble(Double::doubleValue).sum();
        if (total <= 0) {
            Label empty = new Label("No data yet");
            empty.getStyleClass().add("section-help");
            rows.add(empty);
            pieChart.setVisible(false);
            pieChart.setManaged(false);
            return rows;
        }

        int[] colorIndex = {0};
        String[] colors = {"#7C3AED", "#4ADE80", "#38BDF8", "#FBBF24", "#F87171", "#C084FC", "#FB923C", "#818CF8"};

        totals.entrySet().stream()
                .sorted(Comparator.comparingDouble((Map.Entry<String, Double> e) -> e.getValue()).reversed())
                .forEach(entry -> {
                    String color = colors[colorIndex[0] % colors.length];
                    colorIndex[0]++;
                    rows.add(buildCategoryRow(entry.getKey(), entry.getValue(), total, color));

                    PieChart.Data slice = new PieChart.Data(entry.getKey(), entry.getValue());
                    pieChartData.add(slice);
                });

        pieChart.setData(pieChartData);

        // Apply colors to pie chart slices after they are added to the scene chart
        // Note: colors are applied when nodes are available, we can rely on JavaFX lookup or inline style if needed
        // For simplicity and immediate effect, we map slice node style after data is set but notice nodes might not be instantiated immediately.
        int i = 0;
        for (PieChart.Data data : pieChartData) {
            String color = colors[i % colors.length];
            if (data.getNode() != null) {
                data.getNode().setStyle("-fx-pie-color: " + color + ";");
            } else {
                data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                    if (newNode != null) {
                        newNode.setStyle("-fx-pie-color: " + color + ";");
                    }
                });
            }
            i++;
        }

        return rows;
    }

    /**
     * Interprets schemas textual sequences properties variables values bindings configurations mapping logic attributes bindings representations mappings.
     *
     * @param name   Lookup instances list objects.
     * @param amount Processed inputs outputs parameter formatting values attributes.
     * @param total  Values variables definitions.
     * @param color  Settings schemas texts setups templates attributes.
     * @return Represented textual elements sequences layouts components.
     */
    private HBox buildCategoryRow(String name, double amount, double total, String color) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        Region dot = new Region();
        dot.setMinSize(10, 10);
        dot.setMaxSize(10, 10);
        dot.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 50%;");

        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("tx-name");

        double percent = total == 0 ? 0 : (amount / total) * 100.0;
        Label percentLabel = new Label(String.format(Locale.US, "%.0f%%", percent));
        percentLabel.getStyleClass().add("tx-amount");

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        row.getChildren().addAll(dot, nameLabel, spacer, percentLabel);
        return row;
    }

    /**
     * Mappings mathematical processing mappings representations textual formats properties logic attributes values templates arrays bindings.
     *
     * @param value Constants lists mappings.
     * @return Formats items textual logic mapped properties representations bindings.
     */
    private String formatAmount(double value) {
        if (value == (long) value) {
            return String.format(Locale.US, "%d", (long) value);
        }
        return String.format(Locale.US, "%.2f", value);
    }
}