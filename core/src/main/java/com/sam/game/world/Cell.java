package com.sam.game.world;

import com.sam.game.entity.Entity;

public class Cell {
     
    private Entity occupant;

    private final int row;
    private final int column;

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }
    
    public void setOccupant(Entity occupant) {
        this.occupant = occupant;
    }

    public void removeOccupant() {
        this.occupant = null;
    }

    public boolean IsEmpty() {
        return occupant == null;
    }
}
