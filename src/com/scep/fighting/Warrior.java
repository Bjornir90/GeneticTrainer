package com.scep.fighting;

import java.util.List;

public class Warrior extends Fighter {

    final static float DAMAGE_RATE = 0.3f;
    final static float HEALTH_RATE = 5;
    final static float ATK_SPEED_RATE = 0.008f;
    final static float MOV_SPEED_RATE = 0.02f;
    final static float CRIT_CHANC_RATE = 0.55f;
    final static float CRIT_DMG_RATE = 3;
    final static float DODGE_RATE = 0.37f;
    final static float ARMOR_RATE = 0.47f;

    public Warrior(int id, String spritePath) {
        super(id, spritePath);
        this.damage = 8;
        this.initial_health = 150;
        this.roundsPerAttack = 2;
        this.mov_speed = 1;
        // Percentages
        this.crit_chance = 5;
        this.crit_damage = 150;
        this.dodge = 3;
        this.armor = 3;
    }

    @Override
    public void setCarac(List<Integer> pts) {
        this.damage += (int) (DAMAGE_RATE * pts.get(0));
        this.initial_health += (int) (HEALTH_RATE * pts.get(1));
        this.roundsPerAttack -= ATK_SPEED_RATE * pts.get(2);
        this.mov_speed += this.mov_speed * MOV_SPEED_RATE * pts.get(3);
        this.crit_chance += CRIT_CHANC_RATE * pts.get(4);
        this.crit_damage += CRIT_DMG_RATE * pts.get(5);
        this.dodge += DODGE_RATE * pts.get(6);
        this.armor += ARMOR_RATE * pts.get(7);
    }


}
