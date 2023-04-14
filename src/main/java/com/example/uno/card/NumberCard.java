package com.example.uno.card;

import javafx.scene.image.ImageView;

public class NumberCard extends Card {

    public NumberCard() {} // default constructor

    public NumberCard(String color, int number) {
        setColor(CardColor.valueOf(color)); // takes a string as input instead of an instance of the "CardColor" class
        setValue(number); // takes a string as input instead of an instance of the "Value" class
    }

    @Override
    protected void effect() {

    }

    @Override
    public String toString() {
        return "NUMBER CARD | Color: " + this.getColor() + " | Value: " + this.getValue();
    }
}
