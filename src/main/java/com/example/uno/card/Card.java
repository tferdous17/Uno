package com.example.uno.card;

public abstract class Card {
    protected CardColor color;
    protected int value;

    public Card() {

    }

    protected abstract void effect();

    public CardColor getColor() {
        return color;
    }

    public String getColorName() { return color.toString(); }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
