module com.example.uno {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.uno to javafx.fxml;
    exports com.example.uno;
    exports com.example.uno.controllers;
    opens com.example.uno.controllers to javafx.fxml;
    exports com.example.uno.card;
    opens com.example.uno.card to javafx.fxml;
}