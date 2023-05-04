package com.example.uno;

import com.example.uno.card.Card;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Person {
    private ArrayList<Card> personalDeck = new ArrayList<Card>();
    private final int MAX_PLAYER_DECK_SIZE = 14;
    private boolean isWinner = false;
    private boolean hasCurrentTurn = false;

    public Person() { } // Default Constructor

    public int getMaxPlayerDeckSize() {
        return MAX_PLAYER_DECK_SIZE;
    }

    public void setHasCurrentTurn(boolean b) {
        this.hasCurrentTurn = b;
    }

    public boolean doesHaveCurrentTurn() {
        return hasCurrentTurn;
    }

    public boolean hasTakenTurn() {
        return !hasCurrentTurn;
    }

    public void receiveCardFromDeck(Card card) {
        personalDeck.add(card);
    }

    public boolean playerIsWinner() {
        if (hasZeroCards()) {
            isWinner = true;
        }
        return isWinner;
    }

    public void changeTurnTo(Person person) {
        if (this.hasCurrentTurn) {
            person.setHasCurrentTurn(true);
            this.setHasCurrentTurn(false);
        }
    }

    public boolean hasZeroCards() {
        return personalDeck.size() == 0;
    }

    public void printPlayerDeck() {
        for (Card c : personalDeck) {
            System.out.println(c);
        }
    }

    public ArrayList<Card> getPlayerDeck() {
        return personalDeck;
    }
}
