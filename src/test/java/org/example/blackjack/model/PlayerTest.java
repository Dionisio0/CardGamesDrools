package org.example.blackjack.model;

import org.example.core.Rank;
import org.example.core.Suit;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerTest {

    @Test
    public void testHandValueSimple() {
        Player player = new Player("Test", 100);
        player.addCard(new BlackjackCard(Suit.HEART, Rank.FIVE, 5));
        player.addCard(new BlackjackCard(Suit.SPADE, Rank.SEVEN, 7));

        assertThat(player.getHandValue()).isEqualTo(12);
    }

    @Test
    public void testHandValueWithAceAdjustment() {
        Player player = new Player("Test", 100);
        player.addCard(new BlackjackCard(Suit.HEART, Rank.ACE, 11));
        player.addCard(new BlackjackCard(Suit.SPADE, Rank.KING, 10));
        player.addCard(new BlackjackCard(Suit.CLUB, Rank.FIVE, 5));

        // ACE(11) + KING(10) + FIVE(5) = 26 -> ACE becomes 1 -> 16
        assertThat(player.getHandValue()).isEqualTo(16);
    }
}
