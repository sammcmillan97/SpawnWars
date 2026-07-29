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
    @DisplayName("passive gold is earned correctly") 
    void passiveGoldIsAddedAfterTimerReachesInterval() {
        float timePassed = 10; //10 Seconds
        team.earnPassiveGold(timePassed);
        assertEquals(STARTING_GOLD + PASSIVE_GOLD, team.getGold(), EPSILON);
    }

    
    @Test
    @DisplayName("passive gold is not earned when interval is not met")
    void passiveGoldIsNotAfterTimerDoesNotReachInterval() {
        float timePassed = 9; //10 Seconds
        team.earnPassiveGold(timePassed);
        assertEquals(STARTING_GOLD, team.getGold(), EPSILON);
    }

    @Test
    @DisplayName("passive gold is earned correctly when interval has been met and exceeded")
    void passiveGoldIsEarnedAfterTimerReachedIntervalAndExceeded() {
        float timePassed = 5; //5 Seconds
        team.earnPassiveGold(timePassed);
        team.earnPassiveGold(timePassed);
        team.earnPassiveGold(timePassed);

        assertEquals(STARTING_GOLD + PASSIVE_GOLD, team.getGold(), EPSILON);
        assertEquals(timePassed, team.getGoldTimer(), EPSILON);
    }

    

    
}
