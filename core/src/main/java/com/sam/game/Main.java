package com.sam.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sam.game.content.Catalogue;
import com.sam.game.content.BuildingType;
import com.sam.game.entity.Entity;
import com.sam.game.entity.SpawnBuilding;
import com.sam.game.entity.Building;
import com.sam.game.world.WorldMap;

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
    public static final float PASSIVE_GOLD = 100;
    public static final float PASSIVE_GOLD_INTERVAL = 10;

    public static final int CELL_SIZE = 10;
    public static final float WORLD_WIDTH = 1000;
    public static final float WORLD_HEIGHT = 1000;

    //Players team
    public Team player;

    private BitmapFont font;

    private boolean buildMenuOpen = false;
    private BuildingType pendingBuilding = null;

    //Runs once 
    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.position.set(400, 240, 0);
        viewPort = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        font = new BitmapFont();          // built-in 15px white Arial
        font.getData().setScale(2f);

        gameContext = new GameContext(CELL_SIZE, WORLD_WIDTH, WORLD_HEIGHT);
        catalogue = new Catalogue();

        //In future build gamesetup screen to determine these values
        player = new Team(Color.RED, PLAYER_ONE, STARTING_GOLD, PASSIVE_GOLD, PASSIVE_GOLD_INTERVAL);
        Team playerTwo = new Team(Color.BLUE, Player_TWO, STARTING_GOLD, PASSIVE_GOLD, PASSIVE_GOLD_INTERVAL);

        gameContext.addTeam(player);
        gameContext.addTeam(playerTwo);

        player.addEnemy(playerTwo.getTeamNumber());
        playerTwo.addEnemy(player.getTeamNumber());

        Building playerCastle = new Building(1, 1, player, catalogue.castleType, gameContext.map.getCellSize());
        Building enemyCastle = new Building(60, 60, playerTwo, catalogue.castleType, gameContext.map.getCellSize());
        
        Building enemyBarracks = new SpawnBuilding(50, 50, playerTwo, catalogue.barracksType, gameContext.map.getCellSize());

        if (gameContext.addBuilding(playerCastle)) {
            player.setCastle(playerCastle);
        }
        
        if (gameContext.addBuilding(enemyCastle)) {
            playerTwo.setCastle(enemyCastle);
        }

        gameContext.addBuilding(enemyBarracks);
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
            buildMenuOpen = true;
        }

        if (buildMenuOpen) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
                pendingBuilding = catalogue.barracksType;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
                pendingBuilding = catalogue.mineType;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                buildMenuOpen = false;
                pendingBuilding = null;
            }
            if (pendingBuilding != null && Gdx.input.justTouched()) {

                GridPoint2 origin = originCellUnderMouse(pendingBuilding, gameContext.map);  
                SpawnBuilding building = new SpawnBuilding(origin.x, origin.y, player, catalogue.barracksType, gameContext.map.getCellSize());
                gameContext.addBuilding(building);

            }
        }
    }

    private GridPoint2 originCellUnderMouse(BuildingType type, WorldMap map) {
        Vector2 world = viewPort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        int cursorColumn = map.toCell(world.x);
        int cursorRow    = map.toCell(world.y);


        return new GridPoint2(cursorColumn - type.getWidthInCells()  / 2,
                            cursorRow    - type.getHeightInCells() / 2);
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
        
        //Shows building outline goes green when building can be placed (to be abstracted)
        batch.begin();
        if (pendingBuilding != null) {
            GridPoint2 origin = originCellUnderMouse(pendingBuilding, gameContext.map);
            boolean valid  = gameContext.map.canPlace(origin.x, origin.y, origin.x + pendingBuilding.getWidthInCells() - 1, origin.y + pendingBuilding.getHeightInCells() - 1);
            batch.setColor(valid ? Color.GREEN : Color.RED);   
            batch.draw(gameContext.whitePixel,
                    origin.x * gameContext.map.getCellSize(), origin.y * gameContext.map.getCellSize(),
                    pendingBuilding.getWidthInCells()  * gameContext.map.getCellSize(),
                    pendingBuilding.getHeightInCells() * gameContext.map.getCellSize());
            batch.setColor(Color.WHITE);
        }
        for (int i = 0; i < entities.size; i++) {
            entities.get(i).render(batch, gameContext);
        }
        batch.end();

        //Temp HUD to show gold for each player used for debugging
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
