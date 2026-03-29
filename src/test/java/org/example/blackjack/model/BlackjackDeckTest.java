package org.example.blackjack.model;

import org.example.core.Rank;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BlackjackDeckTest {

    @Test
    public void testDeckHas52Cards() {
        BlackjackDeck deck = new BlackjackDeck();
        assertThat(deck.getSize()).isEqualTo(52);
    }

    @Test
    public void testAceValueIs11() {
        BlackjackDeck deck = new BlackjackDeck();
        for (int i = 0; i < 52; i++) {
            BlackjackCard card = deck.draw();
            if (card.getRank() == Rank.ACE) {
                assertThat(card.getValue()).isEqualTo(11);
            }
        }
    }

    @Test
    public void testFaceCardsValueIs10() {
        BlackjackDeck deck = new BlackjackDeck();
        for (int i = 0; i < 52; i++) {
            BlackjackCard card = deck.draw();
            Rank r = card.getRank();
            if (r == Rank.JACK || r == Rank.QUEEN || r == Rank.KING) {
                assertThat(card.getValue()).isEqualTo(10);
            }
        }
    }

    @Test
    public void testNumericCardValue() {
        BlackjackDeck deck = new BlackjackDeck();
        for (int i = 0; i < 52; i++) {
            BlackjackCard card = deck.draw();
            Rank r = card.getRank();
            if (r != Rank.ACE && r != Rank.TEN && r != Rank.JACK && r != Rank.QUEEN && r != Rank.KING) {
                assertThat(card.getValue()).isEqualTo(r.getOrdinal());
            }
        }
    }
}
