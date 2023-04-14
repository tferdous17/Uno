package com.example.uno;

import com.example.uno.card.Card;

import java.util.ArrayList;

public class Person {
    private ArrayList<Card> personalDeck = new ArrayList<Card>();
    private final int MAX_PLAYER_DECK_SIZE = 14;
    private boolean isWinner = false;

    public int getMaxPlayerDeckSize() {
        return MAX_PLAYER_DECK_SIZE;
    }

    public void playCard() {

    }

    public void drawCardFromDeck() {

    }

    public boolean playerIsWinner() {
        if (hasZeroCards()) {
            isWinner = false;
        }
        return isWinner;
    }

    public void receiveCardFromDeck(Card card) {
        personalDeck.add(card);
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
