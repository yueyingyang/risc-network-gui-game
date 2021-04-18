package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpyEntryTest {

    @Test
    @Disabled
    public void test_apply() {
        AbstractMapFactory f = new V1MapFactory();
        List<String> names = Arrays.asList("LiLei", "HanMeiMei");
        GameMap myMap = f.createMap(names, 2);
        PlayerInfo myInfo = new PlayerInfo("LiLei", 6, 280, 180);

        ActionEntry entry0 = new PlaceEntry("0", 8, "LiLei");
        ActionEntry entry1 = new SpyEntry("0", 3, "LiLei");
        ActionEntry entry2 = new SpyEntry("0", 4, "LiLei");


        entry0.apply(myMap, myInfo);
        entry1.apply(myMap, myInfo);

        Territory terr0 = myMap.getTerritory("0");
        assertEquals(5, terr0.getNumSoldiersInArmy("0"));
        assertEquals(3, terr0.getNumSpies());
        assertEquals(120, myInfo.getTechResource());

        entry2.apply(myMap, myInfo);
        assertEquals(1, terr0.getNumSoldiersInArmy("0"));
        assertEquals(7, terr0.getNumSpies());
        assertEquals(40, myInfo.getTechResource());
    }

}