package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
        int numSoldiers = 1;
        Army myArmy = new BasicArmy("HanMeiMei", numSoldiers);
        myArmy.addSoldiers(3, "2");
        myArmy.addSoldiers(2,"5");
        assertEquals(1, myArmy.getNumSoldiers("0"));
        assertEquals(3, myArmy.getNumSoldiers("2"));
        assertEquals(2, myArmy.getNumSoldiers("5"));
        myArmy.removeSoldiers(2, "2");
        assertEquals(1, myArmy.getNumSoldiers("2"));
    }

    @Test
    public void test_fight() {
        Army army0 = new BasicArmy("HanMeiMei", 5);
        Army army1 = new BasicArmy("LiLei", 8);
        test_fight(army0, army1, "HanMeiMei");

        Army army3 = new BasicArmy("Kitty", 12);
        test_fight(army0, army3,"Kitty");
    }

    private void test_fight(Army army0, Army army1, String expectWinner) {
        Random myRandom = new Random(0);
        Army winner = army0.fight(army1, myRandom);
        assertEquals(expectWinner, winner.getOwnerName());
    }

    @Test
    public void test_fightOneRound() {
        BasicArmy army0 = new BasicArmy("HanMeiMei", 5);
        BasicArmy army1 = new BasicArmy("LiLei", 8);
        Random myRandom = new Random(0);
        army0.fightOneRound(army1, myRandom);
        assertEquals(4, army0.getNumSoldiers());
        assertEquals(8, army1.getNumSoldiers());
        army0.fightOneRound(army1, myRandom);
        assertEquals(4, army0.getNumSoldiers());
        assertEquals(7, army1.getNumSoldiers());
    }

    @Test
    public void test_mergeForce() {
        Army army0 = new BasicArmy("HanMeiMei", 5);
        Army army1 = new BasicArmy("LiLei", 3);
        Army army2 = new BasicArmy("HanMeiMei", 6);
        army0.mergeForce(army1);
        assertEquals(5, army0.getNumSoldiers());
        army0.mergeForce(army2);
        assertEquals(11, army0.getNumSoldiers());
    }

    @Test
    public void test_upgradeForce() {
        Army army0 = new BasicArmy("HanMeiMei", 5);
        army0.upgradeForce("0", "2", 3);
        assertEquals(2, army0.getNumSoldiers("0"));
        assertEquals(3, army0.getNumSoldiers("2"));
        army0.upgradeForce("2", "5", 2);
        assertEquals( 2, army0.getNumSoldiers("0"));
        assertEquals(1, army0.getNumSoldiers("2"));
        assertEquals(2, army0.getNumSoldiers("5"));
    }
}