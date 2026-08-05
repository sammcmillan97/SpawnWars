package com.sam.game;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.sam.game.entity.Entity;


public class GameContext {

    public Array<Entity> spawnBuffer;
    private Array<Entity> entityArray;
    public Array<Entity> deathBuffer;
    protected Map<Integer, Team> teams;

    public Texture whitePixel;

    public GameContext() {
        spawnBuffer = new Array<>();
        entityArray = new Array<>();
        deathBuffer = new Array<>();
        teams = new HashMap<>();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        whitePixel = new Texture(pixmap);

    }

    public void addTeam(Team team) {
        teams.put(team.getTeamNumber(), team);
    }

    public Team getTeam(int teamNumber) {
        return teams.get(teamNumber);
    }

    public void addToEntityArray(Entity entity) {
        entityArray.add(entity);
    }

    public void updateTeams(float delta) {
        for (Team team : teams.values()) {
            team.earnPassiveGold(delta);
    }
}

    public void addBufferAndClearBuffer()  {
        entityArray.addAll(spawnBuffer);
        spawnBuffer.clear();
    }

    public void removeDeadEntityAndClearBuffer() {
        entityArray.removeAll(deathBuffer, true);
        deathBuffer.clear();
    }

    public Array<Entity> getEntityArray() {
        return entityArray;
    }

    public void dispose() {
        whitePixel.dispose();
    }

}
