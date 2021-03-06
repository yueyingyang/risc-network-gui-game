package edu.duke.ece651.risc.shared.entry;

import edu.duke.ece651.risc.shared.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class FancyMoveEntryTest {

    @Test
    public void test_apply() {
        List<List<String>> conns = new ArrayList<>();
        conns.add(Arrays.asList("0", "1"));
        Territory terr0 = new Territory("NANJING", 5,20,15);
        Territory terr1 = new Territory("SHANGHAI", 10,25, 18);
        terr0.setOwnerName("LiLei");
        terr1.setOwnerName("LiLei");
        Map<String, Territory> territoryFinder = new HashMap<>();
        territoryFinder.put("0", terr0);
        territoryFinder.put("1", terr1);
        GameMap myMap = new GameMap(conns, territoryFinder);

        PlayerInfo myInfo = new PlayerInfo("LiLei", 6, 500, 420);

        ActionEntry entry0 = new PlaceEntry("0", 10, "LiLei");
        ActionEntry entry1 = new PlaceEntry("1", 3, "LiLei");
        ActionEntry entry2 = new SoldierEntry("0", "0", "3",
                8, "LiLei");
        ActionEntry entry3 = new SoldierEntry("0", "3", "5",
                3, "LiLei");
        ActionEntry entry4 = new FancyMoveEntry("0", "1", 2,
                "3", "LiLei");

        entry0.apply(myMap, myInfo);
        entry1.apply(myMap, myInfo);
        entry2.apply(myMap, myInfo);
        assertEquals(180, myInfo.getTechResource());
        entry3.apply(myMap, myInfo);
        assertEquals(0, myInfo.getTechResource());
        entry4.apply(myMap, myInfo);
        assertEquals(470, myInfo.getFoodResource());

        assertEquals(2, terr0.getNumSoldiersInArmy("0"));
        assertEquals(3, terr0.getNumSoldiersInArmy("3"));
        assertEquals(3, terr0.getNumSoldiersInArmy("5"));

        assertEquals(3, terr1.getNumSoldiersInArmy("0"));
        assertEquals(2, terr1.getNumSoldiersInArmy("3"));
    }

}