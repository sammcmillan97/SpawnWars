package com.sam.game.util;

public class IntervalTimer {

    private float interval;
    private float elapsed;

    public IntervalTimer(float interval) {
        this.elapsed = 0;
        this.interval = interval;
    }

    protected float getElapsed() {
        return elapsed;
    }

    protected float getProgress() {
        return elapsed / interval;
    }

    public boolean advance(float delta) {
        elapsed += delta;              
        if (elapsed >= interval) {
            elapsed-= interval;
            return true;
        } else {
            return false; 
        }
    }
}
