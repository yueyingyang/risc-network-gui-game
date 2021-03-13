package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AttackEntryTest {

    @Test
    public void test_apply() {
        AbstractMapFactory f = new V1MapFactory();
        List<String> names = Arrays.asList("LiLei", "HanMeiMei");
        GameMap myMap = f.createMap(names, 2);
        ActionEntry entry0 = new PlaceEntry("0", 8);
        ActionEntry entry1 = new AttackEntry("0", "2", 3);
        entry0.apply(myMap, null);
        entry1.apply(myMap, null);
        Territory terr0 = myMap.getTerritory("0");
        Territory terr1 = myMap.getTerritory("2");
        assertEquals(5, terr0.getNumSoldiersInArmy());
        assertEquals(3, terr1.getNumSoldiersInAttacker("LiLei"));
    }

}