package com.sam.game.content;

import com.badlogic.gdx.graphics.Texture;

public class SpawnBuildingType extends BuildingType {

    private final float spawnInterval;

    private final UnitType unitType;  

    public SpawnBuildingType(int widthInCells, int heightInCells, float maxHealth, float cost, Texture texture, float spwanInterval, UnitType unitType) {
        super(widthInCells, heightInCells, maxHealth, cost, texture);
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
