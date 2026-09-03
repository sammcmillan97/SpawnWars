package com.sam.game.entity;

import com.sam.game.GameContext;
import com.sam.game.Team;
import com.sam.game.content.EconomyBuildingType;
import com.sam.game.util.IntervalTimer;

public class EconomyBuilding extends Building { 

    private IntervalTimer goldTimer;
    private float goldAmount;

    public EconomyBuilding(int originColumn, int originRow, Team team, EconomyBuildingType type, int cellSize) {
        super(originColumn, originRow, team, type, cellSize);
        this.goldAmount = type.getGoldAmount();
        goldTimer = new IntervalTimer(type.getGoldInterval());
    }

    @Override
    public void update(float delta, GameContext gameContext) {
        if (goldTimer.advance(delta)) {
            this.team.addGold(goldAmount);
        }
    }
}

