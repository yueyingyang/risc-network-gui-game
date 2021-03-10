package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasicSoldierTest {

    @Test
    public void test_fight() {
        Soldier defender = new BasicSoldier();
        Soldier attacker = new BasicSoldier();
        // TODO
        System.out.println(defender.fight(attacker));
    }

}