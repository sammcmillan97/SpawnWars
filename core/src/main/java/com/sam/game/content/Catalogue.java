package com.sam.game.content;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;

public class Catalogue {

    public List<Texture> textures;

    public BuildingType castleType;
    public SpawnBuildingType barracksType;
    public EconomyBuildingType mineType;
    public UnitType footmanType;

    private Texture castleTexture;
    private Texture footmanTexture;
    private Texture mineTexture;
    private Texture barracksTexture;

    public Catalogue() {
        textures = new ArrayList<Texture>();
        loadTextures();

        //units
        buildUnits();

        //buildings
        buildBuildings();
    }

    private void buildUnits() {
        buildFootman();
    }

    private void buildBuildings() {
        buildBarracks();
        buildMine();
        buildCastle();
    }

    private void buildFootman() {
        float width = 30;
        float height = 30;
        float maxHealth = 50;
        float movementSpeed = 50;
        float damage = 10;
        float range = 10;
        float attackSpeed = 0.5f;
        this.footmanType = new UnitType(width, height, maxHealth, footmanTexture, movementSpeed, damage, range, attackSpeed);
    }

    private void buildBarracks() {
        int widthInCells = 6;
        int heightInCells = 6;
        float maxHealth = 500;
        float buildCost = 100;
        float spawnInterval = 10;
        this.barracksType = new SpawnBuildingType(widthInCells, heightInCells, maxHealth, buildCost, barracksTexture, spawnInterval, footmanType);
    }

    private void buildMine() {
        int widthInCells = 6;
        int heightInCells = 6;
        float maxHealth = 300;
        float buildCost = 600;
        float goldAmount = 10;
        float goldInterval = 10;
        this.mineType = new EconomyBuildingType(widthInCells, heightInCells, maxHealth, buildCost, mineTexture, goldInterval, goldAmount);
    }

    private void buildCastle() {
        int widthInCells = 12;
        int heightInCells = 12;
        float maxHealth = 1000;
        float buildCost = 0;
        this.castleType = new BuildingType(widthInCells, heightInCells, maxHealth, buildCost, castleTexture);
    }


    private void loadTextures() {
        castleTexture = new Texture("castle.png");
        textures.add(castleTexture);
        footmanTexture = new Texture("footman.png");
        textures.add(footmanTexture);
        mineTexture = new Texture("mine.png");
        textures.add(mineTexture);
        barracksTexture = new Texture("barracks.png");
        textures.add(barracksTexture);
    }

    public void dispose() {
        for(Texture texture : textures) {
            texture.dispose();
        }
    }

}
