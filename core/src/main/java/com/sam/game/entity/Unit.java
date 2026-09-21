package com.sam.game.entity;

import com.badlogic.gdx.math.Vector2;
import com.sam.game.GameContext;
import com.sam.game.Team;
import com.sam.game.content.UnitType;
import com.sam.game.util.IntervalTimer;

public class Unit extends Entity {

    private Entity target;

    private float movementSpeed;
    private float attackRange;
    private float damage;
    private IntervalTimer attackTimer; 

    protected Unit(Vector2 position, Team team, UnitType unitType) {
        super(position, unitType.getWidth(), unitType.getHeight(), team, unitType.getMaxHealth(), unitType.getTexture());
        this.target = null;
        
        this.movementSpeed = unitType.getMovementSpeed();
        this.damage = unitType.getDamage();
        this.attackRange = unitType.getAttackRange();

        //Attacks per second
        attackTimer = new IntervalTimer(1 / unitType.getAttackSpeed());
    }


    @Override
    public void update(float delta, GameContext gameContext) {
        if (this.health <= 0) {
            return; 
        }

        move(delta, gameContext);
    }   


    private void move(float delta, GameContext gameContext) {
        Vector2 moveTo = new Vector2();
        gameContext.getFlowDirection(this.team, this.position, moveTo);
        Vector2 movement = new Vector2(moveTo).scl(delta * movementSpeed);
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
