package org.example.cartaalta;

import org.example.cartaalta.model.Player;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Player> players = new ArrayList<Player>();
        for(int i = 0; i < 20; i++){
            players.add(new Player(""+i));
        }

        CartaAltaGame game = new CartaAltaGame(players);

        game.start();
    }
}
