package com.sam.game;

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

    protected float getWidth() {
        return this.width;
    }
    
    protected float getHeight() {
        return this.height;
    }
    
    protected float getMaxHealth() {
        return this.maxHealth;
    }
    
    protected Texture getTexture() {
        return this.texture;
    }

    protected float getMovementSpeed() {
        return this.movementSpeed;
    }

    protected float getDamage() {
        return this.damage;
    }

    protected float getRange() {
        return this.range;
    }

    protected float getAttackSpeed() {
        return this.attackSpeed;
    }
}
