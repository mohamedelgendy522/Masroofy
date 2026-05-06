package com.example.masroofy;

import java.time.LocalDate;
import java.util.*;
import java.time.LocalDateTime;

/**
 * Manages the core application logic, acting as an intermediary
 * between the user interface and the Data Access Objects (DAOs).
 */
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

    /**
     * Constructs an AppManager and initializes its corresponding DAOs.
     *
     * @param dbManager The database manager used to initialize DAOs.
     */
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

    /**
     * Registers a new user with a provided PIN and name.
     *
     * @param pin  The user's authentication PIN.
     * @param name The user's name.
     * @return The newly created user ID, or NO_USER_ID if registration fails.
     */
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

    /**
     * Authenticates a user via their user ID and PIN.
     *
     * @param userId The ID of the user attempting to log in.
     * @param pin    The PIN provided for authentication.
     * @return true if authentication is successful, false otherwise.
     */
    public boolean login(int userId, String pin) {
        String storedPin = authDAO.getPin(userId);
        if (storedPin != null && auth.verfiypin(storedPin, pin)) {
            currentUserId = userId;
            return true;
        }
        return false;
    }

    /**
     * Logs out the currently authenticated user by resetting the current user ID.
     */
    public void logout() {
        currentUserId = -1;
    }

    /**
     * Changes the PIN for the currently logged-in user.
     *
     * @param Input  The user's current PIN.
     * @param newPin The new PIN to be set.
     * @return true if the PIN was successfully changed, false otherwise.
     */
    public boolean changePin(String Input ,String newPin) {
        String oldPin = authDAO.getPin(currentUserId);
        if(auth.changepin(oldPin,Input,newPin)){
            return authDAO.updatePin(currentUserId, newPin);
        }
        else{
            return false;
        }
    }

    /**
     * Deletes the currently logged-in user's account and all associated data.
     */
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

    /**
     * Sets up a new financial cycle for the current user and populates default categories.
     *
     * @param totalBudget The total allocated budget for the cycle.
     * @param startDate   The start date of the cycle.
     * @param endDate     The end date of the cycle.
     */
    public void setupCycle(double totalBudget, LocalDate startDate, LocalDate endDate) {

        cycleDAO.setupCycle(new Cycle(currentUserId, totalBudget, startDate, endDate));

        String[] defaultCategories = {"Food", "Transport", "Health", "Entertainment", "Utilities"};
        for (String category : defaultCategories) {
            addCategory(category);
        }
    }

    /**
     * Resets the current user's active cycle.
     */
    public void resetCycle() {
        cycleDAO.resetCycle(currentUserId);

    }

    /**
     * Retrieves the current active cycle for the logged-in user.
     *
     * @return The current Cycle object, or null if none exists.
     */
    public Cycle getCurrentCycle() {
        return cycleDAO.getCycleByUser(currentUserId);
    }

    // ── EXPENSES ──────────────────────────────

    /**
     * Adds a new expense or transaction to the current cycle.
     *
     * @param amount     The monetary amount of the transaction.
     * @param categoryid The ID of the category associated with the transaction.
     * @param TYPE       The type of transaction (e.g., "EXPENSE", "DEPOSIT").
     */
    public void addExpense(double amount, int categoryid , String TYPE) {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        if (cycle == null) {
            return;
        }
        expenseDAO.addExpense(new Expense(amount,TYPE,categoryid,LocalDateTime.now(),cycle.getId()));
    }

    /**
     * Edits the details of an existing expense.
     *
     * @param id         The ID of the expense to edit.
     * @param newAmount  The updated monetary amount.
     * @param Categoryid The updated category ID.
     * @return true if the update was successful, false otherwise.
     */
    public boolean editExpense(int id, double newAmount, int Categoryid) {
        return expenseDAO.updateExpense(new Expense(id,newAmount,Categoryid));
    }

    /**
     * Deletes a specific expense by its ID.
     *
     * @param id The ID of the expense to delete.
     * @return true if the deletion was successful, false otherwise.
     */
    public boolean deleteExpense(int id) {
        return expenseDAO.deleteExpense(id);
    }

    /**
     * Retrieves all expenses associated with the current user's active cycle.
     *
     * @return A list of Expense objects.
     */
    public List<Expense> getAllExpenses() {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        if (cycle == null) {
            return new ArrayList<>();
        }
        return expenseDAO.getAllExpenses(cycle.getId());
    }

    // ── CATEGORIES ────────────────────────────

    /**
     * Adds a new custom category to the current user's active cycle.
     *
     * @param name The name of the category to add.
     */
    public void addCategory(String name) {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        if (cycle == null || name == null || name.isBlank()) {
            return;
        }
        if (!categoryDAO.categoryExists(cycle.getId(), name)) {
            categoryDAO.addCategory(cycle.getId(), name);
        }
    }

    /**
     * Retrieves a list of category names for the current active cycle.
     *
     * @return A list of category name strings.
     */
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

    /**
     * Deletes a specific category by its name from the current cycle.
     *
     * @param name The name of the category to delete.
     * @return true if the category was successfully deleted, false otherwise.
     */
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

    /**
     * Adds an income amount to the current cycle's total budget and logs it as a deposit.
     *
     * @param amount The income amount to add.
     */
    public void addIncome(double amount) {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        if (cycle == null) {
            return;
        }
        cycleDAO.addToBudget(cycle.getId(), amount);

        expenseDAO.addExpense(new Expense(amount, "DEPOSIT", 0, LocalDateTime.now(), cycle.getId()));
    }

    // ── STATS ─────────────────────────────────

    /**
     * Calculates the total amount spent within the current active cycle.
     *
     * @return The sum of all recorded expenses.
     */
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

    /**
     * Calculates the remaining available balance in the current active cycle.
     *
     * @return The remaining balance (budget minus total spent).
     */
    public double getRemainingBalance() {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        if (cycle == null) {
            return 0.0;
        }
        return cycle.getRemainigBalance(getTotalSpent());
    }

    /**
     * Calculates the recommended daily spending limit based on remaining balance and days.
     *
     * @return The daily spending limit.
     */
    public double getDailyLimit() {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        if (cycle == null) {
            return 0.0;
        }
        return cycle.calulatedailyBudget(getTotalSpent());
    }

    /**
     * Retrieves the total spent grouped by category for the current cycle.
     *
     * @return A map mapping category names to their respective total spent amounts.
     */
    public Map<String, Double> getCategoryTotals() {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        if (cycle == null) {
            return new HashMap<>();
        }
        return expenseDAO.getCategoryTotals(cycle.getId());
    }

    /**
     * Calculates the total amount spent within the current calendar week (Monday to Sunday).
     *
     * @return The weekly total spent.
     */
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

    /**
     * Retrieves all expenses recorded on the previous day.
     *
     * @return A list of yesterday's Expense objects.
     */
    public List<Expense> getYesterdayExpenses() {

        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        if (cycle == null) {
            return new ArrayList<>();
        }

        return expenseDAO.getExpensesByDate(cycle.getId(), LocalDate.now().minusDays(1));
    }

    /**
     * Retrieves all expenses recorded on the current day.
     *
     * @return A list of today's Expense objects.
     */
    public List<Expense> getTodayExpenses() {

        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        if (cycle == null) {
            return new ArrayList<>();
        }

        return expenseDAO.getExpensesByDate(cycle.getId(), LocalDate.now());
    }

    /**
     * Gets the ID of the currently authenticated user.
     *
     * @return The current user's ID, or NO_USER_ID if no user is logged in.
     */
    public int getUserId() {
        return currentUserId;
    }

    /**
     * Checks whether a user is currently logged into the application.
     *
     * @return true if a user is logged in, false otherwise.
     */
    public boolean isLoggedIn() {
        return currentUserId != NO_USER_ID;
    }

    /**
     * Retrieves the username of the currently logged-in user.
     *
     * @return The current user's name, or null if no user is logged in.
     */
    public String getCurrentUserName() {
        if (!isLoggedIn()) {
            return null;
        }
        User user = userDAO.getUserById(currentUserId);
        return user == null ? null : user.getUsername();
    }

    /**
     * Retrieves the ID of a category based on its name within the current cycle.
     *
     * @param name The name of the category to look up.
     * @return The ID of the category, or -1 if not found.
     */
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

    /**
     * Retrieves the name of a category based on its ID.
     *
     * @param id The ID of the category.
     * @return The category name, or null if the category does not exist.
     */
    public String getCategoryNameById(int id) {
        Category category = categoryDAO.getCategoryById(id);
        return category == null ? null : category.getName();
    }
}