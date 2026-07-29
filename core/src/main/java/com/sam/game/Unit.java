package com.sam.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Unit extends Entity {

    private Entity target;
    private float movementSpeed;
    private float attackSpeed;
    private float range;
    private float attackCooldown;
    private float damage; 

    protected Unit(Vector2 position, float width, float height, Team team, float maxHealth, Texture texture) {
        super(position, width, height, team, maxHealth, texture);
        this.target = null;
        
        this.movementSpeed = 50f;
        this.attackSpeed = 1;
        this.attackCooldown = 0;
        this.damage = 10;
        this.range = 20;
    }


    @Override
    protected void update(float delta, GameContext gameContext) {
        if (this.health <= 0) {
            return; 
        }

        if (target == null || target.health <= 0) {
            getNearestEnemy(gameContext); 
        }

        if (target != null) {
            if (this.position.dst(this.target.position) > range) {
                move(delta, gameContext);
            } else {
                attack(delta, gameContext);
            }
        }
    }   


    private void move(float delta, GameContext gameContext) {
        Vector2 movement = new Vector2(target.position).sub(position).nor().scl(delta * movementSpeed);
        position.add(movement);
    }



    private void attack(float delta, GameContext gameContext) {
        attackCooldown += delta;              
        if (attackCooldown >= attackSpeed) {
            target.takeDamage(this.damage);
            attackCooldown -= attackSpeed; 
        }
    }


    private void getNearestEnemy(GameContext gameContext) {

        Entity nearestEntity = null;

        for (Entity entity : gameContext.getEntityArray()) {

            if (nearestEntity == null && entity.team != this.team) {
                nearestEntity = entity;

            } else if (entity.team != this.team && this.position.dst(nearestEntity.position) > this.position.dst(entity.position)) {
                nearestEntity = entity;
            }
        }

        this.target = nearestEntity;
    }
    
}
