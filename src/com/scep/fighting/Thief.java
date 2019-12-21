package com.scep.fighting;

import java.util.List;

public class Thief extends Fighter {

    final static float DAMAGE_RATE = 0.4f;
    final static float HEALTH_RATE = 1.5f;
    final static float ATK_SPEED_RATE = 0.01f;
    final static float MOV_SPEED_RATE = 0.04f;
    final static float CRIT_CHANC_RATE = 1;
    final static float CRIT_DMG_RATE = 10;
    final static float DODGE_RATE = 0.9f;
    final static float ARMOR_RATE = 0.05f;

    public Thief(int id, String spritePath) {
        super(id, spritePath);
        this.damage = 8;
        this.initial_health = 70;
        this.roundsPerAttack = 1;
        this.mov_speed = 1.5;
        // Percentages
        this.crit_chance = 30;
        this.crit_damage = 200;
        this.dodge = 20;
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
