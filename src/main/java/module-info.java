module com.example.masroofy {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.masroofy to javafx.fxml;
    exports com.example.masroofy;
}