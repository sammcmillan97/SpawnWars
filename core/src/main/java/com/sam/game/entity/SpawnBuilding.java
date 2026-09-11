package com.sam.game.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sam.game.GameContext;
import com.sam.game.Team;
import com.sam.game.content.SpawnBuildingType;
import com.sam.game.content.UnitType;
import com.sam.game.util.IntervalTimer;
import com.sam.game.world.WorldMap;

public class SpawnBuilding extends Building {

    private IntervalTimer spawnTimer;
    private UnitType unitType;

    public SpawnBuilding(int originColumn, int originRow, Team team, SpawnBuildingType type, int cellSize) {
        super(originColumn, originRow, team, type, cellSize);
        this.spawnTimer = new IntervalTimer(type.getSpawnInterval());
        this.unitType = type.getUnitType();
    }

    @Override
    public void update(float delta, GameContext gameContext) {
        if (spawnTimer.advance(delta)) {
            spawnUnit(gameContext.spawnBuffer, gameContext.map);
        }
    }

    private void spawnUnit(Array<Entity> spawnBuffer, WorldMap map) {
        //find nearest spawn point 
        int startingColumn = this.originColumn - 1;
        int startingRow = this.originRow - 1;
        

        spawnBuffer.add(new Unit(new Vector2(this.position.x, this.position.y), this.team, this.unitType));
    }

}
