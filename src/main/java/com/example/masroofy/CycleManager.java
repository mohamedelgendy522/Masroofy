package com.example.masroofy;
import java.time.LocalDate;
public class CycleManager {

    private AuthManager authManager;
    private CycleDAO cycleDAO;
    private CategoryManager categoryManager;

    public CycleManager(AuthManager authManager, CycleDAO cycleDAO, CategoryManager categoryManager) {
        this.authManager = authManager;
        this.cycleDAO = cycleDAO;
        this.categoryManager = categoryManager;
    }

    public void setupCycle(double totalBudget, LocalDate startDate, LocalDate endDate) {
        cycleDAO.setupCycle(new Cycle(authManager.getUserId(), totalBudget, startDate, endDate));
        String[] defaultCategories = {"Food", "Transport", "Health", "Entertainment", "Utilities"};
        for (String category : defaultCategories) {
            categoryManager.addCategory(category);
        }
    }

    public void resetCycle() {
        cycleDAO.resetCycle(authManager.getUserId());
    }

    public Cycle getCurrentCycle() {
        return cycleDAO.getCycleByUser(authManager.getUserId());
    }

}
