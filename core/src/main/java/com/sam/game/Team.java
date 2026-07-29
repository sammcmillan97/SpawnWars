package com.sam.game;

import com.badlogic.gdx.graphics.Color;

public class Team {

    private Color teamColor;
    private int teamNumber;
    private float gold;

    private float passiveGold;
    private float goldTimer;

    private float goldInterval;

    public Team(Color teamColor, int teamNumber, float startingGold, float passiveGold, float goldInterval) {
        this.teamColor = teamColor;
        this.teamNumber = teamNumber;
        this.goldInterval = goldInterval;
        this.gold = startingGold;

        this.passiveGold = passiveGold;
        this.goldTimer = 0;
    }

    protected float getGold() {
        return this.gold;
    }

    protected float getPassiveGold() {
        return this.passiveGold;
    }

    protected void addGold(float goldAdded) {
        this.gold+= goldAdded; 
    }

    protected void removeGold(float goldRemoved) {
        this.gold-= goldRemoved;
    }

    protected Color getTeamColor() {
        return this.teamColor;
    }

    protected int getTeamNumber() {
        return teamNumber;
    }

    protected float getGoldTimer() {
        return this.goldTimer;
    }

    protected void earnPassiveGold(float delta) {
        goldTimer+= delta;
        if (goldInterval <= goldTimer) {
            gold+= passiveGold;
            goldTimer-= goldInterval;
        }
    }

}
