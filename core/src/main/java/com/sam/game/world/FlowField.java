package com.sam.game.world;

import com.badlogic.gdx.utils.Array;
import com.sam.game.entity.Building;

public class FlowField {

    public FlowField(WorldMap map, Array<Building> goals) {
        int[][] integration = new int[map.getWidthInCells()][map.getHeightInCells()];
        for (int i = 0; i < integration.length; i++) {
            for (int j = 0; i < integration[0].length; j++) {
                integration[i][j] = Integer.MAX_VALUE;
            }
        }
        int head = 0;
        int tail = 0;

        int[] queue = new int[map.getWidthInCells() * map.getHeightInCells()];

        for(Building goal : goals) {
            for(Cell cell : map.getFootprint(goal)) {
                integration[cell.getRow()][cell.getColumn()] = 0;
                queue[tail++] = cell.getRow() * map.getWidthInCells() + cell.getColumn();
            }
        }

        while(head < tail) {
            int currentIndex = queue[head++];
            int currentRow = currentIndex / map.getWidthInCells();
            int currentColumn = currentIndex % map.getWidthInCells();
            int integrationValue = integration[currentRow][currentColumn];
            int neighbourIntegrationValue = 0;

            //Above 
            if(!map.isBlocked(currentRow + 1, currentColumn)) {
                neighbourIntegrationValue = integration[currentRow + 1][currentColumn];
                if (neighbourIntegrationValue > integrationValue) {
                    integration[currentRow + 1][currentColumn] = neighbourIntegrationValue + 1;
                    queue[tail++] = (currentRow + 1) * map.getWidthInCells() + currentColumn;
                }
            }
            //Right
            if(!map.isBlocked(currentRow, currentColumn + 1)) {
                neighbourIntegrationValue = integration[currentRow][currentColumn + 1];
                if (neighbourIntegrationValue > integrationValue) {
                    integration[currentRow][currentColumn + 1] = neighbourIntegrationValue + 1;
                    queue[tail++] = currentRow * map.getWidthInCells() + (currentColumn + 1);
                }
            }
            //below
            if(!map.isBlocked(currentRow - 1, currentColumn)) {
                neighbourIntegrationValue = integration[currentRow - 1][currentColumn];
                if (neighbourIntegrationValue > integrationValue) {
                    integration[currentRow - 1][currentColumn] = neighbourIntegrationValue + 1;
                    queue[tail++] = (currentRow - 1) * map.getWidthInCells() + currentColumn;
                }
            }
            //left
            if(!map.isBlocked(currentRow, currentColumn - 1)) {
                neighbourIntegrationValue = integration[currentRow][currentColumn - 1];
                if (neighbourIntegrationValue > integrationValue) {
                    integration[currentRow][currentColumn - 1] = neighbourIntegrationValue + 1;
                    queue[tail++] = currentRow * map.getWidthInCells() + (currentColumn - 1);
                }
            }
        
        }
        

    }
}
