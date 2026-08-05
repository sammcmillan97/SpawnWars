package com.sam.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;

public class Catalogue {

    public List<Texture> textures;

    public SpawnBuildingType castleType;
    public EconomyBuildingType mineType;
    public UnitType knightType;

    private Texture castleTexture;
    private Texture knightTexture;
    private Texture mineTexture;

    public Catalogue() {
        textures = new ArrayList<Texture>();
        loadTextures();

        //units
        buildUnits();

        //buildings
        buildBuildings();
    }

    private void buildUnits() {
        buildKnight();
    }

    private void buildBuildings() {
        buildCastle();
        buildMine();
    }

    private void buildKnight() {
        float width = 30;
        float height = 30;
        float maxHealth = 50;
        float movementSpeed = 50;
        float damage = 10;
        float range = 10;
        float attackSpeed = 0.5f;
        this.knightType = new UnitType(width, height, maxHealth, knightTexture, movementSpeed, damage, range, attackSpeed);
    }

    private void buildCastle() {
        float width = 100;
        float height = 100;
        float maxHealth = 500;
        float buildCost = 400;
        float spawnInterval = 10;
        this.castleType = new SpawnBuildingType(width, height, maxHealth, buildCost, castleTexture, spawnInterval, knightType);
    }

    private void buildMine() {
        float width = 100;
        float height = 100;
        float maxHealth = 300;
        float buildCost = 600;
        float goldAmount = 10;
        float goldInterval = 10;
        this.mineType = new EconomyBuildingType(width, height, maxHealth, buildCost, mineTexture, goldAmount, goldInterval);
    }


    public void loadTextures() {
        castleTexture = new Texture("castle.png");
        textures.add(castleTexture);
        knightTexture = new Texture("knight.png");
        textures.add(knightTexture);
        mineTexture = new Texture("mine.png");
        textures.add(mineTexture);
    }

    public void dispose() {
        for(Texture texture : textures) {
            texture.dispose();
        }
    }

}
