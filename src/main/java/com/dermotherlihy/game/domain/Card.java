package com.dermotherlihy.game.domain;

/**
 * Created by dermot.herlihy on 26/01/2016.
 */
public class Card {

    private Colour colour;

    public Card(Colour colour) {
        this.colour = colour;
    }

    public enum Colour {
        YELLOW
    }


    public Colour getColour() {
        return colour;
    }
}
