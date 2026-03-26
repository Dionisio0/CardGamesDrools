package org.example.cartaalta.unit;

import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.drools.ruleunits.api.RuleUnitInstance;
import org.drools.ruleunits.api.RuleUnitProvider;
import org.example.cartaalta.model.CartaAltaCard;
import org.example.cartaalta.model.CartaAltaDeck;
import org.example.cartaalta.model.Player;
import org.example.cartaalta.model.Turn;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CartaAltaRuleTest {

    CartaAltaUnit unit;
    RuleUnitInstance<CartaAltaUnit> instance;
    CartaAltaDeck deck;

    @Before
    public void setup(){
        unit = new CartaAltaUnit();
        instance = RuleUnitProvider.get().createRuleUnitInstance(unit);
        deck = new CartaAltaDeck();
    }

    @After
    public void tearDown(){
        instance.close();
    }

    // test not enough cards in the deck

    @Test
    public void testEveryPlayerReceiveOneCard(){
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
    public void testEveryPlayerHasOneCard(){
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        CartaAltaCard c1 = deck.draw();
        CartaAltaCard c2 = deck.draw();
        p1.setCard(new CartaAltaCard(c1.getSuit(), c1.getRank()));
        p2.setCard(new CartaAltaCard(c2.getSuit(), c2.getRank()));
        Turn turn = new Turn(Phase.DRAW);

        unit.getPlayers().add(p1);
        unit.getPlayers().add(p2);
        unit.getTurn().set(turn);

        instance.fire(new RuleNameEqualsAgendaFilter("Tutti i giocatori hanno una carta"));

        assertThat(turn.getPhase()).isEqualTo(Phase.FIND_MAX_SCORE);
    }

    @Test
    public void testFindMaxScore(){
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        Turn turn = new Turn(Phase.FIND_MAX_SCORE);

        unit.getPlayers().add(p1);
        unit.getPlayers().add(p2);
        unit.getTurn().set(turn);

        CartaAltaCard c1 = deck.draw();
        CartaAltaCard c2 = deck.draw();
        p1.setCard(c1);
        p2.setCard(c2);

        instance.fire(new RuleNameEqualsAgendaFilter("Trova carta con il punteggio più alto"));

        int maxScore = Math.max(c1.getScore(), c2.getScore());
        assertThat(turn.getPhase()).isEqualTo(Phase.EVALUATE);
        assertThat(turn.getHighestScore()).isEqualTo(maxScore);
    }

}
