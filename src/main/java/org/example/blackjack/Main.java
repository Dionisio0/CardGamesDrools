package org.example.blackjack;

import org.example.blackjack.model.*;

public class Main {
    public static void main(String[] args) {
        Player player = new Player("Player 1", 500);
        Player dealer = new Player("Dealer");

        BlackjackGame game = new BlackjackGame(player, dealer);
        game.start();
    }
}
