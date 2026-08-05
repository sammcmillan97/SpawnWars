package com.sam.game.content;

import com.badlogic.gdx.graphics.Texture;

public class SpawnBuildingType extends BuildingType {

    private final float spawnInterval;

    private final UnitType unitType;  

    public SpawnBuildingType(float width, float height, float maxHealth, float cost, Texture texture, float spwanInterval, UnitType unitType) {
        super(width, height, maxHealth, cost, texture);
        this.spawnInterval = spwanInterval;
        this.unitType = unitType;
    }

    public float getSpawnInterval() {
        return this.spawnInterval;
    }
    
    public UnitType getUnitType() {
        return this.unitType;
    }
}
