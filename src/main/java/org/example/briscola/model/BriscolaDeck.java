package org.example.briscola.model;

import org.example.core.Deck;
import org.example.core.Rank;
import org.example.core.Suit;

import java.util.List;

public final class BriscolaDeck extends Deck<BriscolaCard> {
    @Override
    protected BriscolaCard createCard(Suit suit, Rank rank) {
        int points = getPoints(rank);
        int power = getPower(rank);
        return new BriscolaCard(suit, rank, points, power);
    }

    @Override
    protected List<Rank> getValidRanks(){
        return List.of(Rank.ACE, Rank.TWO, Rank.THREE, Rank.FOUR, Rank.FIVE,
                       Rank.SIX, Rank.SEVEN, Rank.JACK, Rank.QUEEN, Rank.KING);
    }

    private int getPoints(Rank rank){
        return switch(rank){
            case ACE -> 11;
            case THREE -> 10;
            case KING -> 4;
            case QUEEN -> 3;
            case JACK -> 2;
            default -> 0;
        };
    }

    private int getPower(Rank rank){
        return switch(rank){
            case ACE -> 15;
            case THREE -> 14;
            default -> rank.getOrdinal();
        };
    }
}
