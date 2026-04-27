package com.example.masroofy;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

class ExpenseDAO {

    private DataBaseManager db;

    public ExpenseDAO(DataBaseManager db) {
        this.db = db;
    }

    // بتضيف expense جديدة وبترجع الـ ID بتاعها
    // not complete
    public int addExpense(Expense e) {
        return -1;
    }

    // بتحدث بيانات expense موجودة
    // not complete
    public boolean updateExpense(Expense e) {
        return false;
    }

    // بتحذف expense بالـ ID
    // not complete
    public boolean deleteExpense(int id) {
        return false;
    }

    // بتجيب expense بالـ ID
    // not complete
    public Expense getExpenseById(int id) {
        return null;
    }

    // بتجيب كل الـ expenses بتاعة cycle معينة مرتبة بالتاريخ
    // not complete
    public List<Expense> getAllExpenses(int cycleId) {
        return new ArrayList<>();
    }

    // بتجيب الـ expenses بتاعة يوم معين — for History grouping
    // not complete
    public List<Expense> getExpensesByDate(int cycleId, LocalDate date) {
        return new ArrayList<>();
    }

    // بتحذف كل الـ expenses بتاعة cycle — بتتكلم لما resetCycle يتعمل
    // not complete
    public boolean deleteAllExpenses(int cycleId) {
        return false;
    }

    // بتجيب مجموع كل الـ expenses بتاعة cycle
    // not complete
    public double getTotalByCycle(int cycleId) {
        return 0.0;
    }

    // بتجيب مجموع كل category — for dashboard donut chart
    // not complete
    public Map<String, Double> getCategoryTotals(int cycleId) {
        return new HashMap<>();
    }
}
