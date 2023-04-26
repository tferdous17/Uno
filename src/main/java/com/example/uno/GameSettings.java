package com.example.uno;

import com.example.uno.card.Card;
import com.example.uno.card.NumberCard;
import com.example.uno.card.SpecialCard;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class GameSettings {
    private Deck deck = new Deck();
    private ArrayList<Card> numberCardStack = new ArrayList<Card>();
    private ArrayList<Card> specialCardStack = new ArrayList<Card>();
    private ArrayList<String> linesRead = new ArrayList<String>();

    // This method will read the sample data
    public void readSampleFile(String fileName) {
        String line = "";
        String directory = "src/main/resources/com/example/uno/";
        try {
            BufferedReader br = new BufferedReader(new FileReader(directory + fileName));
            while ((line = br.readLine()) != null) {
                String[] pieces = line.split(",");
                for (String piece : pieces) {
                    createNumberCards(piece);
                    createSpecialCards(piece);
                }
                linesRead.add(line);
            }
            deck.populateDeck(numberCardStack);
            deck.populateDeck(specialCardStack);
            Collections.shuffle(deck.getDeckAsList());
            System.out.println("Deck Size (GameSettings): " + deck.getDeckAsList().size()); // fix later -> deck size may be wrong
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Deck getDeckObject() {
        return deck;
    }

    private void createNumberCards(String piece) {
        if (piece.startsWith("Red")) {
            numberCardStack.add(new NumberCard("Red", Integer.parseInt(piece.substring(4))));
        } else if (piece.startsWith("Blue")) {
            numberCardStack.add(new NumberCard("Blue", Integer.parseInt(piece.substring(5))));
        } else if (piece.startsWith("Green")) {
            numberCardStack.add(new NumberCard("Green", Integer.parseInt(piece.substring(6))));
        } else if (piece.startsWith("Yellow")) {
            numberCardStack.add(new NumberCard("Yellow", Integer.parseInt(piece.substring(7))));
        }
    }

    private void createSpecialCards(String piece) {
        if (piece.startsWith("Reverse")) {
            specialCardStack.add(new SpecialCard("reverse", piece.substring(8)));
        } else if (piece.startsWith("Draw Two")) {
            specialCardStack.add(new SpecialCard("draw_two", piece.substring(9)));
        } else if (piece.startsWith("Wild") && piece.length() <= 4) {
            specialCardStack.add(new SpecialCard("wild"));
        } else if (piece.startsWith("Wild Draw Four")) {
            specialCardStack.add(new SpecialCard("wild_draw_four"));
        } else if (piece.startsWith("Skip")) {
            specialCardStack.add(new SpecialCard("skip", piece.substring(5)));
        }
    }
}
