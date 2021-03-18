package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AttackEntryTest {
    @Test
    public void test_getters() {
        ActionEntry entry = new AttackEntry("0", "1", 5);
        assertEquals("0", entry.getFromName());
        assertEquals("1", entry.getToName());
        assertEquals(5, entry.getNumSoldiers());
    }

    @Test
    public void test_apply() {
        AbstractMapFactory f = new V1MapFactory();
        List<String> names = Arrays.asList("LiLei", "HanMeiMei");
        GameMap myMap = f.createMap(names, 2);

        ActionEntry entry0 = new PlaceEntry("0", 8);  // LiLei
        ActionEntry entry1 = new PlaceEntry("2", 4);  // HanMeiMei
        ActionEntry entry2 = new AttackEntry("0", "2", 3);

        ActionEntry[] entries = {entry0, entry1, entry2};
        for (ActionEntry entry : entries) {
            entry.apply(myMap);
        }

        Territory terr0 = myMap.getTerritory("0");
        Territory terr2 = myMap.getTerritory("2");
        assertEquals(5, terr0.getNumSoldiersInArmy());
        assertEquals(4, terr2.getNumSoldiersInArmy());
        assertEquals(3, terr2.getNumSoldiersInAttacker("LiLei"));
    }

}