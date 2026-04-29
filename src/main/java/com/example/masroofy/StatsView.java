package com.example.masroofy;

import java.util.Map;

class StatsView {

    private AppManager appManager;

    public StatsView(AppManager appManager)
    {
        this.appManager = appManager;
    }

    // NEED TO UI
    public void render()
    {
        System.out.println("Weekly Total: " + appManager.getWeeklyTotalSpent());
        System.out.println("Category Breakdown:");

        Map<String, Double> map = appManager.getCategoryTotals();

        for (Map.Entry<String, Double> entry : map.entrySet()) {
            System.out.println("- " + entry.getKey() + " : " + entry.getValue());
        }
    }
}
