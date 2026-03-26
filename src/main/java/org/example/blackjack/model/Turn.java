package org.example.blackjack.model;

public class Turn {
    private Phase phase;
    private Result result;

    public Turn() {
        this.phase = Phase.BET;
        this.result = Result.NONE;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
