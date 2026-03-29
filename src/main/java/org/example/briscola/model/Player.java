package org.example.briscola.model;

import java.util.ArrayList;
import java.util.List;

public final class Player {
    private final String name;
    private List<BriscolaCard> hand;
    private int score;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public List<BriscolaCard> getHand() {
        return hand;
    }

    public void setHand(List<BriscolaCard> hand) {
        this.hand = hand;
    }

    public void addCardToHand(BriscolaCard card) {
        this.hand.add(card);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public BriscolaCard selectCard(int index) {
        return hand.get(index);
    }
}
