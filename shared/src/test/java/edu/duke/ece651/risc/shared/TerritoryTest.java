package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class TerritoryTest {

    @Test
    public void test_getName() {
        Territory territory = new Territory("NANJING");
        assertEquals("NANJING", territory.getName());
    }

    @Test
    public void test_equals() {
        Territory territory0 = new Territory("NANJING");
        Territory territory1 = new Territory("BEIJING");
        Territory territory2 = new Territory("NANJING");
        assertEquals(territory0, territory2);
        assertNotEquals(territory0, territory1);
        assertNotEquals(territory1, "BEIJING");
    }

    @Test
    public void test_hashCode() {
        Territory territory0 = new Territory("HANGZHOU");
        Territory territory1 = new Territory("SHANGHAI");
        Territory territory2 = new Territory("HANGZHOU");
        assertEquals(territory0.hashCode(), territory2.hashCode());
        assertNotEquals(territory0.hashCode(), territory1.hashCode());
    }

    @Test
    public void test_addNeighbour_isAdjacent() {
        Territory territory0 = new Territory("NANJING");
        Territory territory1 = new Territory("SHANGHAI");
        Territory territory2 = new Territory("HUNAN");
        territory0.addNeighbour(territory1);
        territory1.addNeighbour(territory0);
        assertTrue(territory0.isAdjacent(territory1));
        assertTrue(territory1.isAdjacent(territory0));
        assertFalse(territory0.isAdjacent(territory2));
    }

    @Test
    public void test_getOwnerName_setOwnerName() {
        Territory territory = new Territory("NANJING");
        territory.setOwnerName("Green");
        assertEquals("Green", territory.getOwnerName());
    }

    @Test
    public void test_add_remove() {
        Territory territory = new Territory("NANJING");
        Army myArmy = new BasicArmy("HanMeiMei", 3);
        territory.setMyArmy(myArmy);
        assertEquals(3, territory.getNumSoldiersInArmy());
        territory.addSoldiersToArmy(4);
        assertEquals(7, territory.getNumSoldiersInArmy());
        territory.removeSoldiersFromArmy(2);
        assertEquals(5, territory.getNumSoldiersInArmy());
    }

    @Test
    public void test_bufferAttacker() {
        Territory territory = new Territory("NANJING");
        Army myArmy = new BasicArmy("LiLei", 5);
        territory.setMyArmy(myArmy);

        Army attacker0 = new BasicArmy("HanMeiMei", 4);
        Army attacker1 = new BasicArmy("Kitty", 6);
        Army attacker2 = new BasicArmy("HanMeiMei", 8);

        Army[] attackers = {attacker0, attacker1, attacker2};
        for (Army attacker : attackers) {
            territory.bufferAttacker(attacker);
        }

        Map<String, Army> attckerBuffer = territory.getAttackerBuffer();
        assertEquals(6, attckerBuffer.get("Kitty").getNumSoldiers());
        assertEquals(12, attckerBuffer.get("HanMeiMei").getNumSoldiers());
    }

    @Test
    public void test_resolveCombat() {
        Territory territory = new Territory("NANJING");
        Army myArmy = new BasicArmy("LiLei", 5);
        territory.setMyArmy(myArmy);

        Army attacker0 = new BasicArmy("HanMeiMei", 4);
        Army attacker1 = new BasicArmy("Kitty", 6);
        Army attacker2 = new BasicArmy("HanMeiMei", 8);

        Army[] attackers = {attacker0, attacker1, attacker2};
        for (Army attacker : attackers) {
            territory.bufferAttacker(attacker);
        }

        territory.resolveCombat(new Random(0));
        assertEquals("Kitty", territory.getOwnerName());
    }

}