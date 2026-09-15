package com.sam.game;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.sam.game.entity.Building;
import com.sam.game.entity.Entity;
import com.sam.game.world.WorldMap;


public class GameContext {

    public Array<Entity> spawnBuffer;
    private Array<Entity> entityArray;
    public Array<Entity> deathBuffer;
    protected Map<Integer, Team> teams;
    public WorldMap map;

    public Texture whitePixel;

    public GameContext(int cellSize, float worldWidth, float worldHeight) {
        spawnBuffer = new Array<>();
        entityArray = new Array<>();
        deathBuffer = new Array<>();
        teams = new HashMap<>();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        whitePixel = new Texture(pixmap);

        map = new WorldMap(cellSize, (int) worldWidth / cellSize, (int) worldHeight /cellSize);
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

    public Array<Building> getEnemyCastles(Team team) {
        Set<Integer> enemies = team.getEnemies();
        Array<Building> enemyCastles = new Array<>();
        for (int teamNumber : enemies) {
            enemyCastles.add(teams.get(teamNumber).getCastle());
        }
        return enemyCastles;
    }


    public void addBufferAndClearBuffer()  {
        entityArray.addAll(spawnBuffer);
        spawnBuffer.clear();
    }

    public void removeDeadEntityAndClearBuffer() {
        for (int i =0; i < deathBuffer.size; i++) {
            Entity deadEntity = deathBuffer.get(i);
            if (deadEntity instanceof Building) {
                Building b = (Building) deadEntity;
                this.map.clearFootprint(b);
            }
        }
        entityArray.removeAll(deathBuffer, true);
        deathBuffer.clear();
    }

    public boolean addBuilding(Building building) {
        if (building.getTeam().canAfford(building.getBuildingCost()) && this.map.placeBuilding(building)) {
            building.getTeam().spendGold(building.getBuildingCost());
            this.spawnBuffer.add(building);
            return true;
        }
        return false;
    }

    public Array<Entity> getEntityArray() {
        return entityArray;
    }

    public void dispose() {
        whitePixel.dispose();
    }

}
