package org.example.blackjack.model;

import org.example.core.Rank;

import java.util.ArrayList;
import java.util.List;

public final class Player {
    private final String name;
    private List<BlackjackCard> hand;
    private double balance;
    private double bet;

    public Player(String name, double balance) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.balance = balance;
        this.bet = 0;
    }

    public Player(String name) {
        this(name, 0);
    }

    public int getHandValue() {
        int value = 0;
        int aceCount = 0;

        for (BlackjackCard card : hand) {
            value += card.getValue();
            if (card.getRank() == Rank.ACE) {
                aceCount++;
            }
        }

        while (value > 21 && aceCount > 0) {
            value -= 10;
            aceCount--;
        }

        return value;
    }

    public List<BlackjackCard> getHand() {
        return hand;
    }

    public void setHand(List<BlackjackCard> hand) {
        this.hand = hand;
    }

    public void addCard(BlackjackCard card) {
        hand.add(card);
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getBet() {
        return bet;
    }

    public void setBet(double bet) {
        this.bet = bet;
    }
}
