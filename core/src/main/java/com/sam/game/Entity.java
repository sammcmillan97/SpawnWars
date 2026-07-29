package com.sam.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {

    protected Vector2 position;

    protected float width;

    protected float height;

    protected Texture texture;

    protected Team team;

    protected float health;
    protected float maxHealth;

    private static int healthBarWidth = 50;
    private static int healthBarHeight = 3;
    

    protected Entity(Vector2 position, float width, float height, Team team, float maxHealth, Texture texture) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.team = team;
        this.maxHealth = maxHealth;
        this.texture = texture;

        this.health = maxHealth;
    }

    protected boolean isDead() { return health <= 0; }

    protected abstract void update(float delta, GameContext gameContext);

    protected void render(SpriteBatch batch, GameContext gameContext) {
        batch.setColor(team.getTeamColor());
        batch.draw(texture, position.x, position.y, width, height);
        batch.setColor(Color.WHITE);

        drawHealthBar(batch, gameContext);
    }

    protected void drawHealthBar(SpriteBatch batch, GameContext gameContex) {
        batch.setColor(Color.RED);
        batch.draw(gameContex.whitePixel, position.x + (width/2) - (healthBarWidth /2), position.y + 10 + height, healthBarWidth, healthBarHeight);
        batch.setColor(Color.GREEN);

        float healthFraction = Math.max(0f, Math.min(1f, health / maxHealth));

        batch.draw(gameContex.whitePixel, position.x + (width/2) - (healthBarWidth /2), position.y + 10 + height, healthFraction * healthBarWidth, healthBarHeight);
        batch.setColor(Color.WHITE);
    }

    protected void takeDamage(float damage) {
        health-= damage;
    }
}
