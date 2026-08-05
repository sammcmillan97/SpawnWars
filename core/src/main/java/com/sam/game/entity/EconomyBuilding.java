package com.sam.game.entity;

import com.badlogic.gdx.math.Vector2;
import com.sam.game.GameContext;
import com.sam.game.Team;
import com.sam.game.content.EconomyBuildingType;
import com.sam.game.util.IntervalTimer;

public class EconomyBuilding extends Entity { 

    private IntervalTimer goldTimer;
    private float goldAmount;

    public EconomyBuilding(Vector2 position,Team team, EconomyBuildingType buildingType) {
        super(position, buildingType.getWidth(), buildingType.getHeight(), team, buildingType.getMaxHealth(), buildingType.getTexture());
        this.goldAmount = buildingType.getGoldAmount();
        goldTimer = new IntervalTimer(buildingType.getGoldInterval());
    }

    @Override
    public void update(float delta, GameContext gameContext) {
        if (goldTimer.advance(delta)) {
            this.team.addGold(goldAmount);
        }
    }
}

