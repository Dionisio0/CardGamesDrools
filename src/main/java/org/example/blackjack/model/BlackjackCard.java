package org.example.blackjack.model;

import org.example.core.Card;
import org.example.core.Rank;
import org.example.core.Suit;

public final class BlackjackCard extends Card {
    private final int value;

    public BlackjackCard(Suit suit, Rank rank, int value) {
        super(suit, rank);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getRank() + " of " + getSuit();
    }
}
