package com.sam.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Building extends Entity {

    private final float SPAWN_INTERVAL = 5f;

    private IntervalTimer spwanTimer;
    private Texture knightTexture; 

    private static final float unitHealth = 100;

    protected Building(Vector2 position, float width, float height, Team team, float maxHealth, Texture castleTexture, Texture knightTexture) {
        super(position, width, height, team, maxHealth, castleTexture);
        spwanTimer = new IntervalTimer(SPAWN_INTERVAL);
        this.knightTexture = knightTexture;
    }

    @Override
    protected void update(float delta, GameContext gameContext) {
        if (spwanTimer.advance(delta)) {
            spawnUnit(gameContext.spawnBuffer);
        }
    }

    private void spawnUnit(Array<Entity> spawnBuffer) {
        Unit knight = new Unit(new Vector2(position.x, position.y), 50, 50, team, unitHealth, knightTexture); 
        spawnBuffer.add(knight);
    }

}
