package com.sam.game.world;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector2;
import com.sam.game.content.BuildingType;
import com.sam.game.entity.Building;

public class WorldMapTest {

    private static final int CELL_SIZE = 10;
    private static final int WIDTH_IN_CELLS = 20;
    private static final int HEIGHT_IN_CELLS = 20;

    private static final float EPSILON = 0.0001f;

    private WorldMap map;

    @BeforeEach
    void setUp() {
        map = new WorldMap(CELL_SIZE, WIDTH_IN_CELLS, HEIGHT_IN_CELLS);
    }

    private Building building(int originColumn, int originRow, int widthInCells, int heightInCells) {
        BuildingType type = new BuildingType(widthInCells, heightInCells, 100, 0, null);
        return new Building(originColumn, originRow, null, type, CELL_SIZE);
    }

    // Fills every free cell in the square around a centre cell with a 1x1 blocker.
    // Cells that are already occupied (e.g. the building at the centre) are just rejected.
    private void blockSquareAround(int centreColumn, int centreRow, int radius) {
        for (int row = centreRow - radius; row <= centreRow + radius; row++) {
            for (int column = centreColumn - radius; column <= centreColumn + radius; column++) {
                map.placeBuilding(building(column, row, 1, 1));
            }
        }
    }

    @Test
    @DisplayName("toCell floors a world coordinate onto its cell index")
    void toCellFloorsWorldCoordinates() {
        assertEquals(0, map.toCell(0f));
        assertEquals(0, map.toCell(9.99f));
        assertEquals(1, map.toCell(10f));
        assertEquals(-1, map.toCell(-0.01f));
        assertEquals(-1, map.toCell(-10f));
        assertEquals(-2, map.toCell(-10.01f));
    }

    @Test
    @DisplayName("A building placed on an empty grid is accepted")
    void placesOnEmptyGrid() {
        assertTrue(map.placeBuilding(building(5, 5, 2, 2)));
    }

    @Test
    @DisplayName("Cells covered by a placed building are no longer free")
    void placedFootprintOccupiesItsCells() {
        map.placeBuilding(building(5, 5, 2, 2));

        assertFalse(map.canPlace(5, 5, 6, 6));
        assertFalse(map.canPlace(6, 5, 7, 6));
        assertFalse(map.canPlace(5, 6, 6, 7));
        assertFalse(map.canPlace(6, 6, 7, 7));
    }

    @Test
    @DisplayName("Cells just outside a placed building stay free")
    void placedFootprintDoesNotSpill() {
        map.placeBuilding( building(3, 3, 2, 2));

        assertTrue(map.canPlace(1, 3, 2, 4));   // left of it
        assertTrue(map.canPlace(5, 3, 6, 4));   // right of it
        assertTrue(map.canPlace(3, 5, 4, 6));   // below it
        assertTrue(map.canPlace(3, 1, 4, 2));   // above it
    }

    @Test
    @DisplayName("A non-square footprint reserves width columns and height rows, not a square")
    void nonSquareFootprintReservesTheRightRectangle() {
        map.placeBuilding(building(2, 2, 4, 2));   // 4 wide, 2 tall

        assertFalse(map.canPlace(5, 2, 6, 3));  // 4th column across is taken
        assertTrue(map.canPlace(2, 4, 3, 5));   // 3rd row up is free, it is only 2 tall
    }

    @Test
    @DisplayName("A building overlapping an existing one is rejected")
    void rejectsOverlappingPlacement() {
        map.placeBuilding(building(5, 5, 2, 2));

        assertFalse(map.placeBuilding(building(6, 6, 2, 2)));
    }

    @Test
    @DisplayName("A rejected placement leaves the grid untouched")
    void rejectedPlacementWritesNothing() {
        map.placeBuilding(building(5, 5, 2, 2));
        map.placeBuilding(building(6, 6, 2, 2));   // overlaps at 6,6 so is rejected

        // 7,7 was only ever in the rejected building's footprint
        assertTrue(map.canPlace(7, 7, 8, 8));
    }

    @Test
    @DisplayName("A negative origin is rejected")
    void rejectsNegativeOrigin() {
        assertFalse(map.placeBuilding(building(-1, 5, 2, 2)));
    }

    @Test
    @DisplayName("A footprint running off the far edge is rejected")
    void rejectsFootprintPastTheEdge() {
        int origin = WIDTH_IN_CELLS - 1;   // 2 wide from here would need column 20

        assertFalse(map.placeBuilding(building(origin, 5, 2, 2)));
    }

    @Test
    @DisplayName("A footprint flush with the far edge is accepted")
    void acceptsFootprintFlushWithTheEdge() {
        int column = WIDTH_IN_CELLS - 2;    // covers columns 18 and 19
        int row = HEIGHT_IN_CELLS - 2;      // covers rows 18 and 19

        assertTrue(map.placeBuilding(building(column, row, 2, 2)));
    }

