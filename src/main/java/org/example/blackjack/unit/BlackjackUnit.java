package org.example.blackjack.unit;

import org.drools.ruleunits.api.*;
import org.example.blackjack.model.BlackjackDeck;
import org.example.blackjack.model.Player;
import org.example.blackjack.model.Turn;
import org.example.blackjack.model.PlayerAction;

import java.util.ArrayList;
import java.util.List;

public class BlackjackUnit implements RuleUnitData {
    private final SingletonStore<Player> dealer;
    private final SingletonStore<Player> player;
    private final SingletonStore<Turn> turn;
    private final DataStore<PlayerAction> playerActions;
    private final BlackjackDeck deck;
    private final List<String> events;

    public BlackjackUnit() {
        this.dealer = DataSource.createSingleton();
        this.player = DataSource.createSingleton();
        this.turn = DataSource.createSingleton();
        this.playerActions = DataSource.createStore();
        this.deck = new BlackjackDeck();
        deck.shuffle();
        this.events = new ArrayList<>();
    }

    public SingletonStore<Player> getDealer() {
        return dealer;
    }

    public SingletonStore<Player> getPlayer() {
        return player;
    }

    public SingletonStore<Turn> getTurn() {
        return turn;
    }

    public DataStore<PlayerAction> getPlayerActions() {
        return playerActions;
    }

    public BlackjackDeck getDeck() {
        return deck;
    }

    public List<String> getEvents() {
        return events;
    }
}
