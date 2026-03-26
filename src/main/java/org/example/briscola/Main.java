package org.example.briscola;

import org.drools.ruleunits.api.RuleUnitInstance;
import org.drools.ruleunits.api.RuleUnitProvider;
import org.example.briscola.unit.BriscolaUnit;
import org.example.briscola.model.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Player p1 = new Player("Player 1");
        Player p2 = new Player("Player 2");
        Player p3 = new Player("Player 3");
        Player p4 = new Player("Player 4");

        BriscolaGame game = new BriscolaGame(p1, p2, p3, p4);

        game.start();

    }
}