    @Test
    @DisplayName("Clearing a footprint frees its cells again")
    void clearFootprintFreesCells() {
        Building placed = building(5, 5, 2, 2);
        map.placeBuilding(placed);

        map.clearFootprint(placed);

        assertTrue(map.canPlace(5, 5, 7, 7));
    }

    @Test
    @DisplayName("A non-square grid indexes columns and rows independently")
    void nonSquareGridDoesNotTransposeRowsAndColumns() {
        WorldMap wide = new WorldMap(CELL_SIZE, 10, 4);   // 10 columns, 4 rows

        assertTrue(wide.canPlace(8, 2, 9, 3));    // column 8 and row 2 both exist
        assertFalse(wide.canPlace(2, 8, 3, 9));   // row 8 does not
    }

    @Test 
    @DisplayName("Get nearest avaliable spwan point, all cells surronding the building are free")
    void getNearestAvaliableSpawnPointAllCellsFree() {
        Building building = building(1, 1, 1, 1);
        map.placeBuilding(building);

        Vector2 expectedSpwanPoint = map.toWorldUnit(1, 2);
        Vector2 actualSpawnPoint = map.getNearestAvaliableSpawnPoint(building);

        assertEquals(expectedSpwanPoint.x, actualSpawnPoint.x, EPSILON);
        assertEquals(expectedSpwanPoint.y, actualSpawnPoint.y, EPSILON);

    }

    @Test
    @DisplayName("Get nearest avaliable spwan point, building on top right boundary")
    void getNearestAvaliableSpawnPointBuildingOnTopRightBoundary() {
        Building building = building(19, 0, 1, 1);
        map.placeBuilding(building);

        Vector2 expectedSpwanPoint = map.toWorldUnit(1, 19);
        Vector2 actualSpawnPoint = map.getNearestAvaliableSpawnPoint(building);

        assertEquals(expectedSpwanPoint.x, actualSpawnPoint.x, EPSILON);
        assertEquals(expectedSpwanPoint.y, actualSpawnPoint.y, EPSILON);
    }

    @Test
    @DisplayName("Get nearest avaliable spwan point, default point occupied")
    void getNearestAvaliableSpawnPointBuildingAllDefaultSpotsInvalid() {
        Building spawnBuilding = building(2, 2, 1, 1);
        
        Building rightBuilding = building(3, 2, 1, 1);

        map.placeBuilding(spawnBuilding);
        map.placeBuilding(rightBuilding);


        //top left diagonal to building
        Vector2 expectedSpwanPoint = map.toWorldUnit(1, 1);
        Vector2 actualSpawnPoint = map.getNearestAvaliableSpawnPoint(spawnBuilding);

        assertEquals(expectedSpwanPoint.x, actualSpawnPoint.x, EPSILON);
        assertEquals(expectedSpwanPoint.y, actualSpawnPoint.y, EPSILON);
    }

    @Test
    @DisplayName("Get nearest avaliable spawn point, first ring full, finds the only gap in the second ring")
    void getNearestAvaliableSpawnPointFindsGapInSecondRing() {
        Building spawnBuilding = building(10, 10, 1, 1);
        map.placeBuilding(spawnBuilding);

        // Rings 1 and 2 are columns/rows 8..12. Block all of it, then open one cell
        // on the right side of ring 2, part way along rather than at its starting corner.
        blockSquareAround(10, 10, 2);
        map.clearFootprint(building(12, 10, 1, 1));

        Vector2 expectedSpawnPoint = map.toWorldUnit(10, 12);
        Vector2 actualSpawnPoint = map.getNearestAvaliableSpawnPoint(spawnBuilding);

        assertEquals(expectedSpawnPoint.x, actualSpawnPoint.x, EPSILON);
        assertEquals(expectedSpawnPoint.y, actualSpawnPoint.y, EPSILON);
    }

    @Test
    @DisplayName("Get nearest avaliable spawn point, every searched ring full, returns null instead of hanging")
    void getNearestAvaliableSpawnPointFullyBoxedInReturnsNull() {
        Building spawnBuilding = building(10, 10, 1, 1);
        map.placeBuilding(spawnBuilding);

        // Block every ring the search covers. The ring just beyond is left free:
        // the search must give up at its limit rather than reach further out.
        blockSquareAround(10, 10, WorldMap.MAX_SPAWN_SEARCH_RINGS);

        // Preemptive, so a regression back to an infinite loop fails the test instead of freezing the run.
        Vector2 spawnPoint = assertTimeoutPreemptively(Duration.ofSeconds(1),
                () -> map.getNearestAvaliableSpawnPoint(spawnBuilding));

        assertNull(spawnPoint);
    }

}
