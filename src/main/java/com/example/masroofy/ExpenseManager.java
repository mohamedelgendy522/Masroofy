package com.example.masroofy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ExpenseManager {

    private  CycleDAO cycleDAO;
    private  ExpenseDAO expenseDAO;
    private AuthManager authManager;

    public ExpenseManager(CycleDAO cycleDAO, ExpenseDAO expenseDAO, AuthManager authManager) {
        this.cycleDAO = cycleDAO;
        this.expenseDAO = expenseDAO;
        this.authManager = authManager;
    }

    public void addExpense(double amount, int categoryid , String TYPE) {
        Cycle cycle = cycleDAO.getCycleByUser(authManager.getUserId());
        if (cycle == null) {
            return;
        }
        expenseDAO.addExpense(new Expense(amount,TYPE,categoryid, LocalDateTime.now(),cycle.getId()));
    }

    public boolean editExpense(int id, double newAmount, int Categoryid) {
        return expenseDAO.updateExpense(new Expense(id,newAmount,Categoryid));
    }

    public boolean deleteExpense(int id) {
        return expenseDAO.deleteExpense(id);
    }

    public List<Expense> getAllExpenses() {
        Cycle cycle = cycleDAO.getCycleByUser(authManager.getUserId());
        if (cycle == null) {
            return new ArrayList<>();
        }
        return expenseDAO.getAllExpenses(cycle.getId());
    }

    public void addIncome(double amount) {
        Cycle cycle = cycleDAO.getCycleByUser(authManager.getUserId());
        if (cycle == null) {
            return;
        }
        cycleDAO.addToBudget(cycle.getId(), amount);

        expenseDAO.addExpense(new Expense(amount, "DEPOSIT", 0, LocalDateTime.now(), cycle.getId()));
    }
}
