package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasicArmyTest {

    @Test
    public void test_Constructor() {
        int numSoldiers = 3;
        Army myArmy = new BasicArmy("HanMeiMei", numSoldiers);
        assertEquals("HanMeiMei", myArmy.getOwnerName());
        assertEquals(numSoldiers, myArmy.getNumSoldiers());
    }
}