package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.AbstractMapFactory;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.Territory;
import edu.duke.ece651.risc.shared.V1MapFactory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlaceEntryTest {
    @Test
    public void test_getters() {
        ActionEntry entry = new PlaceEntry("0", 5, "LiLei");
        assertNull(entry.getFromName());
        assertEquals("0", entry.getToName());
        assertEquals(5, entry.getNumSoldiers());
        assertEquals("LiLei", entry.getPlayerName());
    }

    @Test
    public void test_apply() {
        AbstractMapFactory f = new V1MapFactory();
        List<String> names = Arrays.asList("LiLei", "HanMeiMei");
        GameMap myMap = f.createMap(names, 2);
        ActionEntry entry = new PlaceEntry("2", 5, "HanMeiMei");
        entry.apply(myMap, null);
        Territory terr = myMap.getTerritory("2");
        assertEquals(5, terr.getNumSoldiersInArmy());
    }

}