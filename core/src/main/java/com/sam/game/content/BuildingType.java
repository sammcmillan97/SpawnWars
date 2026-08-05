package com.sam.game.content;

import com.badlogic.gdx.graphics.Texture;

public class BuildingType {

    private final float width;
    private final float height;
    private final float maxHealth;
    private final float cost;
    private final Texture texture;

    public BuildingType(float width, float height, float maxHealth, float cost, Texture texture) {
        this.width = width;
        this.height = height;
        this.maxHealth = maxHealth;
        this.cost = cost;
        this.texture = texture;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
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
