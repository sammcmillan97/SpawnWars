package com.sam.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class SpawnBuilding extends Entity {

    private IntervalTimer spawnTimer;
    private UnitType unitType;

    protected SpawnBuilding(Vector2 position, Team team, SpawnBuildingType buildingType) {
        super(position, buildingType.getWidth(), buildingType.getHeight(), team, buildingType.getMaxHealth(), buildingType.getTexture());
        this.spawnTimer = new IntervalTimer(buildingType.getSpawnInterval());
        this.unitType = buildingType.getUnitType();
    }

    @Override
    protected void update(float delta, GameContext gameContext) {
        if (spawnTimer.advance(delta)) {
            spawnUnit(gameContext.spawnBuffer);
        }
    }

    private void spawnUnit(Array<Entity> spawnBuffer) {
        spawnBuffer.add(new Unit(new Vector2(this.position.x, this.position.y), this.team, this.unitType));
    }

}
