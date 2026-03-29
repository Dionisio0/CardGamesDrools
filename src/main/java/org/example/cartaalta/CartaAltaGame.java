package org.example.cartaalta;

import org.drools.ruleunits.api.RuleUnitInstance;
import org.drools.ruleunits.api.RuleUnitProvider;
import org.example.cartaalta.model.Phase;
import org.example.cartaalta.model.Player;
import org.example.cartaalta.model.Turn;
import org.example.cartaalta.unit.CartaAltaUnit;

import java.util.List;

public class CartaAltaGame {
    private final List<Player> players;
    private final Turn turn;
    private final CartaAltaUnit unit;

    public CartaAltaGame(List<Player> players) {
        this(players, new Turn(Phase.DRAW));
    }

    public CartaAltaGame(List<Player> players, Turn turn) {
        this.players = players;
        this.turn = turn;
        this.unit = new CartaAltaUnit();
        setupUnit();
    }

    private void setupUnit() {
        unit.getTurn().set(turn);
        players.forEach(unit.getPlayers()::add);
    }

    public void start(){
        try(RuleUnitInstance<CartaAltaUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(unit)){
            instance.fire();
            unit.getEvents().forEach(System.out::println);
            instance.close();
        }
    }
}
