package com.sam.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class EconomyBuilding extends Entity { 

    private static final float GOLD_INTERVAL = 5f;

    private IntervalTimer goldTimer;
    private float goldAmount;

    protected EconomyBuilding(Vector2 position, float width, float height, Team team, float maxHealth, Texture texture, float goldAmount) {
        super(position, width, height, team, maxHealth, texture);
        this.goldAmount = goldAmount;
        goldTimer = new IntervalTimer(GOLD_INTERVAL);
    }

    @Override
    protected void update(float delta, GameContext gameContext) {
        if (goldTimer.advance(delta)) {
            this.team.addGold(goldAmount);
        }
    }
}

