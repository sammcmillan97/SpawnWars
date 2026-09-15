package com.sam.game.world;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sam.game.entity.Building;

public class WorldMap {

    static final int MAX_SPAWN_SEARCH_RINGS = 5;

    private Cell[][] grid;

    private final int cellSize;
    private final int widthInCells;
    private final int heightInCells;

    public WorldMap(int cellSize, int widthInCells, int heightInCells) {
        this.cellSize = cellSize;
        this.widthInCells = widthInCells;
        this.heightInCells = heightInCells;

        grid = new Cell[heightInCells][widthInCells];

        for (int h = 0; h < heightInCells; h++) {
            for (int w = 0; w < widthInCells; w++) {
                grid[h][w] = new Cell();
            }
        }
    }

    public int getCellSize() { return cellSize; }

    public int getWidthInCells() {
        return widthInCells;
    }

    public int getHeightInCells() {
        return heightInCells;
    }

    public boolean isBlocked(int row, int column) {
        return outBounds(row, column) || !grid[row][column].IsEmpty(); 
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

    public int toCell(float worldCoordinate)  {
        int cell = Math.floorDiv(MathUtils.floor(worldCoordinate), cellSize);
        return cell;
    }

    public Vector2 toWorldUnit(int row, int column) {
        float x = (column + 0.5f) * cellSize;
        float y = (row + 0.5f) * cellSize;

        return new Vector2(x, y);
    }

    private boolean outBounds(int row, int column) {
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

    public boolean placeBuilding(Building building) {
        int finishingRow = building.getOriginRow() + building.getHeightInCells() - 1; //inclusive
        int finishingColumn = building.getOriginColumn() + building.getWidthInCells() - 1; //inclusive

        if (!canPlace(building.getOriginColumn(), building.getOriginRow(), finishingColumn, finishingRow)) {
            return false;
        }

        for(int row = building.getOriginRow(); row <= finishingRow; row++) {
            for(int column = building.getOriginColumn(); column <= finishingColumn; column++) {
                grid[row][column].setOccupant(building);
            }
        }
        return true;
    }

    public List<Cell> getFootprint(Building building) {
        List<Cell> footprint = new ArrayList<>();

        int finishingRow = building.getOriginRow() + building.getHeightInCells() - 1;
        int finishingColumn = building.getOriginColumn() + building.getWidthInCells() - 1;

        for (int row = building.getOriginRow(); row <= finishingRow; row++) {
            for (int column = building.getOriginColumn(); column <= finishingColumn; column++) {
                footprint.add(grid[row][column]);
            }
        }

        return footprint;
    }

    public boolean clearFootprint(Building building) {
        int finishingRow = building.getOriginRow() + building.getHeightInCells() - 1; //inclusive
        int finishingColumn = building.getOriginColumn() + building.getWidthInCells() -1; //inclusive

        for(int row = building.getOriginRow(); row <= finishingRow; row++) {
            for(int column = building.getOriginColumn(); column <= finishingColumn; column++) {
                grid[row][column].removeOccupant();
            }
        }

        return true;
    }

    public Vector2 getNearestAvaliableSpawnPoint(Building building) {

        //try the default point first (cell left middle to center of building)
        Vector2 currentCenterWorldUnits = building.getPosition();
        int defaultRow = toCell(currentCenterWorldUnits.y);
        int defaultColumn = toCell(currentCenterWorldUnits.x + building.getWidth() / 2);

        if (!outBounds(defaultColumn, defaultRow) && grid[defaultRow][defaultColumn].IsEmpty()) {
            return toWorldUnit(defaultRow, defaultColumn);
        }
        
        //Begin Perimeter search starting top left 
        int currentColumn = building.getOriginColumn() - 1;
        int currentRow = building.getOriginRow() - 1;
        int i = 0;
        int steps = 0;
        int lengthOfWidthSearch = building.getWidthInCells() + 1;
        int lengthOfHeightSearch = building.getHeightInCells() + 1;

        //Check perimeter max five cells away
        while(i < MAX_SPAWN_SEARCH_RINGS) {
            
            //go right
            steps = lengthOfWidthSearch;
            while(steps > 0) {
                if (!outBounds(currentColumn, currentRow) && grid[currentRow][currentColumn].IsEmpty()) {
                    return toWorldUnit(currentRow, currentColumn);
                }
                currentColumn++;
                steps--;
            }
            
            //go down
            steps = lengthOfHeightSearch;
            while(steps > 0) {
                if (!outBounds(currentColumn, currentRow) && grid[currentRow][currentColumn].IsEmpty()) {
                    return toWorldUnit(currentRow, currentColumn);
                }
                currentRow++;
                steps--;
            }

            //go left 
            steps = lengthOfWidthSearch;
            while(steps > 0) {
                if (!outBounds(currentColumn, currentRow) && grid[currentRow][currentColumn].IsEmpty()) {
                    return toWorldUnit(currentRow, currentColumn);
                }
                currentColumn--;
                steps--;
            }

            //go up
            steps = lengthOfHeightSearch;
            while(steps > 0) {
                if (!outBounds(currentColumn, currentRow) && grid[currentRow][currentColumn].IsEmpty()) {
                    return toWorldUnit(currentRow, currentColumn);
                }
                currentRow--;
                steps--;
            }

            currentRow--;
            currentColumn--;

            lengthOfWidthSearch+= 2;
            lengthOfHeightSearch+= 2;

            i++;
        }

        return null;
    }
}
