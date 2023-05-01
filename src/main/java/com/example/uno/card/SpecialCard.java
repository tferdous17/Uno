package com.example.uno.card;

import com.example.uno.Deck;
import com.example.uno.Person;
import javafx.scene.image.Image;

import java.util.Objects;

public class SpecialCard extends Card {
    private SpecialCardType cardType;

    public SpecialCard() {} // default constructor

    public SpecialCard(String specialCardType) {
        setCardType(SpecialCardType.valueOf(specialCardType));
        setImage(new Image(imagePath + specialCardType + ".png"));
    }

    public SpecialCard(String specialCardType, String color) {
        setCardType(SpecialCardType.valueOf(specialCardType));
        setColor(CardColor.valueOf(color));
        setImage(new Image(imagePath + color.toLowerCase() + "-" + specialCardType + ".png"));
    }

    public SpecialCardType getCardType() {
        return cardType;
    }

    public void setCardType(SpecialCardType cardType) {
        this.cardType = cardType;
    }

    @Override
    protected void effect() {
        switch (cardType) {
            case skip -> { }
            case reverse -> { }
            case draw_two -> { }
            case wild -> { }
            case wild_draw_four -> { }
            default -> { }
        }
    }

    public void drawTwo(Deck deck, Person person) {
        if (this.cardType == SpecialCardType.draw_two) {
            for (int i = 0; i < 2; i++) {
                deck.giveCardFromCenterDeck(person);
            }
        }
    }

    public void skip(Person person) {
        if (this.cardType == SpecialCardType.skip) {
            person.setHasCurrentTurn(false);
        }
    }

    public void reverse(Person person) {
        person.setHasCurrentTurn(true);
    }

    public void wildDrawFour(Deck deck, Person person) {
        if (this.cardType == SpecialCardType.wild_draw_four) {
            for (int i = 0; i < 4; i++) {
                deck.giveCardFromCenterDeck(person);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SpecialCard that = (SpecialCard) o;
        return color == that.getColor() || cardType == that.getCardType();
    }

    @Override
    public String toString() {
        return "SPECIAL CARD | Type: " + this.getCardType() + " | Color: " + this.getColor();
    }
}
