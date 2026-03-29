package org.example.cartaalta.model;

import org.example.core.Deck;
import org.example.core.Rank;
import org.example.core.Suit;

public final class CartaAltaDeck extends Deck<CartaAltaCard> {

    public CartaAltaDeck() {
        initializeDeck();
    }

    @Override
    protected CartaAltaCard createCard(Suit suit, Rank rank) {
        int score = (rank == Rank.ACE) ? 14 : rank.getOrdinal();
        return new CartaAltaCard(suit, rank, score);
    }
}
