package com.example.uno.card;

public class SpecialCard extends Card {
    private SpecialCardType cardType;

    public SpecialCard() {} // default constructor

    public SpecialCard(String specialCardType) {
        setCardType(SpecialCardType.valueOf(specialCardType));
    }

    public SpecialCard(String specialCardType, int number) {
        setCardType(SpecialCardType.valueOf(specialCardType));
        setNumber(number); // setNumber() method from super class
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
}
