package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BasicSoldierTest {

    @Test
    public void test_fight() {
        Soldier defender = new BasicSoldier();
        Soldier attacker = new BasicSoldier();
        Random myRandom = new Random(0);
        assertEquals(-8, defender.fight(attacker, myRandom));
        assertEquals(2, defender.fight(attacker, myRandom));
    }

    @Test
    public void test_upgrade() {
        BasicSoldier soldier = new BasicSoldier("5");
        assertTrue(soldier.hasType("5"));
    }

    @Test
    public void test_equals() {
        Soldier s0 = new BasicSoldier();
        Soldier s1 = new BasicSoldier("1");
        Soldier s2 = new BasicSoldier();
        assertEquals(s0, s2);
        assertNotEquals(s0, s1);
        assertNotEquals(s1, "1");
    }

    @Test
    public void test_hashCode() {
        Soldier s0 = new BasicSoldier("5");
        Soldier s1 = new BasicSoldier("3");
        Soldier s2 = new BasicSoldier("3");
        assertEquals(s1.hashCode(), s2.hashCode());
        assertNotEquals(s0.hashCode(), s1.hashCode());
    }

    @Test
    public void test_fight_with_bonus() {
        Random myRandom = new Random(0);  // 0, 8, 9, 7, 15, 13
        Soldier defender = new BasicSoldier("5");
        Soldier attacker = new BasicSoldier("3");
        assertEquals(-2, defender.fight(attacker, myRandom));
    }


}