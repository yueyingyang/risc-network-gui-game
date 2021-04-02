package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.AbstractMapFactory;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.Territory;
import edu.duke.ece651.risc.shared.V1MapFactory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AttackEntryTest {
    @Test
    public void test_getters() {
        ActionEntry entry = new AttackEntry("0", "1", 5, "LiLei");
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
        ActionEntry entry1 = new PlaceEntry("3", 4, "HanMeiMei");
        ActionEntry entry2 = new AttackEntry("0", "3", 3, "LiLei");

        ActionEntry[] entries = {entry0, entry1, entry2};
        for (ActionEntry entry : entries) {
            entry.apply(myMap, null);
        }

        Territory terr0 = myMap.getTerritory("0");
        Territory terr2 = myMap.getTerritory("3");
        assertEquals(5, terr0.getNumSoldiersInArmy());
        assertEquals(4, terr2.getNumSoldiersInArmy());
        assertEquals(3, terr2.getNumSoldiersInAttacker("LiLei"));
    }

}