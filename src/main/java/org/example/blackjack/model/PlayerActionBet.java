package org.example.blackjack.model;

public class PlayerActionBet extends PlayerAction{
    private final double betAmount;

    public PlayerActionBet(String playerName, double betAmount) {
        super(Action.BET, playerName);
        this.betAmount = betAmount;
    }

    public double getBetAmount() {
        return betAmount;
    }
}
