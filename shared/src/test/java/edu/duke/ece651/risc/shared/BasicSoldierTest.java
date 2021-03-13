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

}