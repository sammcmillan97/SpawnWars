package com.sam.game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.graphics.Color;

public class TeamTest {
    
    private static final float PASSIVE_GOLD = 10;
    private static final float STARTING_GOLD = 10;
    private static final float EPSILON = 0.0001f;
    public static final float PASSIVE_GOLD_INTERVAL = 10;


    private Team team;

    @BeforeEach
    void setUp() {
        team = new Team(Color.RED, 1, STARTING_GOLD, PASSIVE_GOLD, PASSIVE_GOLD_INTERVAL);
    }

    @Test
    @DisplayName("constructor stores the starting gold and passive gold")
    void constructorStoresStartingGoldAndPassiveGold() {
        assertEquals(PASSIVE_GOLD, team.getPassiveGold(), EPSILON);
        assertEquals(STARTING_GOLD, team.getGold(), EPSILON);
    }

    @Test
    @DisplayName("try spend returns true and subtracts the cost when a team has enough gold")
    void trueIsReturnedWhenTeamHasEnoughGold() {
        float cost = 5;
        assertEquals(true, team.spendGold(cost));
        assertEquals(STARTING_GOLD - cost, team.getGold(), EPSILON);
    }

    @Test
    @DisplayName("try spend returns false and does not subtract the cost when a team does not have enough gold")
    void falseIsReturnedWhenTeamDoesNotHaveEnoughGold() {
        float cost = 15;
        assertEquals(false, team.spendGold(cost));
        assertEquals(STARTING_GOLD, team.getGold(), EPSILON);
    }

    @Test
    @DisplayName("can afford returns true if team has enough gold")
    void trueIsReturnedWhenTeamCanAfford() {
        float cost = 10;
        assertEquals(true, team.canAfford(cost));
    }

    @Test
    @DisplayName("can afford returns false if team does not has enough gold")
    void falseIsReturnedWhenTeamCanNotAfford() {
        float cost = 15;
        assertEquals(false, team.canAfford(cost));
    }

    @Test
    @DisplayName("try spend exactly teams current gold returns try gold is set to zero")
    void trySpendExactlyTeamGoldReturnTrueZeroGoldRemains() {
        float cost = 10;
        assertEquals(true, team.spendGold(cost));
        assertEquals(0, team.getGold(), EPSILON);
    }

    
}
