package com.example.uno.card;

public class SpecialCard extends Card {
    private SpecialCardType cardType;

    public SpecialCard() {} // default constructor

    public SpecialCard(String specialCardType) {
        setCardType(SpecialCardType.valueOf(specialCardType));
    }

    public SpecialCard(String specialCardType, String color) {
        setCardType(SpecialCardType.valueOf(specialCardType));
        setColor(CardColor.valueOf(color));
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

    }

    @Override
    public String toString() {
        return "SPECIAL CARD | Type: " + this.getCardType() + " | Color: " + this.getColor();
    }
}
