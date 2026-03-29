package org.example.cartaalta.model;

public final class Player {
    private final String name;
    private CartaAltaCard card;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public CartaAltaCard getCard() {
        return card;
    }

    public void setCard(CartaAltaCard card) {
        this.card = card;
    }
}
