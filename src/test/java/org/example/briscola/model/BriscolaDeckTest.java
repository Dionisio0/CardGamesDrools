package org.example.briscola.model;

import org.example.core.Rank;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BriscolaDeckTest {

    @Test
    public void testDeckHas40Cards() {
        BriscolaDeck deck = new BriscolaDeck();
        assertThat(deck.getSize()).isEqualTo(40);
    }

    @Test
    public void testAceProperties() {
        BriscolaDeck deck = new BriscolaDeck();
        for (int i = 0; i < 40; i++) {
            BriscolaCard card = deck.draw();
            if (card.getRank() == Rank.ACE) {
                assertThat(card.getPoints()).isEqualTo(11);
                assertThat(card.getPower()).isEqualTo(15);
            }
        }
    }

    @Test
    public void testThreeProperties() {
        BriscolaDeck deck = new BriscolaDeck();
        for (int i = 0; i < 40; i++) {
            BriscolaCard card = deck.draw();
            if (card.getRank() == Rank.THREE) {
                assertThat(card.getPoints()).isEqualTo(10);
                assertThat(card.getPower()).isEqualTo(14);
            }
        }
    }

    @Test
    public void testFaceCardPoints() {
        BriscolaDeck deck = new BriscolaDeck();
        for (int i = 0; i < 40; i++) {
            BriscolaCard card = deck.draw();
            switch (card.getRank()) {
                case KING -> assertThat(card.getPoints()).isEqualTo(4);
                case QUEEN -> assertThat(card.getPoints()).isEqualTo(3);
                case JACK -> assertThat(card.getPoints()).isEqualTo(2);
                default -> {}
            }
        }
    }

    @Test
    public void testNumericCardHasZeroPoints() {
        BriscolaDeck deck = new BriscolaDeck();
        for (int i = 0; i < 40; i++) {
            BriscolaCard card = deck.draw();
            Rank r = card.getRank();
            if (r == Rank.TWO || r == Rank.FOUR || r == Rank.FIVE || r == Rank.SIX || r == Rank.SEVEN) {
                assertThat(card.getPoints()).isEqualTo(0);
            }
        }
    }
}
