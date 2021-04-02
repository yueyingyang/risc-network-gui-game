package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.AbstractMapFactory;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.Territory;
import edu.duke.ece651.risc.shared.V1MapFactory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoveEntryTest {

    @Test
    public void test_getters() {
        ActionEntry entry = new MoveEntry("0", "1", 5, "LiLei");
        assertEquals("0", entry.getFromName());
        assertEquals("1", entry.getToName());
        assertEquals(5, entry.getNumSoldiers());
        assertEquals("LiLei", entry.getPlayerName());
    }

    @Test
    public void test_apply() {
        AbstractMapFactory f = new V1MapFactory();
        List<String> names = Arrays.asList("LiLei", "HanMeiMei");
        GameMap myMap = f.createMap(names, 2);
        ActionEntry entry0 = new PlaceEntry("0", 8, "LiLei");
        ActionEntry entry1 = new PlaceEntry("1", 3, "LiLei");
        ActionEntry entry2 = new MoveEntry("0", "1", 2, "LiLei");

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