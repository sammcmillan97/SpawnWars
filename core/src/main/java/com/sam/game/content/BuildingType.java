package com.sam.game.content;

import com.badlogic.gdx.graphics.Texture;

public class BuildingType {

    private final int widthInCells;
    private final int heightInCells;
    private final float maxHealth;
    private final float cost;
    private final Texture texture;

    public BuildingType(int widthInCells, int heightInCells, float maxHealth, float cost, Texture texture) {
        this.widthInCells = widthInCells;
        this.heightInCells = heightInCells;
        this.maxHealth = maxHealth;
        this.cost = cost;
        this.texture = texture;
    }

    public int getWidthInCells() {
        return this.widthInCells;
    }

    public int getHeightInCells() {
        return this.heightInCells;
    }

    public float getMaxHealth() {
        return this.maxHealth;
    }

    public float getCost() {
        return this.cost;
    }

   public Texture getTexture() {
        return this.texture;
   } 
}
