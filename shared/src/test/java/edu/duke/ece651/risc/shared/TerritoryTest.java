package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

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

}