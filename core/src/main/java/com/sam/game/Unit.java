package com.sam.game;

import com.badlogic.gdx.math.Vector2;

public class Unit extends Entity {

    private Entity target;

    private float movementSpeed;
    private float range;
    private float damage;
    private IntervalTimer attackTimer; 

    protected Unit(Vector2 position, Team team, UnitType unitType) {
        super(position, unitType.getWidth(), unitType.getHeight(), team, unitType.getMaxHealth(), unitType.getTexture());
        this.target = null;
        
        this.movementSpeed = unitType.getMovementSpeed();
        this.damage = unitType.getDamage();
        this.range = unitType.getRange();

        //Attacks per second
        attackTimer = new IntervalTimer(1 / unitType.getAttackSpeed());
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
        if (attackTimer.advance(delta)) {
            target.takeDamage(this.damage);
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
