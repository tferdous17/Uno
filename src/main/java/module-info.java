module com.example.uno {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.uno to javafx.fxml;
    exports com.example.uno;
}