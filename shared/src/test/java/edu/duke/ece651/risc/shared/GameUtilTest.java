package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameUtilTest {

    @Test
    public void test_getBonusAtLevel() {
        int[] expect = {0, 1, 3, 5, 8, 11, 15};
        for (int i = 0; i < 7; i++) {
            assertEquals(expect[i], GameUtil.getBonusAtLevel(String.valueOf(i)));
        }
    }

    @Test
    public void test_getTechCost() {
        int[] expect = {0, 50, 75, 125, 200, 300};
        for (int i = 1; i < 6; i++) {
            assertEquals(expect[i], GameUtil.getTechCost(i));
        }
    }

    @Test
    public void test_getSoldierCost() {
        assertEquals(237 , GameUtil.getSoldierCost("2", "5", 3));
        assertEquals(170, GameUtil.getSoldierCost("4", "6", 2));
        assertEquals(108, GameUtil.getSoldierCost("1", "3", 4));
    }

}