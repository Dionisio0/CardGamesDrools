package org.example.briscola.unit;

import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.drools.ruleunits.api.RuleUnitInstance;
import org.drools.ruleunits.api.RuleUnitProvider;
import org.example.briscola.model.*;
import org.example.core.Rank;
import org.example.core.Suit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BriscolaRuleTest {

    BriscolaUnit unit;
    RuleUnitInstance<BriscolaUnit> instance;
    Dealer dealer;

    @Before
    public void setup() {
        dealer = new Dealer(List.of("P1", "P2", "P3", "P4"));
        unit = new BriscolaUnit(dealer);
        instance = RuleUnitProvider.get().createRuleUnitInstance(unit);
    }

    @After
    public void tearDown() {
        instance.close();
    }

    @Test
    public void testDealThreeCardsToEachPlayer() {
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        Player p3 = new Player("P3");
        Player p4 = new Player("P4");
        Turn turn = new Turn(Phase.SETUP, "P1", "P1");

        unit.getPlayers().add(p1);
        unit.getPlayers().add(p2);
        unit.getPlayers().add(p3);
        unit.getPlayers().add(p4);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("assegna 3 carte ad ogni giocatore"));

        assertThat(p1.getHand()).hasSize(3);
        assertThat(p2.getHand()).hasSize(3);
        assertThat(p3.getHand()).hasSize(3);
        assertThat(p4.getHand()).hasSize(3);
    }

    @Test
    public void testRevealBriscola() {
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        Player p3 = new Player("P3");
        Player p4 = new Player("P4");
        Turn turn = new Turn(Phase.SETUP, "P1", "P1");

        unit.getPlayers().add(p1);
        unit.getPlayers().add(p2);
        unit.getPlayers().add(p3);
        unit.getPlayers().add(p4);
        unit.getTurn().set(turn);

        instance.fire();

        assertThat(turn.getBriscola()).isNotNull();
        assertThat(turn.getPhase()).isEqualTo(Phase.EVALUATE);
    }

    @Test
    public void testBriscolaBeatNonBriscola() {
        BriscolaCard briscolaCard = new BriscolaCard(Suit.HEART, Rank.FOUR, 0, 4);
        BriscolaCard nonBriscolaCard = new BriscolaCard(Suit.SPADE, Rank.ACE, 11, 15);

        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        p1.setHand(new java.util.ArrayList<>(List.of(briscolaCard)));
        p2.setHand(new java.util.ArrayList<>(List.of(nonBriscolaCard)));

        Turn turn = new Turn(Phase.EVALUATE, "P1", "P1");
        turn.setBriscola(new BriscolaCard(Suit.HEART, Rank.SEVEN, 0, 7));

        unit.getPlayers().add(p1);
        unit.getPlayers().add(p2);
        unit.getTable().add(new CardPlayed("P1", briscolaCard));
        unit.getTable().add(new CardPlayed("P2", nonBriscolaCard));
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("briscola batte non-briscola"));

        assertThat(unit.getEvents()).anyMatch(e -> e.contains("[REMOVE]") && e.contains("P2"));
    }

    @Test
    public void testSameSuitHigherPowerWins() {
        BriscolaCard strongCard = new BriscolaCard(Suit.SPADE, Rank.THREE, 10, 14);
        BriscolaCard weakCard = new BriscolaCard(Suit.SPADE, Rank.SEVEN, 0, 7);

        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        p1.setHand(new java.util.ArrayList<>(List.of(strongCard)));
        p2.setHand(new java.util.ArrayList<>(List.of(weakCard)));

        Turn turn = new Turn(Phase.EVALUATE, "P1", "P1");
        turn.setBriscola(new BriscolaCard(Suit.HEART, Rank.SEVEN, 0, 7));

        unit.getPlayers().add(p1);
        unit.getPlayers().add(p2);
        unit.getTable().add(new CardPlayed("P1", strongCard));
        unit.getTable().add(new CardPlayed("P2", weakCard));
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("stesso seme, valore maggiore batte valore minore"));

        assertThat(unit.getEvents()).anyMatch(e -> e.contains("[REMOVE]") && e.contains("P2"));
    }

    @Test
    public void testWinnerGetsPoints() {
        BriscolaCard card1 = new BriscolaCard(Suit.SPADE, Rank.ACE, 11, 15);
        BriscolaCard card2 = new BriscolaCard(Suit.SPADE, Rank.FOUR, 0, 4);

        Player p1 = new Player("P1");
        p1.setHand(new java.util.ArrayList<>(List.of(card1)));

        Turn turn = new Turn(Phase.EVALUATE, "P1", "P1");
        turn.setBriscola(new BriscolaCard(Suit.HEART, Rank.SEVEN, 0, 7));

        unit.getPlayers().add(p1);
        unit.getTable().add(new CardPlayed("P1", card1));
        unit.getTable().add(new CardPlayed("P2", card2));
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("assegna i punti al vincitore e passa alla fase di reset"));

        assertThat(p1.getScore()).isEqualTo(11); // 11 (ACE) + 0 (FOUR)
        assertThat(turn.getPhase()).isEqualTo(Phase.RESET);
    }

    // --- 02-evaluate.drl: regole di stampa e rimozione carta dalla mano ---

    @Test
    public void testPrintPlayerHands() {
        Player p1 = new Player("P1");
        p1.setHand(new ArrayList<>(List.of(new BriscolaCard(Suit.HEART, Rank.ACE, 11, 15))));

        Turn turn = new Turn(Phase.EVALUATE, "P1", "P1");
        turn.setBriscola(new BriscolaCard(Suit.HEART, Rank.SEVEN, 0, 7));

        unit.getPlayers().add(p1);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("stampa la mano dei giocatori"));

        assertThat(unit.getEvents()).anyMatch(e -> e.contains("[HAND]") && e.contains("P1"));
    }

    @Test
    public void testPrintTurnPlayer() {
        Player p1 = new Player("P1");

        Turn turn = new Turn(Phase.EVALUATE, "P1", "P1");
        turn.setBriscola(new BriscolaCard(Suit.HEART, Rank.SEVEN, 0, 7));

        unit.getPlayers().add(p1);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("stampa il giocatore di turno"));

        assertThat(unit.getEvents()).anyMatch(e -> e.contains("[TURN]") && e.contains("P1"));
    }

    @Test
    public void testRemovePlayedCardFromHand() {
        BriscolaCard card = new BriscolaCard(Suit.SPADE, Rank.ACE, 11, 15);
        Player p1 = new Player("P1");
        p1.setHand(new ArrayList<>(List.of(card, new BriscolaCard(Suit.HEART, Rank.FOUR, 0, 4))));

        Turn turn = new Turn(Phase.EVALUATE, "P1", "P1");
        turn.setBriscola(new BriscolaCard(Suit.HEART, Rank.SEVEN, 0, 7));

        unit.getPlayers().add(p1);
        unit.getTable().add(new CardPlayed("P1", card));
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("rimuovi la carta giocata dalla mano del giocatore"));

        assertThat(p1.getHand()).doesNotContain(card);
        assertThat(unit.getEvents()).anyMatch(e -> e.contains("[PLAY]"));
    }

    @Test
    public void testHandSuitBeatsForeignSuit() {
        // P1 gioca SPADE (seme di mano), P2 gioca CLUB (seme estraneo), briscola e HEART
        BriscolaCard handSuitCard = new BriscolaCard(Suit.SPADE, Rank.FOUR, 0, 4);
        BriscolaCard foreignSuitCard = new BriscolaCard(Suit.CLUB, Rank.ACE, 11, 15);

        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        p1.setHand(new ArrayList<>(List.of(handSuitCard)));
        p2.setHand(new ArrayList<>(List.of(foreignSuitCard)));

        Turn turn = new Turn(Phase.EVALUATE, "P1", "P1");
        turn.setBriscola(new BriscolaCard(Suit.HEART, Rank.SEVEN, 0, 7));

        unit.getPlayers().add(p1);
        unit.getPlayers().add(p2);
        unit.getTable().add(new CardPlayed("P1", handSuitCard));
        unit.getTable().add(new CardPlayed("P2", foreignSuitCard));
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("seme di mano batte seme estraneo"));

        assertThat(unit.getEvents()).anyMatch(e -> e.contains("[REMOVE]") && e.contains("P2"));
    }

    // --- 03-reset.drl ---

    @Test
    public void testResetRemovesCardsFromTable() {
        BriscolaCard card = new BriscolaCard(Suit.SPADE, Rank.ACE, 11, 15);
        Turn turn = new Turn(Phase.RESET, "P1", "P1");
        turn.setBriscola(new BriscolaCard(Suit.HEART, Rank.SEVEN, 0, 7));

        unit.getTable().add(new CardPlayed("P1", card));
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("rimuovi carte dal tavolo"));

        assertThat(unit.getEvents()).anyMatch(e -> e.contains("[RESET]") && e.contains("removed from table"));
    }

    @Test
    public void testResetReinsertDefeatedPlayers() {
        Player p2 = new Player("P2");
        Turn turn = new Turn(Phase.RESET, "P1", "P1");
        turn.setBriscola(new BriscolaCard(Suit.HEART, Rank.SEVEN, 0, 7));

        unit.getDefeats().add(p2);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("reinserire giocatori al tavolo"));

        assertThat(unit.getEvents()).anyMatch(e -> e.contains("[RESET]") && e.contains("P2") && e.contains("re-enters"));
    }

    @Test
    public void testResetCompleteAdvancesToDraw() {
        Turn turn = new Turn(Phase.RESET, "P1", "P1");
        turn.setBriscola(new BriscolaCard(Suit.HEART, Rank.SEVEN, 0, 7));

        // Tavolo vuoto, nessun giocatore sconfitto
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("tutti i giocatori sono rientrati e il tavolo è vuoto, si passa alla fase di pesca"));

        assertThat(turn.getPhase()).isEqualTo(Phase.DRAW);
    }

    // --- 04-draw.drl ---

    @Test
    public void testDealOneCardToEachPlayer() {
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        Player p3 = new Player("P3");
        Player p4 = new Player("P4");
        // Ogni giocatore ha 2 carte (dopo aver giocato 1 delle 3 iniziali)
        BriscolaDeck deck = unit.getDeck();
        p1.setHand(new ArrayList<>(deck.draw(2)));
        p2.setHand(new ArrayList<>(deck.draw(2)));
        p3.setHand(new ArrayList<>(deck.draw(2)));
        p4.setHand(new ArrayList<>(deck.draw(2)));

        Turn turn = new Turn(Phase.DRAW, "P1", "P1");
        turn.setBriscola(new BriscolaCard(Suit.HEART, Rank.SEVEN, 0, 7));

        unit.getPlayers().add(p1);
        unit.getPlayers().add(p2);
        unit.getPlayers().add(p3);
        unit.getPlayers().add(p4);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("assegna una carta ad ogni giocatore"));

        assertThat(p1.getHand()).hasSize(3);
        assertThat(p2.getHand()).hasSize(3);
        assertThat(p3.getHand()).hasSize(3);
        assertThat(p4.getHand()).hasSize(3);
    }

    @Test
    public void testAllPlayersHaveCardsAfterDraw() {
        Player p1 = new Player("P1");
        p1.setHand(new ArrayList<>(List.of(new BriscolaCard(Suit.SPADE, Rank.ACE, 11, 15))));

        Turn turn = new Turn(Phase.DRAW, "P1", "P1");
        turn.setBriscola(new BriscolaCard(Suit.HEART, Rank.SEVEN, 0, 7));

        unit.getPlayers().add(p1);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("tutti i giocatori hanno almeno 1 carta in mano"));

        assertThat(turn.getPhase()).isEqualTo(Phase.EVALUATE);
    }

    @Test
    public void testLastDrawTurn() {
        // 3 giocatori hanno gia 3 carte, 1 giocatore ne ha 2, il mazzo e vuoto
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        Player p3 = new Player("P3");
        Player p4 = new Player("P4");

        BriscolaCard briscola = new BriscolaCard(Suit.HEART, Rank.SEVEN, 0, 7);

        p1.setHand(new ArrayList<>(List.of(
            new BriscolaCard(Suit.SPADE, Rank.ACE, 11, 15),
            new BriscolaCard(Suit.SPADE, Rank.THREE, 10, 14),
            new BriscolaCard(Suit.SPADE, Rank.KING, 4, 13)
        )));
        p2.setHand(new ArrayList<>(List.of(
            new BriscolaCard(Suit.CLUB, Rank.ACE, 11, 15),
            new BriscolaCard(Suit.CLUB, Rank.THREE, 10, 14),
            new BriscolaCard(Suit.CLUB, Rank.KING, 4, 13)
        )));
        p3.setHand(new ArrayList<>(List.of(
            new BriscolaCard(Suit.DIAMOND, Rank.ACE, 11, 15),
            new BriscolaCard(Suit.DIAMOND, Rank.THREE, 10, 14),
            new BriscolaCard(Suit.DIAMOND, Rank.KING, 4, 13)
        )));
        p4.setHand(new ArrayList<>(List.of(
            new BriscolaCard(Suit.HEART, Rank.ACE, 11, 15),
            new BriscolaCard(Suit.HEART, Rank.THREE, 10, 14)
        )));

        // Svuota il mazzo
        BriscolaDeck deck = unit.getDeck();
        while (!deck.isEmpty()) {
            deck.draw();
        }

        Turn turn = new Turn(Phase.DRAW, "P4", "P4");
        turn.setBriscola(briscola);

        unit.getPlayers().add(p1);
        unit.getPlayers().add(p2);
        unit.getPlayers().add(p3);
        unit.getPlayers().add(p4);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("ultimo turno di pesca"));

        assertThat(p4.getHand()).hasSize(3);
        assertThat(unit.getEvents()).anyMatch(e -> e.contains("briscola card"));
    }

    @Test
    public void testNoCardsLeftGameOver() {
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        // Mani vuote
        BriscolaDeck deck = unit.getDeck();
        while (!deck.isEmpty()) {
            deck.draw();
        }

        Turn turn = new Turn(Phase.DRAW, "P1", "P1");
        turn.setBriscola(new BriscolaCard(Suit.HEART, Rank.SEVEN, 0, 7));

        unit.getPlayers().add(p1);
        unit.getPlayers().add(p2);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("i giocatori non hanno più carte in mano"));

        assertThat(turn.getPhase()).isEqualTo(Phase.END);
        assertThat(unit.getEvents()).anyMatch(e -> e.contains("[GAME OVER]"));
    }

    // --- 05-end.drl ---

    @Test
    public void testEndPrintsFinalScores() {
        Player p1 = new Player("P1");
        p1.setScore(45);

        Turn turn = new Turn(Phase.END, "P1", "P1");
        turn.setBriscola(new BriscolaCard(Suit.HEART, Rank.SEVEN, 0, 7));

        unit.getPlayers().add(p1);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("fine"));

        assertThat(unit.getEvents()).anyMatch(e -> e.contains("[RESULT]") && e.contains("P1") && e.contains("45"));
    }
}
