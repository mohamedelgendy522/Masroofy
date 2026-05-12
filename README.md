A minimal, lightweight expense tracker built with Java 21, JavaFX, and SQLite.

## Features
- **Expense Tracking:** Effortlessly record and track your daily expenses.
- **Minimal UI:** Clean and straightforward JavaFX user interface.
- **Local Storage:** Secure, offline data storage using an embedded SQLite database.

## Tech Stack
- **Language:** Java 21
- **UI Framework:** JavaFX 21
- **Database:** SQLite JDBC
- **Build Tool:** Apache Maven

## Prerequisites
Ensure you have the following installed on your machine:
- **Java Development Kit (JDK)** 21 or higher
- **Apache Maven** (Make sure `JAVA_HOME` is set correctly)

## Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/mohamedelgendy522/Masroofy.git
cd Masroofy
```

### 2. Running via IDE
- Open the project in your favorite IDE (IntelliJ IDEA, Eclipse, VS Code).
- Let Maven resolve the dependencies.
- Locate and run the launcher class: `src/main/java/com/example/masroofy/Launcher.java`.

### 3. Running via Maven CLI
You can also compile and run the application directly from your terminal using the JavaFX Maven plugin:
```bash
mvn clean javafx:run
```

## Testing
To run the included JUnit tests, use:
```bash
mvn test
```
