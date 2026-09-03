package com.sam.game.entity;

import com.badlogic.gdx.math.Vector2;
import com.sam.game.Team;
import com.sam.game.content.BuildingType;

public abstract class Building extends Entity {

    protected final int originColumn;
    protected final int originRow;
    protected final int widthInCells;
    protected final int heightInCells;

    protected Building(int originColumn, int originRow, Team team, BuildingType type, int cellSize) {
        super(centreOf(originColumn, originRow, type, cellSize), type.getWidthInCells()  * cellSize, type.getHeightInCells() * cellSize, team, type.getMaxHealth(), type.getTexture());
        this.originColumn = originColumn;
        this.originRow = originRow;
        this.widthInCells = type.getWidthInCells();
        this.heightInCells = type.getWidthInCells();
    }

    private static Vector2 centreOf(int originColumn, int originRow, BuildingType type, int cellSize) {
        float xWorldPosition = (originColumn + type.getWidthInCells() / 2f) * cellSize;
        float yWorldPosition = (originRow + type.getHeightInCells() / 2f) * cellSize;
        return new Vector2(xWorldPosition, yWorldPosition);
    }

    public int getOriginColumn()  { return originColumn; }
    public int getOriginRow()     { return originRow; }
    public int getWidthInCells()  { return widthInCells; }
    public int getHeightInCells() { return heightInCells; }
    
}