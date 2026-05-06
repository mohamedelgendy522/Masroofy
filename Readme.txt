======================================================
               MASROOFY - APP DOCUMENTATION
======================================================

1. PROJECT OVERVIEW
--------------------
Masroofy is a personal finance and expense tracking desktop application. It helps users manage their daily budgets, track expenses across different categories, and monitor their financial cycles securely.

2. TOOLS & TECHNOLOGIES USED
-----------------------------
- Programming Language: Java
- UI Framework: JavaFX (Programmatic Layouts)
- Database: SQLite (via JDBC)
- Build System & Dependency Management: Maven
- Styling: Custom CSS (JavaFX CSS)
- IDE: IntelliJ IDEA
- Diagramming Tools: PlantUML (For Class Diagrams)

3. FILES INCLUDED & ARCHITECTURE
---------------------------------
The project follows a structured, layered architecture to ensure separation of concerns between raw data, database logic, and the user interface.

[ Models / Entities ] - Data structures representing database tables:
- User.java      : Stores basic user account details.
- Expense.java   : Stores transaction details (amount, category, date).
- Cycle.java     : Tracks active financial periods (Total budget, start/end dates).
- Category.java  : Represents customizable spending categories.

[ Data Access Objects (DAOs) ] - Handles SQLite CRUD operations:
- DataBaseManager.java : Initializes the DB engine and provides DB connections.
- UserDAO.java         : Manages user creation/deletion.
- AuthDAO.java         : Securely saves and retrieves authentication details.
- ExpenseDAO.java      : Manages expense history, calculating sums and aggregations.
- CycleDAO.java        : Synchronizes cycle state (budget limits).
- CategoryDAO.java     : Handles adding and retrieving expenses tags.

[ Core Business Logic ] - The brains of the application:
- AppManager.java : The central Controller mirroring data from DAOs to Views. Handles routing logic, active sessions, and multi-table transactions.
- AUTH.java       : Validates PINs and constraints securely.

[ Graphical User Interface (Views) ] - The screens directly presented to the user:
- HelloApplication.java : The entry point (Launcher). Contains routing for Login and Register screens, plus the main Navigation bar wrapper.
- DashboardView.java    : Displays current balance, remaining daily limits, and input forms for adding Expenses & Deposits.
- HistoryView.java      : Displays historical logs of all operations.
- StatsView.java        : Visual analytics using PieCharts and statistics per category.
- SettingsView.java     : Manages application configuration (Change PIN, Logout, Delete Account).

[ Configuration & Resources ]
- style.css            : Modern dark-theme stylesheet.
- pom.xml              : Maven settings detailing external libraries (SQLite, JavaFX API).
- module-info.java     : Application module constraints.
======================================================
