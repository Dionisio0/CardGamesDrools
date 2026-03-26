package org.example.cartaalta.unit;

import org.drools.ruleunits.api.*;
import org.example.cartaalta.model.CartaAltaDeck;
import org.example.cartaalta.model.Player;
import org.example.cartaalta.model.Turn;

import java.util.ArrayList;
import java.util.List;

public class CartaAltaUnit implements RuleUnitData {
    private final SingletonStore<Turn> turn;
    private final DataStore<Player> players;
    private final CartaAltaDeck deck;
    private final List<String> events;

    public CartaAltaUnit() {
        this.turn = DataSource.createSingleton();
        this.players = DataSource.createStore();
        this.events = new ArrayList<>();
        this.deck = new CartaAltaDeck();
        deck.shuffle();
    }

    public SingletonStore<Turn> getTurn() {
        return turn;
    }

    public DataStore<Player> getPlayers() {
        return players;
    }

    public List<String> getEvents() {
        return events;
    }

    public CartaAltaDeck getDeck() {
        return deck;
    }
}
