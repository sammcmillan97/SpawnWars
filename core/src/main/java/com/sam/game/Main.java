package com.sam.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

    private OrthographicCamera camera;
    private FitViewport viewPort;
    private GameContext gameContext;
    private Catalogue catalogue;

    //Controls
    public static final float CAMERA_MOVE_SPEED = 300;

    //Team
    public static final int PLAYER_ONE = 1;
    public static final int Player_TWO  = 2;
    public static final float STARTING_GOLD = 500;
    public static final float PASSIVE_GOLD = 10;
    public static final float PASSIVE_GOLD_INTERVAL = 10;

    //Player control
    public Team player;

    private BitmapFont font;

    //Runs once 
    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.position.set(400, 240, 0);
        viewPort = new FitViewport(2000, 2000, camera);

        font = new BitmapFont();          // built-in 15px white Arial
        font.getData().setScale(2f);

        gameContext = new GameContext();
        catalogue = new Catalogue();

        //In future build gamesetup screen to determine these values
        player = new Team(Color.RED, PLAYER_ONE, STARTING_GOLD, PASSIVE_GOLD, PASSIVE_GOLD_INTERVAL);
        Team playerTwo = new Team(Color.BLUE, Player_TWO, STARTING_GOLD, PASSIVE_GOLD, PASSIVE_GOLD_INTERVAL);
        
        gameContext.addTeam(player);
        gameContext.addTeam(playerTwo);

        SpawnBuilding playerCastle = new SpawnBuilding(new Vector2(200, 200), player, catalogue.castleType);
        EconomyBuilding playerMine = new EconomyBuilding(new Vector2(300, 300), player, catalogue.mineType); 

        SpawnBuilding enemyCastle = new SpawnBuilding(new Vector2(600, 600), playerTwo, catalogue.castleType); 

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
            camera.position.x += CAMERA_MOVE_SPEED * delta;
        }
        
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.position.x -= CAMERA_MOVE_SPEED * delta;
        }
        
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.position.y += CAMERA_MOVE_SPEED * delta;
        }
        
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.position.y -= CAMERA_MOVE_SPEED * delta;
        }
    }

    //To be abstracted
    public void playerControl(GameContext gameContext) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            Vector2 buildingPosition = viewPort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            if (player.spendGold(catalogue.castleType.getCost())) {
                gameContext.spawnBuffer.add(new SpawnBuilding(buildingPosition, player, catalogue.castleType));
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
        font.dispose();

        catalogue.dispose();
    }
}
