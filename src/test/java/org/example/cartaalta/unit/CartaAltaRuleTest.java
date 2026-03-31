package org.example.cartaalta.unit;

import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.drools.ruleunits.api.RuleUnitInstance;
import org.drools.ruleunits.api.RuleUnitProvider;
import org.example.cartaalta.model.CartaAltaCard;
import org.example.cartaalta.model.CartaAltaDeck;
import org.example.cartaalta.model.Phase;
import org.example.cartaalta.model.Player;
import org.example.cartaalta.model.Turn;
import org.example.core.Suit;
import org.example.core.Rank;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CartaAltaRuleTest {

    CartaAltaUnit unit;
    RuleUnitInstance<CartaAltaUnit> instance;

    @Before
    public void setup() {
        unit = new CartaAltaUnit();
        instance = RuleUnitProvider.get().createRuleUnitInstance(unit);
    }

    @After
    public void tearDown() {
        instance.close();
    }

    @Test
    public void testEveryPlayerReceivesOneCard() {
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        Turn turn = new Turn(Phase.DRAW);

        unit.getPlayers().add(p1);
        unit.getPlayers().add(p2);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("Ogni giocatore riceve una carta"));

        assertThat(p1.getCard()).isNotNull();
        assertThat(p2.getCard()).isNotNull();
    }

    @Test
    public void testAllPlayersHaveCard() {
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        CartaAltaDeck deck = unit.getDeck();
        p1.setCard(deck.draw());
        p2.setCard(deck.draw());
        Turn turn = new Turn(Phase.DRAW);

        unit.getPlayers().add(p1);
        unit.getPlayers().add(p2);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("Tutti i giocatori hanno una carta"));

        assertThat(turn.getPhase()).isEqualTo(Phase.FIND_MAX_SCORE);
    }

    @Test
    public void testFindMaxScore() {
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        CartaAltaCard c1 = new CartaAltaCard(Suit.HEART, Rank.KING, 13);
        CartaAltaCard c2 = new CartaAltaCard(Suit.SPADE, Rank.FIVE, 5);
        p1.setCard(c1);
        p2.setCard(c2);
        Turn turn = new Turn(Phase.FIND_MAX_SCORE);

        unit.getPlayers().add(p1);
        unit.getPlayers().add(p2);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("Trova carta con il punteggio più alto"));

        assertThat(turn.getPhase()).isEqualTo(Phase.EVALUATE);
        assertThat(turn.getHighestScore()).isEqualTo(13);
    }

    @Test
    public void testLoserEliminated() {
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        p1.setCard(new CartaAltaCard(Suit.HEART, Rank.KING, 13));
        p2.setCard(new CartaAltaCard(Suit.SPADE, Rank.FIVE, 5));
        Turn turn = new Turn(Phase.EVALUATE);
        turn.setHighestScore(13);

        unit.getPlayers().add(p1);
        unit.getPlayers().add(p2);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("I giocatori con punteggio più basso vengono eliminati"));

        assertThat(unit.getEvents()).anyMatch(e -> e.contains("P2"));
    }

    @Test
    public void testUniqueWinner() {
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        p1.setCard(new CartaAltaCard(Suit.HEART, Rank.KING, 13));
        p2.setCard(new CartaAltaCard(Suit.SPADE, Rank.FIVE, 5));
        Turn turn = new Turn(Phase.EVALUATE);
        turn.setHighestScore(13);

        unit.getPlayers().add(p1);
        unit.getPlayers().add(p2);
        unit.getTurn().set(turn);

        instance.fire();

        assertThat(turn.getPhase()).isEqualTo(Phase.END);
        assertThat(unit.getEvents()).anyMatch(e -> e.contains("[WINNER]") && e.contains("P1"));
    }

    @Test
    public void testRematch() {
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        p1.setCard(new CartaAltaCard(Suit.HEART, Rank.KING, 13));
        p2.setCard(new CartaAltaCard(Suit.SPADE, Rank.KING, 13));
        Turn turn = new Turn(Phase.EVALUATE);
        turn.setHighestScore(13);

        unit.getPlayers().add(p1);
        unit.getPlayers().add(p2);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("Rivincita"));

        assertThat(turn.getPhase()).isEqualTo(Phase.REMATCH);
        assertThat(unit.getEvents()).anyMatch(e -> e.contains("[REMATCH]"));
    }

    @Test
    public void testNotEnoughCardsToContinue() {
        // Svuota il mazzo
        CartaAltaDeck deck = unit.getDeck();
        while (!deck.isEmpty()) {
            deck.draw();
        }

        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        Turn turn = new Turn(Phase.DRAW);

        unit.getPlayers().add(p1);
        unit.getPlayers().add(p2);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("Non ci sono abbastanza carte per continuare"));

        assertThat(turn.getPhase()).isEqualTo(Phase.END);
        assertThat(unit.getEvents()).anyMatch(e -> e.contains("[END]"));
    }

    @Test
    public void testRematchRemovesPlayedCards() {
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        p1.setCard(new CartaAltaCard(Suit.HEART, Rank.KING, 13));
        p2.setCard(new CartaAltaCard(Suit.SPADE, Rank.KING, 13));
        Turn turn = new Turn(Phase.REMATCH);

        unit.getPlayers().add(p1);
        unit.getPlayers().add(p2);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("Rimuovi le carte giocate"));

        assertThat(p1.getCard()).isNull();
        assertThat(p2.getCard()).isNull();
    }

    @Test
    public void testRematchAllPlayersEmptyHands() {
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        // Carte gia nulle
        Turn turn = new Turn(Phase.REMATCH);

        unit.getPlayers().add(p1);
        unit.getPlayers().add(p2);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("Tutti i giocatori hanno le mani vuote"));

        assertThat(turn.getPhase()).isEqualTo(Phase.DRAW);
        assertThat(turn.getHighestScore()).isEqualTo(0);
    }

    @Test
    public void testEndTurn() {
        Turn turn = new Turn(Phase.END);

        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("end turn"));

        assertThat(unit.getEvents()).anyMatch(e -> e.contains("[END]"));
    }
}
