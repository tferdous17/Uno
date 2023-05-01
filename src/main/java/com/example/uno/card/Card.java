package com.example.uno.card;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Card {
    protected CardColor color;
    protected int value;
    protected Image image;
    protected ImageView imageView;
    // Change following file path if used on a different device
    protected String imagePath = "C:\\Users\\tferd\\Desktop\\Uno\\Uno\\src\\main\\resources\\com\\example\\uno\\imgs\\card_icons\\";

    public Card() {

    }

    protected abstract void effect();

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

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

    public void setCardTo(Card card) {
        this.color = card.color;
        this.value = card.value;
        this.image = card.image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return this.value == card.getValue() || this.color == card.getColor();
    }
}
