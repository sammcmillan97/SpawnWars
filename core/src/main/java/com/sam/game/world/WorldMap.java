package com.sam.game.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sam.game.entity.Entity;

public class WorldMap {

    private Cell[][] grid;

    private int cellSize;

    public WorldMap(int widthInCells, int heightInCells, int cellSize) {
        this.cellSize = cellSize;

        grid = new Cell[heightInCells][widthInCells];

        for (int h = 0; h < heightInCells; h++) {
            for (int w = 0; w < widthInCells; w++) {
                grid[h][w] = new Cell();
            }
        }
    }

    private int toCell(float worldCoordinate)  {
        int cell =   Math.floorDiv(MathUtils.floor(worldCoordinate), cellSize);
        return cell;
    }

    private boolean outBounds(int column, int row) {
        return (column < 0 || column >= grid[0].length || row  < 0 || row >= grid.length);
    }

    public boolean canPlace(int startingColumn, int startingRow, int finishingColumn, int finishingRow) {
        if (outBounds(startingRow, startingColumn) || outBounds(finishingRow, finishingColumn)) {
            return false; 
        }

        for(int row = startingRow; row <= finishingRow; row++) {
            for(int column = startingColumn; column <= finishingColumn; column++) {
                if(!grid[row][column].IsEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean placeBuilding(int startingColumn, int startingRow, int widthInCells, int heightInCells, Entity building) {
        int finishingRow = startingRow + heightInCells - 1;
        int finishingColumn = startingColumn + widthInCells - 1;

        if (!canPlace(startingColumn, startingRow, finishingColumn, finishingRow)) {
            return false;
        }

        for(int row = startingRow; row <= finishingRow; row++) {
            for(int column = startingColumn; column <= finishingColumn; column++) {
                grid[row][column].setOccupant(building);
            }
        }

        return true;
    }
}
