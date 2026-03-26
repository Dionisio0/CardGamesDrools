package org.example.briscola;

import org.drools.ruleunits.api.RuleUnitInstance;
import org.drools.ruleunits.api.RuleUnitProvider;
import org.example.briscola.model.*;
import org.example.briscola.unit.BriscolaUnit;

import java.util.List;

public class BriscolaGame {
    private final List<Player> players;
    private final Turn turn;
    private final BriscolaUnit unit;
    private final Dealer dealer;

    public BriscolaGame(Player player1, Player player2, Player player3, Player player4) {
        this(player1, player2, player3, player4, new Turn(Phase.SETUP, player1.getName(), player1.getName()));
    }

    public BriscolaGame(Player player1, Player player2, Player player3, Player player4, Turn turn) {
        this.players = List.of(player1, player2, player3, player4);
        this.turn = turn;
        this.dealer = new Dealer(List.of(players.get(0).getName(), players.get(1).getName(), players.get(2).getName(), players.get(3).getName()));
        this.unit = new BriscolaUnit(dealer);
        setupUnit();
    }

    private void setupUnit(){
        players.forEach(unit.getPlayers()::add);
        unit.getTurn().set(turn);
    }

    public void start(){
        RuleUnitInstance<BriscolaUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(unit);

        // game setup
        instance.fire();

        unit.getEvents().forEach(System.out::println);
        unit.getEvents().clear();

        Player p1 = players.get(0);
        Player p2 = players.get(1);
        Player p3 = players.get(2);
        Player p4 = players.get(3);

        // game-loop
        for(int i = 0; i < 10; i++){
            System.out.println("\n\n\n--- TURN: || " + (i+1) + " || ---\n\n\n");

            unit.getTable().add(new CardPlayed(p1.getName(), p1.selectCard(0)));
            unit.getTable().add(new CardPlayed(p2.getName(), p2.selectCard(0)));
            unit.getTable().add(new CardPlayed(p3.getName(), p3.selectCard(0)));
            unit.getTable().add(new CardPlayed(p4.getName(), p4.selectCard(0)));

            instance.fire();

            unit.getEvents().forEach(System.out::println);
            unit.getEvents().clear();
        }

        instance.close();

    }

}
