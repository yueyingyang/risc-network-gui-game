package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasicArmyTest {

    @Test
    public void test_constructor() {
        int numSoldiers = 3;
        Army myArmy = new BasicArmy("HanMeiMei", numSoldiers);
        assertEquals("HanMeiMei", myArmy.getOwnerName());
        assertEquals(numSoldiers, myArmy.getNumSoldiers());
    }

    @Test
    public void test_add_remove() {
        Army myArmy = new BasicArmy("HanMeiMei", 5);
        myArmy.addSoldiers(3);
        assertEquals(8, myArmy.getNumSoldiers());
        myArmy.removeSoldiers(6);
        assertEquals(2, myArmy.getNumSoldiers());
    }
}