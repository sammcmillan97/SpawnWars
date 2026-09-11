package com.sam.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.sam.game.GameContext;
import com.sam.game.Team;

public abstract class Entity {

    //Centre of entity
    protected Vector2 position;

    protected float width;

    protected float height;

    protected Texture texture;

    protected Team team;

    protected float health;
    protected float maxHealth;

    private static int healthBarWidth = 50;
    private static int healthBarHeight = 3;
    private static int healthBarGap = 5;
    

    protected Entity(Vector2 position, float width, float height, Team team, float maxHealth, Texture texture) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.team = team;
        this.maxHealth = maxHealth;
        this.texture = texture;

        this.health = maxHealth;
    }

    public boolean isDead() { return health <= 0; }

    public abstract void update(float delta, GameContext gameContext);

    public void render(SpriteBatch batch, GameContext gameContext) {
        batch.setColor(team.getTeamColor());
        batch.draw(texture, position.x - this.width / 2, position.y - this.height / 2, width, height);
        batch.setColor(Color.WHITE);

        drawHealthBar(batch, gameContext);
    }

    protected void drawHealthBar(SpriteBatch batch, GameContext gameContex) {
        float barX = position.x - (healthBarWidth / 2f);
        float barY = position.y + (height / 2f) + healthBarGap;

        batch.setColor(Color.RED);
        batch.draw(gameContex.whitePixel, barX, barY, healthBarWidth, healthBarHeight);
        batch.setColor(Color.GREEN);

        float healthFraction = Math.max(0f, Math.min(1f, health / maxHealth));

        batch.draw(gameContex.whitePixel, barX, barY, healthFraction * healthBarWidth, healthBarHeight);
        batch.setColor(Color.WHITE);
    }

    protected void takeDamage(float damage) {
        health-= damage;
    }

    public Team getTeam() {
        return this.team;
    }
}
