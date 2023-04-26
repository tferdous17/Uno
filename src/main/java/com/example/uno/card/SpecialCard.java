package com.example.uno.card;

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

    public SpecialCard(String specialCardType, int number) {
        setCardType(SpecialCardType.valueOf(specialCardType));
        setValue(number);
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


    @Override
    public String toString() {
        return "SPECIAL CARD | Type: " + this.getCardType() + " | Color: " + this.getColor();
    }
}
