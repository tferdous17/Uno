package com.example.uno.controllers;

import com.example.uno.Main;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class StartController {
    public Button btnStartGame;
    public Button btnRules;
    @FXML
    private Label welcomeText;

    @FXML
    public void btnStartGame_OnClick(ActionEvent event) throws IOException {
        loadScene(event, "game-view");
    }

    @FXML
    public void btnRules_OnClick(ActionEvent event) throws IOException {
        loadScene(event, "rules-view");
    }

    private void loadScene(Event event, String sceneName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(sceneName + ".fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}