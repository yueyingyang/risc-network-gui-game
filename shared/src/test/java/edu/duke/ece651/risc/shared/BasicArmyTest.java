package edu.duke.ece651.risc.shared;

import edu.duke.ece651.risc.shared.entry.BasicEntry;
import org.junit.jupiter.api.Test;

import java.util.*;

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
        Army army0 = new BasicArmy("HanMeiMei", 1, "2");
        army0.addSoldiers(1, "5");
        army0.addSoldiers(1, "3");

        Army army1 = new BasicArmy("LiLei", 1);
        army1.addSoldiers(1, "6");
        army1.addSoldiers(1, "2");

        test_fight(army0, army1, "HanMeiMei");
    }

    private void test_fight(Army army0, Army army1, String expectWinner) {
        Random myRandom = new Random(0);
        Army winner = army0.fight(army1, myRandom);
        assertEquals(expectWinner, winner.getOwnerName());
    }

    @Test
    public void test_fightOneRound() {
        BasicArmy army0 = new BasicArmy("HanMeiMei", 1, "1");
        army0.addSoldiers(2, "4");
        army0.addSoldiers(1, "2");

        BasicArmy army1 = new BasicArmy("LiLei", 1);
        army1.addSoldiers(1, "1");
        army1.addSoldiers(1, "6");
        army1.addSoldiers(1, "4");

        Collections.sort(army0.getForce());
        Collections.sort(army1.getForce());

        Random myRandom = new Random(0);
        test_fightOneRound(army0, army1, myRandom, 1,
                "[4, 4, 2]", "[6, 4, 1, 0]");
        test_fightOneRound(army0, army1, myRandom, 2,
                "[4, 4, 2]", "[6, 4, 1]");
        test_fightOneRound(army0, army1, myRandom, 3,
                "[4, 4]", "[6, 4, 1]");
        test_fightOneRound(army0, army1, myRandom, 4,
                "[4, 4]", "[6, 4]");

    }

    private void test_fightOneRound(BasicArmy defender, BasicArmy attacker, Random myRandom,
                                    int round, String expDefender, String expAttacker) {
        defender.fightOneRound(attacker, myRandom, round);
        assertEquals(defender.toString(), expDefender);
        assertEquals(attacker.toString(), expAttacker);
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
    public void test_sort() {
        Army army0 = new BasicArmy("HanMeiMei", 1);
        army0.addSoldiers(1, "3");
        army0.addSoldiers(1, "2");
        army0.addSoldiers(1, "5");
        army0.addSoldiers(1, "4");
        army0.addSoldiers(1, "2");
        Collections.sort(army0.getForce());
        assertEquals("[5, 4, 3, 2, 2, 0]", army0.toString());
    }

}