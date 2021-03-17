package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlaceEntryTest {
    @Test
    public void test_getters() {
        ActionEntry entry = new PlaceEntry("0", 5);
        assertNull(entry.getFromName());
        assertEquals("0", entry.getToName());
        assertEquals(5, entry.getNumSoldiers());
    }

    @Test
    public void test_apply() {
        AbstractMapFactory f = new V1MapFactory();
        List<String> names = Arrays.asList("LiLei", "HanMeiMei");
        GameMap myMap = f.createMap(names, 2);
        ActionEntry entry = new PlaceEntry("2", 5);
        entry.apply(myMap, null);
        Territory terr = myMap.getTerritory("2");
        assertEquals(5, terr.getNumSoldiersInArmy());
    }

}