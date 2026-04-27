package com.example.masroofy;

import java.time.LocalDate;
import java.util.*;

// ─────────────────────────────────────────────
// AppManager
// الكلاس الرئيسية اللي بتتحكم في كل العمليات
// هي الوسيط بين الـ Views والـ DAO layer
// كل حاجة بتعدي منها — مش بيكلموا الـ DAO مباشرة
// ─────────────────────────────────────────────
public class AppManager {

    private int currentUserId;
    private UserDAO userDAO;
    private CycleDAO cycleDAO;
    private ExpenseDAO expenseDAO;
    private AuthDAO authDAO;
    private CategoryDAO categoryDAO;

    public AppManager() {
        // not complete
    }

    // ── AUTH ──────────────────────────────────

    // بتسجل يوزر جديد بالـ PIN وبترجع الـ ID بتاعه
    // اليوزر لازم يحفظ الـ ID ده عشان يقدر يدخل تاني
    // not complete
    public int registerUser(String pin) {
        return -1;
    }

    // بتتحقق من الـ userId والـ PIN وبتسمح بالدخول
    // not complete
    public boolean login(int userId, String pin) {
        return false;
    }

    // بتخرج اليوزر وبتمسح الـ currentUserId
    // not complete
    public void logout() {
    }

    // بتغير الـ PIN بعد ما تتأكد من الـ PIN القديم
    // not complete
    public boolean changePin(String oldPin, String newPin) {
        return false;
    }

    // بتحذف اليوزر وكل بياناته من الـ DB
    // not complete
    public void deleteUser() {
    }

    // ── CYCLE ─────────────────────────────────

    // بتنشئ الـ cycle لأول مرة لليوزر بالـ budget والتواريخ
    // not complete
    public void setupCycle(double totalBudget, LocalDate startDate, LocalDate endDate) {
    }

    // بتعمل reset للـ cycle — بتمسح كل الـ expenses والـ categories
    // not complete
    public void resetCycle() {
    }

    // بترجع الـ cycle الحالية بتاعة اليوزر
    // not complete
    public Cycle getCurrentCycle() {
        return null;
    }

    // ── EXPENSES ──────────────────────────────

    // بتضيف expense جديدة للـ cycle الحالية
    // not complete
    public void addExpense(double amount, String category) {
    }

    // بتعدل expense موجودة بالـ ID بتاعها
    // not complete
    public boolean editExpense(int id, double newAmount, String newCategory) {
        return false;
    }

    // بتحذف expense بالـ ID
    // not complete
    public boolean deleteExpense(int id) {
        return false;
    }

    // بترجع كل الـ expenses بتاعة الـ cycle الحالية
    // not complete
    public List<Expense> getAllExpenses() {
        return new ArrayList<>();
    }

    // ── CATEGORIES ────────────────────────────

    // بتضيف category جديدة للـ cycle الحالية
    // not complete
    public void addCategory(String name) {
    }

    // بترجع كل الـ categories بتاعة الـ cycle الحالية
    // not complete
    public List<String> getCategories() {
        return new ArrayList<>();
    }

    // بتحذف category بالاسم
    // not complete
    public boolean deleteCategory(String name) {
        return false;
    }

    // ── INCOME ────────────────────────────────

    // بتضيف income للـ cycle — بتزود الـ budget
    // not complete
    public void addIncome(double amount) {
    }

    // ── STATS ─────────────────────────────────

    // بترجع مجموع كل الـ expenses في الـ cycle الحالية
    // not complete
    public double getTotalSpent() {
        return 0.0;
    }

    // بترجع المبلغ المتبقي من الـ budget
    // not complete
    public double getRemainingBalance() {
        return 0.0;
    }

    // بترجع الحد اليومي المسموح بيه بناءً على الأيام المتبقية
    // not complete
    public double getDailyLimit() {
        return 0.0;
    }

    // بترجع مجموع الـ expenses لكل category — for donut chart
    // not complete
    public Map<String, Double> getCategoryTotals() {
        return new HashMap<>();
    }

    // بترجع مجموع الـ expenses بتاعة الأسبوع الحالي
    // not complete
    public double getWeeklyTotalSpent() {
        return 0.0;
    }

    // بترجع الـ expenses بتاعة امبارح — for History screen grouping
    // not complete
    public List<Expense> getYesterdayExpenses() {
        return new ArrayList<>();
    }

    // بترجع الـ expenses بتاعة النهارده — for History screen grouping
    // not complete
    public List<Expense> getTodayExpenses() {
        return new ArrayList<>();
    }

    // بترجع الـ ID بتاع اليوزر الحالي — for Settings screen
    // not complete
    public int getUserId() {
        return -1;
    }
}