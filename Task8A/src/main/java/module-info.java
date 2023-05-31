module com.example.task8a {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;


    opens com.example.task8a to javafx.fxml;
    exports com.example.task8a;
    exports model;
    opens model to javafx.fxml;
    exports DAO;
    opens DAO to javafx.fxml;
}