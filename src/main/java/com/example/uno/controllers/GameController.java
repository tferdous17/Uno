package com.example.uno.controllers;

import com.example.uno.Deck;
import com.example.uno.Game;
import com.example.uno.GameSettings;
import com.example.uno.Person;
import com.example.uno.card.NumberCard;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    public Button btnQuit;
    public Button btnPause;
    public Button btnResume;
    public Rectangle rectanglePauseOverlay;
    public Label labelGamePausedMessage;
    public Group groupPauseOverlay;
    public Button btnViewExtraCards;
    public Button btnDrawFromDeck;
    public Label labelOppCardsCounter;

    private Deck centerDeck;
    private Person humanPlayer;
    private Person computerPlayer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        humanPlayer = new Person();
        computerPlayer = new Person();

        GameSettings gameSettings = new GameSettings();
        gameSettings.readSampleFile("uno_deck_textfile.txt");
        centerDeck = gameSettings.getDeck();

        for (int i = 0; i < 7; i++) {
            centerDeck.giveCardFromCenterDeck(humanPlayer);
            centerDeck.giveCardFromCenterDeck(computerPlayer);
        }

    }

    // Load title screen when quit button is clicked
    public void btnQuit_OnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Game.class.getResource("start-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    // Sets the "pause overlay" to become visible when this button is clicked, which also makes the game unplayable as intended when paused
    public void btnPause_OnClick(ActionEvent event) {
        groupPauseOverlay.setVisible(true);
    }

    // Sets the "pause overlay" to turn invisible when this button is clicked, which resumes the game
    public void btnResume_OnClick(ActionEvent event) {
        groupPauseOverlay.setVisible(false);
    }

    public void btnViewExtraCards_OnClick(ActionEvent event) {
    }

    public void btnDrawFromDeck_OnClick(ActionEvent event) {
        if (humanPlayer.getPlayerDeck().size() < humanPlayer.getMaxPlayerDeckSize()) { // add condition to check turn
            centerDeck.giveCardFromCenterDeck(humanPlayer);
        }
        System.out.println(humanPlayer.getPlayerDeck().size());
    }
}
