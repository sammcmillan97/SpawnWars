package com.sam.game.world;

import com.badlogic.gdx.utils.Array;
import com.sam.game.entity.Building;

public class FlowField {

    private static final int[] ROW_OFFSETS = {1, 0, -1, 0};
    private static final int[] COL_OFFSETS = {0, 1, 0, -1};

    int[][] integration;

    private final int width;
    private final int height;

    private final WorldMap map;

    public FlowField(WorldMap map, Array<Building> goals) {
        this.width = map.getWidthInCells();
        this.height = map.getHeightInCells();
        this.map = map;
        buildIntegrationField(goals);
    
    }

    private void buildIntegrationField(Array<Building> goals) {
        
        integration = new int[height][width];
        for (int i = 0; i < integration.length; i++) {
            for (int j = 0; j < integration[0].length; j++) {
                integration[i][j] = Integer.MAX_VALUE;
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

            for(int i = 0; i < ROW_OFFSETS.length; i++) {

                int neighbourRow = currentRow + ROW_OFFSETS[i];
                int neighbourColumn = currentRow + COL_OFFSETS[i];

                if(!map.isBlocked(neighbourRow, neighbourColumn)) {
                    if (integration[neighbourRow][neighbourColumn] > newIntegrationValue) {
                        integration[neighbourRow][neighbourColumn] = newIntegrationValue;
                        queue[tail++] = (neighbourRow) * width + neighbourColumn;
                    }
                }
            }
        }
    }

    public void rebuild(Array<Building> goals) {
        buildIntegrationField(goals);
    }
}
