package org.example.briscola.model;

import java.util.List;

public final class Dealer {
    private final List<String> players;

    public Dealer(List<String> players) {
        this.players = players;
    }

    public String getNextPlayer(String currentPlayer) {
        int index = players.indexOf(currentPlayer) + 1;
        return players.get(index % players.size());
    }
}
