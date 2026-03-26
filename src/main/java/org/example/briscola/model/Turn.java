package org.example.briscola.model;


public class Turn {
    private Phase phase;
    private String turnPlayer;
    private BriscolaCard briscola;
    private String activePlayer;
    private boolean emptyDeck;

    public Turn(Phase phase, String turnPlayer, String activePlayer) {
        this.phase = phase;
        this.turnPlayer = turnPlayer;
        this.activePlayer = activePlayer;
        this.emptyDeck = false;
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

    public boolean isEmptyDeck() {
        return emptyDeck;
    }

    public void setEmptyDeck(boolean emptyDeck) {
        this.emptyDeck = emptyDeck;
    }
}

