package com.sam.game.content;

import com.badlogic.gdx.graphics.Texture;

public class UnitType {

    private float width;
    private float height;
    private float maxHealth;
    private Texture texture;
    private float movementSpeed;
    private float damage;
    private float range;
    private float attackSpeed;

    public UnitType(float width, float height, float maxHealth, Texture texture, float movementSpeed, float damage, float range, float attackSpeed) {
        this.width = width;
        this.height = height;
        this.maxHealth = maxHealth;
        this.texture = texture;
        this.movementSpeed = movementSpeed;
        this.damage = damage;
        this.range = range;
        this.attackSpeed = attackSpeed;
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
    
    public Texture getTexture() {
        return this.texture;
    }

    public float getMovementSpeed() {
        return this.movementSpeed;
    }

    public float getDamage() {
        return this.damage;
    }

    public float getRange() {
        return this.range;
    }

    public float getAttackSpeed() {
        return this.attackSpeed;
    }
}
