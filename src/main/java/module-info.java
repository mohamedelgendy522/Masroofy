module com.example.masroofy {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires java.sql; // 👈 دي الجديدة المهمة

    opens com.example.masroofy to javafx.fxml;
    exports com.example.masroofy;
}