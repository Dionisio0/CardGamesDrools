package org.example.blackjack.model;

import org.example.core.Deck;
import org.example.core.Rank;
import org.example.core.Suit;

public final class BlackjackDeck extends Deck<BlackjackCard> {

    @Override
    protected BlackjackCard createCard(Suit suit, Rank rank) {
        int value = getValue(rank);
        return new BlackjackCard(suit, rank, value);
    }

    private int getValue(Rank rank) {
        return switch(rank) {
            case ACE -> 11;
            case TEN, JACK, QUEEN, KING -> 10;
            default -> rank.getOrdinal();
        };
    }
}
