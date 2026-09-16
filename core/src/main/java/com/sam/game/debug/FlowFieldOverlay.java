package com.sam.game.debug;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.sam.game.GameContext;
import com.sam.game.Team;
import com.sam.game.world.WorldMap;


/** Throwaway overlay: one arrow per cell, showing where a team's flow field sends a unit. */
public class FlowFieldOverlay implements Disposable {

    private static final float SHAFT_FRACTION = 0.35f;   // centre to tip, as a fraction of a cell
    private static final float HEAD_FRACTION = 0.2f;
    private static final float HEAD_ANGLE = 30f;

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    // Reused for every cell so the overlay al
    private final Vector2 direction = new Vector2();
    private final Vector2 tail = new Vector2();
    private final Vector2 tip = new Vector2();
    private final Vector2 head = new Vector2();

    public void render(GameContext gameContext, Team team, OrthographicCamera camera) {
        WorldMap map = gameContext.map;

        // Only walk the cells the camera can actually see.
        float halfWidth = camera.viewportWidth * camera.zoom * 0.5f;
        float halfHeight = camera.viewportHeight * camera.zoom * 0.5f;

        int minColumn = Math.max(0, map.toCell(camera.position.x - halfWidth));
        int maxColumn = Math.min(map.getWidthInCells() - 1, map.toCell(camera.position.x + halfWidth));
        int minRow = Math.max(0, map.toCell(camera.position.y - halfHeight));
        int maxRow = Math.min(map.getHeightInCells() - 1, map.toCell(camera.position.y + halfHeight));

        float shaftLength = map.getCellSize() * SHAFT_FRACTION;
        float headLength = map.getCellSize() * HEAD_FRACTION;

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(team.getTeamColor());

        for (int row = minRow; row <= maxRow; row++) {
            for (int column = minColumn; column <= maxColumn; column++) {

                Vector2 centre = map.toWorldUnit(row, column);

                if (!gameContext.getFlowDirection(team, centre, direction)) {
                    continue;
                }

                // Shaft, centred on the cell.
                tail.set(direction).scl(-shaftLength).add(centre);
                tip.set(direction).scl(shaftLength).add(centre);
                shapeRenderer.line(tail.x, tail.y, tip.x, tip.y);

                // Head: two short lines back from the tip, splayed either side of the shaft.
                head.set(direction).scl(-headLength).rotateDeg(HEAD_ANGLE).add(tip);
                shapeRenderer.line(tip.x, tip.y, head.x, head.y);

                head.set(direction).scl(-headLength).rotateDeg(-HEAD_ANGLE).add(tip);
                shapeRenderer.line(tip.x, tip.y, head.x, head.y);
            }
        }

        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}