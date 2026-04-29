package com.example.masroofy;

import java.time.LocalDate;
import java.util.*;
import java.time.LocalDate;
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
        if(auth.isValidpin(pin)){
            currentUserId = userDAO.createUser(name);;
            authDAO.savePin(currentUserId, pin);
        }
        return currentUserId;
    }

    public boolean login(int userId, String pin) {
        if(auth.isValidID(userId,currentUserId) && auth.verfiypin(authDAO.getPin(userId), pin)){
            currentUserId = userId;
            return true;
        }
        else{
            return false;
        }
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

    // ── CYCLE ─────────────────────────────────

    public void setupCycle(double totalBudget, LocalDate startDate, LocalDate endDate) {
        cycleDAO.setupCycle(new Cycle(currentUserId, totalBudget, startDate, endDate));
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
        return expenseDAO.getAllExpenses(cycle.getId());
    }

    // ── CATEGORIES ────────────────────────────

    public void addCategory(String name) {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        if (!categoryDAO.categoryExists(cycle.getId(), name)) {
            categoryDAO.addCategory(cycle.getId(), name);
        }
    }

    public List<String> getCategories() {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);

        List<Category> categories = categoryDAO.getAllCategories(cycle.getId());

        List<String> names = new ArrayList<>();

        for (Category c : categories) {
            names.add(c.getName());
        }

        return names;
    }

    public boolean deleteCategory(String name) {
        return false;
    }

    // ── INCOME ────────────────────────────────

    public void addIncome(double amount) {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        cycleDAO.addToBudget(cycle.getId(), amount);
    }

    // ── STATS ─────────────────────────────────

    public double getTotalSpent() {

        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);

        return expenseDAO.getTotalByCycle(cycle.getId());
    }

    public double getRemainingBalance() {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        return cycle.getRemainigBalance(getTotalSpent());
    }

    public double getDailyLimit() {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        return cycle.calulatedailyBudget(getTotalSpent());
    }

    public Map<String, Double> getCategoryTotals() {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        return expenseDAO.getCategoryTotals(cycle.getId());
    }

    public double getWeeklyTotalSpent() {

        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate weekEnd = weekStart.plusDays(6);

        return expenseDAO.getWeeklyTotal(cycle.getId(), weekStart, weekEnd);
    }

    public List<Expense> getYesterdayExpenses() {

        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);

        return expenseDAO.getExpensesByDate(cycle.getId(), LocalDate.now().minusDays(1));
    }

    public List<Expense> getTodayExpenses() {

        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);

        return expenseDAO.getExpensesByDate(cycle.getId(), LocalDate.now());
    }


    public int getUserId() {
        return currentUserId;
    }
}