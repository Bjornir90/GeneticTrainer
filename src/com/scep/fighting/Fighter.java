package com.scep.fighting;

import com.scep.Position;

import java.util.List;

public abstract class Fighter {
    protected double mov_speed;
    protected int roundsPerAttack;
    protected Position position;
    protected int damage;
    protected int health, initial_health;
    protected int armor;
    protected int crit_chance;
    protected int crit_damage;
    protected int dodge;
    protected Fighter opponent;
    protected long timeSinceLastAttack;
    protected float secondSinceLastAttack;
    protected int id;
    protected float range;
    private boolean initialised = false;

    private Fighter(int id){
        timeSinceLastAttack = 10000000;
        secondSinceLastAttack = 0.0f;
        mov_speed = 10.0;
        range = 50f;
        position = new Position(0,0);
        this.id = id;
    }

    public Fighter(int id, String spritePath) {
        this(id);

    }

    public abstract void setCarac(List<Integer> pts);


    public void update(){
        if(!initialised){
            initialised = true;
            reset();
        }
        timeSinceLastAttack++;

        float distanceToOpponent = position.getDistanceTo(opponent.getPosition());

        if(distanceToOpponent > 50f) {
            float movementX = opponent.getPosition().x - position.x, movementY = opponent.getPosition().y - position.y;
            float movementNorm = (float) Math.sqrt(movementX * movementX + movementY * movementY);
            float actualMovementX = movementX * (float) mov_speed / movementNorm, actualMovementY = movementY * (float) mov_speed / movementNorm;

            moveBy(actualMovementX, actualMovementY);
        }

        if(timeSinceLastAttack >= roundsPerAttack) {
            attack(distanceToOpponent);
        }

    }

    public void reset(){
        health = initial_health;
        timeSinceLastAttack = roundsPerAttack;
    }

    protected Position getPosition(){
        return position;
    }

    public void moveBy(float x, float y){
        position.add(x, y);
    }

    public void moveTo(float x, float y){
        position.x = x;
        position.y = y;
    }

    protected int attack(float distanceToOpponent){
        if(distanceToOpponent <= this.range){
            timeSinceLastAttack = 0;
            boolean crit = Math.random()*100 <= this.crit_chance;
            return opponent.takeDamage(crit?(int)(this.damage*0.001*crit_damage):this.damage);
        }
        return 0;
    }

    protected int takeDamage(int damage){
        if(Math.random()*100 <= this.dodge) return -1;
        int eff_dmg = damage - this.armor;
        this.health -= eff_dmg;
        return eff_dmg;
    }

    public int getId() {
        return id;
    }

    public void setOpponent(Fighter opponent) {
        this.opponent = opponent;
    }

    public void setMov_speed(double mov_speed) {
        this.mov_speed = mov_speed;
    }

    public void setRoundsPerAttack(int roundsPerAttack) {
        this.roundsPerAttack = roundsPerAttack;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public void setCrit_chance(int crit_chance) {
        this.crit_chance = crit_chance;
    }

    public void setCrit_damage(int crit_damage) {
        this.crit_damage = crit_damage;
    }

    public void setDodge(int dodge) {
        this.dodge = dodge;
    }
}
