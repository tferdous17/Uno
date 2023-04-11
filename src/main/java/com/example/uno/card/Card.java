package com.example.uno.card;

public abstract class Card {
    protected CardColor color;
    protected int number;

    public Card() {

    }

    protected abstract void effect();

    public CardColor getColor() {
        return color;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
