package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoveEntryTest {

    @Test
    public void test_apply() {
        AbstractMapFactory f = new V1MapFactory();
        List<String> names = Arrays.asList("LiLei", "HanMeiMei");
        GameMap myMap = f.createMap(names, 2);
        ActionEntry entry0 = new PlaceEntry("0", 8);
        ActionEntry entry1 = new PlaceEntry("1", 3);
        ActionEntry entry2 = new MoveEntry("0", "1", 2);
        ActionEntry[] entries = {entry0, entry1, entry2};
        for (ActionEntry entry : entries) {
            entry.apply(myMap);
        }
        Territory terr0 = myMap.getTerritory("0");
        Territory terr1 = myMap.getTerritory("1");
        assertEquals(6, terr0.getNumSoldiersInArmy());
        assertEquals(5, terr1.getNumSoldiersInArmy());
    }


}