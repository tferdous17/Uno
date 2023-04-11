package com.example.uno.card;

public class NumberCard extends Card {

    public NumberCard() {} // default constructor

    public NumberCard(String color, int number) {
        setColor(CardColor.valueOf(color)); // takes a string as input instead of an instance of the "CardColor" class
        setNumber(number);
    }

    @Override
    protected void effect() {

    }
}
