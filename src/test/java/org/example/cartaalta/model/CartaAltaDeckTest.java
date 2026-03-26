package org.example.cartaalta.model;

import org.example.core.Rank;
import org.example.core.Suit;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class CartaAltaDeckTest {

    @Test
    public void testDeckContainsAllCartaAltaCards() {
        CartaAltaDeck deck = new CartaAltaDeck();

        List<String> expectedDescriptions = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                expectedDescriptions.add(suit + "-" + rank);
            }
        }

        List<String> cardDescriptions = new ArrayList<>();
        for(CartaAltaCard card : deck.getCards()) {
            cardDescriptions.add(card.getSuit() + "-" + card.getRank());
        }

        assertThat(cardDescriptions).containsAll(expectedDescriptions);
        assertThat(cardDescriptions.size()).isEqualTo(52);
    }

    @Test
    public void testCardsHaveValidScore(){
        CartaAltaDeck deck = new CartaAltaDeck();

        List<CartaAltaCard> aceCards = deck.getCards().stream().filter(c -> c.getRank() == Rank.ACE).toList();
        List<CartaAltaCard> noAceCards = deck.getCards().stream().filter(c -> c.getRank() != Rank.ACE).toList();

        assertThat(aceCards).allMatch(c -> c.getScore() == 14);
        assertThat(noAceCards).allMatch(c -> c.getScore() == c.getRank().getOrdinal());
    }
}
