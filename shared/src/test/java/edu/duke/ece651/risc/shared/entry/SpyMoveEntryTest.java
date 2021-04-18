package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpyMoveEntryTest {

    @Test
    public void test_apply() {
        AbstractMapFactory f = new V1MapFactory();
        String name0 = "LiLei";
        String name1 = "HanMeiMei";
        List<String> names = Arrays.asList(name0, name1);
        GameMap myMap = f.createMap(names, 2);
        Territory terr0 = myMap.getTerritory("0");
        Territory terr1 = myMap.getTerritory("1");
        Territory terr2 = myMap.getTerritory("2");
        Territory terr3 = myMap.getTerritory("3");

        PlayerInfo myInfo = new PlayerInfo(name0, 1, 280, 1000);

        ActionEntry entry0 = new PlaceEntry("0", 12, name0);
        ActionEntry entry1 = new SpyEntry("0", 12, name0);

        entry0.apply(myMap, myInfo);
        entry1.apply(myMap, myInfo);

        // my - my
        ActionEntry entry2 = new SpyMoveEntry("0", "1", 8, name0);
        entry2.apply(myMap, myInfo);
        assertEquals(4, terr0.getNumSpies());
        assertEquals(0, terr1.getNumSpies());
        terr1.effectSpyMove();
        assertEquals(8, terr1.getNumSpies());

        // my - other's
        ActionEntry entry3 = new SpyMoveEntry("1", "2", 5, name0);
        entry3.apply(myMap, myInfo);
        assertEquals(3, terr1.getNumSpies());
        assertEquals(0, terr2.getNumEnemySpies(name0));
        terr2.effectSpyMove();
        assertEquals(5, terr2.getNumEnemySpies(name0));

        // other's - my
        ActionEntry entry4 = new SpyMoveEntry("2", "1", 1, name0);
        entry4.apply(myMap, myInfo);
        assertEquals(3, terr1.getNumSpies());
        assertEquals(4, terr2.getNumEnemySpies(name0));
        terr1.effectSpyMove();
        assertEquals(4, terr1.getNumSpies());

        // other's - other's
        ActionEntry entry5 = new SpyMoveEntry("2", "3", 3, name0);
        entry5.apply(myMap, myInfo);
        assertEquals(1, terr2.getNumEnemySpies(name0));
        assertEquals(0, terr3.getNumEnemySpies(name0));
        terr3.effectSpyMove();
        assertEquals(3, terr3.getNumEnemySpies(name0));

    }

}