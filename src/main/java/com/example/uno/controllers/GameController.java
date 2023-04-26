package com.example.uno.controllers;

import com.example.uno.Deck;
import com.example.uno.Game;
import com.example.uno.GameSettings;
import com.example.uno.Person;
import com.example.uno.card.Card;
import com.example.uno.card.NumberCard;
import com.example.uno.card.SpecialCard;
import com.example.uno.card.SpecialCardType;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    public Rectangle card1_holder;

    public ImageView imgview_card0;
    public ImageView imgview_card1;
    public ImageView imgview_card2;
    public ImageView imgview_card3;
    public ImageView imgview_card4;
    public ImageView imgview_card5;
    public ImageView imgview_card6;
    public ImageView imgviewCurrentCard;
    public Text labelYou;
    public Text labelComputer;
    public Group groupWinOverlay;
    public Button btnPlayAgain;
    public Button btnViewOriginalCards;
    private ArrayList<ImageView> imageViewArrayList;

    Random random = new Random();
    private Deck centerDeck;
    private Person humanPlayer;
    private Person computerPlayer;
    private Card currentCardOnDiscardPile;
    private int drawCounter = 0;
    private int turnCounter = 0;
    private static int viewCardCounter = 0;
    private boolean extraCardsCheck = false;


    // Overrode Initialize method to carry out actions that must be done immediately after the player clicks "Start Game" in order to set up the game properly
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imageViewArrayList = new ArrayList<>(List.of(imgview_card0, imgview_card1, imgview_card2, imgview_card3, imgview_card4, imgview_card5, imgview_card6));
        humanPlayer = new Person();
        computerPlayer = new Person();

        GameSettings gameSettings = new GameSettings();
        gameSettings.getDeckObject().getDeckAsList().clear();
        gameSettings.readSampleFile("uno_deck_textfile.txt");
        centerDeck = gameSettings.getDeckObject();

        for (int i = 0; i < 6; i++) {
            centerDeck.giveCardFromCenterDeck(humanPlayer);
            centerDeck.giveCardFromCenterDeck(computerPlayer);
        }
        for (int i = 0; i < humanPlayer.getPlayerDeck().size(); i++) {
            humanPlayer.getPlayerDeck().get(i).setImageView(imageViewArrayList.get(i));
            imageViewArrayList.get(i).setImage(humanPlayer.getPlayerDeck().get(i).getImage());
        }

        // Filtering out all number cards in the center deck so the starting card is always a number card instead of a special card
        List<Card> numberCardsOnly = centerDeck.getDeckAsList().stream().filter(x -> x instanceof NumberCard).toList();
        currentCardOnDiscardPile = numberCardsOnly.get(random.nextInt(numberCardsOnly.size()));

        imgviewCurrentCard.setImage(currentCardOnDiscardPile.getImage());

        humanPlayer.setHasCurrentTurn(true);
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
        if (humanPlayer.getPlayerDeck().size() > 7) {
            btnViewExtraCards.setVisible(false);
            btnViewOriginalCards.setLayoutX(801);
            btnViewOriginalCards.setLayoutY(471);
            btnViewOriginalCards.setVisible(true);
            extraCardsCheck = true;

            for (ImageView iv : imageViewArrayList) {
                iv.setImage(null);
            }
            for (int i = 7; i < humanPlayer.getPlayerDeck().size(); i++) {
                imageViewArrayList.get(i - 7).setImage(humanPlayer.getPlayerDeck().get(i).getImage());
            }
        }
    }

    public void btnViewOriginalCards_OnClick(ActionEvent event) {
        btnViewOriginalCards.setVisible(false);
        btnViewExtraCards.setVisible(true);
        for (ImageView iv : imageViewArrayList) {
            iv.setImage(null);
        }
        for (int i = 0; i < 7; i++) {
            imageViewArrayList.get(i).setImage(humanPlayer.getPlayerDeck().get(i).getImage());
            imageViewArrayList.get(i).setDisable(false);
        }

    }

    public void btnDrawFromDeck_OnClick(ActionEvent event) {
        btnDrawFromDeck.setDisable(!humanPlayer.doesHaveCurrentTurn()); // Disabled button if human player (you) does not have the current turn
        if (humanPlayer.getPlayerDeck().size() < humanPlayer.getMaxPlayerDeckSize()) {
            centerDeck.giveCardFromCenterDeck(humanPlayer);
            if (humanPlayer.getPlayerDeck().size() > 7) {
                btnViewExtraCards.setText("View extra cards (" + (humanPlayer.getPlayerDeck().size() - 7) + ")");
            } else {
                btnViewExtraCards.setText("View extra cards (0)");
            }
            drawCounter++; // have to zero out draw counter whenever its player's turn again
//            humanPlayer.changeTurnTo(computerPlayer);
            autoRearrangeCards();
        }
    }

    /*
    note: maybe use hashmap somewhere for the cards..
    Following set of 7 methods handles the mouse click response on each card to check if they match the discard pile or not (and then does further action)
     */
    public void imgView0OnMouseClick(MouseEvent mouseEvent) throws InterruptedException {
        Card card;
        if (humanPlayer.doesHaveCurrentTurn() && !btnViewOriginalCards.isVisible()) {
            if (humanPlayer.getPlayerDeck().get(0) != null && humanPlayer.getPlayerDeck().get(0).equals(currentCardOnDiscardPile)) {
                card = humanPlayer.getPlayerDeck().get(0);
                humanPlayer.getPlayerDeck().remove(0);
                playMatchingCard(card, imgview_card0);
            }
        } else if (btnViewOriginalCards.isVisible()) {
            if (humanPlayer.getPlayerDeck().get(7) != null && humanPlayer.getPlayerDeck().get(7).equals(currentCardOnDiscardPile)) {
                card = humanPlayer.getPlayerDeck().get(7);
                humanPlayer.getPlayerDeck().remove(7);
                playMatchingCard(card, imgview_card0);
            }
        }
    }

    public void imgView1OnMouseClick(MouseEvent mouseEvent) throws InterruptedException {
        Card card;
        if (humanPlayer.doesHaveCurrentTurn() && !btnViewOriginalCards.isVisible()) {
            if (humanPlayer.getPlayerDeck().get(1) != null && humanPlayer.getPlayerDeck().get(1).equals(currentCardOnDiscardPile)) {
                card = humanPlayer.getPlayerDeck().get(1);
                System.out.println("card that got played: " + card);
                humanPlayer.getPlayerDeck().remove(1);
                playMatchingCard(card, imgview_card1);
            } else if (btnViewOriginalCards.isVisible()) {
                if (humanPlayer.getPlayerDeck().get(8) != null && humanPlayer.getPlayerDeck().get(8).equals(currentCardOnDiscardPile)) {
                    card = humanPlayer.getPlayerDeck().get(8);
                    humanPlayer.getPlayerDeck().remove(8);
                    playMatchingCard(card, imgview_card1);
                }
            }
        }
    }

    public void imgView2OnMouseClick(MouseEvent mouseEvent) throws InterruptedException {
        Card card;
        if (humanPlayer.doesHaveCurrentTurn() && !btnViewOriginalCards.isVisible()) {
            if (humanPlayer.getPlayerDeck().get(2) != null && humanPlayer.getPlayerDeck().get(2).equals(currentCardOnDiscardPile)) {
                card = humanPlayer.getPlayerDeck().get(2);
                System.out.println("card that got played: " + card);
                humanPlayer.getPlayerDeck().remove(2);
                playMatchingCard(card, imgview_card2);
            } else if (btnViewOriginalCards.isVisible()) {
                if (humanPlayer.getPlayerDeck().get(9) != null && humanPlayer.getPlayerDeck().get(9).equals(currentCardOnDiscardPile)) {
                    card = humanPlayer.getPlayerDeck().get(9);
                    humanPlayer.getPlayerDeck().remove(9);
                    playMatchingCard(card, imgview_card2);
                }
            }
        }
    }

    public void imgView3OnMouseClick(MouseEvent mouseEvent) throws InterruptedException {
        if (humanPlayer.doesHaveCurrentTurn()) {
            if (humanPlayer.getPlayerDeck().get(3) != null && humanPlayer.getPlayerDeck().get(3).equals(currentCardOnDiscardPile)) {
                Card card = humanPlayer.getPlayerDeck().get(3);
                System.out.println("card that got played: " + card);
                humanPlayer.getPlayerDeck().remove(3);
                playMatchingCard(card, imgview_card3);

            }
        }
    }

    public void imgView4OnMouseClick(MouseEvent mouseEvent) throws InterruptedException {
        if (humanPlayer.doesHaveCurrentTurn()) {
            if (humanPlayer.getPlayerDeck().get(4) != null && humanPlayer.getPlayerDeck().get(4).equals(currentCardOnDiscardPile)) {
                Card card = humanPlayer.getPlayerDeck().get(4);
                System.out.println("card that got played: " + card);
                humanPlayer.getPlayerDeck().remove(4);
                playMatchingCard(card, imgview_card4);
            }
        }
    }

    public void imgView5OnMouseClick(MouseEvent mouseEvent) throws InterruptedException {
        if (humanPlayer.doesHaveCurrentTurn() && humanPlayer.getPlayerDeck().get(5) != null) {
            if (humanPlayer.getPlayerDeck().get(5).equals(currentCardOnDiscardPile)) {
                Card card = humanPlayer.getPlayerDeck().get(5);
                System.out.println("card that got played: " + card);
                humanPlayer.getPlayerDeck().remove(5);
                playMatchingCard(card, imgview_card5);
            } else {
                System.out.println("cant select");
            }
        }
    }

    public void imgView6OnMouseClick(MouseEvent mouseEvent) throws InterruptedException {
        if (humanPlayer.doesHaveCurrentTurn()) {
            Card card = humanPlayer.getPlayerDeck().get(6);
            if (card.equals(currentCardOnDiscardPile)) {
                System.out.println("card that got played: " + card);
                humanPlayer.getPlayerDeck().remove(6);
                playMatchingCard(card, imgview_card6);

            }
        }
    }

    // If player clicks on a matching card to the discard pile, it will switch that card to be the new card on the discard pile and remove it from player's personal deck
    private void playMatchingCard(Card card, ImageView imgviewCard) throws InterruptedException {
        if (card instanceof SpecialCard) {
            if (((SpecialCard) card).getCardType() == SpecialCardType.draw_two || ((SpecialCard) card).getCardType() == SpecialCardType.wild_draw_four) {
                currentCardOnDiscardPile.setCardTo(card);
            } else if (((SpecialCard) card).equals(currentCardOnDiscardPile)) {
                currentCardOnDiscardPile.setCardTo(card);

            }
        } else {
            currentCardOnDiscardPile.setCardTo(card);
        }
        imgviewCurrentCard.setImage(currentCardOnDiscardPile.getImage());

        imgviewCard.setDisable(true);

        autoRearrangeCards();
        turnCounter++;
//        humanPlayer.changeTurnTo(computerPlayer);

        compPLayerTurnTest();
        if (checkWinner()) {
            groupWinOverlay.setVisible(true);
        }

//        disableClicking(imageViewArrayList);
    }

    private void compPlayerTurn() throws InterruptedException {
        if (computerPlayer.doesHaveCurrentTurn()) {
            for (Card c : computerPlayer.getPlayerDeck()) {
                if (c.equals(currentCardOnDiscardPile)) {
                    currentCardOnDiscardPile.setCardTo(c);
                    imgviewCurrentCard.setImage(c.getImage());
                    humanPlayer.getPlayerDeck().remove(c);
                    labelOppCardsCounter.setText("Opponent's # of Cards Remaining: " + computerPlayer.getPlayerDeck().size());
                }
            }
        }
        computerPlayer.changeTurnTo(humanPlayer);
    }

    private void compPLayerTurnTest() {
        computerPlayer.changeTurnTo(humanPlayer);
    }

    private boolean checkWinner() {
        return computerPlayer.playerIsWinner() || humanPlayer.playerIsWinner();
    }


    private void autoRearrangeCards() { // fix
        if (humanPlayer.getPlayerDeck().size() <= 7) {
            for (ImageView iv : imageViewArrayList) {
                iv.setImage(null);
            }
            for (int i = 0; i < humanPlayer.getPlayerDeck().size(); i++) {
                imageViewArrayList.get(i).setImage(humanPlayer.getPlayerDeck().get(i).getImage());
                imageViewArrayList.get(i).setDisable(false);
            }
        }

        // Method of having the deck update instantly as you draw more cards when viewing your extra cards (>7)
        if (btnViewOriginalCards.isVisible()) {
            for (ImageView iv : imageViewArrayList) {
                iv.setImage(null);
            }
            for (int i = 7; i < humanPlayer.getPlayerDeck().size(); i++) {
                imageViewArrayList.get(i - 7).setImage(humanPlayer.getPlayerDeck().get(i).getImage());
                imageViewArrayList.get(i - 7).setDisable(false);
            }
        }
    }

    private void disableClicking(ArrayList<ImageView> arrayList) {
        if (!(humanPlayer.doesHaveCurrentTurn())) {
            for (ImageView iv : arrayList) {
                iv.setCursor(Cursor.DEFAULT);
            }
        }
    }

    public void btnPlayAgain_OnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Game.class.getResource("start-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
