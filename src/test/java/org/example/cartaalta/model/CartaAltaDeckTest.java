package org.example.cartaalta.model;

import org.example.core.Rank;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CartaAltaDeckTest {

    @Test
    public void testDeckHas52Cards() {
        CartaAltaDeck deck = new CartaAltaDeck();
        assertThat(deck.getSize()).isEqualTo(52);
    }

    @Test
    public void testAceScoreIs14() {
        CartaAltaDeck deck = new CartaAltaDeck();
        for (int i = 0; i < 52; i++) {
            CartaAltaCard card = deck.draw();
            if (card.getRank() == Rank.ACE) {
                assertThat(card.getScore()).isEqualTo(14);
            }
        }
    }

    @Test
    public void testNonAceScoreMatchesOrdinal() {
        CartaAltaDeck deck = new CartaAltaDeck();
        for (int i = 0; i < 52; i++) {
            CartaAltaCard card = deck.draw();
            if (card.getRank() != Rank.ACE) {
                assertThat(card.getScore()).isEqualTo(card.getRank().getOrdinal());
            }
        }
    }
}
