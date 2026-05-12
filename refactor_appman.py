import os
import re

dir_path = "src/main/java/com/example/masroofy"

for filename in os.listdir(dir_path):
    if not filename.endswith(".java"): continue

    filepath = os.path.join(dir_path, filename)
    with open(filepath, "r", encoding="utf-8") as f:
        content = f.read()

    original = content

    content = content.replace("appManager.getAuthManager()", "authManager")
    content = content.replace("appManager.getCycleManager()", "cycleManager")
    content = content.replace("appManager.getCategoryManager()", "categoryManager")
    content = content.replace("appManager.getExpenseManager()", "expenseManager")
    content = content.replace("appManager.getStatsManager()", "statsManager")

    if content != original:
        print(f"Updated {filename}")
        with open(filepath, "w", encoding="utf-8") as f:
            f.write(content)

filepath = "src/main/java/com/example/masroofy/HelloApplication.java"
with open(filepath, "r", encoding="utf-8") as f:
    content = f.read()

# Fix method signatures
content = content.replace("private void showMainApp(Scene scene, AppManager appManager)", "private void showMainApp(Scene scene)")
content = content.replace("private VBox buildLoginRoot(Scene scene, AppManager appManager)", "private VBox buildLoginRoot(Scene scene)")
content = content.replace("private VBox buildRegisterRoot(Scene scene, AppManager appManager)", "private VBox buildRegisterRoot(Scene scene)")

# Fix method calls
content = content.replace("buildLoginRoot(scene, appManager)", "buildLoginRoot(scene)")
content = content.replace("buildRegisterRoot(scene, appManager)", "buildRegisterRoot(scene)")
content = content.replace("showMainApp(scene, appManager)", "showMainApp(scene)")

# Fix view instantiations
content = content.replace("new SettingsView(appManager, () ->", "new SettingsView(authManager, cycleManager, categoryManager, () ->")
content = content.replace("new DashboardView(appManager)", "new DashboardView(expenseManager, cycleManager, categoryManager, statsManager)")
content = content.replace("new StatsView(appManager, () ->", "new StatsView(statsManager, () ->")
content = content.replace("new HistoryView(appManager)", "new HistoryView(expenseManager, categoryManager)")

# Fix leftover AppManager references inside HelloApplication that weren't captured
content = content.replace("appManager.getAuthManager()", "authManager")
content = content.replace("appManager.getCycleManager()", "cycleManager")

# Clean leftover imports or unused variable
content = content.replace("private AppManager appManager;", "")
content = content.replace("appManager = new AppManager(dbManager);", "")

with open(filepath, "w", encoding="utf-8") as f:
    f.write(content)
print("Done HelloApp")
print("Done.")
