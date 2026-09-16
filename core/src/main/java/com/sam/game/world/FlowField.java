package com.sam.game.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sam.game.entity.Building;

public class FlowField {

    private static final int[] ROW_OFFSETS = {1, 0, -1, 0, 1, -1, -1, 1};
    private static final int[] COL_OFFSETS = {0, 1, 0, -1, 1, 1, -1, -1};

    private static final float DIAGONAL = 0.70710678f;

    //                                            N     E     S      W     NE        SE         SW         NW
    private static final float[] DIRECTION_X = {  0f,   1f,   0f,   -1f,   DIAGONAL,  DIAGONAL, -DIAGONAL, -DIAGONAL };
    private static final float[] DIRECTION_Y = {  1f,   0f,  -1f,    0f,   DIAGONAL, -DIAGONAL, -DIAGONAL,  DIAGONAL };

    private static final int UNREACHABLE = Integer.MAX_VALUE;
    private static final int ORTHOGONAL_COUNT = 4;
    private static final int NO_DIRECTION = -1;

    private int[][] integration;
    private int[][] directions;

    private final int width;
    private final int height;

    private final WorldMap map;

    public FlowField(WorldMap map, Array<Building> goals) {
        this.width = map.getWidthInCells();
        this.height = map.getHeightInCells();
        this.map = map;
        
        rebuild(goals);    
    }

    private void buildIntegrationField(Array<Building> goals) {
        
        integration = new int[height][width];
        for (int i = 0; i < integration.length; i++) {
            for (int j = 0; j < integration[0].length; j++) {
                integration[i][j] = UNREACHABLE;
            }
        }
        int head = 0;
        int tail = 0;

        int[] queue = new int[width * height];

        for(Building goal : goals) {
            for(Cell cell : map.getFootprint(goal)) {
                integration[cell.getRow()][cell.getColumn()] = 0;
                queue[tail++] = cell.getRow() * width + cell.getColumn();
            }
        }

        while(head < tail) {
            int currentIndex = queue[head++];
            int currentRow = currentIndex / width;
            int currentColumn = currentIndex % width;
            int newIntegrationValue = integration[currentRow][currentColumn] + 1;

            for(int i = 0; i < ORTHOGONAL_COUNT; i++) {

                int neighbourRow = currentRow + ROW_OFFSETS[i];
                int neighbourColumn = currentColumn + COL_OFFSETS[i];

                if(!map.isBlocked(neighbourRow, neighbourColumn)) {
                    if (integration[neighbourRow][neighbourColumn] > newIntegrationValue) {
                        integration[neighbourRow][neighbourColumn] = newIntegrationValue;
                        queue[tail++] = (neighbourRow) * width + neighbourColumn;
                    }
                }
            }
        }
    }

    private void buildDirectionField() {
        directions = new int[height][width];

        for(int currentRow = 0; currentRow < height; currentRow++) {
            for (int currentColumn = 0; currentColumn < width; currentColumn++) {

            int currentValue = integration[currentRow][currentColumn];

            // goal cells, blocked cells and walled-off cells have nowhere to send a unit.
            if (currentValue == 0 || currentValue == UNREACHABLE) {
                directions[currentRow][currentColumn] = NO_DIRECTION;
                continue;
            }

            int lowestValue = currentValue;
            int bestDirection = NO_DIRECTION;

            for (int i = 0; i < ROW_OFFSETS.length; i++) {

                int neighbourRow = currentRow + ROW_OFFSETS[i];
                int neighbourColumn = currentColumn + COL_OFFSETS[i];

                if (map.outBounds(neighbourRow, neighbourColumn)) {
                    continue;
                }

                // a diagonal travels between two cells, so both of them have to be clear.
                if (i >= ORTHOGONAL_COUNT
                        && (map.isBlocked(neighbourRow, currentColumn)
                         || map.isBlocked(currentRow, neighbourColumn))) {
                    continue;
                }

                if (integration[neighbourRow][neighbourColumn] < lowestValue) {
                    lowestValue = integration[neighbourRow][neighbourColumn];
                    bestDirection = i;
                }
            }

            directions[currentRow][currentColumn] = bestDirection;

            }
        }
    }

    public void rebuild(Array<Building> goals) {
        buildIntegrationField(goals);
        buildDirectionField();
    }

    public boolean getDirection(int row, int column, Vector2 out) {
        int direction = directions[row][column];

        if (direction == NO_DIRECTION) {
            return false;
        }

        out.set(DIRECTION_X[direction], DIRECTION_Y[direction]);
        return true;
    }
}
