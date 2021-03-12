package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

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
        Army myArmy = new BasicArmy("HanMeiMei", 5);
        myArmy.addSoldiers(3);
        assertEquals(8, myArmy.getNumSoldiers());
        myArmy.removeSoldiers(6);
        assertEquals(2, myArmy.getNumSoldiers());
    }

    @Test
    public void test_fight() {
        Army army0 = new BasicArmy("HanMeiMei", 5);
        Army army1 = new BasicArmy("LiLei", 8);
        Random myRandom = new Random(0);
        Army winner = army0.fight(army1, myRandom);
        assertEquals("HanMeiMei", winner.getOwnerName());
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
}