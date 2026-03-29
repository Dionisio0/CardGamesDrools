package org.example.briscola.model;

public final class CardPlayed {
    private final String name;
    private BriscolaCard card;

    public CardPlayed(String name, BriscolaCard card) {
        this.name = name;
        this.card = card;
    }

    public String getName() {
        return name;
    }

    public BriscolaCard getCard() {
        return card;
    }

    public void setCard(BriscolaCard card) {
        this.card = card;
    }
}
