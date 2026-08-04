package com.sam.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;

    private Texture castleTexture;
    private Texture knightTexture;
    private Texture mineTexture;

    private OrthographicCamera camera;
    private FitViewport viewPort;
    private GameContext gameContext;

    //The below will eventually come from config 

    //Unit
    private static final float MOVE_SPEED = 200;
    private static final float UNIT_WIDTH = 30;
    private static final float UNIT_HEIGHT = 30;
    private static final float UNIT_HEALTH = 50;
    private static final float UNIT_MOVEMENT_SPEED = 50;
    private static final float UNIT_DAMAGE = 10;
    private static final float UNIT_RANGE = 10;
    private static final float UNIT_ATTACK_SPEED = 0.5f; //Attacks per second

    //Spawn Building
    private static final float BUILDING_HEALTH = 500;
    private static final float SPAWN_BUILDING_COST = 100;
    private static final float CASTLE_WIDTH = 100;
    private static final float CASTLE_HEIGHT = 100;

    //Econmy Building
    private static final float GOLD_GENERATION = 10;

    //Team
    public static final int PLAYER_ONE = 1;
    public static final int Player_TWO  = 2;
    public static final float STARTING_GOLD = 500;
    public static final float PASSIVE_GOLD = 10;
    public static final float PASSIVE_GOLD_INTERVAL = 10;

    //Costs
    public static final float CASTLE_COST = 400;
    public static final float MINE_COST = 500;

    //Player control
    public Team player;

    private BitmapFont font;

    protected UnitType knight;

    //Runs once 
    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.position.set(400, 240, 0);
        viewPort = new FitViewport(2000, 2000, camera);

        font = new BitmapFont();          // built-in 15px white Arial
        font.getData().setScale(2f);

        castleTexture = new Texture("castle.png");
        knightTexture = new Texture("knight.png");
        mineTexture = new Texture("mine.png");

        gameContext = new GameContext();

        //In future build gamesetup screen to determine these values
        player = new Team(Color.RED, PLAYER_ONE, STARTING_GOLD, PASSIVE_GOLD, PASSIVE_GOLD_INTERVAL);
        Team playerTwo = new Team(Color.BLUE, Player_TWO, STARTING_GOLD, PASSIVE_GOLD, PASSIVE_GOLD_INTERVAL);
        
        gameContext.addTeam(player);
        gameContext.addTeam(playerTwo);

        knight = new UnitType(UNIT_WIDTH, UNIT_HEIGHT, UNIT_HEALTH, knightTexture, UNIT_MOVEMENT_SPEED, UNIT_DAMAGE, UNIT_RANGE, UNIT_ATTACK_SPEED);

        SpawnBuilding playerCastle = new SpawnBuilding(new Vector2(200, 200), CASTLE_WIDTH, CASTLE_HEIGHT, player, BUILDING_HEALTH, castleTexture, knight);
        EconomyBuilding playerMine = new EconomyBuilding(new Vector2(300, 300), 100 , 100, player, BUILDING_HEALTH, mineTexture, GOLD_GENERATION); 

        SpawnBuilding enemyCastle = new SpawnBuilding(new Vector2(600, 600), CASTLE_WIDTH, CASTLE_HEIGHT, playerTwo, BUILDING_HEALTH, castleTexture, knight); 

        gameContext.addToEntityArray(playerMine);
        gameContext.addToEntityArray(playerCastle);
        gameContext.addToEntityArray(enemyCastle);
    }

    public void cameraControl(OrthographicCamera camera, float delta) {
        
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.zoom = Math.min(5f, camera.zoom + delta * 0.5f);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            camera.zoom = Math.max(0.1f, camera.zoom - delta * 0.5f);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.position.x += MOVE_SPEED * delta;
        }
        
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.position.x -= MOVE_SPEED * delta;
        }
        
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.position.y += MOVE_SPEED * delta;
        }
        
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.position.y -= MOVE_SPEED * delta;
        }
    }

    //To be abstracted
    public void playerControl(GameContext gameContext) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            float x = 0;
            float y = 0;
            x = Gdx.input.getX();
            y = Gdx.input.getY();
            Vector2 buildingPosition = viewPort.unproject(new Vector2(x, y));
            if (player.spendGold(SPAWN_BUILDING_COST)) {
                gameContext.spawnBuffer.add(new SpawnBuilding(buildingPosition, 100, 100, player, BUILDING_HEALTH, castleTexture, knight));
            }

        }
    }

    //Every frame 
    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        
        cameraControl(camera, delta);
        playerControl(gameContext);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        
        Array<Entity> entities = gameContext.getEntityArray();

        for (int i = 0; i < entities.size; i++) {
            entities.get(i).update(delta, gameContext);
        }
        
        for (int i = 0; i < entities.size; i++) {
            if (entities.get(i).isDead()) gameContext.deathBuffer.add(entities.get(i));
        }

        gameContext.updateTeams(delta);

        gameContext.addBufferAndClearBuffer();
        gameContext.removeDeadEntityAndClearBuffer();

        entities = gameContext.getEntityArray();
        
        batch.begin();
        for (int i = 0; i < entities.size; i++) {
            entities.get(i).render(batch, gameContext);
        }
        batch.end();

        //Temp
        Matrix4 hudMatrix =new Matrix4();
        hudMatrix.setToOrtho2D(0, 0, viewPort.getWorldWidth(), viewPort.getWorldHeight());
        batch.setProjectionMatrix(hudMatrix);
        batch.begin();
        font.draw(batch, "P1 gold: " + (int) gameContext.getTeam(1).getGold(), 10, viewPort.getWorldHeight() - 10);
        font.draw(batch, "P2 gold: " + (int) gameContext.getTeam(2).getGold(), 10, viewPort.getWorldHeight() - 40);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewPort.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        knightTexture.dispose();
        castleTexture.dispose();
        mineTexture.dispose();
        gameContext.dispose();
        font.dispose();
    }
}
