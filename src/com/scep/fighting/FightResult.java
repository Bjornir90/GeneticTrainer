package com.scep.fighting;

public class FightResult {

    private Fighter winner, looser;

    public FightResult(Fighter winner, Fighter looser) {
        this.winner = winner;
        this.looser = looser;
    }

    public Fighter getWinner() {
        return winner;
    }

    public Fighter getLooser() {
        return looser;
    }
}
