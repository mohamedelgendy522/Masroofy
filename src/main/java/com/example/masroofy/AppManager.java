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

    // بتسجل يوزر جديد بالـ PIN وبترجع الـ ID بتاعه
    // اليوزر لازم يحفظ الـ ID ده عشان يقدر يدخل تاني
    public int registerUser(String pin ,String name) {
        if(auth.isValidpin(pin)){
            currentUserId = userDAO.createUser(name);;
            authDAO.savePin(currentUserId, pin);
        }
        return currentUserId;
    }

    // بتتحقق من الـ userId والـ PIN وبتسمح بالدخول
    public boolean login(int userId, String pin) {
        if(auth.isValidID(userId,currentUserId) && auth.verfiypin(authDAO.getPin(userId), pin)){
            currentUserId = userId;
            return true;
        }
        else{
            return false;
        }
    }

    // بتخرج اليوزر وبتمسح الـ currentUserId
    public void logout() {
        currentUserId = -1;
    }

    // بتغير الـ PIN بعد ما تتأكد من الـ PIN القديم
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

    // بتنشئ الـ cycle لأول مرة لليوزر بالـ budget والتواريخ
    public void setupCycle(double totalBudget, LocalDate startDate, LocalDate endDate) {
        cycleDAO.setupCycle(new Cycle(currentUserId, totalBudget, startDate, endDate));
    }

    // بتعمل reset للـ cycle — بتمسح كل الـ expenses والـ categories
    public void resetCycle() {
        cycleDAO.resetCycle(currentUserId);
    }

    // بترجع الـ cycle الحالية بتاعة اليوزر
    public Cycle getCurrentCycle() {
        return cycleDAO.getCycleByUser(currentUserId);
    }

    // ── EXPENSES ──────────────────────────────

    // بتضيف expense جديدة للـ cycle الحالية
    public void addExpense(double amount, int categoryid , String TYPE) {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        expenseDAO.addExpense(new Expense(amount,TYPE,categoryid,LocalDateTime.now(),cycle.getId()));
    }

    // بتعدل expense موجودة بالـ ID بتاعها
    public boolean editExpense(int id, double newAmount, int Categoryid) {
        return expenseDAO.updateExpense(new Expense(id,newAmount,Categoryid));
    }

    // بتحذف expense بالـ ID
    public boolean deleteExpense(int id) {
        return expenseDAO.deleteExpense(id);
    }

    // بترجع كل الـ expenses بتاعة الـ cycle الحالية
    public List<Expense> getAllExpenses() {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        return expenseDAO.getAllExpenses(cycle.getId());
    }

    // ── CATEGORIES ────────────────────────────

    // بتضيف category جديدة للـ cycle الحالية
    public void addCategory(String name) {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        if (!categoryDAO.categoryExists(cycle.getId(), name)) {
            categoryDAO.addCategory(cycle.getId(), name);
        }
    }

    // بترجع كل الـ categories بتاعة الـ cycle الحالية
    public List<String> getCategories() {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);

        List<Category> categories = categoryDAO.getAllCategories(cycle.getId());

        List<String> names = new ArrayList<>();

        for (Category c : categories) {
            names.add(c.getName());
        }

        return names;
    }

    // بتحذف category بالاسم
    // not complete
    public boolean deleteCategory(String name) {
        return false;
    }

    // ── INCOME ────────────────────────────────

    // بتضيف income للـ cycle — بتزود الـ budget
    public void addIncome(double amount) {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        cycleDAO.addToBudget(cycle.getId(), amount);
    }

    // ── STATS ─────────────────────────────────

    public double getTotalSpent() {

        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);

        return expenseDAO.getTotalByCycle(cycle.getId());
    }

    // بترجع المبلغ المتبقي من الـ budget
    public double getRemainingBalance() {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        return cycle.getRemainigBalance(getTotalSpent());
    }

    // بترجع الحد اليومي المسموح بيه بناءً على الأيام المتبقية
    public double getDailyLimit() {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        return cycle.calulatedailyBudget(getTotalSpent());
    }

    // بترجع مجموع الـ expenses لكل category — for donut chart
    public Map<String, Double> getCategoryTotals() {
        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);
        return expenseDAO.getCategoryTotals(cycle.getId());
    }

    // بترجع مجموع الـ expenses بتاعة الأسبوع الحالي
    public double getWeeklyTotalSpent() {

        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(today.getDayOfWeek().getValue() - 1); // Monday
        LocalDate weekEnd = weekStart.plusDays(6); // Sunday

        return expenseDAO.getWeeklyTotal(cycle.getId(), weekStart, weekEnd);
    }
    // بترجع الـ expenses بتاعة امبارح — for History screen grouping
    // not complete
    public List<Expense> getYesterdayExpenses() {

        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);

        return expenseDAO.getExpensesByDate(cycle.getId(), LocalDate.now().minusDays(1));
    }

    // بترجع الـ expenses بتاعة النهارده — for History screen grouping
    public List<Expense> getTodayExpenses() {

        Cycle cycle = cycleDAO.getCycleByUser(currentUserId);

        return expenseDAO.getExpensesByDate(cycle.getId(), LocalDate.now());
    }


    public int getUserId() {
        return currentUserId;
    }
}