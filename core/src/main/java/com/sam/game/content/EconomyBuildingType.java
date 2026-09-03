package com.sam.game.content;

import com.badlogic.gdx.graphics.Texture;

public class EconomyBuildingType extends BuildingType {
    
    private final float goldInterval;
    private final float goldAmount; 

    public EconomyBuildingType(int widthInCells, int heightInCells, float maxHealth, float cost, Texture texture, float goldInterval, float goldAmount) {
        super(widthInCells, heightInCells, maxHealth, cost, texture);
        this.goldAmount = goldAmount;
        this.goldInterval = goldInterval;
    }

    public float getGoldAmount() {
        return this.goldAmount;
    }

    public float getGoldInterval() {
        return this.goldInterval;
    }
}
