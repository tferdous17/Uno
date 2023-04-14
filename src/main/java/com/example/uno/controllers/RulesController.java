package com.example.uno.controllers;

import com.example.uno.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class RulesController {

    public Button btnReturnToTitle;

    public void btnReturnToTitle_OnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Game.class.getResource("start-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
