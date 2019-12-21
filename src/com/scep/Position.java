package com.scep;

public class Position {
    public float x, y;

    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Position add(float x, float y){
        this.x += x;
        this.y += y;
        return this;
    }

    public float getDistanceTo(Position other){
        float xresult = other.x-x, yresult = other.y-y;
        return (float) Math.sqrt(xresult*xresult+yresult*yresult);
    }
}
