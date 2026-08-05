package com.sam.game;

import com.badlogic.gdx.math.Vector2;

public class EconomyBuilding extends Entity { 

    private IntervalTimer goldTimer;
    private float goldAmount;

    protected EconomyBuilding(Vector2 position,Team team, EconomyBuildingType buildingType) {
        super(position, buildingType.getWidth(), buildingType.getHeight(), team, buildingType.getMaxHealth(), buildingType.getTexture());
        this.goldAmount = buildingType.getGoldAmount();
        goldTimer = new IntervalTimer(buildingType.getGoldInterval());
    }

    @Override
    protected void update(float delta, GameContext gameContext) {
        if (goldTimer.advance(delta)) {
            this.team.addGold(goldAmount);
        }
    }
}

