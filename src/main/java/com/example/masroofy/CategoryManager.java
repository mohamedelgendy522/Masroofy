package com.example.masroofy;
import java.util.ArrayList;
import java.util.List;
public class CategoryManager {

    private AuthManager authManager;
    private CycleDAO cycleDAO;
    private CategoryDAO categoryDAO;

    public CategoryManager(AuthManager authManager, CycleDAO cycleDAO, CategoryDAO categoryDAO) {
        this.authManager = authManager;
        this.cycleDAO = cycleDAO;
        this.categoryDAO = categoryDAO;
    }

    public void addCategory(String name) {
        Cycle cycle = cycleDAO.getCycleByUser(authManager.getUserId());
        if (cycle == null || name == null || name.isBlank()) {
            return;
        }
        if (!categoryDAO.categoryExists(cycle.getId(), name)) {
            categoryDAO.addCategory(cycle.getId(), name);
        }
    }
    public List<String> getCategories() {

        Cycle cycle = cycleDAO.getCycleByUser(authManager.getUserId());

        if (cycle == null) {
            return new ArrayList<>();
        }

        List<Category> categories = categoryDAO.getAllCategories(cycle.getId());

        List<String> names = new ArrayList<>();

        for (Category c : categories) {
            names.add(c.getName());
        }

        return names;
    }


    public boolean deleteCategory(String name) {

        Cycle cycle = cycleDAO.getCycleByUser(authManager.getUserId());
        if (cycle == null || name == null) {
            return false;
        }
        List<Category> categories = categoryDAO.getAllCategories(cycle.getId());

        for (Category category : categories) {
            if (category.getName().equals(name)) {
                return categoryDAO.deleteCategory(category.getId());
            }
        }
        return false;
    }


    public int getCategoryIdByName(String name) {

        Cycle cycle = cycleDAO.getCycleByUser(authManager.getUserId());

        if (cycle == null || name == null) {
            return -1;
        }

        List<Category> categories = categoryDAO.getAllCategories(cycle.getId());
        for (Category category : categories) {
            if (category.getName().equals(name)) return category.getId();
        }

        return -1;

    }
    public String getCategoryNameById(int id) {
        Category category = categoryDAO.getCategoryById(id);
        return category == null ? null : category.getName();
    }
}
