package org.example.core;

import org.example.cartaalta.model.CartaAltaCard;
import org.example.cartaalta.model.CartaAltaDeck;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DeckTest {

    @Test
    public void testDeckInitialSize() {
        CartaAltaDeck deck = new CartaAltaDeck();
        assertThat(deck.getSize()).isEqualTo(52);
    }

    @Test
    public void testDrawRemovesOneCard() {
        CartaAltaDeck deck = new CartaAltaDeck();
        CartaAltaCard card = deck.draw();

        assertThat(card).isNotNull();
        assertThat(deck.getSize()).isEqualTo(51);
    }

    @Test
    public void testDrawFromEmptyDeckReturnsNull() {
        CartaAltaDeck deck = new CartaAltaDeck();
        for (int i = 0; i < 52; i++) {
            deck.draw();
        }

        assertThat(deck.getSize()).isEqualTo(0);
        assertThat(deck.draw()).isNull();
    }

    @Test
    public void testDrawMultiple() {
        CartaAltaDeck deck = new CartaAltaDeck();
        List<CartaAltaCard> drawn = deck.draw(5);

        assertThat(drawn).hasSize(5);
        assertThat(deck.getSize()).isEqualTo(47);
    }

    @Test
    public void testDrawMoreThanAvailable() {
        CartaAltaDeck deck = new CartaAltaDeck();
        List<CartaAltaCard> drawn = deck.draw(60);

        assertThat(drawn).hasSize(52);
        assertThat(deck.isEmpty()).isTrue();
    }

    @Test
    public void testDrawMultipleFromEmptyDeck() {
        CartaAltaDeck deck = new CartaAltaDeck();
        for (int i = 0; i < 52; i++) {
            deck.draw();
        }

        List<CartaAltaCard> drawn = deck.draw(5);
        assertThat(drawn).isEmpty();
    }

    @Test
    public void testIsEmpty() {
        CartaAltaDeck deck = new CartaAltaDeck();
        assertThat(deck.isEmpty()).isFalse();

        for (int i = 0; i < 52; i++) {
            deck.draw();
        }
        assertThat(deck.isEmpty()).isTrue();
    }
}
