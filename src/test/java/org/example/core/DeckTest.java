package org.example.core;

import org.example.cartaalta.model.CartaAltaCard;
import org.example.cartaalta.model.CartaAltaDeck;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DeckTest {

    @Test
    public void testDraw(){
        CartaAltaDeck deck = new CartaAltaDeck();

        CartaAltaCard card = deck.draw();

        assertThat(deck.size()).isEqualTo(51);
        assertThat(deck.getCards()).doesNotContain(card);
    }

    @Test
    public void testDrawEmptyDeck(){
        CartaAltaDeck deck = new CartaAltaDeck();

        for(int i = 0; i < 52; i++){
            deck.draw();
        }

        assertThat(deck.size()).isEqualTo(0);
        assertThat(deck.draw()).isNull();
    }

    @Test
    public void testDrawCards(){
        CartaAltaDeck deck = new CartaAltaDeck();

        List<CartaAltaCard> drawnCards = deck.draw(5);

        assertThat(drawnCards).hasSize(5);
        assertThat(deck.size()).isEqualTo(52-5);
        assertThat(deck.getCards()).doesNotContainAnyElementsOf(drawnCards);
    }

    @Test
    public void testDrawCardsEmptyDeck(){
        CartaAltaDeck deck = new CartaAltaDeck();

        for(int i = 0; i < 52; i++){
            deck.draw();
        }

        List<CartaAltaCard> drawnCards = deck.draw(5);

        assertThat(drawnCards).isEmpty();
        assertThat(deck.getCards()).isEmpty();
    }

    @Test
    public void testDrawCardsMoreThanAvailable(){
        CartaAltaDeck deck = new CartaAltaDeck();

        List<CartaAltaCard> drawnCards = deck.draw(60);

        assertThat(drawnCards).hasSize(52);
        assertThat(deck.getCards()).isEmpty();
    }
}
