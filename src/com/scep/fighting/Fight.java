package com.scep.fighting;

import java.util.concurrent.Callable;

public class Fight implements Callable<FightResult> {
    public Fighter fighter1, fighter2;

    public Fight(Fighter fighter1, Fighter fighter2) {
        this.fighter1 = fighter1;
        this.fighter2 = fighter2;
    }

    @Override
    public FightResult call() {
        return runFight();
    }

    private FightResult runFight(){
        do {
            fighter1.update();
            fighter2.update();
        } while(fighter1.health > 0 && fighter2.health > 0);

        FightResult result;

        if(fighter1.health <= 0){
            result = new FightResult(fighter2, fighter1);
        } else {
            result = new FightResult(fighter1, fighter2);
        }
        return result;
    }
}
