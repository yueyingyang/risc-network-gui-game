package edu.duke.ece651.risc.shared.entry;

import static org.junit.jupiter.api.Assertions.*;

import edu.duke.ece651.risc.shared.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class FancyAttackEntryTest {
    @Test
    public void test_getters() {
        ActionEntry entry = new FancyAttackEntry("0", "1", 5,
                 "0", "LiLei");
        assertEquals("0", entry.getFromName());
        assertEquals("1", entry.getToName());
        assertEquals(5, entry.getNumSoldiers());
        assertEquals("LiLei", entry.getPlayerName());
        assertEquals("0", entry.getFromType());

        ActionEntry entry1 = new FancyAttackEntry("0", "1", 5,
                "0", "LiLei",true);
        assertEquals("0", entry1.getFromName());
        assertEquals("1", entry1.getToName());
        assertEquals(5, entry1.getNumSoldiers());
        assertEquals("LiLei", entry1.getPlayerName());
        assertEquals("0", entry1.getFromType());
        assertTrue(entry1.isUseShip());
    }

    @Test
    public void test_apply() {
        AbstractMapFactory f = new V1MapFactory();
        List<String> names = Arrays.asList("LiLei", "HanMeiMei");
        GameMap myMap = f.createMap(names, 2);
        PlayerInfo myInfo = new PlayerInfo("LiLei", 6, 320, 280);

        ActionEntry entry0 = new PlaceEntry("0", 8, "LiLei");
        ActionEntry entry1 = new PlaceEntry("3", 4, "HanMeiMei");
        ActionEntry entry2 = new SoldierEntry("0", "0", "2", 4, "LiLei");
        ActionEntry entry3 = new SoldierEntry("0", "0", "3", 3, "LiLei");
        ActionEntry entry4 = new FancyAttackEntry("0", "3", 3,
                "2", "LiLei");
        ActionEntry entry5 = new FancyAttackEntry("0", "3", 2,
                 "3", "LiLei");
        ActionEntry entry6 = new FancyAttackEntry("0","2",1,
                "2","LiLei",true);

        entry0.apply(myMap, myInfo);
        entry1.apply(myMap, myInfo);
        entry2.apply(myMap, myInfo);
        assertEquals(236, myInfo.getTechResource());
        entry3.apply(myMap, myInfo);
        assertEquals(146, myInfo.getTechResource());
        entry4.apply(myMap, myInfo);
        assertEquals(317, myInfo.getFoodResource());
        entry5.apply(myMap, myInfo);
        assertEquals(315, myInfo.getFoodResource());

        Territory terr0 = myMap.getTerritory("0");
        Territory terr1 = myMap.getTerritory("3");
        assertEquals(3, terr0.getNumSoldiersInArmy());
        assertEquals(1, terr0.getNumSoldiersInArmy("0"));

        assertEquals(4, terr1.getNumSoldiersInArmy());
        assertEquals(5, terr1.getNumSoldiersInAttacker("LiLei"));
        assertEquals(3, terr1.getNumSoldiersInAttacker("LiLei","2"));
        assertEquals(2, terr1.getNumSoldiersInAttacker("LiLei", "3"));
        myInfo.addProd(Constant.ship,1);
        assertEquals(265,myInfo.getFoodResource());
        entry6.apply(myMap, myInfo);
        assertEquals(264,myInfo.getFoodResource());
        Territory terr2 = myMap.getTerritory("2");
        assertEquals(1,terr2.getNumSoldiersInAttacker("LiLei","2"));
    }
}