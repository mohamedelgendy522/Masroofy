package com.example.masroofy;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class StatsManager {
    private  CycleDAO cycleDAO;
    private  ExpenseDAO expenseDAO;
    private  AuthDAO authDAO;
    private AuthManager authManager;

    public StatsManager(CycleDAO cycleDAO, ExpenseDAO expenseDAO, AuthManager authManager) {
        this.cycleDAO = cycleDAO;
        this.expenseDAO = expenseDAO;
        this.authManager = authManager;
    }

    public double getTotalSpent() {
        Cycle cycle = cycleDAO.getCycleByUser(authManager.getUserId());
        if (cycle == null) {
            return 0.0;
        }

        List<Expense> expenses = expenseDAO.getAllExpenses(cycle.getId());
        double total = 0.0;
        for (Expense e : expenses) {
            if ("EXPENSE".equalsIgnoreCase(e.getType())) {
                total += e.getAmount();
            }
        }
        return total;
    }

    public double getRemainingBalance() {
        Cycle cycle = cycleDAO.getCycleByUser(authManager.getUserId());
        if (cycle == null) {
            return 0.0;
        }
        return cycle.getRemainigBalance(getTotalSpent());
    }

    public double getDailyLimit() {
        Cycle cycle = cycleDAO.getCycleByUser(authManager.getUserId());
        if (cycle == null) {
            return 0.0;
        }
        return cycle.calulatedailyBudget(getTotalSpent());
    }

    public Map<String, Double> getCategoryTotals() {
        Cycle cycle = cycleDAO.getCycleByUser(authManager.getUserId());
        if (cycle == null) {
            return new HashMap<>();
        }
        return expenseDAO.getCategoryTotals(cycle.getId());
    }

    public double getWeeklyTotalSpent() {
        Cycle cycle = cycleDAO.getCycleByUser(authManager.getUserId());
        if (cycle == null) {
            return 0.0;
        }

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate weekEnd = weekStart.plusDays(6);

        double weeklyTotal = 0.0;

        List<Expense> expenses = expenseDAO.getAllExpenses(cycle.getId());

        for (Expense e : expenses) {
            if ("EXPENSE".equalsIgnoreCase(e.getType())) {
                try {
                    String dateOnly = e.getDate().toString().split("T")[0];
                    LocalDate expDate = LocalDate.parse(dateOnly);

                    if (!expDate.isBefore(weekStart) && !expDate.isAfter(weekEnd)) {
                        weeklyTotal += e.getAmount();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        return weeklyTotal;
    }

    public List<Expense> getYesterdayExpenses() {

        Cycle cycle = cycleDAO.getCycleByUser(authManager.getUserId());
        if (cycle == null) {
            return new ArrayList<>();
        }

        return expenseDAO.getExpensesByDate(cycle.getId(), LocalDate.now().minusDays(1));
    }

    public List<Expense> getTodayExpenses() {

        Cycle cycle = cycleDAO.getCycleByUser(authManager.getUserId());
        if (cycle == null) {
            return new ArrayList<>();
        }

        return expenseDAO.getExpensesByDate(cycle.getId(), LocalDate.now());
    }
}
