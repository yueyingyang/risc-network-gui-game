package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SoldierEntryTest {

    @Test
    public void test_getters() {
        ActionEntry se = new SoldierEntry("4", "0", "3",
                3,"HanMeiMei");
        assertEquals("4", se.getToName());
        assertEquals("0", se.getFromType());
        assertEquals("3", se.getToType());
        assertEquals(3, se.getNumSoldiers());
        assertEquals("HanMeiMei", se.getPlayerName());
    }

    @Test
    public void test_apply() {
        AbstractMapFactory f = new V1MapFactory();
        List<String> names = Arrays.asList("LiLei", "HanMeiMei");
        GameMap myMap = f.createMap(names, 2);
        PlayerInfo myInfo = new PlayerInfo("LiLei", 6, 280, 120);

        ActionEntry entry0 = new PlaceEntry("0", 8, "LiLei");
        ActionEntry entry1 = new SoldierEntry("0", "0", "2",
                5, "LiLei");

        entry0.apply(myMap, myInfo);
        entry1.apply(myMap, myInfo);

        Territory terr0 = myMap.getTerritory("0");
        assertEquals(3, terr0.getNumSoldiersInArmy("0"));
        assertEquals(5, terr0.getNumSoldiersInArmy("2"));
        assertEquals(65, myInfo.getTechResource());

    }
}