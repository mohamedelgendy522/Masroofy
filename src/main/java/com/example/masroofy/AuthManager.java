package com.example.masroofy;
public class AuthManager {

    private static final int NO_USER_ID = -1;
    private int currentUserId = NO_USER_ID;
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private CycleDAO cycleDAO;
    private ExpenseDAO expenseDAO;
    private CategoryDAO categoryDAO;
    private AUTH auth;

    public AuthManager(UserDAO userDAO, AuthDAO authDAO, CycleDAO cycleDAO, ExpenseDAO expenseDAO, CategoryDAO categoryDAO, AUTH auth) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.cycleDAO = cycleDAO;
        this.expenseDAO = expenseDAO;
        this.categoryDAO = categoryDAO;
        this.auth = auth;
    }

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
       return false;
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

    public int getUserId() {
        return currentUserId;
    }

    public boolean isLoggedIn() {
        return currentUserId != NO_USER_ID;
    }

    public String getCurrentUserName() {
        if (!isLoggedIn()) return null;
        User user = userDAO.getUserById(currentUserId);
        return user == null ? null : user.getUsername();
    }
}
