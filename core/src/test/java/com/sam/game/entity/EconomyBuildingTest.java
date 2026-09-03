package com.sam.game.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.sam.game.Team;
import com.sam.game.content.EconomyBuildingType;

public class EconomyBuildingTest {

    private static final float EPSILON = 0.0001f;

    public EconomyBuilding economyBuilding;
    public Team team;

    @BeforeEach
    void setUp() {
        team = new Team(null, 0, 0, 0, 0);
        EconomyBuildingType economyBuildingType = new EconomyBuildingType(0, 0, 0, 0, null, 10, 10);
        economyBuilding = new EconomyBuilding(0, 0, team, economyBuildingType, 0);
    } 

    @Test
    @DisplayName("Economy building gives passive gold to team after interval has been reached") 
    void EconomyBuildingGivesGoldToTeam() {
        economyBuilding.update(10, null);
        assertEquals(10, team.getGold(), EPSILON);
    }

    @Test
    @DisplayName("Economy building does not give passive gold to team if an interval hasnt be reached") 
    void EconomyBuildingDoesNotGiveGoldToTeam() {
        economyBuilding.update(9, null);
        assertEquals(0, team.getGold(), EPSILON);
    }

}
