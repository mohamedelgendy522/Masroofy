package com.example.masroofy;

class UserDAO {

    private DataBaseManager db;

    public UserDAO(DataBaseManager db) {
        this.db = db;
    }

    // بتنشئ يوزر جديد وبترجع الـ ID بتاعه
    // not complete
    public int createUser() {
        return -1;
    }

    // بتجيب الـ User object بناءً على الـ ID
    // not complete
    public User getUserById(int id) {
        return null;
    }

    // بتحذف اليوزر من الـ DB
    // not complete
    public boolean deleteUser(int id) {
        return false;
    }
}
