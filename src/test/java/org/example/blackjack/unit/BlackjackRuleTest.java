package org.example.blackjack.unit;

import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.drools.ruleunits.api.RuleUnitInstance;
import org.drools.ruleunits.api.RuleUnitProvider;
import org.example.blackjack.model.*;
import org.example.core.Rank;
import org.example.core.Suit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BlackjackRuleTest {

    BlackjackUnit unit;
    RuleUnitInstance<BlackjackUnit> instance;

    @Before
    public void setup() {
        unit = new BlackjackUnit();
        instance = RuleUnitProvider.get().createRuleUnitInstance(unit);
    }

    @After
    public void tearDown() {
        instance.close();
    }

    @Test
    public void testValidBet() {
        Player player = new Player("Player", 100);
        Player dealer = new Player("Dealer");
        Turn turn = new Turn();

        unit.getPlayer().set(player);
        unit.getDealer().set(dealer);
        unit.getTurn().set(turn);

        unit.getPlayerActions().add(new PlayerActionBet("Player", 25));

        instance.fire(new RuleNameEqualsAgendaFilter("Scommessa valida"));

        assertThat(turn.getPhase()).isEqualTo(Phase.SETUP);
        assertThat(player.getBet()).isEqualTo(25);
        assertThat(player.getBalance()).isEqualTo(75);
    }

    @Test
    public void testInvalidBet() {
        Player player = new Player("Player", 100);
        Player dealer = new Player("Dealer");
        Turn turn = new Turn();

        unit.getPlayer().set(player);
        unit.getDealer().set(dealer);
        unit.getTurn().set(turn);

        unit.getPlayerActions().add(new PlayerActionBet("Player", 200));

        instance.fire(new RuleNameEqualsAgendaFilter("Scommessa non valida"));

        assertThat(turn.getPhase()).isEqualTo(Phase.BET);
        assertThat(player.getBalance()).isEqualTo(100);
        assertThat(unit.getEvents()).anyMatch(e -> e.contains("[INVALID BET]"));
    }

    @Test
    public void testSetupDealsFourCards() {
        Player player = new Player("Player", 100);
        Player dealer = new Player("Dealer");
        Turn turn = new Turn();
        turn.setPhase(Phase.SETUP);

        unit.getPlayer().set(player);
        unit.getDealer().set(dealer);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("assegna due carte al giocatore e due carte al dealer in modo alternato"));

        assertThat(player.getHand()).hasSize(2);
        assertThat(dealer.getHand()).hasSize(2);
    }

    @Test
    public void testPlayerHit() {
        Player player = new Player("Player", 100);
        Player dealer = new Player("Dealer");
        player.addCard(new BlackjackCard(Suit.HEART, Rank.FIVE, 5));
        player.addCard(new BlackjackCard(Suit.SPADE, Rank.THREE, 3));
        Turn turn = new Turn();
        turn.setPhase(Phase.ACTION);

        unit.getPlayer().set(player);
        unit.getDealer().set(dealer);
        unit.getTurn().set(turn);

        unit.getPlayerActions().add(new PlayerAction(Action.HIT, "Player"));

        instance.fire(new RuleNameEqualsAgendaFilter("Il giocatore sceglie HIT"));

        assertThat(player.getHand()).hasSize(3);
    }

    @Test
    public void testPlayerBust() {
        Player player = new Player("Player", 100);
        Player dealer = new Player("Dealer");
        player.addCard(new BlackjackCard(Suit.HEART, Rank.KING, 10));
        player.addCard(new BlackjackCard(Suit.SPADE, Rank.QUEEN, 10));
        player.addCard(new BlackjackCard(Suit.CLUB, Rank.FIVE, 5));
        Turn turn = new Turn();
        turn.setPhase(Phase.ACTION);

        unit.getPlayer().set(player);
        unit.getDealer().set(dealer);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("Se il giocatore supera 21 dopo aver scelto HIT"));

        assertThat(turn.getResult()).isEqualTo(Result.LOSE);
        assertThat(turn.getPhase()).isEqualTo(Phase.PAYOUT);
    }

    @Test
    public void testPlayerStand() {
        Player player = new Player("Player", 100);
        Player dealer = new Player("Dealer");
        Turn turn = new Turn();
        turn.setPhase(Phase.ACTION);

        unit.getPlayer().set(player);
        unit.getDealer().set(dealer);
        unit.getTurn().set(turn);

        unit.getPlayerActions().add(new PlayerAction(Action.STAND, "Player"));

        instance.fire(new RuleNameEqualsAgendaFilter("il giocatore sceglie STAND"));

        assertThat(turn.getPhase()).isEqualTo(Phase.DEALER);
    }

    @Test
    public void testDealerDrawsBelow17() {
        Player player = new Player("Player", 100);
        player.addCard(new BlackjackCard(Suit.HEART, Rank.KING, 10));
        player.addCard(new BlackjackCard(Suit.SPADE, Rank.EIGHT, 8));
        Player dealer = new Player("Dealer");
        dealer.addCard(new BlackjackCard(Suit.HEART, Rank.SIX, 6));
        dealer.addCard(new BlackjackCard(Suit.SPADE, Rank.FIVE, 5));
        Turn turn = new Turn();
        turn.setPhase(Phase.DEALER);

        unit.getPlayer().set(player);
        unit.getDealer().set(dealer);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("il dealer pesca se ha meno di 17"));

        assertThat(dealer.getHand().size()).isGreaterThan(2);
    }

    @Test
    public void testPlayerWinsComparison() {
        Player player = new Player("Player", 100);
        Player dealer = new Player("Dealer");
        player.addCard(new BlackjackCard(Suit.HEART, Rank.KING, 10));
        player.addCard(new BlackjackCard(Suit.SPADE, Rank.NINE, 9));
        dealer.addCard(new BlackjackCard(Suit.CLUB, Rank.KING, 10));
        dealer.addCard(new BlackjackCard(Suit.DIAMOND, Rank.SEVEN, 7));
        Turn turn = new Turn();
        turn.setPhase(Phase.COMPARISON);

        unit.getPlayer().set(player);
        unit.getDealer().set(dealer);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("Il giocatore vince"));

        assertThat(turn.getResult()).isEqualTo(Result.WIN);
        assertThat(turn.getPhase()).isEqualTo(Phase.PAYOUT);
    }

    @Test
    public void testPlayerLosesComparison() {
        Player player = new Player("Player", 100);
        Player dealer = new Player("Dealer");
        player.addCard(new BlackjackCard(Suit.HEART, Rank.KING, 10));
        player.addCard(new BlackjackCard(Suit.SPADE, Rank.SEVEN, 7));
        dealer.addCard(new BlackjackCard(Suit.CLUB, Rank.KING, 10));
        dealer.addCard(new BlackjackCard(Suit.DIAMOND, Rank.NINE, 9));
        Turn turn = new Turn();
        turn.setPhase(Phase.COMPARISON);

        unit.getPlayer().set(player);
        unit.getDealer().set(dealer);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("Il giocatore perde"));

        assertThat(turn.getResult()).isEqualTo(Result.LOSE);
        assertThat(turn.getPhase()).isEqualTo(Phase.PAYOUT);
    }

    @Test
    public void testTieComparison() {
        Player player = new Player("Player", 100);
        Player dealer = new Player("Dealer");
        player.addCard(new BlackjackCard(Suit.HEART, Rank.KING, 10));
        player.addCard(new BlackjackCard(Suit.SPADE, Rank.EIGHT, 8));
        dealer.addCard(new BlackjackCard(Suit.CLUB, Rank.QUEEN, 10));
        dealer.addCard(new BlackjackCard(Suit.DIAMOND, Rank.EIGHT, 8));
        Turn turn = new Turn();
        turn.setPhase(Phase.COMPARISON);

        unit.getPlayer().set(player);
        unit.getDealer().set(dealer);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("pareggio"));

        assertThat(turn.getResult()).isEqualTo(Result.TIE);
        assertThat(turn.getPhase()).isEqualTo(Phase.PAYOUT);
    }

    // --- 02-setup.drl: regole aggiuntive ---

    @Test
    public void testPrintInitialHands() {
        Player player = new Player("Player", 100);
        Player dealer = new Player("Dealer");
        player.addCard(new BlackjackCard(Suit.HEART, Rank.FIVE, 5));
        player.addCard(new BlackjackCard(Suit.SPADE, Rank.THREE, 3));
        dealer.addCard(new BlackjackCard(Suit.CLUB, Rank.KING, 10));
        dealer.addCard(new BlackjackCard(Suit.DIAMOND, Rank.SEVEN, 7));
        Turn turn = new Turn();
        turn.setPhase(Phase.SETUP);

        unit.getPlayer().set(player);
        unit.getDealer().set(dealer);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("stampa la mano iniziale del giocatore e la prima carta del dealer"));

        assertThat(unit.getEvents()).anyMatch(e -> e.contains("Player's initial hand"));
        assertThat(unit.getEvents()).anyMatch(e -> e.contains("Dealer's first card"));
    }

    @Test
    public void testPlayerBlackjack() {
        Player player = new Player("Player", 100);
        Player dealer = new Player("Dealer");
        player.addCard(new BlackjackCard(Suit.HEART, Rank.ACE, 11));
        player.addCard(new BlackjackCard(Suit.SPADE, Rank.KING, 10));
        dealer.addCard(new BlackjackCard(Suit.CLUB, Rank.EIGHT, 8));
        dealer.addCard(new BlackjackCard(Suit.DIAMOND, Rank.SEVEN, 7));
        Turn turn = new Turn();
        turn.setPhase(Phase.SETUP);

        unit.getPlayer().set(player);
        unit.getDealer().set(dealer);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("Se il giocatore fa blackjack e il dealer no, vince automaticamente"));

        assertThat(turn.getResult()).isEqualTo(Result.WIN);
        assertThat(turn.getPhase()).isEqualTo(Phase.PAYOUT);
    }

    @Test
    public void testBothBlackjack() {
        Player player = new Player("Player", 100);
        Player dealer = new Player("Dealer");
        player.addCard(new BlackjackCard(Suit.HEART, Rank.ACE, 11));
        player.addCard(new BlackjackCard(Suit.SPADE, Rank.KING, 10));
        dealer.addCard(new BlackjackCard(Suit.CLUB, Rank.ACE, 11));
        dealer.addCard(new BlackjackCard(Suit.DIAMOND, Rank.QUEEN, 10));
        Turn turn = new Turn();
        turn.setPhase(Phase.SETUP);

        unit.getPlayer().set(player);
        unit.getDealer().set(dealer);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("se il giocatore fa blackjack e il dealer anche, \u00e8 un pareggio"));

        assertThat(turn.getResult()).isEqualTo(Result.TIE);
        assertThat(turn.getPhase()).isEqualTo(Phase.PAYOUT);
    }

    @Test
    public void testSetupToAction() {
        Player player = new Player("Player", 100);
        Player dealer = new Player("Dealer");
        player.addCard(new BlackjackCard(Suit.HEART, Rank.FIVE, 5));
        player.addCard(new BlackjackCard(Suit.SPADE, Rank.THREE, 3));
        dealer.addCard(new BlackjackCard(Suit.CLUB, Rank.KING, 10));
        dealer.addCard(new BlackjackCard(Suit.DIAMOND, Rank.SEVEN, 7));
        Turn turn = new Turn();
        turn.setPhase(Phase.SETUP);

        unit.getPlayer().set(player);
        unit.getDealer().set(dealer);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("SETUP termina e si passa a ACTION"));

        assertThat(turn.getPhase()).isEqualTo(Phase.ACTION);
    }

    // --- 03-action.drl: regole aggiuntive ---

    @Test
    public void testPrintHandAfterHit() {
        Player player = new Player("Player", 100);
        Player dealer = new Player("Dealer");
        player.addCard(new BlackjackCard(Suit.HEART, Rank.FIVE, 5));
        player.addCard(new BlackjackCard(Suit.SPADE, Rank.THREE, 3));
        player.addCard(new BlackjackCard(Suit.CLUB, Rank.TWO, 2));
        Turn turn = new Turn();
        turn.setPhase(Phase.ACTION);

        unit.getPlayer().set(player);
        unit.getDealer().set(dealer);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("Si stampa la nuova mano del giocatore dopo aver scelto HIT"));

        assertThat(unit.getEvents()).anyMatch(e -> e.contains("Player has a new card"));
    }

    @Test
    public void testPlayerHas21AfterHit() {
        Player player = new Player("Player", 100);
        Player dealer = new Player("Dealer");
        player.addCard(new BlackjackCard(Suit.HEART, Rank.KING, 10));
        player.addCard(new BlackjackCard(Suit.SPADE, Rank.FIVE, 5));
        player.addCard(new BlackjackCard(Suit.CLUB, Rank.SIX, 6));
        Turn turn = new Turn();
        turn.setPhase(Phase.ACTION);

        unit.getPlayer().set(player);
        unit.getDealer().set(dealer);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("Se il giocatore ha 21 dopo aver scelto HIT"));

        assertThat(turn.getPhase()).isEqualTo(Phase.DEALER);
    }

    // --- 04-dealer.drl: regole aggiuntive ---

    @Test
    public void testDealerRevealsSecondCard() {
        Player player = new Player("Player", 100);
        Player dealer = new Player("Dealer");
        dealer.addCard(new BlackjackCard(Suit.HEART, Rank.KING, 10));
        dealer.addCard(new BlackjackCard(Suit.SPADE, Rank.SEVEN, 7));
        Turn turn = new Turn();
        turn.setPhase(Phase.DEALER);

        unit.getPlayer().set(player);
        unit.getDealer().set(dealer);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("il dealer rivela la seconda carta"));

        assertThat(unit.getEvents()).anyMatch(e -> e.contains("Dealer's second card revealed"));
    }

    @Test
    public void testDealerBusts() {
        Player player = new Player("Player", 100);
        Player dealer = new Player("Dealer");
        dealer.addCard(new BlackjackCard(Suit.HEART, Rank.KING, 10));
        dealer.addCard(new BlackjackCard(Suit.SPADE, Rank.QUEEN, 10));
        dealer.addCard(new BlackjackCard(Suit.CLUB, Rank.FIVE, 5));
        Turn turn = new Turn();
        turn.setPhase(Phase.DEALER);

        unit.getPlayer().set(player);
        unit.getDealer().set(dealer);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("se il dealer supera 21, il giocatore vince automaticamente"));

        assertThat(turn.getResult()).isEqualTo(Result.WIN);
        assertThat(turn.getPhase()).isEqualTo(Phase.PAYOUT);
    }

    @Test
    public void testDealerStands17OrMore() {
        Player player = new Player("Player", 100);
        Player dealer = new Player("Dealer");
        dealer.addCard(new BlackjackCard(Suit.HEART, Rank.KING, 10));
        dealer.addCard(new BlackjackCard(Suit.SPADE, Rank.EIGHT, 8));
        Turn turn = new Turn();
        turn.setPhase(Phase.DEALER);

        unit.getPlayer().set(player);
        unit.getDealer().set(dealer);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("se il dealer ha 17 o pi\u00f9 e meno di 21, si passa alla fase di confronto"));

        assertThat(turn.getPhase()).isEqualTo(Phase.COMPARISON);
    }

    @Test
    public void testShowDealerHandAfterDraw() {
        Player player = new Player("Player", 100);
        Player dealer = new Player("Dealer");
        dealer.addCard(new BlackjackCard(Suit.HEART, Rank.SIX, 6));
        dealer.addCard(new BlackjackCard(Suit.SPADE, Rank.FIVE, 5));
        dealer.addCard(new BlackjackCard(Suit.CLUB, Rank.SEVEN, 7));
        Turn turn = new Turn();
        turn.setPhase(Phase.DEALER);

        unit.getPlayer().set(player);
        unit.getDealer().set(dealer);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("mostra carte dealer dopo aver pescato"));

        assertThat(unit.getEvents()).anyMatch(e -> e.contains("Dealer's new hand"));
    }

    // --- 05-comparison.drl: regola aggiuntiva ---

    @Test
    public void testDealerBlackjackBeatsPlayer21() {
        Player player = new Player("Player", 100);
        Player dealer = new Player("Dealer");
        // Player ha 21 con 3 carte (non blackjack)
        player.addCard(new BlackjackCard(Suit.HEART, Rank.KING, 10));
        player.addCard(new BlackjackCard(Suit.SPADE, Rank.FIVE, 5));
        player.addCard(new BlackjackCard(Suit.CLUB, Rank.SIX, 6));
        // Dealer ha 21 con 2 carte (blackjack)
        dealer.addCard(new BlackjackCard(Suit.DIAMOND, Rank.ACE, 11));
        dealer.addCard(new BlackjackCard(Suit.HEART, Rank.QUEEN, 10));
        Turn turn = new Turn();
        turn.setPhase(Phase.COMPARISON);

        unit.getPlayer().set(player);
        unit.getDealer().set(dealer);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("pareggio a 21 ma il banco ha blackjack"));

        assertThat(turn.getResult()).isEqualTo(Result.LOSE);
        assertThat(turn.getPhase()).isEqualTo(Phase.PAYOUT);
    }

    // --- 06-payout.drl ---

    @Test
    public void testPayoutBlackjackWin() {
        Player player = new Player("Player", 75); // balance dopo bet di 25
        player.setBet(25);
        player.addCard(new BlackjackCard(Suit.HEART, Rank.ACE, 11));
        player.addCard(new BlackjackCard(Suit.SPADE, Rank.KING, 10));
        Player dealer = new Player("Dealer");
        Turn turn = new Turn();
        turn.setPhase(Phase.PAYOUT);
        turn.setResult(Result.WIN);

        unit.getPlayer().set(player);
        unit.getDealer().set(dealer);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("Il giocatore vince con Blackjack"));

        // 75 + (25 * 2.5) = 75 + 62.5 = 137.5
        assertThat(player.getBalance()).isEqualTo(137.5);
        assertThat(turn.getPhase()).isEqualTo(Phase.RESET);
    }

    @Test
    public void testPayoutNormalWin() {
        Player player = new Player("Player", 75);
        player.setBet(25);
        player.addCard(new BlackjackCard(Suit.HEART, Rank.KING, 10));
        player.addCard(new BlackjackCard(Suit.SPADE, Rank.NINE, 9));
        Player dealer = new Player("Dealer");
        Turn turn = new Turn();
        turn.setPhase(Phase.PAYOUT);
        turn.setResult(Result.WIN);

        unit.getPlayer().set(player);
        unit.getDealer().set(dealer);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("Il giocatore vince "));

        // 75 + (25 * 2) = 75 + 50 = 125
        assertThat(player.getBalance()).isEqualTo(125.0);
        assertThat(turn.getPhase()).isEqualTo(Phase.RESET);
    }

    @Test
    public void testPayoutLose() {
        Player player = new Player("Player", 75);
        player.setBet(25);
        Player dealer = new Player("Dealer");
        Turn turn = new Turn();
        turn.setPhase(Phase.PAYOUT);
        turn.setResult(Result.LOSE);

        unit.getPlayer().set(player);
        unit.getDealer().set(dealer);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("Il giocatore ha perso"));

        assertThat(player.getBalance()).isEqualTo(75.0);
        assertThat(turn.getPhase()).isEqualTo(Phase.RESET);
    }

    @Test
    public void testPayoutTie() {
        Player player = new Player("Player", 75);
        player.setBet(25);
        Player dealer = new Player("Dealer");
        Turn turn = new Turn();
        turn.setPhase(Phase.PAYOUT);
        turn.setResult(Result.TIE);

        unit.getPlayer().set(player);
        unit.getDealer().set(dealer);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("Pareggio"));

        // 75 + 25 = 100 (bet returned)
        assertThat(player.getBalance()).isEqualTo(100.0);
        assertThat(turn.getPhase()).isEqualTo(Phase.RESET);
    }

    // --- 07-reset.drl ---

    @Test
    public void testResetTurn() {
        Player player = new Player("Player", 100);
        player.setBet(25);
        player.addCard(new BlackjackCard(Suit.HEART, Rank.KING, 10));
        Player dealer = new Player("Dealer");
        dealer.addCard(new BlackjackCard(Suit.CLUB, Rank.SEVEN, 7));
        Turn turn = new Turn();
        turn.setPhase(Phase.RESET);
        turn.setResult(Result.WIN);

        unit.getPlayer().set(player);
        unit.getDealer().set(dealer);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("Reset del turno"));

        assertThat(player.getHand()).isEmpty();
        assertThat(player.getBet()).isEqualTo(0);
        assertThat(dealer.getHand()).isEmpty();
        assertThat(turn.getResult()).isEqualTo(Result.NONE);
        assertThat(turn.getPhase()).isEqualTo(Phase.BET);
    }
}
