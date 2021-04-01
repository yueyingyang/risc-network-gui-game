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
        // Creating object of ArrayList<Integer>
        ArrayList<Integer>
                arrlist1 = new ArrayList<Integer>();

        // Populating arrlist1
        arrlist1.add(1);
        arrlist1.add(2);
        arrlist1.add(1);
        arrlist1.add(1);
        arrlist1.add(3);

        // print arrlist1
        System.out.println("ArrayList before "
                + "removeAll() operation : "
                + arrlist1);

        // Creating another object of  ArrayList<Integer>
        ArrayList<Integer>
                arrlist2 = new ArrayList<Integer>();
        arrlist2.add(1);
        // List<Integer> arr1 = Arrays.asList(1, 1, 2, 3, 1, 2, 4);
        // List<Integer> arr2 = Arrays.asList(1);
        arrlist1.removeAll(arrlist2);
        System.out.println(arrlist1);

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
}