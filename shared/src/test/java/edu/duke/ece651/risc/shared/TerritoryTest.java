package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class TerritoryTest {

    @Test
    public void test_getName() {
        Territory terr = new Territory("NANJING");
        assertEquals("NANJING", terr.getName());
    }

    @Test
    public void test_equals() {
        Territory terr0 = new Territory("NANJING");
        Territory terr1 = new Territory("BEIJING");
        Territory terr2 = new Territory("NANJING");
        assertEquals(terr0, terr2);
        assertNotEquals(terr0, terr1);
        assertNotEquals(terr1, "BEIJING");
    }

    @Test
    public void test_hashCode() {
        Territory terr0 = new Territory("HANGZHOU");
        Territory terr1 = new Territory("SHANGHAI");
        Territory terr2 = new Territory("HANGZHOU");
        assertEquals(terr0.hashCode(), terr2.hashCode());
        assertNotEquals(terr0.hashCode(), terr1.hashCode());
    }

    @Test
    public void test_addNeighbour_isAdjacent() {
        Territory terr0 = new Territory("NANJING");
        Territory terr1 = new Territory("SHANGHAI");
        Territory terr2 = new Territory("HUNAN");
        terr0.addNeighbour(terr1);
        terr1.addNeighbour(terr0);
        assertTrue(terr0.isAdjacent(terr1));
        assertTrue(terr1.isAdjacent(terr0));
        assertFalse(terr0.isAdjacent(terr2));
    }

    @Test
    public void test_getOwnerName_setOwnerName() {
        Territory terr = new Territory("NANJING");
        terr.setOwnerName("Green");
        assertEquals("Green", terr.getOwnerName());
    }

    @Test
    public void test_add_remove() {
        Territory terr = new Territory("NANJING");
        Army myArmy = new BasicArmy("HanMeiMei", 3);
        terr.setMyArmy(myArmy);
        assertEquals(3, terr.getNumSoldiersInArmy());
        terr.addSoldiersToArmy(4);
        assertEquals(7, terr.getNumSoldiersInArmy());
        terr.removeSoldiersFromArmy(2);
        assertEquals(5, terr.getNumSoldiersInArmy());
    }

    @Test
    public void test_bufferAttacker() {
        Territory terr = new Territory("NANJING");
        Army myArmy = new BasicArmy("LiLei", 5);
        terr.setMyArmy(myArmy);

        Army attacker0 = new BasicArmy("HanMeiMei", 4);
        Army attacker1 = new BasicArmy("Kitty", 6);
        Army attacker2 = new BasicArmy("HanMeiMei", 8);

        Army[] attackers = {attacker0, attacker1, attacker2};
        for (Army attacker : attackers) {
            terr.bufferAttacker(attacker);
        }

        assertEquals(6, terr.getNumSoldiersInAttacker("Kitty"));
        assertEquals(12, terr.getNumSoldiersInAttacker("HanMeiMei"));
    }

    @Test
    public void test_resolveCombat() {
        Territory terr = new Territory("1");
        Army myArmy = new BasicArmy("Purple", 5);
        terr.setMyArmy(myArmy);

        Army attacker0 = new BasicArmy("Blue", 4);
        Army attacker1 = new BasicArmy("Green", 6);
        Army attacker2 = new BasicArmy("Blue", 8);

        Army[] attackers = {attacker0, attacker1, attacker2};
        for (Army attacker : attackers) {
            terr.bufferAttacker(attacker);
        }

        String ans = terr.resolveCombat(new Random(0));
        String expect = "On territory 1:\n" +
                "Purple player(5 soldiers) defends Blue player(12 soldiers). Purple player wins.\n" +
                "Purple player(1 soldiers) defends Green player(6 soldiers). Green player wins.\n";
        assertEquals(expect, ans);
    }

}