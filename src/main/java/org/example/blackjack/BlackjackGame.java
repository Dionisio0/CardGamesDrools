package org.example.blackjack;

import org.drools.ruleunits.api.RuleUnitInstance;
import org.drools.ruleunits.api.RuleUnitProvider;
import org.example.blackjack.unit.BlackjackUnit;
import org.example.blackjack.model.*;

import java.util.Scanner;

public class BlackjackGame {
    private final Player player;
    private final Player dealer;
    private final Turn turn;
    private final BlackjackUnit unit;
    private final Scanner scanner;

    public BlackjackGame(Player player, Player dealer) {
        this(player, dealer, new Turn());
    }

    public BlackjackGame(Player player, Player dealer, Turn turn) {
        this.player = player;
        this.dealer = dealer;
        this.turn = turn;
        this.unit = new BlackjackUnit();
        this.scanner = new Scanner(System.in);
        setupUnit();
    }

    private void setupUnit() {
        unit.getPlayer().set(player);
        unit.getDealer().set(dealer);
        unit.getTurn().set(turn);
    }

    public void start(){
        RuleUnitInstance<BlackjackUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(unit);

        while(player.getBalance() + player.getBet() > 0) {

            executePhase(instance);
            unit.getEvents().forEach(System.out::println);
            unit.getEvents().clear();
        }

        instance.close();
    }

    private void executePhase(RuleUnitInstance<BlackjackUnit> instance) {
        PlayerAction action = switch (turn.getPhase()) {
            case BET -> getBetAction();
            case ACTION ->  getPlayerAction();
            default -> throw new IllegalStateException("Unexpected value: " + turn.getPhase());
        };

        instance.ruleUnitData().getPlayerActions().add(action);
        instance.fire();
    }

    private PlayerAction getBetAction() {
        System.out.println("[CONSOLE]: Player balance: " + player.getBalance());
        System.out.print("[CONSOLE]: Enter your bet amount: ");

        double betAmount = scanner.nextDouble();
        return new PlayerActionBet(player.getName(), betAmount);
    }

    private PlayerAction getPlayerAction() {
        System.out.print("[CONSOLE]: " + player.getName() + " - Enter 1 for Hit or 2 for Stand: ");

        int choice = scanner.nextInt();
        Action action = switch (choice) {
            case 1 -> Action.HIT;
            case 2 -> Action.STAND;
            default -> throw new IllegalArgumentException("Invalid choice");
        };

        return new PlayerAction(action, player.getName());
    }
}
