package com.sam.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class SpawnBuilding extends Entity {

    private static final float SPAWN_INTERVAL = 5f;

    private IntervalTimer spwanTimer;
    private UnitType unitType;

    protected SpawnBuilding(Vector2 position, float width, float height, Team team, float maxHealth, Texture castleTexture, UnitType spawn) {
        super(position, width, height, team, maxHealth, castleTexture);
        this.spwanTimer = new IntervalTimer(SPAWN_INTERVAL);
        this.unitType = spawn;
    }

    @Override
    protected void update(float delta, GameContext gameContext) {
        if (spwanTimer.advance(delta)) {
            spawnUnit(gameContext.spawnBuffer);
        }
    }

    private void spawnUnit(Array<Entity> spawnBuffer) {
        spawnBuffer.add(new Unit(new Vector2(this.position.x, this.position.y), this.team, this.unitType));
    }

}
