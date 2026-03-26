package org.example.briscola.unit;

import org.drools.ruleunits.api.DataSource;
import org.drools.ruleunits.api.DataStore;
import org.drools.ruleunits.api.RuleUnitData;
import org.drools.ruleunits.api.SingletonStore;
import org.example.briscola.model.*;
import java.util.ArrayList;
import java.util.List;

public class BriscolaUnit implements RuleUnitData {
    private final DataStore<Player> players;
    private final DataStore<CardPlayed> table;
    private final DataStore<Player> defeats;
    private final SingletonStore<Turn> turn;

    private final BriscolaDeck deck;
    private final Dealer dealer;

    private final List<String> events;

    public BriscolaUnit(Dealer dealer){
        this.players = DataSource.createStore();
        this.table = DataSource.createStore();
        this.defeats = DataSource.createStore();
        this.turn = DataSource.createSingleton();
        this.deck = new BriscolaDeck();
        deck.shuffle();
        this.dealer = dealer;
        this.events = new ArrayList<>();
    }

    public DataStore<CardPlayed> getTable() {
        return table;
    }

    public SingletonStore<Turn> getTurn() {
        return turn;
    }

    public DataStore<Player> getDefeats() {
        return defeats;
    }

    public BriscolaDeck getDeck() {
        return deck;
    }

    public List<String> getEvents() {
        return events;
    }

    public DataStore<Player> getPlayers() {
        return players;
    }

    public Dealer getDealer() {
        return dealer;
    }

}
