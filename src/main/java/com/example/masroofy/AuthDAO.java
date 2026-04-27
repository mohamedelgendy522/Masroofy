package com.example.masroofy;

class AuthDAO {

    private DataBaseManager db;

    public AuthDAO(DataBaseManager db) {
        this.db = db;
    }

    // بتحفظ الـ pin hash في DB للـ user
    // not complete
    public boolean savePin(int userId, String pinHash) {
        return false;
    }

    // بتجيب الـ من DB عشان تتحقق منه
    // not complete
    public String getPin(int userId) {
        return null;
    }

    // بتحدث الـ pin hash لما اليوزر يغير الـ PIN
    // not complete
    public boolean updatePin(int userId, String newPinHash) {
        return false;
    }
}
