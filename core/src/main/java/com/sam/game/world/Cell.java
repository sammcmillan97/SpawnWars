package com.sam.game.world;

import com.sam.game.entity.Entity;

public class Cell {
     
    private Entity occupant;

    public Cell() {
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
