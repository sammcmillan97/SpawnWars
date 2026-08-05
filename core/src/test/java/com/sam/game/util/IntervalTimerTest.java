package com.sam.game.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IntervalTimerTest {


    private IntervalTimer intervalTimer;
    private static final float INTERVAL = 10;
    private static final float EPSILON = 0.0001f;

    @BeforeEach
    void setUp() {
        intervalTimer = new IntervalTimer(INTERVAL);
    }

    @Test
    @DisplayName("not enough delta to reach interval returns false") 
    void notEnoughDelta() {
        assertEquals(false, intervalTimer.advance(9));
    }

    @Test
    @DisplayName("Exactly enough delta to reach interval returns true and remainig count is 0") 
    void exactlyEnoughDelta() {
        assertEquals(true, intervalTimer.advance(10));
        assertEquals(0, intervalTimer.getElapsed(), EPSILON);
    }

    @Test
    @DisplayName("Over flow of delta to exceed interval returns true and remainig count retains overflow") 
    void overFlowOfDelta() {
        assertEquals(true, intervalTimer.advance(12));
        assertEquals(2, intervalTimer.getElapsed(), EPSILON);
    }

    @Test
    @DisplayName("advanced called twice that combined go over the threshold") 
    void twoAdavancedCalls() {
        assertEquals(false, intervalTimer.advance(6));
        assertEquals(true, intervalTimer.advance(6));
        assertEquals(2, intervalTimer.getElapsed(), EPSILON);
    }
}
