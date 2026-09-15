package com.sam.game.world;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.sam.game.entity.Building;

public class FlowField {

    public FlowField(WorldMap map, Array<Building> goals) {
        int[][] integration = new int[map.getWidthInCells()][map.getHeightInCells()];
        for (int i = 0; i < integration.length; i++) {
            for (int j = 0; i < integration[0].length; j++) {
                integration[i][j] = Integer.MAX_VALUE;
            }
        }

        

        Queue<Bul> queue = new Queue<>();
    }

}
