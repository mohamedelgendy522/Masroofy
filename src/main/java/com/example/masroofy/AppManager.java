package com.example.masroofy;

import java.time.LocalDate;
import java.util.*;
import java.time.LocalDateTime;

public class AppManager {

    private static final int NO_USER_ID = -1;

    private int currentUserId;
    private  UserDAO userDAO;
    private  CycleDAO cycleDAO;
    private  ExpenseDAO expenseDAO;
    private  AuthDAO authDAO;
    private  AUTH auth;
    private  CategoryDAO categoryDAO;
    private  DataBaseManager dbManager;

    public AppManager(DataBaseManager dbManager) {
        this.currentUserId = NO_USER_ID;
        this.dbManager = dbManager;
        this.userDAO = new UserDAO(dbManager);
        this.cycleDAO = new CycleDAO(dbManager);
        this.expenseDAO = new ExpenseDAO(dbManager);
        this.authDAO = new AuthDAO(dbManager);
        this.categoryDAO = new CategoryDAO(dbManager);
        this.auth = new AUTH();
    }

    // ── AUTH ──────────────────────────────────

    public int registerUser(String pin ,String name) {
        if (name == null || name.isBlank()) {
            return NO_USER_ID;
        }

        if (!auth.isValidpin(pin)) {
            return NO_USER_ID;
        }

        currentUserId = userDAO.createUser(name.trim());
        if (currentUserId == NO_USER_ID) {
            return NO_USER_ID;
        }
        authDAO.savePin(currentUserId, pin);
        return currentUserId;
    }

    public boolean login(int userId, String pin) {
        String storedPin = authDAO.getPin(userId);
        if (storedPin != null && auth.verfiypin(storedPin, pin)) {
            currentUserId = userId;
            return true;
        }
        return false;
    }

    public void logout() {
        currentUserId = -1;
    }

    public boolean changePin(String Input ,String newPin) {
       String oldPin = authDAO.getPin(currentUserId);
       if(auth.changepin(oldPin,Input,newPin)){
           return authDAO.updatePin(currentUserId, newPin);
       }
       else{
           return false;
       }
    }

    public void deleteCurrentAccount() {
        if (!isLoggedIn()) return;

        expenseDAO.deleteAllExpensesByUserId(currentUserId);
        categoryDAO.deleteAllCategoriesByUserId(currentUserId);
        cycleDAO.deleteAllCyclesByUserId(currentUserId);
        authDAO.deleteAuth(currentUserId);
        userDAO.deleteUser(currentUserId);

        logout();
    }

    // ── CYCLE ─────────────────────────────────

    public void setupCycle(double totalBudget, LocalDate startDate, LocalDate endDate) {

        cycleDAO.setupCycle(new Cycle(currentUserId, totalBudget, startDate, endDate));

        String[] defaultCategories = {"Food", "Transport", "Health", "Entertainment", "Utilities"};
        for (String category : defaultCategories) {
            addCategory(category);
        }
    }

    public void resetCycle() {
        cycleDAO.resetCycle(currentUserId);

    }

    public Cycle getCurrentCycle() {
        return cycleDAO.getCycleByUser(currentUserId);
    }

    // ── EXPENSES ──────────────────────────────

    public void addExpense(double amount, int categoryid , String TYPE) {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        if (cycle == null) {
            return;
        }
        expenseDAO.addExpense(new Expense(amount,TYPE,categoryid,LocalDateTime.now(),cycle.getId()));
    }

    public boolean editExpense(int id, double newAmount, int Categoryid) {
        return expenseDAO.updateExpense(new Expense(id,newAmount,Categoryid));
    }

    public boolean deleteExpense(int id) {
        return expenseDAO.deleteExpense(id);
    }

    public List<Expense> getAllExpenses() {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        if (cycle == null) {
            return new ArrayList<>();
        }
        return expenseDAO.getAllExpenses(cycle.getId());
    }

    // ── CATEGORIES ────────────────────────────

