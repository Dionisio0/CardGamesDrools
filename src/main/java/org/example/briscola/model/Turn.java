package org.example.briscola.model;


public final class Turn {
    private Phase phase;
    private String turnPlayer;
    private BriscolaCard briscola;
    private String activePlayer;

    public Turn(Phase phase, String turnPlayer, String activePlayer) {
        this.phase = phase;
        this.turnPlayer = turnPlayer;
        this.activePlayer = activePlayer;
    }

    public Phase getPhase(){
        return phase;
    }

    public void setPhase(Phase phase){
        this.phase = phase;
    }

    public BriscolaCard getBriscola() {
        return briscola;
    }

    public void setBriscola(BriscolaCard briscola) {
        this.briscola = briscola;
    }

    public String getTurnPlayer() {
        return turnPlayer;
    }

    public void setTurnPlayer(String turnPlayer) {
        this.turnPlayer = turnPlayer;
    }

    public String getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(String activePlayer) {
        this.activePlayer = activePlayer;
    }
}

