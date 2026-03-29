package org.example.cartaalta.model;

public final class Turn {
    private Phase phase;
    private int highestScore;

    public Turn(Phase phase){
        this.phase = phase;
        this.highestScore = 0;
    }

    public Phase getPhase(){
        return phase;
    }

    public void setPhase(Phase phase){
        this.phase = phase;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }
}
