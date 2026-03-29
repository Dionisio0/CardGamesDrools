package org.example.briscola.model;

import org.example.core.Card;
import org.example.core.Rank;
import org.example.core.Suit;

public final class BriscolaCard extends Card {
    private final int points;
    private final int power;

    public BriscolaCard(Suit suit, Rank rank, int points, int power) {
        super(suit, rank);
        this.points = points;
        this.power = power;
    }

    public int getPoints() {
        return points;
    }

    public int getPower() {
        return power;
    }

    @Override
    public String toString() {
        return getRank() + " of " + getSuit() + " (points: " + points + ", power: " + power + ")";
    }
}
