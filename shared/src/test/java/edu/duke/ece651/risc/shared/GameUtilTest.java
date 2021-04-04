package edu.duke.ece651.risc.shared;

import edu.duke.ece651.risc.shared.game.GameUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameUtilTest {

    @Test
    public void test_getBonusAtLevel() {
        int[] expect = {0, 1, 3, 5, 8, 11, 15};
        for (int i = 0; i < 7; i++) {
            assertEquals(expect[i], GameUtil.getBonus(String.valueOf(i)));
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

    @Test
    public void test_getTechRequire() {
        assertEquals(1, GameUtil.getTechRequire("1"));
        assertEquals(2, GameUtil.getTechRequire("2"));
        assertEquals(3, GameUtil.getTechRequire("3"));
        assertEquals(4, GameUtil.getTechRequire("4"));
        assertEquals(5, GameUtil.getTechRequire("5"));
        assertEquals(6, GameUtil.getTechRequire("6"));
    }

}