package com.sam.game.world;

import com.badlogic.gdx.math.MathUtils;
import com.sam.game.entity.Entity;

public class WorldMap {

    private Cell[][] grid;

    private int cellSize;

    public WorldMap(int cellSize, int widthInCells, int heightInCells) {
        this.cellSize = cellSize;

        grid = new Cell[heightInCells][widthInCells];

        for (int h = 0; h < heightInCells; h++) {
            for (int w = 0; w < widthInCells; w++) {
                grid[h][w] = new Cell();
            }
        }
    }

    public void printGrid() {
    for (int h = 0; h < grid.length; h++) {
        System.out.print("[");
        
        for (int w = 0; w < grid[h].length; w++) {
            if (grid[h][w].IsEmpty()) {
                System.out.print("0");
            } else {
                System.out.print("x");
            }

            if (w < grid[h].length - 1) {
                System.out.print(", ");
            }
        }
        
        System.out.println("]");
    }
}

    public int getCellSize() { return cellSize; }

    public int toCell(float worldCoordinate)  {
        int cell = Math.floorDiv(MathUtils.floor(worldCoordinate), cellSize);
        return cell;
    }

    private boolean outBounds(int column, int row) {
        return (column < 0 || column >= grid[0].length || row  < 0 || row >= grid.length);
    }

    public boolean canPlace(int startingColumn, int startingRow, int finishingColumn, int finishingRow) {
        if (outBounds(startingColumn, startingRow) || outBounds(finishingColumn, finishingRow)) {
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
        int finishingRow = startingRow + heightInCells;
        int finishingColumn = startingColumn + widthInCells;
        System.out.println("place building");
        System.out.println("starting column: " + startingColumn);
        System.out.println("starting row: " + startingRow);
        System.out.println("finishingRow: " + finishingRow);
        System.out.println("finshing column: " +  finishingColumn);

        if (!canPlace(startingColumn, startingRow, finishingColumn, finishingRow)) {
            return false;
        }

        for(int row = startingRow; row <= finishingRow; row++) {
            for(int column = startingColumn; column <= finishingColumn; column++) {
                System.out.println("row: " + row + " column: " + column);
                grid[row][column].setOccupant(building);
            }
        }
        return true;
    }

    public boolean clearFootprint(int startingColumn, int startingRow, int widthInCells, int heightInCells) {
        int finishingRow = startingRow + heightInCells;
        int finishingColumn = startingColumn + widthInCells;

        for(int row = startingRow; row <= finishingRow; row++) {
            for(int column = startingColumn; column <= finishingColumn; column++) {
                grid[row][column].removeOccupant();
            }
        }

        return true;
    }
}
