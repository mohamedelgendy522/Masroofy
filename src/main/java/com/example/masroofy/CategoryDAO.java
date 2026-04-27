package com.example.masroofy;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

class CategoryDAO {

    private DataBaseManager db;

    public CategoryDAO(DataBaseManager db) {
        this.db = db;
    }

    // بتضيف category جديدة للـ cycle وبترجع الـ ID
    // not complete
    public int addCategory(int cycleId, String name) {
        return -1;
    }

    // بتجيب category بالـ ID
    // not complete
    public Category getCategoryById(int id) {
        return null;
    }

    // بتجيب كل الـ categories بتاعة cycle معينة
    // not complete
    public List<Category> getAllCategories(int cycleId) {
        return new ArrayList<>();
    }

    // بتحذف category بالـ ID
    // not complete
    public boolean deleteCategory(int id) {
        return false;
    }

    // بتحذف كل الـ categories بتاعة cycle — بتتكلم لما resetCycle يتعمل
    // not complete
    public boolean deleteAllCategories(int cycleId) {
        return false;
    }

    // بتتحقق إن الـ category مش موجودة قبل ما تضيفها
    // not complete
    public boolean categoryExists(int cycleId, String name) {
        return false;
    }
}
