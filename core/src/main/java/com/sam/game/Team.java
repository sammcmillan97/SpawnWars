package com.sam.game;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.sam.game.entity.Building;
import com.sam.game.util.IntervalTimer;

public class Team {

    private Color teamColor;
    private int teamNumber;
    private Building teamCastle;
    private Set<Integer> enemies;
    
    private float gold;
    private float passiveGold;
    private IntervalTimer goldTimer;

    public Team(Color teamColor, int teamNumber, float startingGold, float passiveGold, float goldInterval) {
        this.teamColor = teamColor;
        this.teamNumber = teamNumber;
        this.gold = startingGold;
        this.passiveGold = passiveGold;

        enemies = new HashSet<>();

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

    public void addEnemy(int teamNumber) {
        enemies.add(teamNumber);
    }

    public Set<Integer> getEnemies() {
        return this.enemies;
    }

    public void setCastle(Building building) {
        teamCastle = building;
    }

    public Building getCastle() {
        return teamCastle;
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
