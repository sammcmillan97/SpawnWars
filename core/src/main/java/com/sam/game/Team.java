package com.sam.game;

import com.badlogic.gdx.graphics.Color;
import com.sam.game.util.IntervalTimer;

public class Team {

    private Color teamColor;
    private int teamNumber;
    
    private float gold;
    private float passiveGold;
    private IntervalTimer goldTimer;

    public Team(Color teamColor, int teamNumber, float startingGold, float passiveGold, float goldInterval) {
        this.teamColor = teamColor;
        this.teamNumber = teamNumber;
        this.gold = startingGold;
        this.passiveGold = passiveGold;

        goldTimer = new IntervalTimer(goldInterval);
    }

    public float getGold() {
        return this.gold;
    }

    protected float getPassiveGold() {
        return this.passiveGold;
    }

    public void addGold(float goldAdded) {
        this.gold+= goldAdded; 
    }

    protected boolean spendGold(float goldRemoved) {
        if (canAfford(goldRemoved)) {
            this.gold-= goldRemoved;
            return true;
        } else {
            return false;
        }
    }

    protected boolean canAfford(float cost) {
        return this.gold >= cost;
    }

    public Color getTeamColor() {
        return this.teamColor;
    }

    protected int getTeamNumber() {
        return teamNumber;
    }

    protected void earnPassiveGold(float delta) {
        if (goldTimer.advance(delta)) {
            gold+= passiveGold;    
        }
    }

}
