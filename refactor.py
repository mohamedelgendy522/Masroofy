import os
import re
ui_files = [
    "src/main/java/com/example/masroofy/DashboardView.java",
    "src/main/java/com/example/masroofy/HelloApplication.java",
    "src/main/java/com/example/masroofy/HistoryView.java",
    "src/main/java/com/example/masroofy/SettingsView.java",
    "src/main/java/com/example/masroofy/StatsView.java"
]
method_to_manager = {
    "registerUser": "getAuthManager",
    "login": "getAuthManager",
    "logout": "getAuthManager",
    "changePin": "getAuthManager",
    "deleteCurrentAccount": "getAuthManager",
    "isLoggedIn": "getAuthManager",
    "getUserId": "getAuthManager",
    "getCurrentUserName": "getAuthManager",
    "setupCycle": "getCycleManager",
    "resetCycle": "getCycleManager",
    "getCurrentCycle": "getCycleManager",
    "addExpense": "getExpenseManager",
    "editExpense": "getExpenseManager",
    "deleteExpense": "getExpenseManager",
    "getAllExpenses": "getExpenseManager",
    "getTodayExpenses": "getExpenseManager",
    "getYesterdayExpenses": "getExpenseManager",
    "addIncome": "getExpenseManager",
    "addCategory": "getCategoryManager",
    "getCategories": "getCategoryManager",
    "deleteCategory": "getCategoryManager",
    "getCategoryIdByName": "getCategoryManager",
    "getCategoryNameById": "getCategoryManager",
    "getTotalSpent": "getStatsManager",
    "getRemainingBalance": "getStatsManager",
    "getDailyLimit": "getStatsManager",
    "getWeeklyTotalSpent": "getStatsManager",
    "getCategoryTotals": "getStatsManager"
}
for file in ui_files:
    if os.path.exists(file):
        with open(file, 'r', encoding='utf-8') as f:
            content = f.read()
        def rep(m):
            method = m.group(1)
            manager = method_to_manager.get(method)
            if manager:
                return f"appManager.{manager}().{method}"
            return m.group(0)
        new_content = re.sub(r'appManager\.([a-zA-Z0-9_]+)', rep, content)
        with open(file, 'w', encoding='utf-8') as f:
            f.write(new_content)
        print(f"Updated {file}")
