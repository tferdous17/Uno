package com.example.uno.controllers;

import com.example.uno.Deck;
import com.example.uno.Game;
import com.example.uno.GameSettings;
import com.example.uno.Person;
import com.example.uno.card.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.util.*;

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
    public Group groupSelectColorOverlay;
    public Group groupLoseOverlay;
    public Group groupNoMoreCardsOverlay;
    public Label labelWildColor;
    public Label labelWildColorComputer;
    private ArrayList<ImageView> imageViewArrayList;

    private GameSettings gameSettings = new GameSettings();
    private Random random = new Random();
    private Deck centerDeck;
    private Person humanPlayer;
    private Person computerPlayer;
    private Card currentCardOnDiscardPile;
    private int compCardMatchCounter = 0;
    private Card card;


    // Overrode Initialize method to carry out actions that must be done immediately after the player clicks "Start Game" in order to set up the game properly
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imageViewArrayList = new ArrayList<>(List.of(imgview_card0, imgview_card1, imgview_card2, imgview_card3, imgview_card4, imgview_card5, imgview_card6));
        humanPlayer = new Person();
        computerPlayer = new Person();

        gameSettings.getDeckObject().getDeckAsList().clear();
        gameSettings.readSampleFile("uno_deck_textfile.txt");
        centerDeck = gameSettings.getDeckObject();

        for (int i = 0; i < 7; i++) {
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
        updateCompPlayerCounter();
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
    public void btnPause_OnClick() {
        groupPauseOverlay.setVisible(true);
    }

    // Sets the "pause overlay" to turn invisible when this button is clicked, which resumes the game
    public void btnResume_OnClick() {
        groupPauseOverlay.setVisible(false);
    }

    public void btnViewExtraCards_OnClick(ActionEvent event) {
        if (humanPlayer.getPlayerDeck().size() > 7) {
            btnViewExtraCards.setVisible(false);
            btnViewOriginalCards.setLayoutX(801);
            btnViewOriginalCards.setLayoutY(471);
            btnViewOriginalCards.setVisible(true);

            for (ImageView iv : imageViewArrayList) {
                iv.setImage(null);
            }
            for (int i = 7; i < humanPlayer.getPlayerDeck().size(); i++) {
                imageViewArrayList.get(i - 7).setImage(humanPlayer.getPlayerDeck().get(i).getImage());
            }
            autoRearrangeCards();
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
        // handle deck if cards run out
        btnDrawFromDeck.setDisable(!humanPlayer.doesHaveCurrentTurn()); // Disabled button if human player (you) does not have the current turn
        if (humanPlayer.getPlayerDeck().size() < humanPlayer.getMaxPlayerDeckSize()) {
            centerDeck.giveCardFromCenterDeck(humanPlayer);
            updateExtraCardsCounter();
//            drawCounter++; // have to zero out draw counter whenever its player's turn again
            humanPlayer.changeTurnTo(computerPlayer);
            compPlayerTurn();
            autoRearrangeCards();
        }
        if (centerDeck.getDeckAsList().size() == 0) {
            groupNoMoreCardsOverlay.setVisible(true);
        }
    }

    private void updateExtraCardsCounter() {
        if (humanPlayer.getPlayerDeck().size() > 7) {
            btnViewExtraCards.setText("View extra cards (" + (humanPlayer.getPlayerDeck().size() - 7) + ")");
        } else {
            btnViewExtraCards.setText("View extra cards (0)");
        }
    }

    /*
    Following set of 7 methods handles the mouse click response on each card to check if they match the discard pile or not (and then does further action)
     */
    public void imgView0OnMouseClick() {
        if (humanPlayer.doesHaveCurrentTurn() && !btnViewOriginalCards.isVisible()) {
            if (humanPlayer.getPlayerDeck().get(0) != null) {
                card = humanPlayer.getPlayerDeck().get(0);
                playMatchingCard(card, 0, imgview_card0);
            }
        } else if (humanPlayer.doesHaveCurrentTurn() && btnViewOriginalCards.isVisible()) {
            if (humanPlayer.getPlayerDeck().get(7) != null) {
                card = humanPlayer.getPlayerDeck().get(7);
                playMatchingCard(card, 7, imgview_card0);
            }
        }
    }

    public void imgView1OnMouseClick() {
        if (humanPlayer.doesHaveCurrentTurn() && !btnViewOriginalCards.isVisible()) {
            if (humanPlayer.getPlayerDeck().get(1) != null) {
                card = humanPlayer.getPlayerDeck().get(1);
                playMatchingCard(card, 1, imgview_card1);
            }
        } else if (humanPlayer.doesHaveCurrentTurn() && btnViewOriginalCards.isVisible()) {
            if (humanPlayer.getPlayerDeck().get(8) != null) {
                card = humanPlayer.getPlayerDeck().get(8);
                playMatchingCard(card, 8, imgview_card1);
            }
        }
    }

    public void imgView2OnMouseClick() {
        if (humanPlayer.doesHaveCurrentTurn() && !btnViewOriginalCards.isVisible()) {
            if (humanPlayer.getPlayerDeck().get(2) != null) {
                card = humanPlayer.getPlayerDeck().get(2);
                playMatchingCard(card, 2, imgview_card2);
            }
        } else if (humanPlayer.doesHaveCurrentTurn() && btnViewOriginalCards.isVisible()) {
            if (humanPlayer.getPlayerDeck().get(9) != null) {
                card = humanPlayer.getPlayerDeck().get(9);
                playMatchingCard(card, 9, imgview_card2);
            }
        }
    }

    public void imgView3OnMouseClick() {
        if (humanPlayer.doesHaveCurrentTurn() && !btnViewOriginalCards.isVisible()) {
            if (humanPlayer.getPlayerDeck().get(3) != null) {
                card = humanPlayer.getPlayerDeck().get(3);
                playMatchingCard(card, 3, imgview_card3);
            }
        } else if (humanPlayer.doesHaveCurrentTurn() && btnViewOriginalCards.isVisible()) {
            if (humanPlayer.getPlayerDeck().get(10) != null) {
                card = humanPlayer.getPlayerDeck().get(10);
                playMatchingCard(card, 10, imgview_card3);
            }
        }
    }

    public void imgView4OnMouseClick() {
        if (humanPlayer.doesHaveCurrentTurn() && !btnViewOriginalCards.isVisible()) {
            if (humanPlayer.getPlayerDeck().get(4) != null) {
                card = humanPlayer.getPlayerDeck().get(4);
                playMatchingCard(card, 4, imgview_card4);
            }
        } else if (humanPlayer.doesHaveCurrentTurn() && btnViewOriginalCards.isVisible()) {
            if (humanPlayer.getPlayerDeck().get(11) != null) {
                card = humanPlayer.getPlayerDeck().get(11);
                playMatchingCard(card, 11, imgview_card4);
            }
        }
    }

    public void imgView5OnMouseClick() {
        if (humanPlayer.doesHaveCurrentTurn() && !btnViewOriginalCards.isVisible()) {
            if (humanPlayer.getPlayerDeck().get(5) != null) {
                card = humanPlayer.getPlayerDeck().get(5);
                playMatchingCard(card, 5, imgview_card5);
            }
        } else if (humanPlayer.doesHaveCurrentTurn() && btnViewOriginalCards.isVisible()) {
            if (humanPlayer.getPlayerDeck().get(12) != null) {
                card = humanPlayer.getPlayerDeck().get(12);
                playMatchingCard(card, 12, imgview_card5);
            }
        }
    }

    public void imgView6OnMouseClick() {
        if (humanPlayer.doesHaveCurrentTurn() && !btnViewOriginalCards.isVisible()) {
            if (humanPlayer.getPlayerDeck().get(6) != null) {
                card = humanPlayer.getPlayerDeck().get(6);
                playMatchingCard(card, 6, imgview_card6);
            }
        } else if (humanPlayer.doesHaveCurrentTurn() && btnViewOriginalCards.isVisible()) {
            if (humanPlayer.getPlayerDeck().get(13) != null) {
                card = humanPlayer.getPlayerDeck().get(13);
                playMatchingCard(card, 13, imgview_card6);
            }
        }
    }

    // * fix cards not automatically re-arranging for cards in original deck
    // If player clicks on a matching card to the discard pile, it will switch that card to be the new card on the discard pile and remove it from player's personal deck
    private void playMatchingCard(Card card, int index, ImageView imgviewCard) {
        if (card instanceof NumberCard) {
            playNumberCard(card, index, imgviewCard);
        } else if (card instanceof SpecialCard) {
            playSpecialCard(card, index, imgviewCard);
        }
        labelWildColorComputer.setVisible(false);
        autoRearrangeCards();
        compPlayerTurn();

        if (checkWinner()) {
            winOrLoseOverlay(humanPlayer.hasZeroCards());
        }
        updateExtraCardsCounter();
    }

    // Triggers win or lose overlay depending on the human player's # of cards
    private void winOrLoseOverlay(boolean humanPlayerZeroCards) {
        if (humanPlayerZeroCards) {
            groupWinOverlay.setVisible(true);
        } else {
            groupLoseOverlay.setVisible(true);
        }
    }

    private void playNumberCard(Card card, int index, ImageView imgviewCard) {
        if (card.equals(currentCardOnDiscardPile)) {
            currentCardOnDiscardPile.setCardTo(card);
            imgviewCurrentCard.setImage(card.getImage());
            imgviewCard.setImage(null);
            humanPlayer.getPlayerDeck().remove(index);
            imgviewCard.setDisable(true);
            humanPlayer.changeTurnTo(computerPlayer);
        }
    }

    private void playSpecialCard(Card card, int index, ImageView imgviewCard) {
        if (((SpecialCard) card).getCardType() == SpecialCardType.draw_two && card.getColor() == currentCardOnDiscardPile.getColor()) {
            ((SpecialCard) card).drawTwo(centerDeck, computerPlayer);
            currentCardOnDiscardPile.setCardTo(card);
            imgviewCurrentCard.setImage(card.getImage());
            humanPlayer.getPlayerDeck().remove(index);
            imgviewCard.setDisable(true);
            humanPlayer.changeTurnTo(computerPlayer);
        } else if (((SpecialCard) card).getCardType() == SpecialCardType.reverse && card.getColor() == currentCardOnDiscardPile.getColor()) {
            ((SpecialCard) card).reverse(computerPlayer);
            currentCardOnDiscardPile.setCardTo(card);
            imgviewCurrentCard.setImage(card.getImage());
            humanPlayer.getPlayerDeck().remove(index);
            imgviewCard.setDisable(true);
        } else if (((SpecialCard) card).getCardType() == SpecialCardType.skip && card.getColor() == currentCardOnDiscardPile.getColor()) {
            ((SpecialCard) card).skip(computerPlayer);
            currentCardOnDiscardPile.setCardTo(card);
            imgviewCurrentCard.setImage(card.getImage());
            humanPlayer.getPlayerDeck().remove(index);
            imgviewCard.setDisable(true);
        } else if (((SpecialCard) card).getCardType() == SpecialCardType.wild) {
            currentCardOnDiscardPile.setCardTo(card);
            imgviewCurrentCard.setImage(card.getImage());
            humanPlayer.getPlayerDeck().remove(index);
            imgviewCard.setDisable(true);
            humanPlayer.changeTurnTo(computerPlayer);
            groupSelectColorOverlay.setVisible(true);
        } else if (((SpecialCard) card).getCardType() == SpecialCardType.wild_draw_four) {
            ((SpecialCard) card).wildDrawFour(centerDeck, computerPlayer);
            currentCardOnDiscardPile.setCardTo(card);
            imgviewCurrentCard.setImage(card.getImage());
            humanPlayer.getPlayerDeck().remove(index);
            imgviewCard.setDisable(true);
            humanPlayer.changeTurnTo(computerPlayer);
            groupSelectColorOverlay.setVisible(true);
        }
        updateCompPlayerCounter();
    }

    private void playSpecialCardCOMPUTER(Card card, int index) {
        List<CardColor> cardColors = new ArrayList<>(List.of(CardColor.Red, CardColor.Blue, CardColor.Green, CardColor.Yellow));
        if (((SpecialCard) card).getCardType() == SpecialCardType.draw_two && card.getColor() == currentCardOnDiscardPile.getColor()
                && (humanPlayer.getPlayerDeck().size() + 2 < humanPlayer.getMaxPlayerDeckSize())) {
            ((SpecialCard) card).drawTwo(centerDeck, humanPlayer);
            currentCardOnDiscardPile.setCardTo(card);
            imgviewCurrentCard.setImage(card.getImage());
            computerPlayer.getPlayerDeck().remove(index);
            computerPlayer.changeTurnTo(humanPlayer);
        } else if (((SpecialCard) card).getCardType() == SpecialCardType.reverse && card.getColor() == currentCardOnDiscardPile.getColor()) {
            ((SpecialCard) card).reverse(humanPlayer);
            currentCardOnDiscardPile.setCardTo(card);
            imgviewCurrentCard.setImage(card.getImage());
            computerPlayer.getPlayerDeck().remove(index);
        } else if (((SpecialCard) card).getCardType() == SpecialCardType.skip && card.getColor() == currentCardOnDiscardPile.getColor()) {
            ((SpecialCard) card).skip(humanPlayer);
            computerPlayer.setHasCurrentTurn(true);
            currentCardOnDiscardPile.setCardTo(card);
            imgviewCurrentCard.setImage(card.getImage());
            computerPlayer.getPlayerDeck().remove(index);
        } else if (((SpecialCard) card).getCardType() == SpecialCardType.wild) {
            currentCardOnDiscardPile.setCardTo(card);
            imgviewCurrentCard.setImage(card.getImage());
            computerPlayer.getPlayerDeck().remove(index);
            CardColor randomCardColor = cardColors.get(random.nextInt(cardColors.size()));
            currentCardOnDiscardPile.setColor(randomCardColor);
            currentCardOnDiscardPile.setValue(-1);
            labelWildColorComputer.setText("WILD: " + randomCardColor);
            labelWildColorComputer.setVisible(true);
            computerPlayer.changeTurnTo(humanPlayer);
        } else if (((SpecialCard) card).getCardType() == SpecialCardType.wild_draw_four && (humanPlayer.getPlayerDeck().size() + 4 < humanPlayer.getMaxPlayerDeckSize())) {
            ((SpecialCard) card).wildDrawFour(centerDeck, humanPlayer);
            currentCardOnDiscardPile.setCardTo(card);
            imgviewCurrentCard.setImage(card.getImage());
            computerPlayer.getPlayerDeck().remove(index);
            CardColor randomCardColor = cardColors.get(random.nextInt(cardColors.size()));
            currentCardOnDiscardPile.setColor(randomCardColor);
            currentCardOnDiscardPile.setValue(-1);
            labelWildColorComputer.setText("WILD: " + randomCardColor);
            labelWildColorComputer.setVisible(true);
            computerPlayer.changeTurnTo(humanPlayer);
        }
        updateCompPlayerCounter();
        updateExtraCardsCounter();
        autoRearrangeCards();
    }

    private void compPlayerTurn() {
        // also fix comp player losing a card when trying to match wild card before color is selected
        btnDrawFromDeck.setDisable(true);
        if (computerPlayer.doesHaveCurrentTurn()) {
            for (int i = 0; i < computerPlayer.getPlayerDeck().size(); i++) {
                if (computerPlayer.getPlayerDeck().get(i).equals(currentCardOnDiscardPile)) {
                    while (compCardMatchCounter < 1) {
                        currentCardOnDiscardPile.setCardTo(computerPlayer.getPlayerDeck().get(i));
                        imgviewCurrentCard.setImage(computerPlayer.getPlayerDeck().get(i).getImage());
                        computerPlayer.getPlayerDeck().remove(computerPlayer.getPlayerDeck().get(i));
                        updateCompPlayerCounter();
                        compCardMatchCounter++;
                    }
                } else if (computerPlayer.getPlayerDeck().get(i) instanceof SpecialCard) {
                    playSpecialCardCOMPUTER(computerPlayer.getPlayerDeck().get(i), i);
                }
            }
            if (compCardMatchCounter < 1) { // If this variable is still 0, it means the computer player had zero matching cards in total, so it must "draw from the deck"
                centerDeck.giveCardFromCenterDeck(computerPlayer);
            }
            if (centerDeck.getDeckAsList().size() == 0) { // Way to handle center deck running out of cards to draw from
                groupNoMoreCardsOverlay.setVisible(true);
            }
            computerPlayer.changeTurnTo(humanPlayer);
        }
        if (checkWinner()) {
            winOrLoseOverlay(humanPlayer.hasZeroCards());
        }
        updateCompPlayerCounter();
        btnDrawFromDeck.setDisable(false);
        compCardMatchCounter = 0;
    }

    private boolean checkWinner() {
        return computerPlayer.playerIsWinner() || humanPlayer.playerIsWinner();
    }

    private void updateCompPlayerCounter() {
        labelOppCardsCounter.setText("Opponent's # of Cards Remaining: " + computerPlayer.getPlayerDeck().size());
    }

    private void autoRearrangeCards() {
        if (humanPlayer.getPlayerDeck().size() <= 7) {
            for (ImageView iv : imageViewArrayList) {
                iv.setImage(null);
            }
            for (int i = 0; i < humanPlayer.getPlayerDeck().size(); i++) {
                imageViewArrayList.get(i).setImage(humanPlayer.getPlayerDeck().get(i).getImage());
                imageViewArrayList.get(i).setDisable(false);
            }
            for (ImageView iv : imageViewArrayList) {
                if (iv.getImage() == null) {
                    iv.setDisable(true);
                }
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
            for (ImageView iv : imageViewArrayList) {
                if (iv.getImage() == null) {
                    iv.setDisable(true);
                }
            }
        } else if (humanPlayer.getPlayerDeck().size() > 7 && !btnViewOriginalCards.isVisible()) {
            for (ImageView iv : imageViewArrayList) {
                iv.setImage(null);
            }
            for (int i = 0; i < 7; i++) {
                imageViewArrayList.get(i).setImage(humanPlayer.getPlayerDeck().get(i).getImage());
                imageViewArrayList.get(i).setDisable(false);
            }
        }
        labelWildColor.setVisible(false);
    }

    public void testButton_OnClick() {
        System.out.println("-----------------------------------------------");
        System.out.println("HUMAN DECK:");
        humanPlayer.printPlayerDeck();
        System.out.println("-----------------------------------------------");
        System.out.println("COMPUTER DECK: ");
        computerPlayer.printPlayerDeck();
        System.out.println("-----------------------------------------------");
    }

    public void btnSelectRed_OnClick() {
        currentCardOnDiscardPile.setColor(CardColor.Red);
        currentCardOnDiscardPile.setValue(-1);
        groupSelectColorOverlay.setVisible(false);
        System.out.println(currentCardOnDiscardPile);
        labelWildColor.setText("WILD: Red");
        labelWildColor.setVisible(true);
    }

    public void btnSelectBlue_OnClick() {
        currentCardOnDiscardPile.setColor(CardColor.Blue);
        currentCardOnDiscardPile.setValue(-1);
        groupSelectColorOverlay.setVisible(false);
        System.out.println(currentCardOnDiscardPile);
        labelWildColor.setText("WILD: Blue");
        labelWildColor.setVisible(true);
    }

    public void btnSelectGreen_OnClick() {
        currentCardOnDiscardPile.setColor(CardColor.Green);
        currentCardOnDiscardPile.setValue(-1);
        groupSelectColorOverlay.setVisible(false);
        System.out.println(currentCardOnDiscardPile);
        labelWildColor.setText("WILD: Green");
        labelWildColor.setVisible(true);
    }

    public void btnSelectYellow_OnClick() {
        currentCardOnDiscardPile.setColor(CardColor.Yellow);
        currentCardOnDiscardPile.setValue(-1);
        groupSelectColorOverlay.setVisible(false);
        System.out.println(currentCardOnDiscardPile);
        labelWildColor.setText("WILD: Yellow");
        labelWildColor.setVisible(true);
    }

    public void btnPlayAgain_OnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Game.class.getResource("start-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void btnResetDrawPile_OnClick() {
        if (centerDeck.getDeckAsList().size() == 0) {
            gameSettings.refreshDeck(centerDeck);
        }
        groupNoMoreCardsOverlay.setVisible(false);
    }
}
