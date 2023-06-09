package com.example.uno;

import com.example.uno.card.Card;

import java.util.ArrayList;
import java.util.Random;

public class Deck {
    private final static int MAX_DECK_SIZE = 108;
    private Random random = new Random();
    private static ArrayList<Card> centerDeck = new ArrayList<Card>();

    public Deck() {
    }

    public Deck(ArrayList<Card> cards) {
        populateDeck(cards);
    }

    public void populateDeck(ArrayList<Card> cards) {
        centerDeck.addAll(cards);
    }

    public void giveCardFromCenterDeck(Person person) {
        if (this.getDeckAsList().size() >= 1) {
            Card drawnCard = centerDeck.get(random.nextInt(centerDeck.size())); // picks out a randomly chosen card from the center deck
            person.receiveCardFromDeck(drawnCard); // puts the drawn card into Person object's personal deck
            centerDeck.remove(drawnCard); // removes this card from the deck so the deck isn't infinite
        } else {
            System.out.println("No more cards to draw from the deck. | DECK SIZE: " + this.getDeckAsList().size());
        }
    }

    public void printDeck() {
        for (Card c : centerDeck) {
            System.out.println(c);
        }
    }

    public ArrayList<Card> getDeckAsList() {
        return centerDeck;
    }

}