    public void addCategory(String name) {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        if (cycle == null || name == null || name.isBlank()) {
            return;
        }
        if (!categoryDAO.categoryExists(cycle.getId(), name)) {
            categoryDAO.addCategory(cycle.getId(), name);
        }
    }

    public List<String> getCategories() {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        if (cycle == null) {
            return new ArrayList<>();
        }

        List<Category> categories = categoryDAO.getAllCategories(cycle.getId());

        List<String> names = new ArrayList<>();

        for (Category c : categories) {
            names.add(c.getName());
        }

        return names;
    }

    public boolean deleteCategory(String name) {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        if (cycle == null || name == null) {
            return false;
        }

        List<Category> categories = categoryDAO.getAllCategories(cycle.getId());
        for (Category category : categories) {
            if (category.getName().equals(name)) {
                return categoryDAO.deleteCategory(category.getId());
            }
        }

        return false;
    }

    // ── INCOME ────────────────────────────────

    public void addIncome(double amount) {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        if (cycle == null) {
            return;
        }
        cycleDAO.addToBudget(cycle.getId(), amount);

        expenseDAO.addExpense(new Expense(amount, "DEPOSIT", 0, LocalDateTime.now(), cycle.getId()));
    }

    // ── STATS ─────────────────────────────────

    public double getTotalSpent() {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
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
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        if (cycle == null) {
            return 0.0;
        }
        return cycle.getRemainigBalance(getTotalSpent());
    }

    public double getDailyLimit() {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        if (cycle == null) {
            return 0.0;
        }
        return cycle.calulatedailyBudget(getTotalSpent());
    }

    public Map<String, Double> getCategoryTotals() {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        if (cycle == null) {
            return new HashMap<>();
        }
        return expenseDAO.getCategoryTotals(cycle.getId());
    }

    public double getWeeklyTotalSpent() {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        if (cycle == null) {
            return 0.0;
        }

        LocalDate today = LocalDate.now();
        // بنحدد بداية الأسبوع (الاثنين) ونهايته (الأحد)
        LocalDate weekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate weekEnd = weekStart.plusDays(6);

        double weeklyTotal = 0.0;

        // ✅ الحل: هنجيب كل المصاريف ونفلترها هنا في الجافا بدل الداتابيز عشان نتفادى مشكلة الوقت
        List<Expense> expenses = expenseDAO.getAllExpenses(cycle.getId());

        for (Expense e : expenses) {
            // نتأكد إنها مصروفات مش إيداع
            if ("EXPENSE".equalsIgnoreCase(e.getType())) {
                try {
                    // نقص جزء الوقت زي ما عملنا في HistoryView
                    String dateOnly = e.getDate().toString().split("T")[0];
                    LocalDate expDate = LocalDate.parse(dateOnly);

                    // لو التاريخ بتاع المصروف ده جوه الأسبوع الحالي، اجمعه
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

        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        if (cycle == null) {
            return new ArrayList<>();
        }

        return expenseDAO.getExpensesByDate(cycle.getId(), LocalDate.now().minusDays(1));
    }

    public List<Expense> getTodayExpenses() {

        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        if (cycle == null) {
            return new ArrayList<>();
        }

        return expenseDAO.getExpensesByDate(cycle.getId(), LocalDate.now());
    }


    public int getUserId() {
        return currentUserId;
    }

    public boolean isLoggedIn() {
        return currentUserId != NO_USER_ID;
    }

    public String getCurrentUserName() {
        if (!isLoggedIn()) {
            return null;
        }
        User user = userDAO.getUserById(currentUserId);
        return user == null ? null : user.getUsername();
    }

    public int getCategoryIdByName(String name) {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        if (cycle == null || name == null) {
            return -1;
        }

        List<Category> categories = categoryDAO.getAllCategories(cycle.getId());
        for (Category category : categories) {
            if (category.getName().equals(name)) {
                return category.getId();
            }
        }

        return -1;
    }

    public String getCategoryNameById(int id) {
        Category category = categoryDAO.getCategoryById(id);
        return category == null ? null : category.getName();
    }
}