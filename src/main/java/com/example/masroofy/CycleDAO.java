package com.example.masroofy;

class CycleDAO {

    private DataBaseManager db;

    public CycleDAO(DataBaseManager db) {
        this.db = db;
    }

    // بتنشئ الـ cycle لأول مرة لليوزر
    // not complete
    public void setupCycle(Cycle c) {
    }

    // بتجيب الـ cycle الخاصة بيوزر معين
    // not complete
    public Cycle getCycleByUser(int userId) {
        return null;
    }

    // بتجيب الـ cycle بالـ ID بتاعها
    // not complete
    public Cycle getCycleById(int id) {
        return null;
    }

    // بتحدث بيانات الـ cycle
    // not complete
    public boolean updateCycle(Cycle c) {
        return false;
    }

    // بتعمل reset للـ cycle — بتمسح الـ expenses والـ categories
    // وبترجع الـ budget والتواريخ لـ default
    // not complete
    public void resetCycle(int userId) {
    }

    // بتضيف مبلغ للـ budget بتاع الـ cycle
    // not complete
    public void addToBudget(int cycleId, double amount) {
    }
}
