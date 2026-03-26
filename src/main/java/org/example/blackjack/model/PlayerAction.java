package org.example.blackjack.model;

public class PlayerAction {
    private final Action action;
    private final String playerName;

    public PlayerAction(Action action, String playerName) {
        this.action = action;
        this.playerName = playerName;
    }

    public Action getAction() {
        return action;
    }

    public String getPlayerName() {
        return playerName;
    }
}
