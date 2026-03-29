package org.example.cartaalta.model;

import org.example.core.Card;
import org.example.core.Rank;
import org.example.core.Suit;

public final class CartaAltaCard extends Card {
    private final int score;

    public CartaAltaCard(Suit suit, Rank rank, int score) {
        super(suit, rank);
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return getRank() + " of " + getSuit() + " (score: " + score + ")";
    }
}
